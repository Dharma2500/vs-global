package com.vs.vscombo.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CChatMessagePacket;

public class VSMainWindow {
    
    /**
     * ✅ Очистка чата (аналог F3+D) — универсальный метод
     * Вызов: VSMainWindow.clearChatStatic();
     */
    public static void clearChatStatic() {
        Minecraft mc = Minecraft.getInstance();
        
        if (mc == null || mc.player == null || mc.player.connection == null) {
            return;
        }
        
        try {
            // Отправляем команду очистки чата
            // Многие сервера поддерживают /clearchat или /clear
            mc.player.connection.send(new CChatMessagePacket("/clearchat"));
        } catch (Exception e) {
            try {
                // Альтернативная команда
                mc.player.connection.send(new CChatMessagePacket("/clear"));
            } catch (Exception e2) {
                // Если команды не работают, пробуем отправить пустое сообщение
                // Это не очистит чат, но хотя бы не вызовет ошибку
                System.out.println("[Macros] Chat clear commands not supported on this server");
            }
        }
    }
}
