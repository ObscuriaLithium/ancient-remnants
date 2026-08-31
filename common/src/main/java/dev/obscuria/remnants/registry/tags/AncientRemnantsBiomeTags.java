package dev.obscuria.remnants.registry.tags;

import dev.obscuria.remnants.AncientRemnants;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public interface AncientRemnantsBiomeTags {

    TagKey<Biome> HAS_HUNTER_MONOLITH = create("has_structure/hunter_monolith");
    TagKey<Biome> HAS_WARRIOR_MONOLITH = create("has_structure/warrior_monolith");
    TagKey<Biome> HAS_SENTINEL_MONOLITH = create("has_structure/sentinel_monolith");
    TagKey<Biome> HAS_CHERRY_MONUMENT = create("has_structure/cherry_monument");
    TagKey<Biome> HAS_OAKWOOD_MONUMENT = create("has_structure/oakwood_monument");
    TagKey<Biome> HAS_CONIFEROUS_MONUMENT = create("has_structure/coniferous_monument");

    private static TagKey<Biome> create(String name) {
        return TagKey.create(Registries.BIOME, AncientRemnants.id(name));
    }
}