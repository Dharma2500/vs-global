package com.vs.vscombo.client.util;

import net.minecraft.client.Minecraft;

public class VSMainWindow {
    
    /**
     * ✅ Очистка чата — точный поиск NewChatGui + игнорирование ложных срабатываний
     * Вызов: VSMainWindow.clearChatStatic();
     */
    public static void clearChatStatic() {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) return;
            
            System.out.println("[Macros] === Starting targeted chat clear search ===");
            
            // 🔍 1. Ищем объект типа NewChatGui (точный поиск по имени класса)
            Object chat = findNewChatGui(mc, 4);
            
            if (chat != null) {
                System.out.println("[Macros] Found NewChatGui object: " + chat.getClass().getName());
                
                // 🔍 2. Выводим ВСЕ методы класса для отладки
                System.out.println("[Macros] === All methods in " + chat.getClass().getSimpleName() + " ===");
                for (java.lang.reflect.Method method : chat.getClass().getDeclaredMethods()) {
                    String name = method.getName().toLowerCase();
                    if (name.contains("clear") || name.contains("reset") || name.contains("clean") || 
                        name.contains("remove") || name.contains("delete") || name.contains("wipe")) {
                        System.out.println("[Macros] Potential clear method: " + method.getName() + 
                            java.util.Arrays.toString(method.getParameterTypes()));
                    }
                }
                System.out.println("[Macros] === End of methods ===");
                
                // 🔍 3. Пробуем вызвать найденные методы
                if (invokeKnownClearMethods(chat)) {
                    System.out.println("[Macros] ✅ Chat cleared successfully!");
                    return;
                }
            }
            
            // 🔍 4. Если не нашли — ищем через IngameGui.getChatGUI() (Forge API)
            if (chat == null) {
                chat = findChatViaForgeApi(mc);
                if (chat != null) {
                    if (invokeKnownClearMethods(chat)) {
                        System.out.println("[Macros] ✅ Chat cleared via Forge API!");
                        return;
                    }
                }
            }
            
            System.err.println("[Macros] Could not find or clear NewChatGui object");
            System.out.println("[Macros] === End of search ===");
            
        } catch (Exception e) {
            System.err.println("[Macros] Clear chat error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 🔍 Ищет объект класса NewChatGui рекурсивно
     */
    private static Object findNewChatGui(Object obj, int depth) {
        if (depth <= 0 || obj == null) return null;
        
        try {
            Class<?> clazz = obj.getClass();
            String className = clazz.getSimpleName();
            
            // ✅ Точная проверка: ищем именно NewChatGui
            if (className.equals("NewChatGui") || className.contains("ChatGui")) {
                // Игнорируем ложные срабатывания (CompletableFuture и др.)
                if (!className.equals("CompletableFuture") && 
                    !clazz.getName().startsWith("java.util.concurrent")) {
                    System.out.println("[Macros] Found chat class: " + clazz.getName());
                    return obj;
                }
            }
            
            // Перебираем все поля
            for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    
                    if (value != null) {
                        Class<?> valueClass = value.getClass();
                        String valueClassName = valueClass.getSimpleName();
                        
                        // ✅ Точная проверка значения
                        if ((valueClassName.equals("NewChatGui") || valueClassName.contains("ChatGui")) &&
                            !valueClassName.equals("CompletableFuture") &&
                            !valueClass.getName().startsWith("java.util.concurrent")) {
                            System.out.println("[Macros] Found via field: " + field.getName() + 
                                " -> " + valueClassName);
                            return value;
                        }
                        
                        // Рекурсивный поиск
                        Object found = findNewChatGui(value, depth - 1);
                        if (found != null) return found;
                    }
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}
        
        return null;
    }
    
    /**
     * 🔍 Пытается получить чат через Forge API: ingameGUI.getChatGUI()
     */
    private static Object findChatViaForgeApi(Minecraft mc) {
        try {
            // Ищем поле ingameGUI или gui
            Object gui = null;
            for (java.lang.reflect.Field field : mc.getClass().getDeclaredFields()) {
                String typeName = field.getType().getSimpleName();
                if (typeName.equals("IngameGUI") || typeName.equals("IngameGui")) {
                    field.setAccessible(true);
                    gui = field.get(mc);
                    break;
                }
            }
            
            if (gui != null) {
                // Пытаемся вызвать getChatGUI()
                try {
                    java.lang.reflect.Method getChatMethod = gui.getClass().getMethod("getChatGUI");
                    Object chat = getChatMethod.invoke(gui);
                    if (chat != null) {
                        System.out.println("[Macros] Found chat via getChatGUI(): " + chat.getClass().getName());
                        return chat;
                    }
                } catch (NoSuchMethodException ignored) {
                    // Метода нет, ищем поле chat
                    for (java.lang.reflect.Field field : gui.getClass().getDeclaredFields()) {
                        String typeName = field.getType().getSimpleName();
                        if (typeName.equals("NewChatGui") || typeName.equals("ChatGui")) {
                            field.setAccessible(true);
                            Object chat = field.get(gui);
                            if (chat != null) {
                                System.out.println("[Macros] Found chat field in GUI: " + field.getName());
                                return chat;
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
        return null;
    }
    
    /**
     * ✅ Вызывает известные методы очистки чата
     */
    private static boolean invokeKnownClearMethods(Object chat) {
        if (chat == null) return false;
        
        // Список известных методов очистки в порядке приоритета
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
        
        return false;
    }
}
