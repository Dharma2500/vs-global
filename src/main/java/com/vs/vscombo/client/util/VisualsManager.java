package com.vs.vscombo.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

public class VisualsManager {
    
    private static boolean blockPortalEnabled = false;
    private static BlockPos lastBrokenBlock = null;
    private static long lastBreakTime = 0;
    
    public static void setBlockPortalEnabled(boolean enabled) {
        blockPortalEnabled = enabled;
        System.out.println("[Visuals] Block Portal: " + (enabled ? "ENABLED" : "DISABLED"));
    }
    
    public static boolean isBlockPortalEnabled() {
        return blockPortalEnabled;
    }
    
    public static void onBlockBroken(BlockPos pos) {
        if (!blockPortalEnabled) return;
        if (pos == null) return;
        
        lastBrokenBlock = pos;
        lastBreakTime = System.currentTimeMillis();
        
        playPortalParticles();
    }
    
    /**
     * ✅ Добавляет частицы портала (без звука, без рефлексии — максимально просто)
     */
    private static void playPortalParticles() {
        Minecraft mc = Minecraft.getInstance();
        ClientPlayerEntity player = mc.player;
        
        if (player == null || lastBrokenBlock == null) return;
        
        // Координаты центра блока
        double x = lastBrokenBlock.getX() + 0.5;
        double y = lastBrokenBlock.getY() + 0.5;
        double z = lastBrokenBlock.getZ() + 0.5;
        
        // ✅ Используем прямой вызов addParticle через Minecraft
        // Это работает в любых маппингах 1.16.5
        try {
            // Получаем мир через игрока
            net.minecraft.client.world.ClientWorld world = player.world;
            if (world != null) {
                // Добавляем частицы портала (30 штук)
                for (int i = 0; i < 30; i++) {
                    world.addParticle(
                        net.minecraft.particles.ParticleTypes.PORTAL,
                        x + (Math.random() - 0.5) * 2,
                        y + (Math.random() - 0.5) * 2,
                        z + (Math.random() - 0.5) * 2,
                        (Math.random() - 0.5) * 0.2,
                        (Math.random() - 0.5) * 0.2,
                        (Math.random() - 0.5) * 0.2
                    );
                }
                System.out.println("[Visuals] Portal particles spawned at " + lastBrokenBlock);
            }
        } catch (Exception e) {
            // Если не вышло — просто логируем
            System.out.println("[Visuals] Failed to spawn particles: " + e.getMessage());
        }
    }
    
    public static boolean shouldShowPortalEffect() {
        if (!blockPortalEnabled) return false;
        if (lastBrokenBlock == null) return false;
        return (System.currentTimeMillis() - lastBreakTime) < 1500;
    }
    
    public static void reset() {
        lastBrokenBlock = null;
        lastBreakTime = 0;
    }
}
