package com.vs.vscombo.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.vs.vscombo.client.screen.tabs.ITab;
import com.vs.vscombo.client.screen.tabs.Tab1;
import com.vs.vscombo.client.screen.tabs.Tab2;
import com.vs.vscombo.client.screen.tabs.Tab3;
import com.vs.vscombo.client.screen.tabs.Tab4;
import com.vs.vscombo.client.screen.tabs.Tab5;
import com.vs.vscombo.client.screen.widget.CustomButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MythicalEquipmentScreen extends Screen {
    
    private ITab[] tabs;
    private ITab currentTab;
    private CustomButton[] tabButtons;
    private CustomButton closeButton;
    private CustomButton settingsButton;
    
    private static final int GUI_WIDTH = 512;
    private static final int GUI_HEIGHT = 320;
    private static final int SIDEBAR_WIDTH = 120;
    private static final int BUTTON_HEIGHT = 24;
    private static final int BUTTON_SPACING = 4;
    
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
        
        this.closeButton = new CustomButton(
            guiLeft + GUI_WIDTH - 28, guiTop + 6, 22, 22,
            new StringTextComponent("X"),
            button -> this.minecraft.setScreen(null)
        );
        this.addButton(this.closeButton);
        
        this.settingsButton = new CustomButton(
            guiLeft + 6, guiTop + 6, 22, 22,
            new StringTextComponent("⚙"),
            button -> {}
        );
        this.addButton(this.settingsButton);
        
        this.tabButtons = new CustomButton[5];
        String[] tabNames = {"Weapons", "Armor", "Artifacts", "Creatures", "Bosses"};
        
        for (int i = 0; i < 5; i++) {
            final int tabIndex = i;
            tabButtons[i] = new CustomButton(
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
        this.renderBackground(matrixStack);
        AbstractGui.fill(matrixStack, 0, 0, this.width, this.height, 
            (DARKEN_ALPHA << 24) | 0x000000);
        
        int guiLeft = (this.width - GUI_WIDTH) / 2;
        int guiTop = (this.height - GUI_HEIGHT) / 2;
        
        drawGlassPanel(matrixStack, guiLeft, guiTop, GUI_WIDTH, GUI_HEIGHT, GLASS_ALPHA);
        
        String title = "Created by Vitaly_Sokolov";
        int titleWidth = this.font.width(title);
        this.font.draw(matrixStack, title, 
            (float)(guiLeft + (GUI_WIDTH - titleWidth) / 2), 
            (float)(guiTop + 12), 0xFFFFFFFF);
        
        AbstractGui.fill(matrixStack, guiLeft + 10, guiTop + 28, guiLeft + GUI_WIDTH - 10, 
            guiTop + 30, 0x50FFFFFF);
        
        drawGlassPanel(matrixStack, guiLeft + 5, guiTop + 35, 
            SIDEBAR_WIDTH - 10, GUI_HEIGHT - 40, GLASS_ALPHA - 30);
        
        drawGlassPanel(matrixStack, guiLeft + SIDEBAR_WIDTH + 5, guiTop + 35, 
            GUI_WIDTH - SIDEBAR_WIDTH - 10, GUI_HEIGHT - 40, GLASS_ALPHA - 30);
        
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        
        for (int i = 0; i < tabButtons.length; i++) {
            if (currentTab == tabs[i]) {
                drawBorder(matrixStack, 
                    tabButtons[i].x - 1, tabButtons[i].y - 1,
                    tabButtons[i].getWidth() + 2, tabButtons[i].getHeight() + 2,
                    0x8040FF40);
            }
        }
        
        if (currentTab != null) {
            int contentX = guiLeft + SIDEBAR_WIDTH + 15;
            int contentY = guiTop + 45;
            currentTab.render(matrixStack, mouseX, mouseY, partialTicks, contentX, contentY);
        }
    }
    
    private void drawGlassPanel(MatrixStack matrixStack, int x, int y, int width, int height, int alpha) {
        int backgroundColor = (alpha << 24) | 0x1A1A2E;
        AbstractGui.fill(matrixStack, x, y, x + width, y + height, backgroundColor);
        AbstractGui.fill(matrixStack, x, y, x + width, y + 1, 0x70FFFFFF);
        AbstractGui.fill(matrixStack, x, y, x + 1, y + height, 0x70FFFFFF);
        AbstractGui.fill(matrixStack, x, y + height - 1, x + width, y + height, 0x50000000);
        AbstractGui.fill(matrixStack, x + width - 1, y, x + width, y + height, 0x50000000);
    }
    
    private void drawBorder(MatrixStack matrixStack, int x, int y, int width, int height, int color) {
        AbstractGui.fill(matrixStack, x, y, x + width, y + 1, color);
        AbstractGui.fill(matrixStack, x, y + height - 1, x + width, y + height, color);
        AbstractGui.fill(matrixStack, x, y, x + 1, y + height, color);
        AbstractGui.fill(matrixStack, x + width - 1, y, x + width, y + height, color);
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
    public boolean shouldCloseOnEsc() { return true; }
    @Override
    public boolean isPauseScreen() { return false; }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (currentTab != null && currentTab.charTyped(codePoint, modifiers)) {
            return true;
        }
        return super.charTyped(codePoint, modifiers);
}
