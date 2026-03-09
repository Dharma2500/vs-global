package com.vs.vscombo.client.screen.tabs;

import com.mojang.blaze3d.matrix.MatrixStack;

public class Tab3 implements ITab {
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, 
                       float partialTicks, int contentX, int contentY) {
        net.minecraft.client.Minecraft.getInstance().font.draw(matrixStack, 
            "§6§lArtifacts", (float)contentX, (float)contentY, 0xFFD4AF37);
    }
    @Override public String getTabId() { return "tab3"; }
    @Override public String getTabName() { return "Artifacts"; }
}
