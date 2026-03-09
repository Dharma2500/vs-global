package com.vs.vscombo.client.keybind;

import com.vs.vscombo.VSGlobalMod;
import com.vs.vscombo.client.screen.MythicalEquipmentScreen;
import net.minecraft.client.Minecraft; // ✅ Импорт
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = VSGlobalMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModKeyBindings {
    
    public static KeyBinding openGuiKey;
    
    public static void register() {
        openGuiKey = new KeyBinding(
            "key.vscombo.open_gui",
            InputMappings.Type.KEYSYM,
            InputMappings.KEY_R, // ✅ 1.16.5: константа прямо в InputMappings
            "key.categories.vscombo"
        );
        ClientRegistry.registerKeyBinding(openGuiKey);
    }
    
    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        // ✅ 1.16.5: инстанс-метод isPressed()
        if (openGuiKey.isPressed()) {
            // ✅ 1.16.5: метод setScreen(), а не displayGuiScreen
            Minecraft.getInstance().setScreen(new MythicalEquipmentScreen());
        }
    }
}
