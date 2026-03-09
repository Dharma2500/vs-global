package com.vs.vscombo.client.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.glfw.GLFW;

public class SmallNumberField {
    private final FontRenderer font;
    private final int x, y, width, height;
    private String text = "";
    private boolean isFocused = false;
    private int maxLength = 10;
    private ITextFilter filter = s -> s.matches("\\d*");
    
    public interface ITextFilter { boolean test(String text); }
    
    public SmallNumberField(FontRenderer font, int x, int y, int width, int height, ITextComponent initial) {
        this.font = font; this.x = x; this.y = y; this.width = width; this.height = height;
        this.text = initial.getString();
    }
    
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        int bgColor = isFocused ? 0xFF2A2A3A : 0xFF1A1A2A;
        AbstractGui.fill(matrixStack, x, y, x + width, y + height, bgColor);
        int borderColor = isFocused ? 0xFF40FF40 : 0xFF606080;
        AbstractGui.fill(matrixStack, x, y, x + width, y + 1, borderColor);
        AbstractGui.fill(matrixStack, x, y, x + 1, y + height, borderColor);
        AbstractGui.fill(matrixStack, x + width - 1, y, x + width, y + height, 0xFF303050);
        AbstractGui.fill(matrixStack, x, y + height - 1, x + width, y + height, 0xFF303050);
        
        String display = text.isEmpty() && !isFocused ? "0" : text;
        int textWidth = font.width(display);
        int textX = x + width - 4 - textWidth;
        int textY = y + (height - 9) / 2;
        font.draw(matrixStack, display, (float)textX, (float)textY, 0xFFFFFFFF);
        
        if (isFocused && (System.currentTimeMillis() / 500) % 2 == 0) {
            AbstractGui.fill(matrixStack, x + width - 4, textY, x + width - 3, textY + 9, 0xFFFFFFFF);
        }
    }
    
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {
            isFocused = true; return true;
        } else if (button == 0) isFocused = false;
        return false;
    }
    
    // ✅ keyPressed обрабатывает ТОЛЬКО специальные клавиши (не цифры!)
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!isFocused) return false;
        
        // Backspace
        if (keyCode == GLFW.GLFW_KEY_BACKSPACE && !text.isEmpty()) { 
            text = text.substring(0, text.length() - 1); 
            return true; 
        }
        // Delete
        if (keyCode == GLFW.GLFW_KEY_DELETE) { 
            text = ""; 
            return true; 
        }
        // Escape - снять фокус
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) { 
            isFocused = false; 
            return true; 
        }
        // Стрелки (если нужно)
        if (keyCode == GLFW.GLFW_KEY_LEFT || keyCode == GLFW.GLFW_KEY_RIGHT) {
            return true;
        }
        
        // ✅ Цифры НЕ обрабатываем здесь - они пойдут через charTyped
        return false;
    }
    
    // ✅ charTyped обрабатывает ТОЛЬКО цифры
    public boolean charTyped(char codePoint, int modifiers) {
        if (!isFocused) return false;
        if (Character.isDigit(codePoint) && filter.test(text + codePoint) && text.length() < maxLength) { 
            text += codePoint; 
            return true; 
        }
        return false;
    }
    
    public String getText() { return text; }
    public void setText(String text) { if (filter.test(text)) this.text = text; }
    public boolean isFocused() { return isFocused; }
    public void setFocused(boolean focused) { this.isFocused = focused; }
    public void setMaxLength(int max) { this.maxLength = max; }
    public void setFilter(ITextFilter filter) { this.filter = filter; }
}
