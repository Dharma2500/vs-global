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
    
    private static void playPortalParticles() {
        Minecraft mc = Minecraft.getInstance();
        ClientPlayerEntity player = mc.player;
        
        if (player == null || lastBrokenBlock == null) return;
        
        double x = lastBrokenBlock.getX() + 0.5;
        double y = lastBrokenBlock.getY() + 0.5;
        double z = lastBrokenBlock.getZ() + 0.5;
        
        try {
            // 🔍 Получаем мир через рефлексию (обход доступа к полю world)
            Object world = null;
            
            // Пробуем получить через игрока
            for (java.lang.reflect.Field field : player.getClass().getDeclaredFields()) {
                String typeName = field.getType().getSimpleName();
                if (typeName.equals("ClientWorld") || typeName.equals("World")) {
                    field.setAccessible(true);
                    world = field.get(player);
                    break;
                }
            }
            
            // Если не нашли у игрока, пробуем у Minecraft
            if (world == null) {
                for (java.lang.reflect.Field field : mc.getClass().getDeclaredFields()) {
                    String typeName = field.getType().getSimpleName();
                    if (typeName.equals("ClientWorld") || typeName.equals("World")) {
                        field.setAccessible(true);
                        world = field.get(mc);
                        break;
                    }
                }
            }
            
            if (world != null) {
                // 🔍 Ищем тип частиц через рефлексию
                Class<?> particleTypesClass = Class.forName("net.minecraft.particles.ParticleTypes");
                java.lang.reflect.Field portalField = null;
                
                for (java.lang.reflect.Field f : particleTypesClass.getDeclaredFields()) {
                    if (f.getName().toUpperCase().contains("PORTAL")) {
                        portalField = f;
                        break;
                    }
                }
                
                if (portalField != null) {
                    portalField.setAccessible(true);
                    Object portalParticle = portalField.get(null);
                    
                    // 🔍 Добавляем частицы через рефлексию
                    for (int i = 0; i < 30; i++) {
                        java.lang.reflect.Method addParticleMethod = world.getClass()
                            .getMethod("addParticle", 
                                Class.forName("net.minecraft.particles.IParticleData"),
                                double.class, double.class, double.class,
                                double.class, double.class, double.class);
                        
                        addParticleMethod.invoke(world, portalParticle,
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
            }
        } catch (Exception e) {
            // Частицы опциональны - пропускаем если не найдены
            System.out.println("[Visuals] Particles skipped: " + e.getMessage());
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
