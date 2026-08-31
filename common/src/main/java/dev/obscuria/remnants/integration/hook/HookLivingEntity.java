package dev.obscuria.remnants.integration.hook;

import dev.obscuria.remnants.AncientRemnantsHelper;
import dev.obscuria.remnants.integration.ValueContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public final class HookLivingEntity {

    public static float modifyIncomingDamage(LivingEntity self, DamageSource source, float damage) {
        var context = new ValueContext(damage);
        if (self instanceof Player player) {
            @Nullable var blessing = AncientRemnantsHelper.getBlessing(player);
            if (blessing != null) blessing.value().modifyIncomingDamage(player, source, context);
        }
        if (source.getEntity() instanceof Player player) {
            @Nullable var blessing = AncientRemnantsHelper.getBlessing(player);
            if (blessing != null) blessing.value().modifyOutgoingDamage(player, self, source, context);
        }
        return context.result();
    }

    public static float modifyArmorValue(LivingEntity self, DamageSource source, float armor) {
        var context = new ValueContext(armor);
        if (self instanceof Player player) {
            @Nullable var blessing = AncientRemnantsHelper.getBlessing(player);
            if (blessing != null) blessing.value().modifyArmorValue(player, source, context);
        }
        return context.result();
    }

    public static void dropExtraDeathLoot(LivingEntity self, ServerLevel level, DamageSource source) {
        if (!(self.getLastHurtByPlayer() instanceof ServerPlayer)) return;
        if (self.getRandom().nextFloat() > 0.002) return;
        var biome = level.getBiome(self.blockPosition());
        var entries = AncientRemnantsHelper.listResearchEntries(level.registryAccess(), biome).toList();
        if (entries.isEmpty()) return;
        var entry = entries.get(self.getRandom().nextInt(entries.size()));
        self.spawnAtLocation(level, AncientRemnantsHelper.createResearchScrap(entry));
    }
}