package com.vs.vscombo.client.util;

import net.minecraft.client.Minecraft;

public class VSMainWindow {
    
    /**
     * ✅ Очистка чата (аналог F3+D) — универсальный метод с рефлексией
     * Вызов: VSMainWindow.clearChatStatic();
     */
    public static void clearChatStatic() {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) return;
            
            // Пробуем разные варианты имён полей для разных маппингов
            Object gui = null;
            
            // Вариант 1: поле "gui" (Mojang Mappings)
            try {
                java.lang.reflect.Field guiField = mc.getClass().getDeclaredField("gui");
                guiField.setAccessible(true);
                gui = guiField.get(mc);
            } catch (NoSuchFieldException e1) {
                // Вариант 2: поле "ingameGUI" (Forge Mappings)
                try {
                    java.lang.reflect.Field guiField = mc.getClass().getDeclaredField("ingameGUI");
                    guiField.setAccessible(true);
                    gui = guiField.get(mc);
                } catch (NoSuchFieldException e2) {
                    // Поле не найдено
                    return;
                }
            }
            
            if (gui == null) return;
            
            // Ищем поле чата: "chat" или "chatGUI"
            Object chatGUI = null;
            try {
                java.lang.reflect.Field chatField = gui.getClass().getDeclaredField("chat");
                chatField.setAccessible(true);
                chatGUI = chatField.get(gui);
            } catch (NoSuchFieldException e1) {
                try {
                    java.lang.reflect.Field chatField = gui.getClass().getDeclaredField("chatGUI");
                    chatField.setAccessible(true);
                    chatGUI = chatField.get(gui);
                } catch (NoSuchFieldException e2) {
                    // Поле чата не найдено
                    return;
                }
            }
            
            if (chatGUI == null) return;
            
            // Ищем метод очистки: clearChatMessages(boolean) или resetChat()
            try {
                java.lang.reflect.Method clearMethod = chatGUI.getClass().getMethod("clearChatMessages", boolean.class);
                clearMethod.invoke(chatGUI, true);
            } catch (NoSuchMethodException e1) {
                try {
                    java.lang.reflect.Method clearMethod = chatGUI.getClass().getMethod("resetChat");
                    clearMethod.invoke(chatGUI);
                } catch (NoSuchMethodException e2) {
                    // Метод очистки не найден
                }
            }
            
        } catch (Exception e) {
            // Игнорируем ошибки рефлексии — чат не очистится, но мод продолжит работать
        }
    }
}
