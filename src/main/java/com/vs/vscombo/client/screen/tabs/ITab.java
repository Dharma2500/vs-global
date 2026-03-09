package com.vs.vscombo.client.screen.tabs;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;

public interface ITab {
    void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, int contentX, int contentY);
    default void init(Minecraft minecraft, int width, int height) {}
    default boolean mouseClicked(double mouseX, double mouseY, int button) { return false; }
    default boolean keyPressed(int keyCode, int scanCode, int modifiers) { return false; }
    
    // ✅ Новый метод для обработки символов
    default boolean charTyped(char codePoint, int modifiers) { return false; }
    
    String getTabId();
    String getTabName();
}
