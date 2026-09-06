package dev.obscuria.remnants.registry.references;

import dev.obscuria.remnants.AncientRemnants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public interface AncientRemnantsLootTables {

    ResourceKey<LootTable> EXTENSION_PILLAGER_OUTPOST = key("extensions/pillager_outpost");
    ResourceKey<LootTable> RANDOM_RESEARCH_SCRAP = key("random_research_scrap");

    private static ResourceKey<LootTable> key(String name) {
        return ResourceKey.create(Registries.LOOT_TABLE, AncientRemnants.id(name));
    }
}
