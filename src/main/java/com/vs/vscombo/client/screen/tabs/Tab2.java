package com.vs.vscombo.client.screen.tabs;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;

public class Tab2 implements ITab {
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        mc.fontRenderer.drawString(matrixStack, "Content of Tab 2", 10, 10, 0xFFFFFF);
        mc.fontRenderer.drawString(matrixStack, "This is test2 tab content", 10, 25, 0xAAAAAA);
    }
    
    @Override
    public String getTabId() {
        return "tab2";
    }
    
    @Override
    public String getTabName() {
        return "test2";
    }
}
