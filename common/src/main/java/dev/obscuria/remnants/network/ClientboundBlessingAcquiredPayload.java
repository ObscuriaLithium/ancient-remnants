package dev.obscuria.remnants.network;

import dev.obscuria.remnants.AncientRemnants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public record ClientboundBlessingAcquiredPayload() implements CustomPacketPayload {

    public static final ClientboundBlessingAcquiredPayload SHARED;
    public static final Type<ClientboundBlessingAcquiredPayload> TYPE;
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundBlessingAcquiredPayload> STREAM_CODEC;

    @Override
    public Type<ClientboundBlessingAcquiredPayload> type() {
        return TYPE;
    }

    public static void handle(Player player, ClientboundBlessingAcquiredPayload payload) {
        ClientPayloadListener.handleBlessingAcquired(player, payload);
    }

    static {
        SHARED = new ClientboundBlessingAcquiredPayload();
        TYPE = new Type<>(AncientRemnants.id("blessing_acquired"));
        STREAM_CODEC = StreamCodec.unit(new ClientboundBlessingAcquiredPayload());
    }
}
