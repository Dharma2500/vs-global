package com.vs.vscombo.client.util;

import net.minecraft.client.Minecraft;

public class VSMainWindow {
    
    /**
     * ✅ Очистка чата (аналог F3+D) — универсальный поиск по методу clearMessages
     * Вызов: VSMainWindow.clearChatStatic();
     */
    public static void clearChatStatic() {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) return;
            
            Object chat = findChatObject(mc);
            
            if (chat != null) {
                invokeClearMethod(chat);
            } else {
                System.err.println("[Macros] Could not find chat object with clear method");
            }
            
        } catch (Exception e) {
            System.err.println("[Macros] Clear chat error: " + e.getMessage());
        }
    }
    
    /**
     * 🔍 Ищет объект чата, у которого есть метод clearMessages или resetChat
     */
    private static Object findChatObject(Minecraft mc) {
        // 1. Проверяем все поля в Minecraft
        for (java.lang.reflect.Field field : mc.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object value = field.get(mc);
                if (value != null && hasClearMethod(value)) {
                    System.out.println("[Macros] Found chat in Minecraft: " + field.getName());
                    return value;
                }
            } catch (Exception ignored) {}
        }
        
        // 2. Проверяем поля в IngameGui (если найдём)
        for (java.lang.reflect.Field guiField : mc.getClass().getDeclaredFields()) {
            String typeName = guiField.getType().getSimpleName();
            if (typeName.equals("IngameGUI") || typeName.equals("IngameGui")) {
                try {
                    guiField.setAccessible(true);
                    Object gui = guiField.get(mc);
                    if (gui != null) {
                        for (java.lang.reflect.Field field : gui.getClass().getDeclaredFields()) {
                            try {
                                field.setAccessible(true);
                                Object value = field.get(gui);
                                if (value != null && hasClearMethod(value)) {
                                    System.out.println("[Macros] Found chat in IngameGui: " + field.getName());
                                    return value;
                                }
                            } catch (Exception ignored) {}
                        }
                    }
                } catch (Exception ignored) {}
                break;
            }
        }
        
        // 3. Проверяем все поля рекурсивно (на случай кастомных модов)
        return findChatRecursive(mc, 3);
    }
    
    /**
     * 🔁 Рекурсивный поиск объекта с методом clearMessages
     */
    private static Object findChatRecursive(Object obj, int depth) {
        if (depth <= 0 || obj == null) return null;
        
        try {
            for (java.lang.reflect.Field field : obj.getClass().getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (value != null) {
                        if (hasClearMethod(value)) {
                            return value;
                        }
                        // Рекурсивный поиск вложенных объектов
                        Object found = findChatRecursive(value, depth - 1);
                        if (found != null) return found;
                    }
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}
        return null;
    }
    
    /**
     * ✅ Проверяет, есть ли у объекта метод clearMessages или resetChat
     */
    private static boolean hasClearMethod(Object obj) {
        if (obj == null) return false;
        Class<?> clazz = obj.getClass();
        
        try {
            clazz.getMethod("clearMessages", boolean.class);
            return true;
        } catch (NoSuchMethodException e1) {
            try {
                clazz.getMethod("clearMessages");
                return true;
            } catch (NoSuchMethodException e2) {
                try {
                    clazz.getMethod("resetChat");
                    return true;
                } catch (NoSuchMethodException e3) {
                    return false;
                }
            }
        }
    }
    
    /**
     * ✅ Вызывает метод очистки чата
     */
    private static void invokeClearMethod(Object chat) {
        try {
            java.lang.reflect.Method m = chat.getClass().getMethod("clearMessages", boolean.class);
            m.invoke(chat, true); // true = очистить всё
            System.out.println("[Macros] ✅ Chat cleared via clearMessages(true)!");
            return;
        } catch (Exception e1) {
            try {
                java.lang.reflect.Method m = chat.getClass().getMethod("clearMessages");
                m.invoke(chat);
                System.out.println("[Macros] ✅ Chat cleared via clearMessages()!");
                return;
            } catch (Exception e2) {
                try {
                    java.lang.reflect.Method m = chat.getClass().getMethod("resetChat");
                    m.invoke(chat);
                    System.out.println("[Macros] ✅ Chat cleared via resetChat()!");
                    return;
                } catch (Exception e3) {
                    System.err.println("[Macros] Could not invoke clear method");
                }
            }
        }
    }
}
