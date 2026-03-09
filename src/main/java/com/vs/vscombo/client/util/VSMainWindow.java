package com.vs.vscombo.client.util;

import net.minecraft.client.Minecraft;

public class VSMainWindow {
    
    /**
     * ✅ Очистка чата (аналог F3+D) — с рефлексией для protected полей
     * Вызов: VSMainWindow.clearChatStatic();
     */
    public static void clearChatStatic() {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) return;
            
            // Получаем поле gui через рефлексию
            java.lang.reflect.Field guiField = Minecraft.class.getDeclaredField("gui");
            guiField.setAccessible(true);
            Object gui = guiField.get(mc);
            
            if (gui == null) return;
            
            // Получаем защищённое поле chat через рефлексию
            java.lang.reflect.Field chatField = gui.getClass().getDeclaredField("chat");
            chatField.setAccessible(true);
            Object chat = chatField.get(gui);
            
            if (chat == null) return;
            
            // Вызываем метод clearMessages(boolean)
            java.lang.reflect.Method clearMethod = chat.getClass().getMethod("clearMessages", boolean.class);
            clearMethod.invoke(chat, true);
            
        } catch (Exception e) {
            // Если не вышло — пробуем альтернативные имена методов/полей
            try {
                Minecraft mc = Minecraft.getInstance();
                if (mc == null) return;
                
                java.lang.reflect.Field guiField = Minecraft.class.getDeclaredField("gui");
                guiField.setAccessible(true);
                Object gui = guiField.get(mc);
                
                if (gui == null) return;
                
                java.lang.reflect.Field chatField = gui.getClass().getDeclaredField("chat");
                chatField.setAccessible(true);
                Object chat = chatField.get(gui);
                
                if (chat == null) return;
                
                // Пробуем альтернативное имя метода
                java.lang.reflect.Method clearMethod = chat.getClass().getMethod("resetChat");
                clearMethod.invoke(chat);
                
            } catch (Exception e2) {
                // Не удалось очистить чат
                System.out.println("[Macros] Could not clear chat: " + e2.getMessage());
            }
        }
    }
}
