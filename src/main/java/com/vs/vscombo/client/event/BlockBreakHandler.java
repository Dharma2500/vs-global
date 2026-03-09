package com.vs.vscombo.client.event;

import com.vs.vscombo.client.util.VisualsManager;
import com.vs.vscombo.VSGlobalMod;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;

@Mod.EventBusSubscriber(modid = VSGlobalMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class BlockBreakHandler {
    
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        // Проверяем, что это локальный игрок
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            // ✅ Используем getUUID() вместо getUniqueID()
            if (event.getPlayer().getUUID().equals(mc.player.getUUID())) {
                BlockPos pos = event.getPos();
                if (pos != null) {
                    VisualsManager.onBlockBroken(pos);
                }
            }
        }
    }
}
