package dev.obscuria.remnants.common.blessing;

import dev.obscuria.fragmentum.v2.api.common.registry.Deferred;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

import java.util.function.Supplier;

public final class DesperateVitalityBlessing extends Blessing {

    public DesperateVitalityBlessing(Supplier<Deferred<MobEffect>> effectSupplier) {
        super(effectSupplier);
    }

    @Override
    public void onTick(Player player) {
        super.onTick(player);
        if (player.getHealth() / player.getMaxHealth() > 0.33) return;
        if (player.hasEffect(MobEffects.REGENERATION)) return;
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200));
    }
}