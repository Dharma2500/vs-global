// Найдите метод handleKeyPress и измените:

public boolean handleKeyPress(int keyCode, int scanCode, int modifiers) {
    if (!isFocused || !isEditable) return false;
    boolean ctrl = (modifiers & 2) != 0;
    
    // ✅ Пробел (keyCode 32) должен обрабатываться в charTyped, не блокируем его здесь
    if (keyCode == GLFW.GLFW_KEY_SPACE) {
        return false; // Позволяем charTyped обработать пробел
    }
    
    if (keyCode == GLFW.GLFW_KEY_LEFT && cursorPos > 0) { cursorPos--; if (selectionStart == -1) selectionStart = cursorPos; autoScroll(); return true; }
    if (keyCode == GLFW.GLFW_KEY_RIGHT && cursorPos < text.length()) { cursorPos++; if (selectionStart == -1) selectionStart = cursorPos; autoScroll(); return true; }
    if (keyCode == GLFW.GLFW_KEY_UP) { moveCursorUp(); return true; }
    if (keyCode == GLFW.GLFW_KEY_DOWN) { moveCursorDown(); return true; }
    if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) { insertText("\n"); return true; }
    if (keyCode == GLFW.GLFW_KEY_BACKSPACE) { if (hasSelection()) deleteSelection(); else if (cursorPos > 0) { text = text.substring(0, cursorPos - 1) + text.substring(cursorPos); cursorPos--; selectionStart = -1; } autoScroll(); return true; }
    if (keyCode == GLFW.GLFW_KEY_DELETE) { if (hasSelection()) deleteSelection(); else if (cursorPos < text.length()) text = text.substring(0, cursorPos) + text.substring(cursorPos + 1); return true; }
    if (keyCode == GLFW.GLFW_KEY_HOME) { moveToLineStart(); return true; }
    if (keyCode == GLFW.GLFW_KEY_END) { moveToLineEnd(); return true; }
    if (ctrl && keyCode == GLFW.GLFW_KEY_A) { selectionStart = 0; cursorPos = text.length(); return true; }
    
    // Блокируем клавишу открытия мода (R = 82)
    if (keyCode == 82) return true;
    
    return false; // ✅ Важно: возвращаем false для всех остальных клавиш (букв, цифр, пробела)
}

// Метод charTyped должен обрабатывать только printable символы:
public boolean charTyped(char codePoint, int modifiers) {
    if (!isFocused || !isEditable) return false;
    
    // ✅ Разрешаем пробел (codePoint 32) и все printable символы
    if (codePoint < 32 && codePoint != 10 && codePoint != 13) { // 10=\n, 13=\r
        return false;
    }
    
    if (!filter.test(text + codePoint)) return false;
    insertText(String.valueOf(codePoint));
    return true;
}
