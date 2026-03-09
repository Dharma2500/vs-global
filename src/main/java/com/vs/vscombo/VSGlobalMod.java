package com.vs.vscombo;

import com.vs.vscombo.client.keybind.ModKeyBindings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("vsglobal")
public class VSGlobalMod {
    
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "vsglobal";
    
    public VSGlobalMod() {
        LOGGER.info("VS Global Mod initialized with ID: {}", MODID);
        
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        // Регистрируем ключи на клиенте
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> {
            ModKeyBindings.register();
            MinecraftForge.EVENT_BUS.register(this);
        });
    }
}
