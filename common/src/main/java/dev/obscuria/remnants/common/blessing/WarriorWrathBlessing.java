package dev.obscuria.remnants.common.blessing;

import dev.obscuria.fragmentum.v2.api.common.Easing;
import dev.obscuria.fragmentum.v2.api.common.registry.Deferred;
import dev.obscuria.remnants.integration.ValueContext;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;

import java.util.function.Supplier;

public final class WarriorWrathBlessing extends Blessing {

    public WarriorWrathBlessing(Supplier<Deferred<MobEffect>> effectSupplier) {
        super(effectSupplier);
    }

    @Override
    public void modifyOutgoingDamage(Player player, LivingEntity victim, DamageSource source, ValueContext context) {
        if (!source.isDirect()) return;
        var monstersNearby = player.level().getEntitiesOfClass(Monster.class, player.getBoundingBox().inflate(12)).size();
        var damageBonus = Mth.clampedMap(monstersNearby, 0, 12, 0, 1);
        context.multiply(1f + Easing.EASE_IN_CIRCLE.compute(damageBonus));
    }
}