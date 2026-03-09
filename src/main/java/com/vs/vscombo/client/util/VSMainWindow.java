package com.vs.vscombo.client.util;

import net.minecraft.client.Minecraft;

public class VSMainWindow {
    
    /**
     * ✅ Очистка чата — брутфорс-поиск любого метода очистки
     * Вызов: VSMainWindow.clearChatStatic();
     */
    public static void clearChatStatic() {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) return;
            
            System.out.println("[Macros] === Starting brute-force chat clear search ===");
            
            // 🔍 Ищем объект чата перебором всех полей
            Object chat = findChatByBruteForce(mc, 4);
            
            if (chat != null) {
                System.out.println("[Macros] Found chat object: " + chat.getClass().getName());
                
                // 🔍 Ищем и вызываем метод очистки
                if (invokeAnyClearMethod(chat)) {
                    System.out.println("[Macros] ✅ Chat cleared successfully!");
                    return;
                }
            }
            
            System.err.println("[Macros] Could not find or clear chat object");
            System.out.println("[Macros] === End of search ===");
            
        } catch (Exception e) {
            System.err.println("[Macros] Clear chat error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 🔍 Брутфорс-поиск объекта с методом очистки (рекурсивно)
     */
    private static Object findChatByBruteForce(Object obj, int depth) {
        if (depth <= 0 || obj == null) return null;
        
        try {
            Class<?> clazz = obj.getClass();
            String className = clazz.getSimpleName().toLowerCase();
            
            // Быстрая проверка: если класс похож на чат, проверяем его методы
            if (className.contains("chat") || className.contains("gui")) {
                if (hasAnyClearMethod(clazz)) {
                    System.out.println("[Macros] Potential chat class: " + clazz.getName());
                    return obj;
                }
            }
            
            // Перебираем все поля
            for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    
                    if (value != null) {
                        // Проверяем сам объект
                        if (hasAnyClearMethod(value.getClass())) {
                            System.out.println("[Macros] Found via field: " + field.getName() + 
                                " in " + clazz.getSimpleName() + " -> " + value.getClass().getSimpleName());
                            return value;
                        }
                        
                        // Рекурсивный поиск
                        Object found = findChatByBruteForce(value, depth - 1);
                        if (found != null) return found;
                    }
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}
        
        return null;
    }
    
    /**
     * ✅ Проверяет, есть ли в классе любой метод, похожий на очистку чата
     */
    private static boolean hasAnyClearMethod(Class<?> clazz) {
        for (java.lang.reflect.Method method : clazz.getDeclaredMethods()) {
            String name = method.getName().toLowerCase();
            if (name.contains("clear") || name.contains("reset") || name.contains("clean")) {
                Class<?>[] params = method.getParameterTypes();
                // Подходят методы: без параметров, с boolean, или с одним параметром любого типа
                if (params.length == 0 || (params.length == 1 && 
                    (params[0] == boolean.class || params[0] == Boolean.class))) {
                    System.out.println("[Macros] Found potential clear method: " + 
                        clazz.getSimpleName() + "." + method.getName() + 
                        java.util.Arrays.toString(params));
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * ✅ Вызывает первый найденный метод очистки
     */
    private static boolean invokeAnyClearMethod(Object obj) {
        try {
            for (java.lang.reflect.Method method : obj.getClass().getDeclaredMethods()) {
                String name = method.getName().toLowerCase();
                if (name.contains("clear") || name.contains("reset") || name.contains("clean")) {
                    Class<?>[] params = method.getParameterTypes();
                    method.setAccessible(true);
                    
                    try {
                        if (params.length == 0) {
                            method.invoke(obj);
                            System.out.println("[Macros] Invoked: " + method.getName() + "()");
                            return true;
                        } else if (params.length == 1 && 
                                  (params[0] == boolean.class || params[0] == Boolean.class)) {
                            method.invoke(obj, true);
                            System.out.println("[Macros] Invoked: " + method.getName() + "(true)");
                            return true;
                        }
                    } catch (Exception e) {
                        // Пробуем следующий метод
                        System.out.println("[Macros] Failed to invoke " + method.getName() + ": " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[Macros] Error invoking clear method: " + e.getMessage());
        }
        return false;
    }
}
