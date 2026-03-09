package com.vs.vscombo.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.vs.vscombo.client.screen.tabs.ITab;
import com.vs.vscombo.client.screen.tabs.Tab1;
import com.vs.vscombo.client.screen.tabs.Tab2;
import com.vs.vscombo.client.screen.tabs.Tab3;
import com.vs.vscombo.client.screen.tabs.Tab4;
import com.vs.vscombo.client.screen.tabs.Tab5;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MythicalEquipmentScreen extends Screen {
    
    private ITab[] tabs;
    private ITab currentTab;
    private Button[] tabButtons;
    private Button closeButton;
    private Button settingsButton;
    
    // Размеры GUI
    private static final int GUI_WIDTH = 512;
    private static final int GUI_HEIGHT = 320;
    private static final int SIDEBAR_WIDTH = 120;
    private static final int BUTTON_HEIGHT = 24;
    private static final int BUTTON_SPACING = 4;
    
    // Прозрачность (0-255)
    private static final int GLASS_ALPHA = 160;
    private static final int DARKEN_ALPHA = 96;
    
    public MythicalEquipmentScreen() {
        super(new TranslationTextComponent("gui.vsglobal.mythical_equipment"));
        this.tabs = new ITab[]{
            new Tab1(), new Tab2(), new Tab3(), new Tab4(), new Tab5()
        };
        this.currentTab = tabs[0];
    }
    
    @Override
    protected void init() {
        super.init();
        
        int guiLeft = (this.width - GUI_WIDTH) / 2;
        int guiTop = (this.height - GUI_HEIGHT) / 2;
        
        // Кнопка закрытия (X)
        this.closeButton = new Button(
            guiLeft + GUI_WIDTH - 28,
            guiTop + 6,
            22,
            22,
            new StringTextComponent("X"),
            button -> this.minecraft.setScreen(null)
        );
        this.addButton(this.closeButton);
        
        // Кнопка настроек (⚙)
        this.settingsButton = new Button(
            guiLeft + 6,
            guiTop + 6,
            22,
            22,
            new StringTextComponent("⚙"),
            button -> {
                // TODO: Открыть настройки
            }
        );
        this.addButton(this.settingsButton);
        
        // Кнопки вкладок слева
        this.tabButtons = new Button[5];
        String[] tabNames = {"Weapons", "Armor", "Artifacts", "Creatures", "Bosses"};
        
        for (int i = 0; i < 5; i++) {
            final int tabIndex = i;
            tabButtons[i] = new Button(
                guiLeft + 8,
                guiTop + 40 + (i * (BUTTON_HEIGHT + BUTTON_SPACING)),
                SIDEBAR_WIDTH - 16,
                BUTTON_HEIGHT,
                new StringTextComponent(tabNames[i]),
                button -> switchTab(tabIndex)
            );
            this.addButton(tabButtons[i]);
        }
        
        if (currentTab != null) {
            currentTab.init(this.minecraft, this.width, this.height);
        }
    }
    
    private void switchTab(int index) {
        if (index >= 0 && index < tabs.length) {
            this.currentTab = tabs[index];
            if (currentTab != null) {
                currentTab.init(this.minecraft, this.width, this.height);
            }
        }
    }
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // Затемнение фона (полупрозрачное чёрное)
        this.renderBackground(matrixStack);
        fill(matrixStack, 0, 0, this.width, this.height, 
            (DARKEN_ALPHA << 24) | 0x000000);
        
        int guiLeft = (this.width - GUI_WIDTH) / 2;
        int guiTop = (this.height - GUI_HEIGHT) / 2;
        
        // Основная стеклянная панель (фон GUI)
        drawGlassPanel(matrixStack, guiLeft, guiTop, GUI_WIDTH, GUI_HEIGHT, GLASS_ALPHA);
        
        // ✅ Заголовок изменён на "Created by Vitaly_Sokolov"
        String title = "Created by Vitaly_Sokolov";
        int titleWidth = this.font.width(title);
        this.font.draw(matrixStack, title, 
            (float)(guiLeft + (GUI_WIDTH - titleWidth) / 2), 
            (float)(guiTop + 12), 0xFFFFFFFF);
        
        // Разделительная линия под заголовком
        fill(matrixStack, guiLeft + 10, guiTop + 28, guiLeft + GUI_WIDTH - 10, 
            guiTop + 30, 0x50FFFFFF);
        
        // Боковая панель (левая)
        drawGlassPanel(matrixStack, guiLeft + 5, guiTop + 35, 
            SIDEBAR_WIDTH - 10, GUI_HEIGHT - 40, GLASS_ALPHA - 30);
        
        // Область контента (правая часть)
        drawGlassPanel(matrixStack, guiLeft + SIDEBAR_WIDTH + 5, guiTop + 35, 
            GUI_WIDTH - SIDEBAR_WIDTH - 10, GUI_HEIGHT - 40, GLASS_ALPHA - 30);
        
        // Рендерим кнопки
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        
        // ✅ Подсветка активной кнопки (только рамка, без полосок)
        for (int i = 0; i < tabButtons.length; i++) {
            boolean isActive = (currentTab == tabs[i]);
            if (isActive) {
                // Зелёная рамка вокруг активной кнопки
                drawBorder(matrixStack, 
                    tabButtons[i].x - 1, tabButtons[i].y - 1,
                    tabButtons[i].getWidth() + 2, tabButtons[i].getHeight() + 2,
                    0x8040FF40);
            }
        }
        
        // Рендерим содержимое текущей вкладки (пустое)
        if (currentTab != null) {
            int contentX = guiLeft + SIDEBAR_WIDTH + 15;
            int contentY = guiTop + 45;
            currentTab.render(matrixStack, mouseX, mouseY, partialTicks, contentX, contentY);
        }
    }
    
    /**
     * Рисует стеклянную панель с прозрачностью
     */
    private void drawGlassPanel(MatrixStack matrixStack, int x, int y, 
                                 int width, int height, int alpha) {
        // Фоновый цвет (тёмно-синий/серый с прозрачностью)
        int backgroundColor = (alpha << 24) | 0x1A1A2E;
        fill(matrixStack, x, y, x + width, y + height, backgroundColor);
        
        // Граница сверху (светлая)
        fill(matrixStack, x, y, x + width, y + 1, 0x70FFFFFF);
        // Граница слева (светлая)
        fill(matrixStack, x, y, x + 1, y + height, 0x70FFFFFF);
        // Граница снизу (тёмная)
        fill(matrixStack, x, y + height - 1, x + width, y + height, 0x50000000);
        // Граница справа (тёмная)
        fill(matrixStack, x + width - 1, y, x + width, y + height, 0x50000000);
    }
    
    /**
     * Рисует рамку вокруг области
     */
    private void drawBorder(MatrixStack matrixStack, int x, int y, 
                            int width, int height, int color) {
        // Верхняя граница
        fill(matrixStack, x, y, x + width, y + 1, color);
        // Нижняя граница
        fill(matrixStack, x, y + height - 1, x + width, y + height, color);
        // Левая граница
        fill(matrixStack, x, y, x + 1, y + height, color);
        // Правая граница
        fill(matrixStack, x + width - 1, y, x + width, y + height, color);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (currentTab != null) {
            currentTab.mouseClicked(mouseX, mouseY, button);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Закрытие по ESC
        if (keyCode == 256) {
            this.minecraft.setScreen(null);
            return true;
        }
        
        if (currentTab != null) {
            if (currentTab.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    
    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
