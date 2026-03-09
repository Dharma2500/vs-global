package com.vs.vscombo.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

public class VisualsManager {
    
    private static boolean blockTotemEnabled = false;
    private static BlockPos lastBrokenBlock = null;
    private static long lastBreakTime = 0;
    
    /**
     * ✅ Включить/выключить анимацию тотема на блоках
     */
    public static void setBlockTotemEnabled(boolean enabled) {
        blockTotemEnabled = enabled;
        System.out.println("[Visuals] Block Totem: " + (enabled ? "ENABLED" : "DISABLED"));
    }
    
    /**
     * ✅ Проверить, включена ли анимация
     */
    public static boolean isBlockTotemEnabled() {
        return blockTotemEnabled;
    }
    
    /**
     * ✅ Вызывается при разрушении блока
     */
    public static void onBlockBroken(BlockPos pos) {
        if (!blockTotemEnabled) return;
        
        lastBrokenBlock = pos;
        lastBreakTime = System.currentTimeMillis();
        
        // Воспроизводим анимацию тотема
        playTotemAnimation();
    }
    
    /**
     * ✅ Воспроизводит анимацию тотема
     */
    private static void playTotemAnimation() {
        Minecraft mc = Minecraft.getInstance();
        ClientPlayerEntity player = mc.player;
        
        if (player == null) return;
        
        // ✅ Создаём временный предмет тотем для анимации
        ItemStack totem = new ItemStack(Items.TOTEM_OF_UNDYING);
        
        // ✅ Проигрываем звук тотема
        player.playSound(net.minecraft.util.SoundEvents.ITEM_TOTEM_USE, 1.0f, 1.0f);
        
        // ✅ Отправляем частицы тотема
        ClientWorld world = mc.world;
        if (world != null && lastBrokenBlock != null) {
            double x = lastBrokenBlock.getX() + 0.5;
            double y = lastBrokenBlock.getY() + 0.5;
            double z = lastBrokenBlock.getZ() + 0.5;
            
            // Частицы тотема
            for (int i = 0; i < 30; i++) {
                world.addParticle(
                    net.minecraft.particles.ParticleTypes.END_ROD,
                    x + (Math.random() - 0.5) * 2,
                    y + (Math.random() - 0.5) * 2,
                    z + (Math.random() - 0.5) * 2,
                    (Math.random() - 0.5) * 0.5,
                    (Math.random() - 0.5) * 0.5,
                    (Math.random() - 0.5) * 0.5
                );
            }
        }
        
        System.out.println("[Visuals] Totem animation played at block: " + lastBrokenBlock);
    }
    
    /**
     * ✅ Проверяет, нужно ли показать анимацию (для рендера)
     */
    public static boolean shouldShowTotemEffect() {
        if (!blockTotemEnabled) return false;
        if (lastBrokenBlock == null) return false;
        
        // Показываем эффект в течение 2 секунд после разрушения
        return (System.currentTimeMillis() - lastBreakTime) < 2000;
    }
    
    /**
     * ✅ Сбросить состояние
     */
    public static void reset() {
        lastBrokenBlock = null;
        lastBreakTime = 0;
    }
}
