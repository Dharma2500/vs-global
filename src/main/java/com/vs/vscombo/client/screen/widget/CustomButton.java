package com.vs.vscombo.client.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

public class CustomButton extends Button {
    
    public CustomButton(int x, int y, int widthIn, int heightIn, ITextComponent textIn, IPressable onPressIn) {
        super(x, y, widthIn, heightIn, textIn, onPressIn);
    }
    
    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // ✅ Цвет фона в зависимости от состояния
        int bgColor;
        if (!this.active) {
            bgColor = 0xFF303030; // Неактивная
        } else if (this.isHovered()) {
            bgColor = 0xFF404040; // Наведение
        } else {
            bgColor = 0xFF202020; // Обычная
        }
        
        // ✅ Рисуем фон (без белых полосок!)
        AbstractGui.fill(matrixStack, this.x, this.y, this.x + this.width, this.y + this.height, bgColor);
        
        // ✅ Только вертикальные границы (убираем горизонтальные белые линии)
        AbstractGui.fill(matrixStack, this.x, this.y, this.x + 1, this.y + this.height, 0x60FFFFFF); // Левая
        AbstractGui.fill(matrixStack, this.x + this.width - 1, this.y, this.x + this.width, this.y + this.height, 0x40000000); // Правая
        
        // ✅ Цвет текста
        int textColor;
        if (!this.active) {
            textColor = 0xFFAAAAAA; // Серый
        } else if (this.isHovered()) {
            textColor = 0xFF40FF40; // Зелёный при наведении
        } else {
            textColor = 0xFFFFFFFF; // Белый
        }
        
        // ✅ Центрируем текст
        FontRenderer font = Minecraft.getInstance().font;
        String message = this.getMessage().getFormattedText(); // ✅ Mojang Mappings
        int textWidth = font.width(message); // ✅ Mojang Mappings: width() вместо getStringWidth()
        int textX = this.x + (this.width - textWidth) / 2;
        int textY = this.y + (this.height - 9) / 2; // 9 = высота шрифта
        
        // ✅ Рисуем текст (float координаты для Mojang Mappings)
        font.draw(matrixStack, message, (float)textX, (float)textY, textColor);
    }
}
