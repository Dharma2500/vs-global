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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MythicalEquipmentScreen extends Screen {
    
    // ✅ Было: "vscombo" → Стало: "vsglobal"
    private static final ResourceLocation BACKGROUND = 
        new ResourceLocation("vsglobal", "textures/gui/mythical_equipment.png");
    
    private ITab[] tabs;
    private ITab currentTab;
    private Button[] tabButtons;
    
    public MythicalEquipmentScreen() {
        // ✅ Было: "gui.vscombo.mythical_equipment" → Стало: "gui.vsglobal.mythical_equipment"
        super(new TranslationTextComponent("gui.vsglobal.mythical_equipment"));
        this.tabs = new ITab[]{
            new Tab1(), new Tab2(), new Tab3(), new Tab4(), new Tab5()
        };
        this.currentTab = tabs[0];
    }
    
    // ... остальной код без изменений (init, render, etc.)
    // Все методы остаются как были, меняются только строки выше
}
