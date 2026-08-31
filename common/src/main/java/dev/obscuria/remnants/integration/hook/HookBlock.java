package dev.obscuria.remnants.integration.hook;

import dev.obscuria.remnants.common.blessing.BountifulHarvestBlessing;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class HookBlock {

    public static void onPlayerDestroy(
            Level level, Player player, BlockPos pos,
            BlockState state, BlockEntity blockEntity,
            ItemStack destroyedWith
    ) {
        if (level.isClientSide()) return;
        if (!BountifulHarvestBlessing.isFullyGrown(state)) return;
        if (!BountifulHarvestBlessing.shouldTrigger(player)) return;
        Block.dropResources(state, level, pos, blockEntity, player, destroyedWith);
    }
}
