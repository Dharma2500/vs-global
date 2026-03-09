package com.vs.vscombo.util;

import com.vs.vscombo.VSGlobalMod;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class MacroStorage {
    
    private static final String MACRO_FILENAME = "macros.txt";
    
    public static File getMacroFile() {
        File configDir = new File(Minecraft.getInstance().gameDirectory, "config");
        if (!configDir.exists()) configDir.mkdirs();
        File modDir = new File(configDir, "vsglobal");
        if (!modDir.exists()) modDir.mkdirs();
        return new File(modDir, MACRO_FILENAME);
    }
    
    // ✅ Java 8: используем FileWriter/BufferedWriter вместо Files.writeString()
    public static boolean saveMacros(String content) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(getMacroFile()), StandardCharsets.UTF_8))) {
            writer.write(content);
            return true;
        } catch (IOException e) {
            VSGlobalMod.LOGGER.error("Failed to save macros: {}", e.getMessage());
            return false;
        }
    }
    
    // ✅ Java 8: используем FileReader/BufferedReader вместо Files.readString()
    public static String loadMacros() {
        File file = getMacroFile();
        if (!file.exists()) return "";
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            VSGlobalMod.LOGGER.error("Failed to load macros: {}", e.getMessage());
            return "";
        }
    }
}
