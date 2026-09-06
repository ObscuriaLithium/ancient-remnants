package dev.obscuria.remnants.fabric;

import dev.obscuria.remnants.AncientRemnants;
import dev.obscuria.remnants.AncientRemnantsHelper;
import dev.obscuria.remnants.registry.AncientRemnantsItems;
import dev.obscuria.remnants.registry.references.AncientRemnantsLootTables;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;

public final class FabricAncientRemnants implements ModInitializer {

    private static final ResourceKey<LootTable> PILLAGER_OUTPOST_CHEST;

    @Override
    public void onInitialize() {
        AncientRemnants.init();

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(output -> {
            output.accept(AncientRemnantsItems.MONOLITH_CODEX);
            output.accept(AncientRemnantsHelper.createCompleteCodex(output.getContext().holders()));
            AncientRemnantsHelper.createAllResearchScraps(output.getContext().holders()).forEach(output::accept);
        });

        LootTableEvents.MODIFY.register((key, builder, _, _) -> {
            if (!key.equals(PILLAGER_OUTPOST_CHEST)) return;
            var reference = NestedLootTable.lootTableReference(AncientRemnantsLootTables.EXTENSION_PILLAGER_OUTPOST);
            builder.withPool(LootPool.lootPool().add(reference));
        });
    }

    static {
        PILLAGER_OUTPOST_CHEST = ResourceKey.create(
                Registries.LOOT_TABLE,
                Identifier.withDefaultNamespace("chests/pillager_outpost"));
    }
}