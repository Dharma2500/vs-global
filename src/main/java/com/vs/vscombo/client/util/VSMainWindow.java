package com.vs.vscombo.client.util;

import net.minecraft.client.Minecraft;

public class VSMainWindow {
    
    /**
     * ✅ Очистка чата (аналог F3+D) — поиск поля по типу IngameGui
     * Вызов: VSMainWindow.clearChatStatic();
     */
    public static void clearChatStatic() {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) return;
            
            // 🔍 1. Ищем поле типа IngameGui в классе Minecraft
            Object gui = null;
            java.lang.reflect.Field guiField = null;
            
            for (java.lang.reflect.Field field : mc.getClass().getDeclaredFields()) {
                String typeName = field.getType().getSimpleName();
                if (typeName.equals("IngameGUI") || typeName.equals("IngameGui")) {
                    field.setAccessible(true);
                    gui = field.get(mc);
                    guiField = field;
                    System.out.println("[Macros] Found GUI field by type: " + field.getName() + " (type: " + typeName + ")");
                    break;
                }
            }
            
            if (gui == null) {
                System.err.println("[Macros] Could not find IngameGui field in Minecraft class");
                return;
            }
            
            // 🔍 2. Ищем поле типа ChatGui в классе IngameGui
            Object chat = null;
            java.lang.reflect.Field chatField = null;
            
            for (java.lang.reflect.Field field : gui.getClass().getDeclaredFields()) {
                String typeName = field.getType().getSimpleName();
                if (typeName.equals("ChatGui") || typeName.equals("ChatScreen")) {
                    field.setAccessible(true);
                    chat = field.get(gui);
                    chatField = field;
                    System.out.println("[Macros] Found chat field by type: " + field.getName() + " (type: " + typeName + ")");
                    break;
                }
            }
            
            if (chat == null) {
                System.err.println("[Macros] Could not find ChatGui field in IngameGui class");
                return;
            }
            
            // 🔍 3. Ищем метод очистки (clearMessages, resetChat, и т.д.)
            java.lang.reflect.Method clearMethod = null;
            
            // Пробуем с boolean параметром
            try {
                clearMethod = chat.getClass().getMethod("clearMessages", boolean.class);
                System.out.println("[Macros] Found method: clearMessages(boolean)");
            } catch (NoSuchMethodException e1) {
                try {
                    clearMethod = chat.getClass().getMethod("clearMessages");
                    System.out.println("[Macros] Found method: clearMessages()");
                } catch (NoSuchMethodException e2) {
                    try {
                        clearMethod = chat.getClass().getMethod("resetChat");
                        System.out.println("[Macros] Found method: resetChat()");
                    } catch (NoSuchMethodException e3) {
                        try {
                            clearMethod = chat.getClass().getMethod("clearChatMessages", boolean.class);
                            System.out.println("[Macros] Found method: clearChatMessages(boolean)");
                        } catch (NoSuchMethodException e4) {
                            System.err.println("[Macros] Could not find clear method");
                        }
                    }
                }
            }
            
            // ✅ 4. Вызываем метод очистки
            if (clearMethod != null) {
                if (clearMethod.getParameterCount() == 1) {
                    clearMethod.invoke(chat, true); // true = очистить всё
                } else {
                    clearMethod.invoke(chat);
                }
                System.out.println("[Macros] ✅ Chat cleared successfully!");
            }
            
        } catch (Exception e) {
            System.err.println("[Macros] Clear chat error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
