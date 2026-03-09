package com.vs.vscombo.client.util;

import net.minecraft.client.Minecraft;

public class VSMainWindow {
    
    /**
     * ✅ Очистка чата — полный вывод всех полей для диагностики
     * Вызов: VSMainWindow.clearChatStatic();
     */
    public static void clearChatStatic() {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) return;
            
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
                        System.out.println("[Macros] GUI class: " + ingameGui.getClass().getName());
                        break;
                    }
                } catch (NoSuchFieldException ignored) {}
            }
            
            if (ingameGui == null) {
                System.err.println("[Macros] Could not find IngameGui");
                return;
            }
            
            // 🔍 Шаг 2: Выводим ВСЕ поля IngameGui для диагностики
            System.out.println("[Macros] === ALL fields in IngameGui ===");
            Object chatGui = null;
            
            for (java.lang.reflect.Field field : ingameGui.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(ingameGui);
                String fieldType = field.getType().getName();
                String fieldSimpleName = field.getType().getSimpleName();
                
                System.out.println("[Macros] Field: " + field.getName() + " : " + fieldType);
                
                // Ищем поле, которое может быть чатом
                if (value != null) {
                    if (fieldSimpleName.contains("Chat") || 
                        fieldType.contains("ChatGui") || 
                        fieldType.contains("NewChatGui") ||
                        field.getName().toLowerCase().contains("chat")) {
                        chatGui = value;
                        System.out.println("[Macros] >>> POTENTIAL CHAT FIELD: " + field.getName() + 
                            " -> " + fieldType);
                        
                        // Выводим методы этого объекта
                        System.out.println("[Macros] === Methods in " + fieldSimpleName + " ===");
                        for (java.lang.reflect.Method method : value.getClass().getDeclaredMethods()) {
                            String methodName = method.getName().toLowerCase();
                            if (methodName.contains("clear") || methodName.contains("reset") || 
                                methodName.contains("clean") || methodName.contains("remove") ||
                                methodName.contains("delete") || methodName.contains("wipe")) {
                                System.out.println("[Macros]   Method: " + method.getName() + 
                                    java.util.Arrays.toString(method.getParameterTypes()));
                            }
                        }
                        System.out.println("[Macros] === End of methods ===");
                    }
                }
            }
            System.out.println("[Macros] === End of IngameGui fields ===");
            
            if (chatGui == null) {
                System.err.println("[Macros] Could not find ChatGui in IngameGui");
                System.out.println("[Macros] Try searching in TopkaChatCopier classes...");
                
                // 🔍 Шаг 3: Ищем в классах TopkaChatCopier
                findChatInTopkaClasses();
                return;
            }
            
            // ✅ Шаг 4: Вызываем метод очистки
            invokeClearMethod(chatGui);
            
        } catch (Exception e) {
            System.err.println("[Macros] Clear chat error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 🔍 Ищет чат в классах TopkaChatCopier
     */
    private static void findChatInTopkaClasses() {
        try {
            Minecraft mc = Minecraft.getInstance();
            
            // Перебираем все поля Minecraft
            for (java.lang.reflect.Field field : mc.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(mc);
                
                if (value != null) {
                    String className = value.getClass().getName();
                    
                    // Ищем классы TopkaChatCopier
                    if (className.toLowerCase().contains("topka") || 
                        className.toLowerCase().contains("chatcopier")) {
                        System.out.println("[Macros] Found Topka class: " + className);
                        
                        // Проверяем методы
                        for (java.lang.reflect.Method method : value.getClass().getDeclaredMethods()) {
                            String methodName = method.getName().toLowerCase();
                            if (methodName.contains("clear") || methodName.contains("reset") || 
                                methodName.contains("clean")) {
                                System.out.println("[Macros] Potential method: " + method.getName() + 
                                    java.util.Arrays.toString(method.getParameterTypes()));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[Macros] Topka search error: " + e.getMessage());
        }
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
        
        String[] methodNames = {
            "clearMessages", "resetChat", "clearChat", "cleanChat", 
            "removeAllMessages", "deleteMessages", "wipeChat", "clear"
        };
        
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
                System.out.println("[Macros] Failed to invoke " + methodName + ": " + e.getMessage());
            }
        }
        
        System.err.println("[Macros] Could not find suitable clear method");
    }
}
