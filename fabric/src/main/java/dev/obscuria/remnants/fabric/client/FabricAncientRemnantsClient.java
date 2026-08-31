package dev.obscuria.remnants.fabric.client;

import dev.obscuria.remnants.client.AncientRemnantsClient;
import net.fabricmc.api.ClientModInitializer;

public final class FabricAncientRemnantsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AncientRemnantsClient.init();
    }
}