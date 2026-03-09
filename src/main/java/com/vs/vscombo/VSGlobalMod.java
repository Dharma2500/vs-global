package com.vs.vscombo;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("vsglobal")
public class VSGlobalMod {
    
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "vsglobal";  // ✅ Правильное имя: MODID (без подчёркивания)
    
    public VSGlobalMod() {
        LOGGER.info("VS Global Mod initialized with ID: {}", MODID);
    }
}
