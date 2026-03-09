package com.vs.vscombo.client.screen.tabs;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;

public class Tab1 implements ITab {
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, 
                       float partialTicks, int contentX, int contentY) {
        Minecraft mc = Minecraft.getInstance();
        
        // Заголовок вкладки
        mc.font.draw(matrixStack, "§6§lWeapons", 
            (float)contentX, (float)contentY, 0xFFD4AF37);
        
        // Описание
        mc.font.draw(matrixStack, "§7Browse mythical weapons", 
            (float)contentX, (float)(contentY + 15), 0xAAAAAA);
        
        // Пример контента
        mc.font.draw(matrixStack, "§fDragon Sword", 
            (float)contentX, (float)(contentY + 40), 0xFFFFFF);
        mc.font.draw(matrixStack, "§8Damage: §c+12", 
            (float)contentX, (float)(contentY + 52), 0xAAAAAA);
        mc.font.draw(matrixStack, "§8Fire Aspect II", 
            (float)contentX, (float)(contentY + 64), 0xAAAAAA);
        
        // Рамка для предмета
        int itemX = contentX + 150;
        int itemY = contentY + 40;
        
        // Фон слота
        fill(matrixStack, itemX, itemY, itemX + 18, itemY + 18, 0x80000000);
        // Граница слота
        drawBorder(matrixStack, itemX, itemY, 18, 0x60FFFFFF);
    }
    
    private void fill(MatrixStack matrixStack, int x1, int y1, int x2, int y2, int color) {
        net.minecraft.client.gui.AbstractGui.fill(matrixStack, x1, y1, x2, y2, color);
    }
    
    private void drawBorder(MatrixStack matrixStack, int x, int y, int size, int color) {
        // Верхняя и левая границы
        fill(matrixStack, x, y, x + size, y + 1, color);
        fill(matrixStack, x, y, x + 1, y + size, color);
        // Нижняя и правая границы
        fill(matrixStack, x, y + size - 1, x + size, y + size, 0x40000000);
        fill(matrixStack, x + size - 1, y, x + size, y + size, 0x40000000);
    }
    
    @Override public String getTabId() { return "tab1"; }
    @Override public String getTabName() { return "Weapons"; }
}
