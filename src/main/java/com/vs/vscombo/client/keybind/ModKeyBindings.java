package com.vs.vscombo.client.keybind;

import com.vs.vscombo.VSGlobalMod;
import com.vs.vscombo.client.screen.MythicalEquipmentScreen;
import net.minecraft.client.Minecraft; // ✅ Добавлен импорт
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings; // ✅ InputMappings.Input
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
            InputMappings.Input.KEY_R, // ✅ 1.16.5: Input.KEY_R
            "key.categories.vscombo"
        );
        ClientRegistry.registerKeyBinding(openGuiKey);
    }
    
    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        // ✅ 1.16.5: статический метод KeyBinding.isPressed()
        if (KeyBinding.isPressed(openGuiKey)) {
            Minecraft.getInstance().displayGuiScreen(new MythicalEquipmentScreen());
        }
    }
}
