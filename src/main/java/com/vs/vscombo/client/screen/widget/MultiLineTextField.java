package com.vs.vscombo.client.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class MultiLineTextField {
    
    private final FontRenderer font;
    private final int x, y, width, height;
    private final ITextComponent hint;
    
    private String text = "";
    private int cursorPos = 0;
    private int selectionStart = -1;
    private boolean isFocused = false;
    private boolean isEditable = true;
    
    // Прокрутка
    private int scrollX = 0;
    private int scrollY = 0;
    private final int lineHeight = 10;
    private final int padding = 4;
    
    // Мигание курсора
    private long lastCursorToggle = 0;
    private boolean cursorVisible = true;
    private static final long CURSOR_BLINK_INTERVAL = 500; // мс
    
    // Фильтр ввода
    private ITextFilter filter = s -> true;
    
    public interface ITextFilter {
        boolean test(String text);
    }
    
    public MultiLineTextField(FontRenderer font, int x, int y, int width, int height, ITextComponent hint) {
        this.font = font;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hint = hint;
    }
    
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // Фон поля
        int bgColor = isFocused ? 0xFF2A2A3A : 0xFF1A1A2A;
        AbstractGui.fill(matrixStack, x, y, x + width, y + height, bgColor);
        
        // Граница
        int borderColor = isFocused ? 0xFF40FF40 : 0xFF606080;
        AbstractGui.fill(matrixStack, x, y, x + width, y + 1, borderColor);
        AbstractGui.fill(matrixStack, x, y, x + 1, y + height, borderColor);
        AbstractGui.fill(matrixStack, x + width - 1, y, x + width, y + height, 0xFF303050);
        AbstractGui.fill(matrixStack, x, y + height - 1, x + width, y + height, 0xFF303050);
        
        // Обрезаем рендеринг внутри поля
        RenderSystem.enableScissor(x + 1, Minecraft.getInstance().getMainWindow().getScaledHeight() - (y + height - 1), 
            width - 2, height - 2);
        
        // Рендеринг текста
        renderText(matrixStack);
        
        // Рендеринг курсора
        renderCursor(matrixStack);
        
        // Подсказка если пусто
        if (text.isEmpty() && !isFocused) {
            font.draw(matrixStack, hint.getFormattedText(), 
                (float)(x + padding - scrollX), (float)(y + padding - scrollY), 0xFF606080);
        }
        
        RenderSystem.disableScissor();
    }
    
    private void renderText(MatrixStack matrixStack) {
        String[] lines = text.split("\n", -1);
        int visibleLines = (height - 2 * padding) / lineHeight;
        
        for (int i = 0; i < lines.length; i++) {
            int lineY = y + padding + i * lineHeight - scrollY;
            
            // Пропускаем невидимые строки
            if (lineY < y + padding) continue;
            if (lineY > y + height - padding) break;
            
            String line = lines[i];
            // Обрезаем строку по ширине
            int maxWidth = width - 2 * padding;
            String displayLine = line;
            if (font.width(displayLine) > maxWidth + scrollX) {
                // Находим видимую часть
                int startIdx = MathHelper.clamp(scrollX / 6, 0, line.length()); // ~6px на символ
                displayLine = line.substring(startIdx);
                while (font.width(displayLine) > maxWidth && !displayLine.isEmpty()) {
                    displayLine = displayLine.substring(0, displayLine.length() - 1);
                }
            }
            
            font.draw(matrixStack, displayLine, 
                (float)(x + padding - scrollX), (float)lineY, 0xFFFFFFFF);
        }
    }
    
    private void renderCursor(MatrixStack matrixStack) {
        if (!isFocused || !isEditable) return;
        
        // Мигание
        long now = System.currentTimeMillis();
        if (now - lastCursorToggle > CURSOR_BLINK_INTERVAL) {
            cursorVisible = !cursorVisible;
            lastCursorToggle = now;
        }
        
        if (!cursorVisible) return;
        
        // Позиция курсора
        String[] lines = text.split("\n", -1);
        int currentLine = 0;
        int colInLine = 0;
        int pos = 0;
        
        for (int i = 0; i < lines.length && pos < cursorPos; i++) {
            if (pos + lines[i].length() + 1 <= cursorPos) {
                pos += lines[i].length() + 1;
                currentLine++;
            } else {
                colInLine = cursorPos - pos;
                break;
            }
        }
        
        // Если курсор в конце пустой строки
        if (currentLine >= lines.length) {
            currentLine = Math.max(0, lines.length - 1);
            colInLine = lines.length > 0 ? lines[lines.length - 1].length() : 0;
        }
        
        int cursorX = x + padding + font.width(lines[currentLine].substring(0, Math.min(colInLine, lines[currentLine].length()))) - scrollX;
        int cursorY = y + padding + currentLine * lineHeight - scrollY;
        
        // Рисуем курсор
        AbstractGui.fill(matrixStack, cursorX, cursorY, cursorX + 1, cursorY + lineHeight - 2, 0xFFFFFFFF);
    }
    
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean inBounds = mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
        
        if (button == 0) { // ЛКМ
            if (inBounds) {
                isFocused = true;
                // Вычисляем позицию курсора по клику
                updateCursorFromClick(mouseX, mouseY);
                selectionStart = cursorPos;
                return true;
            } else {
                isFocused = false;
            }
        }
        return inBounds;
    }
    
    private void updateCursorFromClick(double mouseX, double mouseY) {
        String[] lines = text.split("\n", -1);
        int clickedLine = (int)((mouseY - y - padding + scrollY) / lineHeight);
        clickedLine = MathHelper.clamp(clickedLine, 0, Math.max(0, lines.length - 1));
        
        // Находим колонку
        int col = 0;
        String line = lines.length > clickedLine ? lines[clickedLine] : "";
        for (int i = 0; i < line.length(); i++) {
            int charX = x + padding + font.width(line.substring(0, i)) - scrollX;
            if (mouseX < charX + 3) break;
            col++;
        }
        
        // Вычисляем абсолютную позицию
        cursorPos = 0;
        for (int i = 0; i < clickedLine; i++) {
            cursorPos += lines[i].length() + 1;
        }
        cursorPos += col;
        cursorPos = MathHelper.clamp(cursorPos, 0, text.length());
        
        // Автопрокрутка
        autoScroll();
    }
    
    public boolean handleKeyPress(int keyCode, int scanCode, int modifiers) {
        if (!isFocused || !isEditable) return false;
        
        boolean ctrl = (modifiers & 2) != 0; // GLFW_MOD_CONTROL
        
        // ✅ Навигация стрелками
        if (keyCode == GLFW.GLFW_KEY_LEFT) {
            if (cursorPos > 0) {
                cursorPos--;
                if (selectionStart == -1) selectionStart = cursorPos;
            }
            autoScroll();
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_RIGHT) {
            if (cursorPos < text.length()) {
                cursorPos++;
                if (selectionStart == -1) selectionStart = cursorPos;
            }
            autoScroll();
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_UP) {
            moveCursorUp();
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_DOWN) {
            moveCursorDown();
            return true;
        }
        
        // ✅ Enter - новая строка
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            insertText("\n");
            return true;
        }
        
        // ✅ Backspace
        if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            if (hasSelection()) {
                deleteSelection();
            } else if (cursorPos > 0) {
                text = text.substring(0, cursorPos - 1) + text.substring(cursorPos);
                cursorPos--;
                selectionStart = -1;
            }
            autoScroll();
            return true;
        }
        
        // ✅ Delete
        if (keyCode == GLFW.GLFW_KEY_DELETE) {
            if (hasSelection()) {
                deleteSelection();
            } else if (cursorPos < text.length()) {
                text = text.substring(0, cursorPos) + text.substring(cursorPos + 1);
            }
            return true;
        }
        
        // ✅ Home/End
        if (keyCode == GLFW.GLFW_KEY_HOME) {
            moveToLineStart();
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_END) {
            moveToLineEnd();
            return true;
        }
        
        // ✅ Ctrl+A - выделить всё
        if (ctrl && keyCode == GLFW.GLFW_KEY_A) {
            selectionStart = 0;
            cursorPos = text.length();
            return true;
        }
        
        // ✅ Ctrl+C / Ctrl+X - копировать/вырезать
        if (ctrl && (keyCode == GLFW.GLFW_KEY_C || keyCode == GLFW.GLFW_KEY_X)) {
            if (hasSelection()) {
                String selected = getSelectedText();
                Minecraft.getInstance().keyboardListener.setClipboardString(selected);
                if (keyCode == GLFW.GLFW_KEY_X) {
                    deleteSelection();
                }
            }
            return true;
        }
        
        // ✅ Ctrl+V - вставить
        if (ctrl && keyCode == GLFW.GLFW_KEY_V) {
            String clipboard = Minecraft.getInstance().keyboardListener.getClipboardString();
            if (clipboard != null && !clipboard.isEmpty()) {
                insertText(clipboard);
            }
            return true;
        }
        
        // ✅ Блокируем клавишу открытия мода (по умолчанию R = 82)
        if (keyCode == 82 && !isTypingAllowed()) {
            return true; // Поглощаем нажатие
        }
        
        return false;
    }
    
    public boolean charTyped(char codePoint, int modifiers) {
        if (!isFocused || !isEditable) return false;
        
        // Фильтр символов
        if (!filter.test(text + codePoint)) return false;
        
        // Вставка символа
        insertText(String.valueOf(codePoint));
        return true;
    }
    
    private void insertText(String newText) {
        if (hasSelection()) {
            deleteSelection();
        }
        
        text = text.substring(0, cursorPos) + newText + text.substring(cursorPos);
        cursorPos += newText.length();
        selectionStart = -1;
        autoScroll();
    }
    
    private boolean hasSelection() {
        return selectionStart != -1 && selectionStart != cursorPos;
    }
    
    private String getSelectedText() {
        if (!hasSelection()) return "";
        int start = Math.min(selectionStart, cursorPos);
        int end = Math.max(selectionStart, cursorPos);
        return text.substring(start, end);
    }
    
    private void deleteSelection() {
        if (!hasSelection()) return;
        int start = Math.min(selectionStart, cursorPos);
        int end = Math.max(selectionStart, cursorPos);
        text = text.substring(0, start) + text.substring(end);
        cursorPos = start;
        selectionStart = -1;
    }
    
    private void moveCursorUp() {
        String[] lines = text.split("\n", -1);
        int currentLine = getCurrentLine();
        int colInLine = getColumnInLine();
        
        if (currentLine > 0) {
            int newLine = currentLine - 1;
            String prevLine = lines.length > newLine ? lines[newLine] : "";
            int newCol = Math.min(colInLine, prevLine.length());
            
            // Пересчитываем позицию
            cursorPos = 0;
            for (int i = 0; i < newLine; i++) {
                cursorPos += lines[i].length() + 1;
            }
            cursorPos += newCol;
            autoScroll();
        }
    }
    
    private void moveCursorDown() {
        String[] lines = text.split("\n", -1);
        int currentLine = getCurrentLine();
        int colInLine = getColumnInLine();
        
        if (currentLine < lines.length - 1) {
            int newLine = currentLine + 1;
            String nextLine = lines.length > newLine ? lines[newLine] : "";
            int newCol = Math.min(colInLine, nextLine.length());
            
            cursorPos = 0;
            for (int i = 0; i < newLine; i++) {
                cursorPos += lines[i].length() + 1;
            }
            cursorPos += newCol;
            autoScroll();
        }
    }
    
    private void moveToLineStart() {
        String[] lines = text.split("\n", -1);
        int currentLine = getCurrentLine();
        
        cursorPos = 0;
        for (int i = 0; i < currentLine; i++) {
            cursorPos += lines[i].length() + 1;
        }
        autoScroll();
    }
    
    private void moveToLineEnd() {
        String[] lines = text.split("\n", -1);
        int currentLine = getCurrentLine();
        
        cursorPos = 0;
        for (int i = 0; i < currentLine; i++) {
            cursorPos += lines[i].length() + 1;
        }
        if (currentLine < lines.length) {
            cursorPos += lines[currentLine].length();
        }
        autoScroll();
    }
    
    private int getCurrentLine() {
        String[] lines = text.split("\n", -1);
        int pos = 0;
        for (int i = 0; i < lines.length; i++) {
            if (pos + lines[i].length() >= cursorPos) return i;
            pos += lines[i].length() + 1;
        }
        return Math.max(0, lines.length - 1);
    }
    
    private int getColumnInLine() {
        String[] lines = text.split("\n", -1);
        int line = getCurrentLine();
        int pos = 0;
        for (int i = 0; i < line; i++) {
            pos += lines[i].length() + 1;
        }
        return cursorPos - pos;
    }
    
    private void autoScroll() {
        // Вертикальная прокрутка
        int visibleLines = (height - 2 * padding) / lineHeight;
        int totalLines = text.split("\n", -1).length;
        int currentLine = getCurrentLine();
        
        if (currentLine * lineHeight - scrollY < padding) {
            scrollY = currentLine * lineHeight - padding;
        } else if ((currentLine + 1) * lineHeight - scrollY > height - padding) {
            scrollY = (currentLine + 1) * lineHeight - (height - padding);
        }
        scrollY = MathHelper.clamp(scrollY, 0, Math.max(0, totalLines * lineHeight - (height - 2 * padding)));
        
        // Горизонтальная прокрутка
        String[] lines = text.split("\n", -1);
        int col = getColumnInLine();
        if (lines.length > getCurrentLine()) {
            String currentLineText = lines[getCurrentLine()];
            int cursorPixelX = font.width(currentLineText.substring(0, Math.min(col, currentLineText.length())));
            
            if (cursorPixelX - scrollX < padding) {
                scrollX = cursorPixelX - padding;
            } else if (cursorPixelX - scrollX > width - 2 * padding) {
                scrollX = cursorPixelX - (width - 2 * padding);
            }
            scrollX = Math.max(0, scrollX);
        }
    }
    
    private boolean isTypingAllowed() {
        // Разрешаем ввод только если поле в фокусе
        return isFocused;
    }
    
    // Геттеры и сеттеры
    public String getText() { return text; }
    public void setText(String text) { 
        this.text = text; 
        cursorPos = text.length();
        selectionStart = -1;
    }
    public boolean isFocused() { return isFocused; }
    public void setFocused(boolean focused) { 
        this.isFocused = focused;
        if (!focused) selectionStart = -1;
    }
    public void setEditable(boolean editable) { this.isEditable = editable; }
    public void setFilter(ITextFilter filter) { this.filter = filter; }
    public void setMaxLength(int max) {
        this.filter = s -> s.length() <= max;
    }
}
