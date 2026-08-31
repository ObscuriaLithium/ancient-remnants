package dev.obscuria.remnants.neoforge;

import dev.obscuria.remnants.AncientRemnants;
import dev.obscuria.remnants.AncientRemnantsHelper;
import dev.obscuria.remnants.registry.AncientRemnantsItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(AncientRemnants.MOD_ID)
public final class NeoForgeAncientRemnants {

    public NeoForgeAncientRemnants(IEventBus eventBus) {
        AncientRemnants.init();
        eventBus.addListener(NeoForgeAncientRemnants::buildCreativeModeTabs);
    }

    private static void buildCreativeModeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() != CreativeModeTabs.TOOLS_AND_UTILITIES) return;
        event.accept(AncientRemnantsItems.MONOLITH_CODEX);
        event.accept(AncientRemnantsHelper.createCompleteCodex(event.getParameters().holders()));
        AncientRemnantsHelper.createAllResearchScraps(event.getParameters().holders()).forEach(event::accept);
    }
}