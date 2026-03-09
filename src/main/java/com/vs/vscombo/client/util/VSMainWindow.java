package com.vs.vscombo.client.util;

import net.minecraft.client.Minecraft;

public class VSMainWindow {
    
    /**
     * ✅ Очистка чата — поиск по всем полям с правильной фильтрацией
     * Вызов: VSMainWindow.clearChatStatic();
     */
    public static void clearChatStatic() {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) return;
            
            System.out.println("[Macros] === Starting chat clear search ===");
            
            // 🔍 Ищем объект чата перебором всех полей
            Object chat = findChatObject(mc, 5);
            
            if (chat != null) {
                System.out.println("[Macros] Found chat object: " + chat.getClass().getName());
                
                // 🔍 Вызываем метод очистки
                if (invokeClearMethod(chat)) {
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
     * 🔍 Рекурсивный поиск объекта чата
     */
    private static Object findChatObject(Object obj, int depth) {
        if (depth <= 0 || obj == null) return null;
        
        try {
            Class<?> clazz = obj.getClass();
            String className = clazz.getSimpleName();
            String fullClassName = clazz.getName();
            
            // ✅ Проверяем текущий объект (если это чат)
            if (isChatClass(className, fullClassName)) {
                System.out.println("[Macros] Found chat class: " + fullClassName);
                return obj;
            }
            
            // 🔍 Перебираем все поля
            for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    
                    if (value != null) {
                        String valueClassName = value.getClass().getSimpleName();
                        String valueFullClassName = value.getClass().getName();
                        
                        // ✅ Проверяем поле
                        if (isChatClass(valueClassName, valueFullClassName)) {
                            System.out.println("[Macros] Found via field: " + field.getName() + 
                                " -> " + valueFullClassName);
                            return value;
                        }
                        
                        // 🔁 Рекурсивный поиск (но не в примитивах и строках)
                        if (!isPrimitiveOrCommon(valueClassName)) {
                            Object found = findChatObject(value, depth - 1);
                            if (found != null) return found;
                        }
                    }
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}
        
        return null;
    }
    
    /**
     * ✅ Проверяет, является ли класс классом чата
     */
    private static boolean isChatClass(String className, String fullClassName) {
        // Игнорируем java.util.concurrent и другие системные классы
        if (fullClassName.startsWith("java.util.concurrent") ||
            fullClassName.startsWith("java.lang") ||
            className.equals("CompletableFuture") ||
            className.equals("String") ||
            className.equals("Integer") ||
            className.equals("Boolean") ||
            className.equals("Float") ||
            className.equals("Double")) {
            return false;
        }
        
        // ✅ Ищем классы содержащие "Chat" в имени
        String lowerName = className.toLowerCase();
        if (lowerName.contains("chat") && 
            (lowerName.contains("gui") || lowerName.contains("screen"))) {
            return true;
        }
        
        // ✅ Специфичные имена классов чата
        if (className.equals("NewChatGui") || 
            className.equals("ChatGui") || 
            className.equals("ChatScreen") ||
            className.equals("GuiChat")) {
            return true;
        }
        
        return false;
    }
    
    /**
     * ✅ Проверяет, является ли тип примитивом или распространённым типом
     */
    private static boolean isPrimitiveOrCommon(String className) {
        return className.equals("String") ||
               className.equals("Integer") ||
               className.equals("Int") ||
               className.equals("Boolean") ||
               className.equals("Float") ||
               className.equals("Double") ||
               className.equals("Long") ||
               className.equals("Short") ||
               className.equals("Byte") ||
               className.equals("Char") ||
               className.equals("Object") ||
               className.equals("Class") ||
               className.equals("Thread") ||
               className.equals("Runnable") ||
               className.equals("Callable") ||
               className.equals("Future") ||
               className.equals("CompletableFuture") ||
               className.equals("Optional") ||
               className.equals("Stream") ||
               className.equals("List") ||
               className.equals("ArrayList") ||
               className.equals("Map") ||
               className.equals("HashMap") ||
               className.equals("Set") ||
               className.equals("HashSet");
    }
    
    /**
     * ✅ Вызывает метод очистки чата
     */
    private static boolean invokeClearMethod(Object chat) {
        if (chat == null) return false;
        
        // Список методов в порядке приоритета
        String[] methodNames = {
            "clearMessages", "resetChat", "clearChat", "cleanChat", 
            "removeAllMessages", "deleteMessages", "wipeChat", "clear"
        };
        
        for (String methodName : methodNames) {
            try {
                // Пробуем с boolean параметром
                java.lang.reflect.Method m = chat.getClass().getMethod(methodName, boolean.class);
                m.setAccessible(true);
                m.invoke(chat, true);
                System.out.println("[Macros] Invoked: " + methodName + "(true)");
                return true;
            } catch (NoSuchMethodException e1) {
                try {
                    // Пробуем без параметров
                    java.lang.reflect.Method m = chat.getClass().getMethod(methodName);
                    m.setAccessible(true);
                    m.invoke(chat);
                    System.out.println("[Macros] Invoked: " + methodName + "()");
                    return true;
                } catch (Exception e2) {
                    // Пробуем следующий метод
                }
            } catch (Exception e) {
                System.out.println("[Macros] Failed to invoke " + methodName + ": " + e.getMessage());
            }
        }
        
        // 🔍 Если не нашли стандартные методы, выводим все доступные методы для отладки
        System.out.println("[Macros] === All methods in " + chat.getClass().getSimpleName() + " ===");
        for (java.lang.reflect.Method method : chat.getClass().getDeclaredMethods()) {
            String name = method.getName().toLowerCase();
            if (name.contains("clear") || name.contains("reset") || name.contains("clean") || 
                name.contains("remove") || name.contains("delete") || name.contains("wipe")) {
                System.out.println("[Macros] Potential method: " + method.getName() + 
                    java.util.Arrays.toString(method.getParameterTypes()));
            }
        }
        System.out.println("[Macros] === End of methods ===");
        
        return false;
    }
}
