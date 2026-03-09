package com.vs.vscombo.client.util;

import net.minecraft.client.Minecraft;

public class VSMainWindow {
    
    /**
     * ✅ Очистка чата (аналог F3+D) — проверенная реализация через рефлексию
     * Вызов: VSMainWindow.clearChatStatic();
     */
    public static void clearChatStatic() {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) return;
            
            // 1. Получаем поле gui из Minecraft
            java.lang.reflect.Field guiField = mc.getClass().getDeclaredField("gui");
            guiField.setAccessible(true);
            Object gui = guiField.get(mc);
            if (gui == null) return;
            
            // 2. Получаем защищённое поле chat из IngameGui
            java.lang.reflect.Field chatField = gui.getClass().getDeclaredField("chat");
            chatField.setAccessible(true);
            Object chat = chatField.get(gui);
            if (chat == null) return;
            
            // 3. Вызываем метод clearMessages(boolean) из ChatGui
            java.lang.reflect.Method clearMethod = chat.getClass().getMethod("clearMessages", boolean.class);
            clearMethod.invoke(chat, true); // true = очистить всё, включая историю
            
        } catch (NoSuchFieldException e) {
            System.err.println("[Macros] Field not found: " + e.getMessage());
        } catch (NoSuchMethodException e) {
            System.err.println("[Macros] Method not found: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[Macros] Clear chat error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
