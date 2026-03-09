package com.vs.vscombo.client.util;

import net.minecraft.client.Minecraft;

public class VSMainWindow {
    
    /**
     * ✅ Очистка чата (аналог F3+D) — для официальных маппингов Mojang 1.16.5
     * Вызов: VSMainWindow.clearChatStatic();
     */
    public static void clearChatStatic() {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) return;
            
            // ✅ Mojang Mappings 1.16.5: прямой доступ к полям
            // mc.gui -> ChatGui -> clearMessages(true)
            if (mc.gui != null && mc.gui.chat != null) {
                mc.gui.chat.clearMessages(true);
            }
        } catch (Exception e) {
            // Фолбэк через рефлексию (на случай обфускации)
            try {
                java.lang.reflect.Field guiField = Minecraft.class.getDeclaredField("gui");
                guiField.setAccessible(true);
                Object gui = guiField.get(Minecraft.getInstance());
                
                if (gui != null) {
                    java.lang.reflect.Field chatField = gui.getClass().getDeclaredField("chat");
                    chatField.setAccessible(true);
                    Object chat = chatField.get(gui);
                    
                    if (chat != null) {
                        java.lang.reflect.Method clearMethod = chat.getClass().getMethod("clearMessages", boolean.class);
                        clearMethod.invoke(chat, true);
                    }
                }
            } catch (Exception e2) {
                // Не удалось очистить чат
                System.out.println("[Macros] Could not clear chat: " + e2.getMessage());
            }
        }
    }
}
