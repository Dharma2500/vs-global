package com.vs.vscombo.client.screen.tabs;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;

public class Tab2 implements ITab { // ✅ Класс называется Tab2, файл Tab2.java
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        mc.font.drawString(matrixStack, "Content of Tab 2", 10f, 10f, 0xFFFFFF);
        mc.font.drawString(matrixStack, "This is test2 tab content", 10f, 25f, 0xAAAAAA);
    }
    @Override public String getTabId() { return "tab2"; }
    @Override public String getTabName() { return "test2"; }
}
