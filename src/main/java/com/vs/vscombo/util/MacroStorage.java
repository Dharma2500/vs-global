package com.vs.vscombo.util;

import com.vs.vscombo.VSGlobalMod;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;

public class MacroStorage {
    
    private static final String MACRO_FILENAME = "macros.txt";
    
    /**
     * Получает путь к файлу макросов
     */
    public static File getMacroFile() {
        File configDir = new File(Minecraft.getInstance().gameDirectory, "config");
        if (!configDir.exists()) configDir.mkdirs();
        
        File modDir = new File(configDir, "vsglobal");
        if (!modDir.exists()) modDir.mkdirs();
        
        return new File(modDir, MACRO_FILENAME);
    }
    
    /**
     * Сохраняет макросы в файл
     */
    public static boolean saveMacros(String content) {
        try {
            File file = getMacroFile();
            Files.writeString(file.toPath(), content, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            VSGlobalMod.LOGGER.error("Failed to save macros: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Загружает макросы из файла
     */
    public static String loadMacros() {
        try {
            File file = getMacroFile();
            if (file.exists()) {
                return Files.readString(file.toPath(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            VSGlobalMod.LOGGER.error("Failed to load macros: {}", e.getMessage());
        }
        return "";
    }
}
