package com.vs.vscombo.client.util;

import net.minecraft.client.Minecraft;

public class VSMainWindow {
    
    /**
     * ✅ Очистка чата (аналог F3+D)
     * Вызов: VSMainWindow.clearChatStatic();
     */
    public static void clearChatStatic() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.ingameGUI != null && mc.ingameGUI.getChatGUI() != null) {
            mc.ingameGUI.getChatGUI().clearChatMessages(true);
        }
    }
}
