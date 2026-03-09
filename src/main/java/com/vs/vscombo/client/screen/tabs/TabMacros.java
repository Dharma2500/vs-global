package com.vs.vscombo.client.screen.tabs;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.vs.vscombo.client.screen.widget.CustomButton;
import com.vs.vscombo.client.screen.widget.MultiLineTextField;
import com.vs.vscombo.client.screen.widget.SmallNumberField;
import com.vs.vscombo.util.MacroStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

import java.io.File;

public class TabMacros implements ITab {
    
    private MultiLineTextField textField;
    private CustomButton executeButton;
    private CustomButton startButton;
    private CustomButton stopButton;
    private SmallNumberField loopCountField;
    private SmallNumberField intervalField;
    
    private boolean isRunning = false;
    private Thread executionThread;
    private int contentX, contentY;
    
    private static final int TEXT_WIDTH = 340;
    private static final int TEXT_HEIGHT = 120;
    private static final int BUTTON_WIDTH = 70;
    private static final int BUTTON_HEIGHT = 20;
    private static final int SMALL_FIELD_WIDTH = 50;
    private static final int SMALL_FIELD_HEIGHT = 18;
    
    @Override
    public void init(Minecraft minecraft, int width, int height) {
        // Позиции относительно области контента
        int textX = contentX;
        int textY = contentY;
        
        // ✅ Многострочный текстовый редактор
        this.textField = new MultiLineTextField(
            minecraft.font,
            textX, textY,
            TEXT_WIDTH, TEXT_HEIGHT,
            new StringTextComponent("Enter macros here...")
        );
        
        // Загружаем сохранённые макросы
        File macroFile = MacroStorage.getMacroFile();
        if (macroFile.exists()) {
            String saved = MacroStorage.loadMacros();
            if (saved != null && !saved.isEmpty()) {
                this.textField.setText(saved);
            }
        }
        
        // ✅ Кнопка Execute (справа снизу)
        this.executeButton = new CustomButton(
            contentX + TEXT_WIDTH - BUTTON_WIDTH,
            contentY + TEXT_HEIGHT + 10,
            BUTTON_WIDTH, BUTTON_HEIGHT,
            new StringTextComponent("Execute"),
            btn -> executeMacros()
        );
        
        // ✅ Кнопки Start/Stop (слева снизу)
        this.startButton = new CustomButton(
            contentX,
            contentY + TEXT_HEIGHT + 10,
            50, BUTTON_HEIGHT,
            new StringTextComponent("Start"),
            btn -> startLoop()
        );
        
        this.stopButton = new CustomButton(
            contentX + 55,
            contentY + TEXT_HEIGHT + 10,
            50, BUTTON_HEIGHT,
            new StringTextComponent("Stop"),
            btn -> stopLoop()
        );
        this.stopButton.active = false;
        
        // ✅ Поля для чисел (рядом с кнопками)
        this.loopCountField = new SmallNumberField(
            minecraft.font,
            contentX + 110,
            contentY + TEXT_HEIGHT + 12,
            SMALL_FIELD_WIDTH, SMALL_FIELD_HEIGHT,
            new StringTextComponent("1")
        );
        this.loopCountField.setMaxLength(4);
        this.loopCountField.setFilter(s -> s.matches("\\d*"));
        
        this.intervalField = new SmallNumberField(
            minecraft.font,
            contentX + 165,
            contentY + TEXT_HEIGHT + 12,
            SMALL_FIELD_WIDTH, SMALL_FIELD_HEIGHT,
            new StringTextComponent("1000")
        );
        this.intervalField.setMaxLength(5);
        this.intervalField.setFilter(s -> s.matches("\\d*"));
    }
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, 
                       float partialTicks, int contentX, int contentY) {
        this.contentX = contentX;
        this.contentY = contentY;
        
        // Рендерим текстовое поле
        if (textField != null) {
            textField.render(matrixStack, mouseX, mouseY, partialTicks);
        }
        
        // Рендерим кнопки
        if (executeButton != null) executeButton.render(matrixStack, mouseX, mouseY, partialTicks);
        if (startButton != null) startButton.render(matrixStack, mouseX, mouseY, partialTicks);
        if (stopButton != null) stopButton.render(matrixStack, mouseX, mouseY, partialTicks);
        
        // Рендерим поля чисел
        if (loopCountField != null) loopCountField.render(matrixStack, mouseX, mouseY, partialTicks);
        if (intervalField != null) intervalField.render(matrixStack, mouseX, mouseY, partialTicks);
        
        // Подписи к полям
        Minecraft mc = Minecraft.getInstance();
        mc.font.draw(matrixStack, "Loops:", (float)(contentX + 110), (float)(contentY + TEXT_HEIGHT + 5), 0xFFFFFF);
        mc.font.draw(matrixStack, "ms:", (float)(contentX + 165), (float)(contentY + TEXT_HEIGHT + 5), 0xFFFFFF);
        
        // Индикатор состояния
        String status = isRunning ? "§aRunning..." : "§7Stopped";
        mc.font.draw(matrixStack, status, (float)(contentX + 220), (float)(contentY + TEXT_HEIGHT + 15), 0xFFFFFF);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean handled = false;
        
        if (textField != null) {
            // ✅ Важно: текстовое поле должно получать фокус и обрабатывать клики
            handled = textField.mouseClicked(mouseX, mouseY, button) || handled;
        }
        if (executeButton != null) {
            handled = executeButton.mouseClicked(mouseX, mouseY, button) || handled;
        }
        if (startButton != null) {
            handled = startButton.mouseClicked(mouseX, mouseY, button) || handled;
        }
        if (stopButton != null) {
            handled = stopButton.mouseClicked(mouseX, mouseY, button) || handled;
        }
        if (loopCountField != null) {
            handled = loopCountField.mouseClicked(mouseX, mouseY, button) || handled;
        }
        if (intervalField != null) {
            handled = intervalField.mouseClicked(mouseX, mouseY, button) || handled;
        }
        
        return handled;
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // ✅ Если фокус на текстовом поле — передаём ввод туда
        if (textField != null && textField.isFocused()) {
            // ✅ Блокируем клавишу открытия мода (R) — она не должна вводить символы
            if (textField.handleKeyPress(keyCode, scanCode, modifiers)) {
                // Автосохранение после каждого изменения
                MacroStorage.saveMacros(textField.getText());
                return true;
            }
        }
        
        // Обработка для полей чисел
        if (loopCountField != null && loopCountField.isFocused()) {
            return loopCountField.keyPressed(keyCode, scanCode, modifiers);
        }
        if (intervalField != null && intervalField.isFocused()) {
            return intervalField.keyPressed(keyCode, scanCode, modifiers);
        }
        
        return false;
    }
    
    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        // ✅ Обработка ввода символов в текстовое поле
        if (textField != null && textField.isFocused()) {
            if (textField.charTyped(codePoint, modifiers)) {
                MacroStorage.saveMacros(textField.getText());
                return true;
            }
        }
        if (loopCountField != null && loopCountField.isFocused()) {
            return loopCountField.charTyped(codePoint, modifiers);
        }
        if (intervalField != null && intervalField.isFocused()) {
            return intervalField.charTyped(codePoint, modifiers);
        }
        return false;
    }
    
    /**
     * Отправка макросов на сервер (однократно)
     */
    private void executeMacros() {
        if (textField == null) return;
        
        String text = textField.getText();
        if (text.isEmpty()) return;
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.player.connection == null) return;
        
        // Разбиваем на строки и отправляем каждую
        String[] lines = text.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty()) {
                if (line.startsWith("/")) {
                    // Команда
                    mc.player.connection.sendCommand(line.substring(1));
                } else {
                    // Сообщение в чат
                    mc.player.connection.sendChat(line);
                }
            }
        }
    }
    
    /**
     * Запуск цикла отправки
     */
    private void startLoop() {
        if (isRunning || textField == null) return;
        
        try {
            int loops = Integer.parseInt(loopCountField.getText().isEmpty() ? "1" : loopCountField.getText());
            int interval = Integer.parseInt(intervalField.getText().isEmpty() ? "1000" : intervalField.getText());
            
            isRunning = true;
            startButton.active = false;
            stopButton.active = true;
            
            executionThread = new Thread(() -> {
                String text = textField.getText();
                String[] lines = text.split("\n");
                
                for (int loop = 0; loop < loops && isRunning; loop++) {
                    for (String line : lines) {
                        if (!isRunning) break;
                        
                        line = line.trim();
                        if (!line.isEmpty()) {
                            Minecraft mc = Minecraft.getInstance();
                            if (mc.player != null && mc.player.connection != null) {
                                if (line.startsWith("/")) {
                                    mc.player.connection.sendCommand(line.substring(1));
                                } else {
                                    mc.player.connection.sendChat(line);
                                }
                            }
                        }
                    }
                    
                    if (isRunning && loop < loops - 1) {
                        try {
                            Thread.sleep(interval);
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                }
                
                // Завершение
                isRunning = false;
                Minecraft.getInstance().execute(() -> {
                    startButton.active = true;
                    stopButton.active = false;
                });
            });
            executionThread.start();
            
        } catch (NumberFormatException e) {
            // Неверный формат числа — игнорируем
        }
    }
    
    /**
     * Остановка цикла
     */
    private void stopLoop() {
        isRunning = false;
        if (executionThread != null && executionThread.isAlive()) {
            executionThread.interrupt();
        }
    }
    
    @Override public String getTabId() { return "tab1"; }
    @Override public String getTabName() { return "Macros"; }
}
