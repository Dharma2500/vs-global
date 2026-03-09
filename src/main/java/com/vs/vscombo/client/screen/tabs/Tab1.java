package com.vs.vscombo.client.screen.tabs;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;

public class Tab1 implements ITab {
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        
        // Пример контента вкладки
        mc.fontRenderer.drawString(matrixStack, "Content of Tab 1", 10, 10, 0xFFFFFF);
        mc.fontRenderer.drawString(matrixStack, "This is test1 tab content", 10, 25, 0xAAAAAA);
        
        // Здесь будет ваш уникальный контент для каждой вкладки
    }
    
    @Override
    public String getTabId() {
        return "tab1";
    }
    
    @Override
    public String getTabName() {
        return "test1";
    }
}
