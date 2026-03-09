package com.vs.vscombo.client.screen.tabs;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;

public class Tab1 implements ITab {
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        
        // ✅ 1.16.5: mc.font (не fontRenderer)
        mc.font.drawString(matrixStack, "Content of Tab 1", 10, 10, 0xFFFFFF);
        mc.font.drawString(matrixStack, "This is test1 tab content", 10, 25, 0xAAAAAA);
    }
    
    @Override
    public String getTabId() { return "tab1"; }
    
    @Override
    public String getTabName() { return "test1"; }
}
