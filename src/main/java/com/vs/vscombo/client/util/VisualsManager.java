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
    
    /**
     * ✅ Включить/выключить эффект портала на блоках
     */
    public static void setBlockPortalEnabled(boolean enabled) {
        blockPortalEnabled = enabled;
        System.out.println("[Visuals] Block Portal: " + (enabled ? "ENABLED" : "DISABLED"));
    }
    
    /**
     * ✅ Проверить, включен ли эффект
     */
    public static boolean isBlockPortalEnabled() {
        return blockPortalEnabled;
    }
    
    /**
     * ✅ Вызывается при разрушении блока
     */
    public static void onBlockBroken(BlockPos pos) {
        if (!blockPortalEnabled) return;
        
        lastBrokenBlock = pos;
        lastBreakTime = System.currentTimeMillis();
        
        // Воспроизводим эффект портала
        playPortalAnimation();
    }
    
    /**
     * ✅ Воспроизводит эффект портала (через рефлексию для совместимости)
     */
    private static void playPortalAnimation() {
        Minecraft mc = Minecraft.getInstance();
        ClientPlayerEntity player = mc.player;
        
        if (player == null) return;
        
        // ✅ Создаём предмет обсидиан для визуальной связи с порталом
        ItemStack obsidian = new ItemStack(Items.OBSIDIAN);
        
        // ✅ Частицы портала (через рефлексию для совместимости с маппингами)
        try {
            // Получаем мир через игрока (рефлексия для обхода доступа)
            Object world = null;
            for (java.lang.reflect.Field field : player.getClass().getDeclaredFields()) {
                String typeName = field.getType().getSimpleName();
                if (typeName.equals("ClientWorld") || typeName.equals("World")) {
                    field.setAccessible(true);
                    world = field.get(player);
                    break;
                }
            }
            
            if (world != null && lastBrokenBlock != null) {
                double x = lastBrokenBlock.getX() + 0.5;
                double y = lastBrokenBlock.getY() + 0.5;
                double z = lastBrokenBlock.getZ() + 0.5;
                
                // 🔍 Ищем тип частиц PORTAL через рефлексию
                Class<?> particleTypesClass = Class.forName("net.minecraft.particles.ParticleTypes");
                java.lang.reflect.Field portalField = null;
                
                // Ищем поле с "PORTAL" в имени
                for (java.lang.reflect.Field f : particleTypesClass.getDeclaredFields()) {
                    if (f.getName().toUpperCase().contains("PORTAL")) {
                        portalField = f;
                        break;
                    }
                }
                
                if (portalField != null) {
                    portalField.setAccessible(true);
                    Object portalParticle = portalField.get(null);
                    
                    // Добавляем частицы портала
                    for (int i = 0; i < 40; i++) {
                        java.lang.reflect.Method addParticleMethod = world.getClass()
                            .getMethod("addParticle", 
                                Class.forName("net.minecraft.particles.IParticleData"),
                                double.class, double.class, double.class,
                                double.class, double.class, double.class);
                        
                        addParticleMethod.invoke(world, portalParticle,
                            x + (Math.random() - 0.5) * 3,
                            y + (Math.random() - 0.5) * 3,
                            z + (Math.random() - 0.5) * 3,
                            (Math.random() - 0.5) * 0.3,
                            (Math.random() - 0.5) * 0.3,
                            (Math.random() - 0.5) * 0.3
                        );
                    }
                }
            }
        } catch (Exception e) {
            // Частицы опциональны - пропускаем если не найдены
            System.out.println("[Visuals] Portal particles skipped (mapping compatibility)");
        }
        
        System.out.println("[Visuals] Portal animation played at block: " + lastBrokenBlock);
    }
    
    /**
     * ✅ Проверяет, нужно ли показать эффект (для рендера)
     */
    public static boolean shouldShowPortalEffect() {
        if (!blockPortalEnabled) return false;
        if (lastBrokenBlock == null) return false;
        
        // Показываем эффект в течение 1.5 секунд после разрушения
        return (System.currentTimeMillis() - lastBreakTime) < 1500;
    }
    
    /**
     * ✅ Сбросить состояние
     */
    public static void reset() {
        lastBrokenBlock = null;
        lastBreakTime = 0;
    }
}
