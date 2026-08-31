package dev.obscuria.remnants.network;

import dev.obscuria.remnants.registry.AncientRemnantsSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.world.entity.player.Player;

final class ClientPayloadListener {

    public static void handleBlessingAcquired(Player player, ClientboundBlessingAcquiredPayload payload) {
        var sound = SimpleSoundInstance.forUI(AncientRemnantsSoundEvents.EFFECT_BLESSING, 1, 1);
        Minecraft.getInstance().getSoundManager().play(sound);
    }
}
