package dev.obscuria.remnants.common.blessing;

import dev.obscuria.fragmentum.v2.api.common.registry.Deferred;
import dev.obscuria.remnants.AncientRemnantsHelper;
import dev.obscuria.remnants.registry.AncientRemnantsBlessings;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

public class BountifulHarvestBlessing extends Blessing {

    public BountifulHarvestBlessing(Supplier<Deferred<MobEffect>> effectSupplier) {
        super(effectSupplier);
    }

    public static boolean shouldTrigger(Player player) {
        @Nullable var blessing = AncientRemnantsHelper.getBlessing(player);
        if (!Objects.equals(AncientRemnantsBlessings.BOUNTIFUL_HARVEST.holder(), blessing)) return false;
        return player.getRandom().nextBoolean();
    }

    public static boolean isFullyGrown(BlockState state) {
        var block = state.getBlock();
        if (block instanceof CropBlock crop)
            return crop.isMaxAge(state);
        if (block instanceof NetherWartBlock)
            return state.getValue(NetherWartBlock.AGE) >= 3;
        return false;
    }
}