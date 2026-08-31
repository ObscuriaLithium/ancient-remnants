package dev.obscuria.remnants.integration.hook;

import dev.obscuria.remnants.AncientRemnantsHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public final class HookPlayer {

    public static void restoreFrom(ServerPlayer self, ServerPlayer oldPlayer, boolean restoreAll) {
        AncientRemnantsHelper.setBlessing(self, AncientRemnantsHelper.getBlessing(oldPlayer));
    }

    public static void tick(Player self) {
        @Nullable var blessing = AncientRemnantsHelper.getBlessing(self);
        if (blessing == null) return;
        blessing.value().onTick(self);
    }
}
