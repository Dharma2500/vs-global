package com.vs.vscombo;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(VSGlobalMod.MOD_ID)
public class VSGlobalMod {
    public static final String MOD_ID = "vscombo";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    
    public static VSGlobalMod INSTANCE;
    
    public VSGlobalMod() {
        INSTANCE = this;
        
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        // Регистрируем себя в Forge
        MinecraftForge.EVENT_BUS.register(this);
        
        LOGGER.info("VS Global Mod initialized!");
    }
}
