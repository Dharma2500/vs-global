package com.vs.vscombo.client.screen.tabs;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;

public class Tab4 implements ITab {
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        mc.font.draw(matrixStack, "Content of Tab 4", 10f, 10f, 0xFFFFFF);
        mc.font.draw(matrixStack, "This is test4 tab content", 10f, 25f, 0xAAAAAA);
    }
    @Override public String getTabId() { return "tab4"; }
    @Override public String getTabName() { return "test4"; }
}
