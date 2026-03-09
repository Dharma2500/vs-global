package com.vs.vscombo.client.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

public class CustomButton extends Button {
    
    public CustomButton(int x, int y, int width, int height, ITextComponent text, IPressable onPress) {
        super(x, y, width, height, text, onPress);
    }
    
    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        
        // ✅ Цвет фона кнопки
        int bgColor;
        if (!this.active) {
            bgColor = 0xFF303030; // Неактивная
        } else if (this.isHovered()) {
            bgColor = 0xFF404040; // Наведение
        } else {
            bgColor = 0xFF202020; // Обычная
        }
        
        // ✅ Рисуем фон кнопки (без белых полосок!)
        fill(matrixStack, this.x, this.y, this.x + this.width, this.y + this.height, bgColor);
        
        // ✅ Только вертикальные границы (без горизонтальных белых линий)
        // Левая граница
        fill(matrixStack, this.x, this.y, this.x + 1, this.y + this.height, 0x60FFFFFF);
        // Правая граница
        fill(matrixStack, this.x + this.width - 1, this.y, this.x + this.width, this.y + this.height, 0x40000000);
        
        // ✅ Текст кнопки
        int textColor = this.active ? 0xFFFFFFFF : 0xFFAAAAAA;
        if (this.isHovered() && this.active) {
            textColor = 0xFF40FF40; // Зелёный при наведении
        }
        
        this.renderString(matrixStack, fontRenderer, textColor);
    }
    
    private void fill(MatrixStack matrixStack, int x1, int y1, int x2, int y2, int color) {
        net.minecraft.client.gui.AbstractGui.fill(matrixStack, x1, y1, x2, y2, color);
    }
}
