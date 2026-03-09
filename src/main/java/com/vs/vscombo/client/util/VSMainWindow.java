package com.vs.vscombo.client.util;

import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class VSMainWindow {
    
    /**
     * ✅ Очистка чата (аналог F3+D) — через симуляцию нажатия клавиш
     * Вызов: VSMainWindow.clearChatStatic();
     */
    public static void clearChatStatic() {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null || mc.keyboardListener == null) return;
            
            long windowHandle = mc.getMainWindow().getHandle();
            
            // Симулируем нажатие F3 (левый модификатор)
            GLFW.glfwSetKeyModifier(windowHandle, GLFW.GLFW_KEY_LEFT_ALT, GLFW.GLFW_PRESS);
            
            // Симулируем нажатие D
            GLFW.glfwSetKey(windowHandle, GLFW.GLFW_KEY_D, GLFW.GLFW_PRESS, 0);
            GLFW.glfwSetKey(windowHandle, GLFW.GLFW_KEY_D, GLFW.GLFW_RELEASE, 0);
            
            // Снимаем модификатор
            GLFW.glfwSetKeyModifier(windowHandle, GLFW.GLFW_KEY_LEFT_ALT, GLFW.GLFW_RELEASE);
            
            // Принудительно обрабатываем события (опционально)
            GLFW.glfwPollEvents();
            
        } catch (Exception e) {
            // Если не вышло — пробуем альтернативу через рефлексию
            clearChatFallback();
        }
    }
    
    /**
     * Фолбэк-метод очистки через рефлексию (если симуляция клавиш не сработала)
     */
    private static void clearChatFallback() {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) return;
            
            // Пробуем разные варианты имён полей
            Object gui = null;
            try {
                java.lang.reflect.Field f = mc.getClass().getDeclaredField("gui");
                f.setAccessible(true);
                gui = f.get(mc);
            } catch (NoSuchFieldException e) {
                try {
                    java.lang.reflect.Field f = mc.getClass().getDeclaredField("ingameGUI");
                    f.setAccessible(true);
                    gui = f.get(mc);
                } catch (NoSuchFieldException e2) {
                    return;
                }
            }
            
            if (gui == null) return;
            
            Object chat = null;
            try {
                java.lang.reflect.Field f = gui.getClass().getDeclaredField("chat");
                f.setAccessible(true);
                chat = f.get(gui);
            } catch (NoSuchFieldException e) {
                try {
                    java.lang.reflect.Field f = gui.getClass().getDeclaredField("chatGUI");
                    f.setAccessible(true);
                    chat = f.get(gui);
                } catch (NoSuchFieldException e2) {
                    return;
                }
            }
            
            if (chat == null) return;
            
            // Пробуем разные имена методов очистки
            try {
                java.lang.reflect.Method m = chat.getClass().getMethod("clearMessages", boolean.class);
                m.invoke(chat, true);
            } catch (NoSuchMethodException e) {
                try {
                    java.lang.reflect.Method m = chat.getClass().getMethod("clearChatMessages", boolean.class);
                    m.invoke(chat, true);
                } catch (NoSuchMethodException e2) {
                    try {
                        java.lang.reflect.Method m = chat.getClass().getMethod("resetChat");
                        m.invoke(chat);
                    } catch (NoSuchMethodException e3) {
                        // Метод не найден
                    }
                }
            }
            
        } catch (Exception e) {
            // Игнорируем — чат не очистится, но мод продолжит работать
        }
    }
}
