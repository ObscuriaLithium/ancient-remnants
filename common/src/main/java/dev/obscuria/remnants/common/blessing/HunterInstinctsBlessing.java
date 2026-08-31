package dev.obscuria.remnants.common.blessing;

import dev.obscuria.fragmentum.v2.api.common.Easing;
import dev.obscuria.fragmentum.v2.api.common.registry.Deferred;
import dev.obscuria.remnants.integration.ValueContext;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.function.Supplier;

public final class HunterInstinctsBlessing extends Blessing {

    public HunterInstinctsBlessing(Supplier<Deferred<MobEffect>> effectSupplier) {
        super(effectSupplier);
    }

    @Override
    public void modifyOutgoingDamage(Player player, LivingEntity victim, DamageSource source, ValueContext context) {
        if (source.isDirect()) return;
        var damageBonus = Mth.clampedMap(player.distanceTo(victim), 0, 64, 0, 1);
        context.multiply(1f + Easing.EASE_IN_CIRCLE.compute(damageBonus));
    }
}