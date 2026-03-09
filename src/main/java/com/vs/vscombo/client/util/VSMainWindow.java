package com.vs.vscombo.client.util;

import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class VSMainWindow {
    
    /**
     * ✅ Очистка чата (аналог F3+D) — симуляция нажатия клавиш
     * Вызов: VSMainWindow.clearChatStatic();
     */
    public static void clearChatStatic() {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) return;
            
            // Получаем handle окна
            long windowHandle = mc.getMainWindow().getHandle();
            
            // 🔍 Пробуем разные комбинации модификаторов для F3+D
            int[] modifiers = {
                GLFW.GLFW_MOD_CONTROL,  // Ctrl (иногда F3 мапится на Ctrl)
                GLFW.GLFW_MOD_ALT,      // Alt
                GLFW.GLFW_MOD_SHIFT,    // Shift
                0                       // Без модификатора
            };
            
            for (int mod : modifiers) {
                try {
                    // Эмулируем нажатие F3 (клавиша 290)
                    GLFW.glfwSetKey(windowHandle, GLFW.GLFW_KEY_F3, GLFW.GLFW_PRESS, mod);
                    GLFW.glfwSetKey(windowHandle, GLFW.GLFW_KEY_F3, GLFW.GLFW_RELEASE, mod);
                    
                    // Эмулируем нажатие D
                    GLFW.glfwSetKey(windowHandle, GLFW.GLFW_KEY_D, GLFW.GLFW_PRESS, mod);
                    GLFW.glfwSetKey(windowHandle, GLFW.GLFW_KEY_D, GLFW.GLFW_RELEASE, mod);
                    
                    // Обрабатываем события
                    GLFW.glfwPollEvents();
                    
                    System.out.println("[Macros] Sent F3+D key press (modifier: " + mod + ")");
                    break;
                    
                } catch (Exception e) {
                    // Пробуем следующий модификатор
                }
            }
            
            System.out.println("[Macros] ✅ Chat clear command sent!");
            
        } catch (Exception e) {
            // Фолбэк: пробуем через рефлексию (на случай если GLFW не сработал)
            clearChatFallback();
        }
    }
    
    /**
     * Фолбэк-метод через рефлексию (если GLFW не сработал)
     */
    private static void clearChatFallback() {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) return;
            
            // Ищем ЛЮБОЕ поле, которое может быть чатом (по методам)
            Object chat = null;
            
            // Проверяем все поля в Minecraft
            for (java.lang.reflect.Field field : mc.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(mc);
                
                if (value != null) {
                    // Проверяем, есть ли у объекта метод clearMessages или resetChat
                    try {
                        value.getClass().getMethod("clearMessages", boolean.class);
                        chat = value;
                        System.out.println("[Macros] Found chat via reflection: " + field.getName());
                        break;
                    } catch (NoSuchMethodException e1) {
                        try {
                            value.getClass().getMethod("clearMessages");
                            chat = value;
                            System.out.println("[Macros] Found chat via reflection: " + field.getName());
                            break;
                        } catch (NoSuchMethodException e2) {
                            try {
                                value.getClass().getMethod("resetChat");
                                chat = value;
                                System.out.println("[Macros] Found chat via reflection: " + field.getName());
                                break;
                            } catch (NoSuchMethodException e3) {
                                // Не подходит
                            }
                        }
                    }
                }
            }
            
            // Если не нашли в Minecraft, ищем в IngameGui
            if (chat == null) {
                for (java.lang.reflect.Field guiField : mc.getClass().getDeclaredFields()) {
                    String typeName = guiField.getType().getSimpleName();
                    if (typeName.equals("IngameGUI") || typeName.equals("IngameGui")) {
                        guiField.setAccessible(true);
                        Object gui = guiField.get(mc);
                        
                        if (gui != null) {
                            for (java.lang.reflect.Field field : gui.getClass().getDeclaredFields()) {
                                field.setAccessible(true);
                                Object value = field.get(gui);
                                
                                if (value != null) {
                                    try {
                                        value.getClass().getMethod("clearMessages", boolean.class);
                                        chat = value;
                                        System.out.println("[Macros] Found chat in GUI via reflection: " + field.getName());
                                        break;
                                    } catch (NoSuchMethodException e1) {
                                        try {
                                            value.getClass().getMethod("clearMessages");
                                            chat = value;
                                            System.out.println("[Macros] Found chat in GUI via reflection: " + field.getName());
                                            break;
                                        } catch (NoSuchMethodException e2) {
                                            // Не подходит
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
            
            // Вызываем метод очистки
            if (chat != null) {
                try {
                    java.lang.reflect.Method m = chat.getClass().getMethod("clearMessages", boolean.class);
                    m.invoke(chat, true);
                } catch (NoSuchMethodException e) {
                    try {
                        java.lang.reflect.Method m = chat.getClass().getMethod("clearMessages");
                        m.invoke(chat);
                    } catch (NoSuchMethodException e2) {
                        try {
                            java.lang.reflect.Method m = chat.getClass().getMethod("resetChat");
                            m.invoke(chat);
                        } catch (NoSuchMethodException e3) {
                            System.err.println("[Macros] Could not find clear method");
                        }
                    }
                }
                System.out.println("[Macros] ✅ Chat cleared via reflection!");
            }
            
        } catch (Exception e) {
            System.err.println("[Macros] Fallback clear chat error: " + e.getMessage());
        }
    }
}
