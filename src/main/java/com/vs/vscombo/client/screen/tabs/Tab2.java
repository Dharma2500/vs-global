package com.vs.vscombo.client.screen.tabs;

import com.mojang.blaze3d.matrix.MatrixStack;

public class Tab2 implements ITab {
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, 
                       float partialTicks, int contentX, int contentY) {
        net.minecraft.client.Minecraft.getInstance().font.draw(matrixStack, 
            "§6§lArmor", (float)contentX, (float)contentY, 0xFFD4AF37);
    }
    
    // ✅ Принимаем новые параметры (даже если не используем)
    @Override
    public void init(net.minecraft.client.Minecraft minecraft, int width, int height, 
                     int contentX, int contentY) {
        // Пустая реализация
    }
    
    @Override public String getTabId() { return "tab2"; }
    @Override public String getTabName() { return "Armor"; }
}
