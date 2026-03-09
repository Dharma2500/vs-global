package com.vs.vscombo.client.util;

import net.minecraft.client.Minecraft;

public class VSMainWindow {
    
    /**
     * ✅ Очистка чата (аналог F3+D) — с полной отладкой полей
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
            
            // 🔍 2. Выводим ВСЕ поля IngameGui для отладки
            System.out.println("[Macros] === All fields in IngameGui ===");
            Object chat = null;
            
            for (java.lang.reflect.Field field : gui.getClass().getDeclaredFields()) {
                String fieldName = field.getName();
                String typeName = field.getType().getSimpleName();
                System.out.println("[Macros]   Field: " + fieldName + " : " + typeName);
                
                // Ищем поле чата по разным возможным типам
                if (typeName.equals("ChatGui") || 
                    typeName.equals("ChatScreen") || 
                    typeName.equals("NewChatGui") ||
                    fieldName.toLowerCase().contains("chat")) {
                    field.setAccessible(true);
                    chat = field.get(gui);
                    System.out.println("[Macros] >>> Found chat field: " + fieldName + " (type: " + typeName + ")");
                }
            }
            System.out.println("[Macros] === End of IngameGui fields ===");
            
            if (chat == null) {
                System.err.println("[Macros] Could not find chat field in IngameGui class");
                return;
            }
            
            // 🔍 3. Ищем метод очистки
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
                        try {
                            clearMethod = chat.getClass().getMethod("clearChatMessages", boolean.class);
                            System.out.println("[Macros] Found method: clearChatMessages(boolean)");
                        } catch (NoSuchMethodException e4) {
                            // Выводим все методы содержащие "clear" или "reset"
                            System.out.println("[Macros] === All clear/reset methods in chat class ===");
                            for (java.lang.reflect.Method m : chat.getClass().getDeclaredMethods()) {
                                String name = m.getName().toLowerCase();
                                if (name.contains("clear") || name.contains("reset")) {
                                    System.out.println("[Macros]   Method: " + m.getName() + 
                                        java.util.Arrays.toString(m.getParameterTypes()));
                                }
                            }
                            System.out.println("[Macros] === End of methods ===");
                        }
                    }
                }
            }
            
            // ✅ 4. Вызываем метод очистки
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
