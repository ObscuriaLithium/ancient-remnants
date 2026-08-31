package dev.obscuria.remnants.integration.mixin;

import dev.obscuria.remnants.integration.hook.HookBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class MixinBlock {

    @Inject(method = "playerDestroy", at = @At("TAIL"))
    private void playerDestroy$onTail(
            Level level, Player player, BlockPos pos,
            BlockState state, BlockEntity blockEntity,
            ItemStack destroyedWith, CallbackInfo ci
    ) {
        HookBlock.onPlayerDestroy(level, player, pos, state, blockEntity, destroyedWith);
    }
}