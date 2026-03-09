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
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = VSGlobalMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModKeyBindings {
    
    public static KeyBinding openGuiKey;
    
    public static void register() {
        // ✅ Регистрация клавиши
        openGuiKey = new KeyBinding(
            "key.vscombo.open_gui",                    // Translation key
            InputMappings.Type.KEYSYM,                  // Тип ввода
            GLFW.GLFW_KEY_R,                            // GLFW код клавиши R
            "key.categories.vscombo"                    // Категория в настройках
        );
        ClientRegistry.registerKeyBinding(openGuiKey);  // ✅ Обязательно!
    }
    
    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        // ✅ 1.16.5: isDown() + проверка что экран ещё не открыт
        if (openGuiKey.isDown() && Minecraft.getInstance().currentScreen == null) {
            // ✅ 1.16.5: displayGuiScreen() вместо setScreen()
            Minecraft.getInstance().displayGuiScreen(new MythicalEquipmentScreen());
        }
    }
}
