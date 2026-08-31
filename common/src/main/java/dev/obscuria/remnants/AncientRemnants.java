package dev.obscuria.remnants;

import dev.obscuria.fragmentum.v2.api.common.network.FragmentumNetworking;
import dev.obscuria.fragmentum.v2.api.server.FragmentumServerRegistry;
import dev.obscuria.remnants.config.CommonConfig;
import dev.obscuria.remnants.network.ClientboundBlessingAcquiredPayload;
import dev.obscuria.remnants.registry.AncientRemnantsRegistries;
import dev.obscuria.remnants.server.BlessingCommand;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AncientRemnants {

    public static final String MOD_ID = "ancient_remnants";
    public static final String DISPLAY_NAME = "Ancient Remnants";
    public static final Logger LOG = LoggerFactory.getLogger(DISPLAY_NAME);

    public static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(MOD_ID, name);
    }

    public static void init() {
        CommonConfig.init();
        AncientRemnantsRegistries.init();
        FragmentumServerRegistry.registerCommand(BlessingCommand::register);
        registerPayloads();
    }

    private static void registerPayloads() {
        var registrar = FragmentumNetworking.registrar(MOD_ID);
        registrar.registerClientbound(
                ClientboundBlessingAcquiredPayload.class,
                ClientboundBlessingAcquiredPayload.TYPE,
                ClientboundBlessingAcquiredPayload.STREAM_CODEC,
                ClientboundBlessingAcquiredPayload::handle);
    }
}
