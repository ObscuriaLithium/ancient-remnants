package dev.obscuria.remnants.neoforge.client;

import dev.obscuria.remnants.AncientRemnants;
import dev.obscuria.remnants.client.AncientRemnantsClient;
import dev.obscuria.remnants.config.ConfigScreenBuilder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = AncientRemnants.MOD_ID, dist = Dist.CLIENT)
public final class NeoForgeAncientRemnantsClient {

    public NeoForgeAncientRemnantsClient() {
        AncientRemnantsClient.init();
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                NeoForgeAncientRemnantsClient::createConfigFactory);
    }

    private static IConfigScreenFactory createConfigFactory() {
        return (_, parent) -> ConfigScreenBuilder.create(parent);
    }
}
