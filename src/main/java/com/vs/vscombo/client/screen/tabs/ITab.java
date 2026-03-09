package com.vs.vscombo.client.screen.tabs;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;

public interface ITab {
    void render(MatrixStack matrixStack, int mouseX, int mouseY, 
                float partialTicks, int contentX, int contentY);
    
    // ✅ Передаём координаты контента в init()
    default void init(Minecraft minecraft, int width, int height, int contentX, int contentY) {}
    
    default boolean mouseClicked(double mouseX, double mouseY, int button) { return false; }
    default boolean keyPressed(int keyCode, int scanCode, int modifiers) { return false; }
    default boolean charTyped(char codePoint, int modifiers) { return false; }
    
    String getTabId();
    String getTabName();
}
