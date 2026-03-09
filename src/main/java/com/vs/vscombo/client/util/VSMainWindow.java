package com.vs.vscombo.client.util;

import net.minecraft.client.Minecraft;

public class VSMainWindow {
    
    /**
     * ✅ Очистка чата (аналог F3+D) — финальная версия
     * Вызов: VSMainWindow.clearChatStatic();
     */
    public static void clearChatStatic() {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) {
                System.err.println("[Macros] Minecraft instance is null");
                return;
            }
            
            System.out.println("[Macros] === Starting chat clear ===");
            
            Object chatGui = null;
            
            // 🔍 Способ 1: Через метод getChatGUI() у ForgeIngameGui
            try {
                Object gui = null;
                String[] guiFieldNames = {"gui", "ingameGUI", "field_71456_v"};
                
                for (String fieldName : guiFieldNames) {
                    try {
                        java.lang.reflect.Field field = mc.getClass().getDeclaredField(fieldName);
                        field.setAccessible(true);
                        gui = field.get(mc);
                        if (gui != null) {
                            System.out.println("[Macros] Found GUI via field: " + fieldName);
                            break;
                        }
                    } catch (NoSuchFieldException ignored) {}
                }
                
                if (gui != null) {
                    System.out.println("[Macros] GUI class: " + gui.getClass().getName());
                    
                    // Пытаемся вызвать метод getChatGUI()
                    try {
                        java.lang.reflect.Method getChatMethod = gui.getClass().getMethod("getChatGUI");
                        chatGui = getChatMethod.invoke(gui);
                        if (chatGui != null) {
                            System.out.println("[Macros] Found chat via getChatGUI() method");
                            System.out.println("[Macros] Chat class: " + chatGui.getClass().getName());
                        }
                    } catch (NoSuchMethodException e) {
                        System.out.println("[Macros] getChatGUI() method not found");
                    }
                }
            } catch (Exception e) {
                System.out.println("[Macros] Method 1 failed: " + e.getMessage());
            }
            
            // 🔍 Способ 2: Ищем NewChatGui напрямую в полях Minecraft
            if (chatGui == null) {
                System.out.println("[Macros] Trying method 2: Search in Minecraft fields...");
                for (java.lang.reflect.Field field : mc.getClass().getDeclaredFields()) {
                    try {
                        field.setAccessible(true);
                        Object value = field.get(mc);
                        if (value != null) {
                            String className = value.getClass().getName();
                            if (className.contains("NewChatGui") || className.contains("ChatGui")) {
                                chatGui = value;
                                System.out.println("[Macros] Found chat via field: " + field.getName());
                                System.out.println("[Macros] Chat class: " + className);
                                break;
                            }
                        }
                    } catch (Exception ignored) {}
                }
            }
            
            // 🔍 Способ 3: Ищем через поле gui.chat (если есть)
            if (chatGui == null) {
                System.out.println("[Macros] Trying method 3: Search gui.chat field...");
                try {
                    java.lang.reflect.Field guiField = mc.getClass().getDeclaredField("gui");
                    guiField.setAccessible(true);
                    Object gui = guiField.get(mc);
                    if (gui != null) {
                        for (java.lang.reflect.Field field : gui.getClass().getDeclaredFields()) {
                            field.setAccessible(true);
                            Object value = field.get(gui);
                            if (value != null) {
                                String className = value.getClass().getName();
                                if (className.contains("Chat")) {
                                    chatGui = value;
                                    System.out.println("[Macros] Found chat via gui." + field.getName());
                                    System.out.println("[Macros] Chat class: " + className);
                                    break;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("[Macros] Method 3 failed: " + e.getMessage());
                }
            }
            
            if (chatGui == null) {
                System.err.println("[Macros] ❌ Could not find chat GUI by any method");
                System.out.println("[Macros] === End of search ===");
                return;
            }
            
            // ✅ Вызываем метод очистки
            System.out.println("[Macros] === Searching clear methods ===");
            
            String[] methodNames = {"clearMessages", "resetChat", "clearChat", "cleanChat"};
            
            for (String methodName : methodNames) {
                try {
                    java.lang.reflect.Method m = chatGui.getClass().getMethod(methodName, boolean.class);
                    m.setAccessible(true);
                    m.invoke(chatGui, true);
                    System.out.println("[Macros] ✅ Invoked: " + methodName + "(true)");
                    System.out.println("[Macros] === Chat cleared successfully! ===");
                    return;
                } catch (NoSuchMethodException e1) {
                    try {
                        java.lang.reflect.Method m = chatGui.getClass().getMethod(methodName);
                        m.setAccessible(true);
                        m.invoke(chatGui);
                        System.out.println("[Macros] ✅ Invoked: " + methodName + "()");
                        System.out.println("[Macros] === Chat cleared successfully! ===");
                        return;
                    } catch (Exception e2) {}
                } catch (Exception e) {
                    System.out.println("[Macros] Failed " + methodName + ": " + e.getMessage());
                }
            }
            
            System.err.println("[Macros] ❌ No suitable clear method found");
            System.out.println("[Macros] === End of search ===");
            
        } catch (Exception e) {
            System.err.println("[Macros] Clear chat error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
