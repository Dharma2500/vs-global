package com.vs.vscombo.client.screen.tabs;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.vs.vscombo.client.screen.widget.CustomButton;
import com.vs.vscombo.client.util.VisualsManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.StringTextComponent;

public class Tab2 implements ITab {
    
    private CustomButton blockButton;
    private CustomButton stopButton;
    private FontRenderer font;
    private int contentX, contentY;
    
    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 20;
    
    @Override
    public void init(Minecraft minecraft, int width, int height, int contentX, int contentY) {
        this.font = minecraft.font;
        this.contentX = contentX;
        this.contentY = contentY;
        
        // ✅ Кнопка Block (включает эффект портала)
        this.blockButton = new CustomButton(
            contentX,
            contentY,
            BUTTON_WIDTH,
            BUTTON_HEIGHT,
            new StringTextComponent("Block"),
            btn -> VisualsManager.setBlockPortalEnabled(true)
        );
        
        // ✅ Кнопка Stop (отключает эффект портала)
        this.stopButton = new CustomButton(
            contentX,
            contentY + BUTTON_HEIGHT + 5,
            BUTTON_WIDTH,
            BUTTON_HEIGHT,
            new StringTextComponent("Stop"),
            btn -> VisualsManager.setBlockPortalEnabled(false)
        );
    }
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, 
                       float partialTicks, int contentX, int contentY) {
        this.contentX = contentX;
        this.contentY = contentY;
        
        // Рендерим кнопки
        if (blockButton != null) blockButton.render(matrixStack, mouseX, mouseY, partialTicks);
        if (stopButton != null) stopButton.render(matrixStack, mouseX, mouseY, partialTicks);
        
        // ✅ Индикатор состояния
        String status = VisualsManager.isBlockPortalEnabled() ? "§a§lENABLED" : "§c§lDISABLED";
        font.draw(matrixStack, "Block Portal: " + status, 
            (float)(contentX), (float)(contentY + BUTTON_HEIGHT * 2 + 15), 0xFFFFFF);
        
        // Описание
        font.draw(matrixStack, "Shows portal particles when", 
            (float)(contentX), (float)(contentY + BUTTON_HEIGHT * 2 + 35), 0x808080);
        font.draw(matrixStack, "a block is broken", 
            (float)(contentX), (float)(contentY + BUTTON_HEIGHT * 2 + 45), 0x808080);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean handled = false;
        if (blockButton != null) handled = blockButton.mouseClicked(mouseX, mouseY, button) || handled;
        if (stopButton != null) handled = stopButton.mouseClicked(mouseX, mouseY, button) || handled;
        return handled;
    }
    
    @Override public String getTabId() { return "tab2"; }
    @Override public String getTabName() { return "Visuals"; }
}
