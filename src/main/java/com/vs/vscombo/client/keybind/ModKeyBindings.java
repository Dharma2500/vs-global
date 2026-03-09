package com.vs.vscombo.client.keybind;

import com.vs.vscombo.VSGlobalMod;
import com.vs.vscombo.client.screen.MythicalEquipmentScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = VSGlobalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModKeyBindings {
    
    public static KeyBinding openModGui;
    
    public static void register() {
        openModGui = new KeyBinding(
            "key.vsglobal.open_gui",
            KeyConflictContext.IN_GAME,
            InputMappings.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "category.vsglobal.main"
        );
        ClientRegistry.registerKeyBinding(openModGui);
    }
    
    @SubscribeEvent
    public static void onKeyInput(net.minecraftforge.client.event.InputEvent.KeyInputEvent event) {
        // ✅ Правильный метод для официальных маппингов: isDown()
        while (openModGui.isDown()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.screen == null) {
                mc.setScreen(new MythicalEquipmentScreen());
                // ✅ Важно: сбрасываем нажатие, чтобы не открывать мод многократно
                openModGui.setKeyModifierAndCode(
                    openModGui.getKeyModifier(),
                    openModGui.getKey()
                );
                break;
            }
        }
    }
}
