// Убедитесь, что у вас есть этот файл с аннотацией @Mod
// Forge автоматически зарегистрирует BlockBreakHandler благодаря @Mod.EventBusSubscriber

package com.vs.vscombo;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("vsglobal")
public class VSGlobalMod {
    
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "vsglobal";
    
    public VSGlobalMod() {
        LOGGER.info("VS Global Mod initialized with ID: {}", MODID);
        // Forge автоматически зарегистрирует все @EventBusSubscriber классы
    }
}
