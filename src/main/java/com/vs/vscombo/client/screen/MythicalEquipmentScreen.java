package com.vs.vscombo.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.vs.vscombo.client.screen.tabs.ITab;
import com.vs.vscombo.client.screen.tabs.Tab1;
import com.vs.vscombo.client.screen.tabs.Tab2;
import com.vs.vscombo.client.screen.tabs.Tab3;
import com.vs.vscombo.client.screen.tabs.Tab4;
import com.vs.vscombo.client.screen.tabs.Tab5;
import com.vs.vscombo.util.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class MythicalEquipmentScreen extends Screen {
    
    private static final ResourceLocation BACKGROUND = 
        new ResourceLocation("vscombo", "textures/gui/mythical_equipment.png");
    
    private ITab[] tabs;
    private ITab currentTab;
    private Button[] tabButtons;
    
    public MythicalEquipmentScreen() {
        super(new TranslationTextComponent("gui.vscombo.mythical_equipment"));
        this.tabs = new ITab[]{
            new Tab1(),
            new Tab2(),
            new Tab3(),
            new Tab4(),
            new Tab5()
        };
        this.currentTab = tabs[0];
    }
    
    @Override
    protected void init() {
        super.init();
        
        int guiLeft = (this.width - Constants.GUI_WIDTH) / 2;
        int guiTop = (this.height - Constants.GUI_HEIGHT) / 2;
        
        // Создаем кнопки вкладок
        this.tabButtons = new Button[5];
        for (int i = 0; i < 5; i++) {
            final int tabIndex = i;
            tabButtons[i] = new Button(
                guiLeft + Constants.BUTTON_X,
                guiTop + Constants.BUTTON_Y_START + (i * (Constants.BUTTON_HEIGHT + Constants.BUTTON_SPACING)),
                Constants.BUTTON_WIDTH,
                Constants.BUTTON_HEIGHT,
                new StringTextComponent(Constants.TAB_BUTTONS[i]),
                button -> switchTab(tabIndex)
            );
            this.addButton(tabButtons[i]);
        }
        
        // Инициализируем текущую вкладку
        if (currentTab != null) {
            currentTab.init(this.minecraft, this.width, this.height);
        }
    }
    
    private void switchTab(int index) {
        if (index >= 0 && index < tabs.length) {
            this.currentTab = tabs[index];
            // Обновляем состояние кнопок
            for (int i = 0; i < tabButtons.length; i++) {
                tabButtons[i].active = (i != index);
            }
            // Инициализируем новую вкладку
            currentTab.init(this.minecraft, this.width, this.height);
        }
    }
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // Рендерим фон
        this.renderBackground(matrixStack);
        
        int guiLeft = (this.width - Constants.GUI_WIDTH) / 2;
        int guiTop = (this.height - Constants.GUI_HEIGHT) / 2;
        
        // Рендерим фон GUI
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(BACKGROUND);
        this.blit(matrixStack, guiLeft, guiTop, 0, 0, Constants.GUI_WIDTH, Constants.GUI_HEIGHT);
        
        // Рендерим заголовок
        String title = "Created by Vitaly_Sokolov";
        int titleWidth = this.font.getStringWidth(title);
        this.font.drawString(matrixStack, title, 
            guiLeft + (Constants.GUI_WIDTH - titleWidth) / 2, 
            guiTop + 8, 0xFFFFFF);
        
        // Рендерим содержимое текущей вкладки
        if (currentTab != null) {
            // Область контента
            fill(matrixStack, 
                guiLeft + Constants.CONTENT_X, 
                guiTop + Constants.CONTENT_Y,
                guiLeft + Constants.CONTENT_X + Constants.CONTENT_WIDTH,
                guiTop + Constants.CONTENT_Y + Constants.CONTENT_HEIGHT,
                0xFF333333);
            
            // Рендерим контент вкладки
            currentTab.render(matrixStack, mouseX, mouseY, partialTicks);
        }
        
        // Рендерим кнопки и остальной UI
        super.render(matrixStack, mouseX, mouseY, partialTicks);
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
}
