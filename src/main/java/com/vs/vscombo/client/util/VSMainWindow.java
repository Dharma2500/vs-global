package com.vs.vscombo.client.util;

import net.minecraft.client.Minecraft;

public class VSMainWindow {
    
    /**
     * ✅ Очистка чата (аналог F3+D) — прямой доступ через рефлексию
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
            
            // 🔍 Шаг 1: Ищем поле gui/ingameGUI в Minecraft
            Object ingameGui = null;
            String[] guiFieldNames = {"gui", "ingameGUI", "field_71456_v", "f_91014_"};
            
            for (String fieldName : guiFieldNames) {
                try {
                    java.lang.reflect.Field field = mc.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    ingameGui = field.get(mc);
                    if (ingameGui != null) {
                        System.out.println("[Macros] Found GUI via field: " + fieldName);
                        break;
                    }
                } catch (NoSuchFieldException ignored) {}
            }
            
            if (ingameGui == null) {
                System.err.println("[Macros] Could not find IngameGui");
                // Пробуем найти NewChatGui напрямую в Minecraft
                findAndClearChatDirectly(mc);
                return;
            }
            
            // 🔍 Шаг 2: Ищем поле chat в IngameGui
            Object chatGui = null;
            String[] chatFieldNames = {"chat", "chatGUI", "field_146291_b", "f_93017_"};
            
            for (String fieldName : chatFieldNames) {
                try {
                    java.lang.reflect.Field field = ingameGui.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    chatGui = field.get(ingameGui);
                    if (chatGui != null) {
                        System.out.println("[Macros] Found ChatGui via field: " + fieldName);
                        System.out.println("[Macros] ChatGui class: " + chatGui.getClass().getName());
                        break;
                    }
                } catch (NoSuchFieldException ignored) {}
            }
            
            // Если не нашли через поля, ищем в всех полях IngameGui
            if (chatGui == null) {
                System.out.println("[Macros] Searching all fields in IngameGui...");
                for (java.lang.reflect.Field field : ingameGui.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    Object value = field.get(ingameGui);
                    if (value != null) {
                        String className = value.getClass().getName();
                        if (className.contains("ChatGui") || className.contains("NewChatGui")) {
                            chatGui = value;
                            System.out.println("[Macros] Found ChatGui via field: " + field.getName());
                            System.out.println("[Macros] ChatGui class: " + className);
                            break;
                        }
                    }
                }
            }
            
            if (chatGui == null) {
                System.err.println("[Macros] Could not find ChatGui in IngameGui");
                // Пробуем найти NewChatGui напрямую в Minecraft
                findAndClearChatDirectly(mc);
                return;
            }
            
            // ✅ Шаг 3: Вызываем метод очистки
            invokeClearMethod(chatGui);
            
        } catch (Exception e) {
            System.err.println("[Macros] Clear chat error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 🔍 Прямой поиск NewChatGui в классе Minecraft
     */
    private static void findAndClearChatDirectly(Minecraft mc) {
        System.out.println("[Macros] Searching for NewChatGui directly in Minecraft...");
        
        try {
            for (java.lang.reflect.Field field : mc.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(mc);
                
                if (value != null) {
                    String className = value.getClass().getName();
                    
                    // Ищем любой класс содержащий "ChatGui"
                    if (className.contains("ChatGui") || className.contains("NewChatGui")) {
                        System.out.println("[Macros] Found chat object via field: " + field.getName());
                        System.out.println("[Macros] Chat class: " + className);
                        invokeClearMethod(value);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[Macros] Direct search error: " + e.getMessage());
        }
        
        System.err.println("[Macros] Could not find NewChatGui anywhere");
    }
    
    /**
     * ✅ Вызывает метод очистки чата
     */
    private static void invokeClearMethod(Object chatGui) {
        if (chatGui == null) {
            System.err.println("[Macros] ChatGui is null");
            return;
        }
        
        System.out.println("[Macros] === Searching clear methods in " + chatGui.getClass().getSimpleName() + " ===");
        
        // Список методов в порядке приоритета
        String[] methodNames = {
            "clearMessages", "resetChat", "clearChat", "cleanChat", 
            "removeAllMessages", "deleteMessages", "wipeChat", "clear"
        };
        
        for (String methodName : methodNames) {
            try {
                // Пробуем с boolean параметром
                java.lang.reflect.Method m = chatGui.getClass().getMethod(methodName, boolean.class);
                m.setAccessible(true);
                m.invoke(chatGui, true);
                System.out.println("[Macros] ✅ Invoked: " + methodName + "(true)");
                System.out.println("[Macros] === Chat cleared successfully! ===");
                return;
            } catch (NoSuchMethodException e1) {
                try {
                    // Пробуем без параметров
                    java.lang.reflect.Method m = chatGui.getClass().getMethod(methodName);
                    m.setAccessible(true);
                    m.invoke(chatGui);
                    System.out.println("[Macros] ✅ Invoked: " + methodName + "()");
                    System.out.println("[Macros] === Chat cleared successfully! ===");
                    return;
                } catch (Exception e2) {
                    // Пробуем следующий метод
                }
            } catch (Exception e) {
                System.out.println("[Macros] Failed to invoke " + methodName + ": " + e.getMessage());
            }
        }
        
        // 🔍 Если не нашли стандартные методы, выводим ВСЕ методы для отладки
        System.out.println("[Macros] === All methods containing 'clear', 'reset', 'clean' ===");
        for (java.lang.reflect.Method method : chatGui.getClass().getDeclaredMethods()) {
            String name = method.getName().toLowerCase();
            if (name.contains("clear") || name.contains("reset") || name.contains("clean") || 
                name.contains("remove") || name.contains("delete") || name.contains("wipe")) {
                System.out.println("[Macros] Found: " + method.getName() + 
                    java.util.Arrays.toString(method.getParameterTypes()));
            }
        }
        System.out.println("[Macros] === End of methods ===");
        
        System.err.println("[Macros] Could not find suitable clear method");
    }
}
