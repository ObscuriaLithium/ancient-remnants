package dev.obscuria.remnants.fabric;

import dev.obscuria.remnants.AncientRemnants;
import dev.obscuria.remnants.AncientRemnantsHelper;
import dev.obscuria.remnants.registry.AncientRemnantsItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.world.item.CreativeModeTabs;

public final class FabricAncientRemnants implements ModInitializer {

    @Override
    public void onInitialize() {
        AncientRemnants.init();

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(output -> {
            output.accept(AncientRemnantsItems.MONOLITH_CODEX);
            output.accept(AncientRemnantsHelper.createCompleteCodex(output.getContext().holders()));
            AncientRemnantsHelper.createAllResearchScraps(output.getContext().holders()).forEach(output::accept);
        });
    }
}