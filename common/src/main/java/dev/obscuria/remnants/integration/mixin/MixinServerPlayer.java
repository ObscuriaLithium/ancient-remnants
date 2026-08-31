package dev.obscuria.remnants.integration.mixin;

import dev.obscuria.remnants.integration.hook.HookPlayer;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer {

    @Inject(method = "restoreFrom", at = @At("HEAD"))
    private void restoreFrom$onHead(ServerPlayer oldPlayer, boolean restoreAll, CallbackInfo ci) {
        HookPlayer.restoreFrom((ServerPlayer) (Object) this, oldPlayer, restoreAll);
    }
}