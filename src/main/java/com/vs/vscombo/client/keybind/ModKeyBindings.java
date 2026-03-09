package com.vs.vscombo.client.keybind;

import com.vs.vscombo.VSGlobalMod;
import com.vs.vscombo.client.screen.MythicalEquipmentScreen;
import net.minecraft.client.Minecraft;
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
        // ✅ 1.16.5: KEY_R = 82 (GLFW key code), используем конструктор с int
        openGuiKey = new KeyBinding(
            "key.vscombo.open_gui",
            InputMappings.Type.KEYSYM,
            82, // GLFW_KEY_R
            "key.categories.vscombo"
        );
        ClientRegistry.registerKeyBinding(openGuiKey);
    }
    
    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        // ✅ 1.16.5: isDown() вместо isPressed()
        if (openGuiKey.isDown()) {
            Minecraft.getInstance().setScreen(new MythicalEquipmentScreen());
        }
    }
}
