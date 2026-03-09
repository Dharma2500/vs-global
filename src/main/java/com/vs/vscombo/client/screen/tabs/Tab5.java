package com.vs.vscombo.client.screen.tabs;

import com.mojang.blaze3d.matrix.MatrixStack;

public class Tab5 implements ITab {
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, 
                       float partialTicks, int contentX, int contentY) {
        // Вкладка пустая - содержимое будет добавлено позже
    }
    
    @Override public String getTabId() { return "tab5"; }
    @Override public String getTabName() { return "Bosses"; }
}
