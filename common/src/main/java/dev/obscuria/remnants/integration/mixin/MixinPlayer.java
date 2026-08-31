package dev.obscuria.remnants.integration.mixin;

import dev.obscuria.remnants.common.blessing.Blessing;
import dev.obscuria.remnants.integration.extension.IPlayerExtension;
import dev.obscuria.remnants.integration.hook.HookPlayer;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class MixinPlayer implements IPlayerExtension {

    private @Unique @Nullable Holder<Blessing> ancientRemnants$blessing;

    @Override
    public @Nullable Holder<Blessing> ancientRemnants$getBlessing() {
        return ancientRemnants$blessing;
    }

    @Override
    public void ancientRemnants$setBlessing(@Nullable Holder<Blessing> blessing) {
        if (ancientRemnants$blessing != null)
            this.ancientRemnants$blessing.value().onRemoved((Player) (Object) this);
        this.ancientRemnants$blessing = blessing;
        if (ancientRemnants$blessing != null)
            this.ancientRemnants$blessing.value().onAdded((Player) (Object) this);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void addAdditionalSaveData$onHead(ValueOutput output, CallbackInfo ci) {
        output.storeNullable("AncientRemnantsBlessing", Blessing.CODEC, ancientRemnants$blessing);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void readAdditionalSaveData$onHead(ValueInput input, CallbackInfo ci) {
        input.read("AncientRemnantsBlessing", Blessing.CODEC).ifPresent(this::ancientRemnants$setBlessingInternal);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick$onTail(CallbackInfo ci) {
        HookPlayer.tick((Player) (Object) this);
    }

    @Unique
    private void ancientRemnants$setBlessingInternal(@Nullable Holder<Blessing> blessing) {
        this.ancientRemnants$blessing = blessing;
    }
}
