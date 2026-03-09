package com.vs.vscombo.client.event;

import com.vs.vscombo.client.util.VisualsManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "vsglobal", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BlockBreakHandler {
    
    /**
     * ✅ Вызывается при разрушении блока клиентом
     */
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        // Проверяем, что это локальный игрок
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && event.getPlayer().getUniqueID().equals(mc.player.getUniqueID())) {
            BlockPos pos = event.getPos();
            if (pos != null) {
                VisualsManager.onBlockBroken(pos);
            }
        }
    }
    
    /**
     * ✅ Альтернатива: если BreakEvent не срабатывает, пробуем через правый клик
     */
    @SubscribeEvent
    public static void onBlockRightClick(net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock event) {
        // Можно добавить дополнительную логику если нужно
    }
}
