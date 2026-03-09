package com.vs.vscombo.client.keybind;

import com.vs.vscombo.VSGlobalMod;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

@Mod.EventBusSubscriber(modid = VSGlobalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModKeyBindings {
    
    public static KeyBinding openModGui;
    
    public static void register() {
        openModGui = new KeyBinding(
            "key.vsglobal.open_gui",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            GLFW.GLFW_KEY_R,
            "category.vsglobal.main"
        );
        ClientRegistry.registerKeyBinding(openModGui);
    }
    
    @SubscribeEvent
    public static void onKeyInput(net.minecraftforge.client.event.InputEvent.KeyInputEvent event) {
        if (openModGui.isPressed()) {
            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
            if (mc.screen == null) {
                mc.setScreen(new com.vs.vscombo.client.screen.MythicalEquipmentScreen());
            }
        }
    }
}
