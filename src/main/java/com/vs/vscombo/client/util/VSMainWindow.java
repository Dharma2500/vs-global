package com.vs.vscombo.client.util;

import net.minecraft.client.Minecraft;

public class VSMainWindow {
    
    /**
     * ✅ Очистка чата (аналог F3+D) — через метод getChatGUI()
     * Вызов: VSMainWindow.clearChatStatic();
     */
    public static void clearChatStatic() {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) return;
            
            // 🔍 1. Ищем поле типа IngameGui в классе Minecraft
            Object gui = null;
            
            for (java.lang.reflect.Field field : mc.getClass().getDeclaredFields()) {
                String typeName = field.getType().getSimpleName();
                if (typeName.equals("IngameGUI") || typeName.equals("IngameGui")) {
                    field.setAccessible(true);
                    gui = field.get(mc);
                    System.out.println("[Macros] Found GUI field: " + field.getName() + " (type: " + typeName + ")");
                    break;
                }
            }
            
            if (gui == null) {
                System.err.println("[Macros] Could not find IngameGui field in Minecraft class");
                return;
            }
            
            // 🔍 2. Пытаемся получить чат через метод getChatGUI()
            Object chat = null;
            
            try {
                java.lang.reflect.Method getChatMethod = gui.getClass().getMethod("getChatGUI");
                chat = getChatMethod.invoke(gui);
                System.out.println("[Macros] Found chat via getChatGUI() method");
            } catch (NoSuchMethodException e1) {
                // Если метода нет, ищем поле с "chat" в имени
                System.out.println("[Macros] getChatGUI() method not found, trying fields...");
                
                for (java.lang.reflect.Field field : gui.getClass().getDeclaredFields()) {
                    String fieldName = field.getName().toLowerCase();
                    if (fieldName.contains("chat")) {
                        field.setAccessible(true);
                        chat = field.get(gui);
                        System.out.println("[Macros] Found chat field: " + field.getName() + " (type: " + field.getType().getSimpleName() + ")");
                        break;
                    }
                }
            }
            
            // 🔍 3. Если чат не найден, ищем в Minecraft напрямую
            if (chat == null) {
                System.out.println("[Macros] Chat not found in IngameGui, searching in Minecraft class...");
                
                for (java.lang.reflect.Field field : mc.getClass().getDeclaredFields()) {
                    String typeName = field.getType().getSimpleName();
                    if (typeName.equals("ChatGui") || 
                        typeName.equals("ChatScreen") || 
                        typeName.equals("NewChatGui")) {
                        field.setAccessible(true);
                        chat = field.get(mc);
                        System.out.println("[Macros] Found chat field in Minecraft: " + field.getName() + " (type: " + typeName + ")");
                        break;
                    }
                }
            }
            
            if (chat == null) {
                System.err.println("[Macros] Could not find chat GUI in any location");
                return;
            }
            
            // 🔍 4. Ищем метод очистки
            java.lang.reflect.Method clearMethod = null;
            
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
                        System.err.println("[Macros] Could not find clear method");
                    }
                }
            }
            
            // ✅ 5. Вызываем метод очистки
            if (clearMethod != null) {
                if (clearMethod.getParameterCount() == 1) {
                    clearMethod.invoke(chat, true);
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
