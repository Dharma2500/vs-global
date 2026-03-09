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
        
        // ✅ Проигрываем звук тотема (с защитой от ошибок маппингов)
        try {
            // Пытаемся вызвать звук через рефлексию (безопасно для любых маппингов)
            Class<?> soundEventsClass = Class.forName("net.minecraft.util.SoundEvents");
            java.lang.reflect.Field totemField = null;
            
            // Ищем поле с "TOTEM" в имени
            for (java.lang.reflect.Field f : soundEventsClass.getDeclaredFields()) {
                if (f.getName().toUpperCase().contains("TOTEM") && 
                    f.getName().toUpperCase().contains("USE")) {
                    totemField = f;
                    break;
                }
            }
            
            if (totemField != null) {
                totemField.setAccessible(true);
                Object soundEvent = totemField.get(null);
                
                if (soundEvent != null) {
                    // Пытаемся вызвать playSound
                    java.lang.reflect.Method playSoundMethod = player.getClass()
                        .getMethod("playSound", 
                            Class.forName("net.minecraft.util.SoundEvent"),
                            Class.forName("net.minecraft.util.SoundCategory"),
                            float.class, float.class);
                    
                    Class<?> soundCategoryClass = Class.forName("net.minecraft.util.SoundCategory");
                    Object playersCategory = null;
                    for (Object cat : soundCategoryClass.getEnumConstants()) {
                        if (cat.toString().equalsIgnoreCase("PLAYERS")) {
                            playersCategory = cat;
                            break;
                        }
                    }
                    
                    if (playersCategory != null) {
                        playSoundMethod.invoke(player, soundEvent, playersCategory, 1.0f, 1.0f);
                    }
                }
            }
        } catch (Exception e) {
            // Если звук не удалось воспроизвести - просто пропускаем
            System.out.println("[Visuals] Totem sound skipped (mapping compatibility)");
        }
        
        // ✅ Отправляем частицы тотема
        // ✅ Используем Minecraft.getInstance().world (более надёжно)
        ClientWorld world = mc.world;
        if (world != null && lastBrokenBlock != null) {
            double x = lastBrokenBlock.getX() + 0.5;
            double y = lastBrokenBlock.getY() + 0.5;
            double z = lastBrokenBlock.getZ() + 0.5;
            
            // Частицы тотема (END_ROD работает в 1.16.5)
            try {
                Class<?> particleTypesClass = Class.forName("net.minecraft.particles.ParticleTypes");
                java.lang.reflect.Field endRodField = particleTypesClass.getDeclaredField("END_ROD");
                endRodField.setAccessible(true);
                Object endRodParticle = endRodField.get(null);
                
                for (int i = 0; i < 30; i++) {
                    java.lang.reflect.Method addParticleMethod = world.getClass()
                        .getMethod("addParticle", 
                            Class.forName("net.minecraft.particles.IParticleData"),
                            double.class, double.class, double.class,
                            double.class, double.class, double.class);
                    
                    addParticleMethod.invoke(world, endRodParticle,
                        x + (Math.random() - 0.5) * 2,
                        y + (Math.random() - 0.5) * 2,
                        z + (Math.random() - 0.5) * 2,
                        (Math.random() - 0.5) * 0.5,
                        (Math.random() - 0.5) * 0.5,
                        (Math.random() - 0.5) * 0.5
                    );
                }
            } catch (Exception e) {
                System.out.println("[Visuals] Particles skipped (mapping compatibility)");
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
