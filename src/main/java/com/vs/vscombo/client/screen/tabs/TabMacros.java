package com.vs.vscombo.client.screen.tabs;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.vs.vscombo.client.screen.widget.CustomButton;
import com.vs.vscombo.client.screen.widget.MultiLineTextField;
import com.vs.vscombo.client.screen.widget.SmallNumberField;
import com.vs.vscombo.util.MacroStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.play.client.CChatMessagePacket;

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
    private FontRenderer font;
    
    private static final int TEXT_WIDTH = 340;
    private static final int TEXT_HEIGHT = 120;
    private static final int BUTTON_WIDTH = 70;
    private static final int BUTTON_HEIGHT = 20;
    private static final int SMALL_FIELD_WIDTH = 50;
    private static final int SMALL_FIELD_HEIGHT = 18;
    
    @Override
    public void init(Minecraft minecraft, int width, int height, int contentX, int contentY) {
        this.font = minecraft.font;
        this.contentX = contentX;
        this.contentY = contentY;
        
        int textX = contentX;
        int textY = contentY;
        
        this.textField = new MultiLineTextField(
            font,
            textX, textY,
            TEXT_WIDTH, TEXT_HEIGHT,
            new net.minecraft.util.text.StringTextComponent("Enter macros here...")
        );
        
        java.io.File macroFile = MacroStorage.getMacroFile();
        if (macroFile.exists()) {
            String saved = MacroStorage.loadMacros();
            if (saved != null && !saved.isEmpty()) {
                this.textField.setText(saved);
            }
        }
        
        // ✅ Кнопка Execute
        this.executeButton = new CustomButton(
            contentX + TEXT_WIDTH - BUTTON_WIDTH,
            contentY + TEXT_HEIGHT + 10,
            BUTTON_WIDTH, BUTTON_HEIGHT,
            new net.minecraft.util.text.StringTextComponent("Execute"),
            btn -> executeMacros()
        );
        
        // ✅ Кнопки Start/Stop
        this.startButton = new CustomButton(
            contentX,
            contentY + TEXT_HEIGHT + 10,
            50, BUTTON_HEIGHT,
            new net.minecraft.util.text.StringTextComponent("Start"),
            btn -> startLoop()
        );
        
        this.stopButton = new CustomButton(
            contentX + 55,
            contentY + TEXT_HEIGHT + 10,
            50, BUTTON_HEIGHT,
            new net.minecraft.util.text.StringTextComponent("Stop"),
            btn -> stopLoop()
        );
        this.stopButton.active = false;
        
        // ✅ Поля для чисел
        this.loopCountField = new SmallNumberField(
            font,
            contentX + 110,
            contentY + TEXT_HEIGHT + 12,
            SMALL_FIELD_WIDTH, SMALL_FIELD_HEIGHT,
            new net.minecraft.util.text.StringTextComponent("1")
        );
        this.loopCountField.setMaxLength(4);
        this.loopCountField.setFilter(s -> s.matches("\\d*"));
        
        this.intervalField = new SmallNumberField(
            font,
            contentX + 165,
            contentY + TEXT_HEIGHT + 12,
            SMALL_FIELD_WIDTH, SMALL_FIELD_HEIGHT,
            new net.minecraft.util.text.StringTextComponent("1000")
        );
        this.intervalField.setMaxLength(5);
        this.intervalField.setFilter(s -> s.matches("\\d*"));
    }
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, 
                       float partialTicks, int contentX, int contentY) {
        this.contentX = contentX;
        this.contentY = contentY;
        
        if (textField != null) {
            textField.render(matrixStack, mouseX, mouseY, partialTicks);
        }
        if (executeButton != null) executeButton.render(matrixStack, mouseX, mouseY, partialTicks);
        if (startButton != null) startButton.render(matrixStack, mouseX, mouseY, partialTicks);
        if (stopButton != null) stopButton.render(matrixStack, mouseX, mouseY, partialTicks);
        if (loopCountField != null) loopCountField.render(matrixStack, mouseX, mouseY, partialTicks);
        if (intervalField != null) intervalField.render(matrixStack, mouseX, mouseY, partialTicks);
        
        font.draw(matrixStack, "Loops:", (float)(contentX + 110), (float)(contentY + TEXT_HEIGHT + 5), 0xFFFFFF);
        font.draw(matrixStack, "ms:", (float)(contentX + 165), (float)(contentY + TEXT_HEIGHT + 5), 0xFFFFFF);
        
        String status = isRunning ? "§aRunning..." : "§7Stopped";
        font.draw(matrixStack, status, (float)(contentX + 220), (float)(contentY + TEXT_HEIGHT + 15), 0xFFFFFF);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean handled = false;
        if (textField != null) handled = textField.mouseClicked(mouseX, mouseY, button) || handled;
        if (executeButton != null) handled = executeButton.mouseClicked(mouseX, mouseY, button) || handled;
        if (startButton != null) handled = startButton.mouseClicked(mouseX, mouseY, button) || handled;
        if (stopButton != null) handled = stopButton.mouseClicked(mouseX, mouseY, button) || handled;
        if (loopCountField != null) handled = loopCountField.mouseClicked(mouseX, mouseY, button) || handled;
        if (intervalField != null) handled = intervalField.mouseClicked(mouseX, mouseY, button) || handled;
        return handled;
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (textField != null && textField.isFocused()) {
            if (textField.handleKeyPress(keyCode, scanCode, modifiers)) {
                MacroStorage.saveMacros(textField.getText());
                return true;
            }
        }
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
    
    private void sendChat(ClientPlayerEntity player, String message) {
        if (player == null || player.connection == null) return;
        
        try {
            player.connection.send(new CChatMessagePacket(message));
        } catch (Exception e) {
            try {
                java.lang.reflect.Method chatMethod = player.getClass().getMethod("chat", String.class);
                chatMethod.invoke(player, message);
            } catch (Exception e2) {
                try {
                    java.lang.reflect.Method sendMethod = player.getClass().getMethod("sendChatMessage", String.class);
                    sendMethod.invoke(player, message);
                } catch (Exception e3) {
                    System.out.println("[Macros] Message: " + message);
                }
            }
        }
    }
    
    // ❌ Метод clearChat() удалён вместе с кнопкой Clear
    
    private void executeMacros() {
        if (textField == null) return;
        String text = textField.getText();
        if (text.isEmpty()) return;
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        
        String[] lines = text.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty()) {
                sendChat(mc.player, line);
            }
        }
    }
    
    private void startLoop() {
        if (isRunning || textField == null) return;
        
        try {
            int loops = 1;
            int interval = 1000;
            
            try {
                String loopText = loopCountField.getText();
                if (loopText != null && !loopText.isEmpty()) loops = Integer.parseInt(loopText);
            } catch (NumberFormatException ignored) {}
            
            try {
                String intervalText = intervalField.getText();
                if (intervalText != null && !intervalText.isEmpty()) interval = Integer.parseInt(intervalText);
            } catch (NumberFormatException ignored) {}
            
            isRunning = true;
            startButton.active = false;
            stopButton.active = true;
            
            final int finalLoops = loops;
            final int finalInterval = interval;
            
            executionThread = new Thread(() -> {
                String text = textField.getText();
                String[] lines = text.split("\n");
                
                for (int loop = 0; loop < finalLoops && isRunning; loop++) {
                    for (String line : lines) {
                        if (!isRunning) break;
                        line = line.trim();
                        if (!line.isEmpty()) {
                            Minecraft mc = Minecraft.getInstance();
                            if (mc.player != null) {
                                sendChat(mc.player, line);
                            }
                        }
                    }
                    if (isRunning && loop < finalLoops - 1) {
                        try { Thread.sleep(finalInterval); } catch (InterruptedException e) { break; }
                    }
                }
                
                isRunning = false;
                Minecraft.getInstance().execute(() -> {
                    startButton.active = true;
                    stopButton.active = false;
                });
            });
            executionThread.setDaemon(true);
            executionThread.start();
            
        } catch (Exception e) {
            isRunning = false;
            startButton.active = true;
            stopButton.active = false;
        }
    }
    
    private void stopLoop() {
        isRunning = false;
        if (executionThread != null && executionThread.isAlive()) executionThread.interrupt();
        startButton.active = true;
        stopButton.active = false;
    }
    
    @Override public String getTabId() { return "tab1"; }
    @Override public String getTabName() { return "Macros"; }
}
