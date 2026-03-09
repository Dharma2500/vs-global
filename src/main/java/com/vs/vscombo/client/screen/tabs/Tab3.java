package com.vs.vscombo.client.screen.tabs;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;

public class Tab3 implements ITab {
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        mc.fontRenderer.drawString(matrixStack, "Content of Tab 3", 10, 10, 0xFFFFFF);
        mc.fontRenderer.drawString(matrixStack, "This is test3 tab content", 10, 25, 0xAAAAAA);
    }
    
    @Override
    public String getTabId() {
        return "tab3";
    }
    
    @Override
    public String getTabName() {
        return "test3";
    }
}
