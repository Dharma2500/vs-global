package com.vs.vscombo.client.screen.tabs;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;

public interface ITab {
    /**
     * Отрисовка содержимого вкладки
     */
    void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks);
    
    /**
     * Инициализация вкладки
     */
    default void init(Minecraft minecraft, int width, int height) {}
    
    /**
     * Обработка нажатий мыши
     */
    default boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }
    
    /**
     * Обработка нажатий клавиш
     */
    default boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }
    
    /**
     * Внутреннее имя вкладки
     */
    String getTabId();
    
    /**
     * Отображаемое имя вкладки
     */
    String getTabName();
}
