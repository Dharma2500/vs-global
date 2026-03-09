package com.vs.vscombo.client.screen.tabs;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;

public class Tab5 implements ITab {
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        mc.font.draw(matrixStack, "Content of Tab 5", 10f, 10f, 0xFFFFFF);
        mc.font.draw(matrixStack, "This is test5 tab content", 10f, 25f, 0xAAAAAA);
    }
    @Override public String getTabId() { return "tab5"; }
    @Override public String getTabName() { return "test5"; }
}
