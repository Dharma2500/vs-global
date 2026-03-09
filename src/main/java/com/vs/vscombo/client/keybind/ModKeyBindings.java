package com.vs.vscombo.client.keybind;

import com.vs.vscombo.VSGlobalMod; // ✅ MOD_ID теперь "vsglobal"
import com.vs.vscombo.client.screen.MythicalEquipmentScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = VSGlobalMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModKeyBindings {
    
    public static KeyBinding openGuiKey;
    
    public static void register() {
        openGuiKey = new KeyBinding(
            "key.vsglobal.open_gui",              // ✅ Было: key.vscombo.open_gui
            InputMappings.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "key.categories.vsglobal"             // ✅ Было: key.categories.vscombo
        );
        ClientRegistry.registerKeyBinding(openGuiKey);
    }
    
    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (openGuiKey.isDown() && Minecraft.getInstance().currentScreen == null) {
            Minecraft.getInstance().displayGuiScreen(new MythicalEquipmentScreen());
        }
    }
}
