package dev.obscuria.remnants.common.blessing;

import dev.obscuria.fragmentum.v2.api.common.Easing;
import dev.obscuria.fragmentum.v2.api.common.registry.Deferred;
import dev.obscuria.remnants.integration.ValueContext;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;

import java.util.function.Supplier;

public final class SentinelAegisBlessing extends Blessing {

    public SentinelAegisBlessing(Supplier<Deferred<MobEffect>> effectSupplier) {
        super(effectSupplier);
    }

    @Override
    public void modifyArmorValue(Player player, DamageSource source, ValueContext context) {
        var healthPercentage = player.getHealth() / player.getMaxHealth();
        var armorBonus = Mth.clampedMap(healthPercentage, 0, 1, 1, 0);
        context.add(20 * Easing.EASE_IN_CIRCLE.compute(armorBonus));
    }
}