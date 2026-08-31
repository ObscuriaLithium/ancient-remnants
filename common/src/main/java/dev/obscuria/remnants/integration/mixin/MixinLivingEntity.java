package dev.obscuria.remnants.integration.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.obscuria.remnants.integration.hook.HookLivingEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

    @ModifyVariable(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getUseItem()Lnet/minecraft/world/item/ItemStack;"), argsOnly = true)
    private float hurt$modifyDamageAmount(float amount, @Local(argsOnly = true) DamageSource source) {
        return HookLivingEntity.modifyIncomingDamage((LivingEntity) (Object) this, source, amount);
    }

    @ModifyArg(method = "getDamageAfterArmorAbsorb", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/CombatRules;getDamageAfterAbsorb(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/damagesource/DamageSource;FF)F"), index = 3)
    private float getDamageAfterArmorAbsorb$modifyArmorValue(float armorValue, @Local(argsOnly = true) DamageSource source) {
        return HookLivingEntity.modifyArmorValue((LivingEntity) (Object) this, source, armorValue);
    }

    @Inject(method = "dropAllDeathLoot", at = @At("TAIL"))
    private void dropAllDeathLoot$onTail(ServerLevel level, DamageSource source, CallbackInfo ci) {
        HookLivingEntity.dropExtraDeathLoot((LivingEntity) (Object) this, level, source);
    }
}
