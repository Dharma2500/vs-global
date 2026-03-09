package com.vs.vscombo.client.util;

import net.minecraft.client.Minecraft;

public class VSMainWindow {
    
    /**
     * ✅ Очистка чата (аналог F3+D) — с перебором имён полей и отладкой
     * Вызов: VSMainWindow.clearChatStatic();
     */
    public static void clearChatStatic() {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) return;
            
            // 🔍 Пробуем разные возможные имена поля для GUI
            Object gui = null;
            String[] possibleGuiFieldNames = {
                "gui",              // Mojang Mappings
                "ingameGUI",        // Forge Mappings  
                "field_71467_ac",   // Obfuscated name (1.16.5)
                "f_91014_"          // Mojang hashed name (newer mappings)
            };
            
            for (String fieldName : possibleGuiFieldNames) {
                try {
                    java.lang.reflect.Field guiField = mc.getClass().getDeclaredField(fieldName);
                    guiField.setAccessible(true);
                    gui = guiField.get(mc);
                    if (gui != null) {
                        System.out.println("[Macros] Found GUI field: " + fieldName);
                        break;
                    }
                } catch (NoSuchFieldException ignored) {
                    // Пробуем следующее имя
                }
            }
            
            if (gui == null) {
                System.err.println("[Macros] Could not find GUI field in Minecraft class");
                // 🔧 Отладка: выводим все поля класса для диагностики
                System.err.println("[Macros] Available fields in Minecraft:");
                for (java.lang.reflect.Field f : mc.getClass().getDeclaredFields()) {
                    System.err.println("[Macros]   - " + f.getName() + " : " + f.getType().getSimpleName());
                }
                return;
            }
            
            // 🔍 Пробуем разные возможные имена поля для чата
            Object chat = null;
            String[] possibleChatFieldNames = {
                "chat",             // Mojang Mappings
                "chatGUI",          // Forge Mappings
                "field_146291_b",   // Obfuscated name (1.16.5)
                "f_93017_"          // Mojang hashed name
            };
            
            for (String fieldName : possibleChatFieldNames) {
                try {
                    java.lang.reflect.Field chatField = gui.getClass().getDeclaredField(fieldName);
                    chatField.setAccessible(true);
                    chat = chatField.get(gui);
                    if (chat != null) {
                        System.out.println("[Macros] Found chat field: " + fieldName);
                        break;
                    }
                } catch (NoSuchFieldException ignored) {
                    // Пробуем следующее имя
                }
            }
            
            if (chat == null) {
                System.err.println("[Macros] Could not find chat field in GUI class");
                // 🔧 Отладка: выводим все поля класса GUI
                System.err.println("[Macros] Available fields in GUI: " + gui.getClass().getName());
                for (java.lang.reflect.Field f : gui.getClass().getDeclaredFields()) {
                    System.err.println("[Macros]   - " + f.getName() + " : " + f.getType().getSimpleName());
                }
                return;
            }
            
            // 🔍 Пробуем разные возможные имена метода очистки
            java.lang.reflect.Method clearMethod = null;
            String[] possibleMethodNames = {"clearMessages", "resetChat", "clearChatMessages"};
            
            for (String methodName : possibleMethodNames) {
                try {
                    clearMethod = chat.getClass().getMethod(methodName, boolean.class);
                    if (clearMethod != null) {
                        System.out.println("[Macros] Found clear method: " + methodName + "(boolean)");
                        break;
                    }
                } catch (NoSuchMethodException ignored) {
                    try {
                        clearMethod = chat.getClass().getMethod(methodName); // без параметров
                        if (clearMethod != null) {
                            System.out.println("[Macros] Found clear method: " + methodName + "()");
                            break;
                        }
                    } catch (NoSuchMethodException ignored2) {
                        // Пробуем следующее имя
                    }
                }
            }
            
            if (clearMethod != null) {
                if (clearMethod.getParameterCount() == 1) {
                    clearMethod.invoke(chat, true);
                } else {
                    clearMethod.invoke(chat);
                }
                System.out.println("[Macros] Chat cleared successfully!");
            } else {
                System.err.println("[Macros] Could not find clear method in chat class");
                // 🔧 Отладка: выводим методы, содержащие "clear" или "reset"
                System.err.println("[Macros] Available methods in chat: " + chat.getClass().getName());
                for (java.lang.reflect.Method m : chat.getClass().getDeclaredMethods()) {
                    String name = m.getName().toLowerCase();
                    if (name.contains("clear") || name.contains("reset")) {
                        System.err.println("[Macros]   - " + m.getName() + java.util.Arrays.toString(m.getParameterTypes()));
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("[Macros] Clear chat error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
