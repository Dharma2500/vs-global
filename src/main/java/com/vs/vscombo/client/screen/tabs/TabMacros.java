// Найдите метод sendChat и замените на более надёжный:

/**
 * ✅ Универсальная отправка через прямой вызов метода
 */
private void sendChat(ClientPlayerEntity player, String message) {
    if (player == null || player.connection == null) return;
    
    try {
        // Пробуем метод send() с IPacket
        player.connection.send(new CChatMessagePacket(message));
    } catch (Exception e) {
        // Если не получилось, пробуем через chat()
        try {
            java.lang.reflect.Method chatMethod = player.getClass().getMethod("chat", String.class);
            chatMethod.invoke(player, message);
        } catch (Exception e2) {
            // Последняя попытка - через sendChatMessage
            try {
                java.lang.reflect.Method sendMethod = player.getClass().getMethod("sendChatMessage", String.class);
                sendMethod.invoke(player, message);
            } catch (Exception e3) {
                // Если совсем ничего не вышло - пишем в лог
                System.out.println("[Macros] Failed to send: " + message);
            }
        }
    }
}

// Метод executeMacros оставьте как есть, он должен работать
