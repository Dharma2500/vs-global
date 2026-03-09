package com.vs.vscombo.client.event;

import com.vs.vscombo.VSGlobalMod;
import com.vs.vscombo.client.keybind.ModKeyBindings;
import com.vs.vscombo.client.screen.MythicalEquipmentScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = VSGlobalMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandler {
    
    /**
     * ✅ Обработка нажатия клавиши для открытия мода
     * Регистрируется на FORGE шине (не MOD), поэтому может слушать InputEvent
     */
    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (ModKeyBindings.openModGui != null && ModKeyBindings.openModGui.isDown()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.screen == null) {
                mc.setScreen(new MythicalEquipmentScreen());
            }
        }
    }
}
