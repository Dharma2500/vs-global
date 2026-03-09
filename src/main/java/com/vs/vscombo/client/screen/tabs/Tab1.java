package com.vs.vscombo.client.screen.tabs;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;

public class Tab1 implements ITab {
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        // ✅ 1.16.5: font.width() вместо getStringWidth(), draw() с float координатами
        mc.font.draw(matrixStack, "Content of Tab 1", 10f, 10f, 0xFFFFFF);
        mc.font.draw(matrixStack, "This is test1 tab content", 10f, 25f, 0xAAAAAA);
    }
    
    @Override public String getTabId() { return "tab1"; }
    @Override public String getTabName() { return "test1"; }
}
