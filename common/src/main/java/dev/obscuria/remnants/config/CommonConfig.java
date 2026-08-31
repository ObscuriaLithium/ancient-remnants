package dev.obscuria.remnants.config;

import dev.obscuria.remnants.AncientRemnants;
import dev.obscuria.fragmentum.v2.api.config.ConfigBuilder;
import dev.obscuria.fragmentum.v2.api.config.ConfigValue;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.List;

@UtilityClass
public final class CommonConfig {

    public static final ConfigValue<Integer> HUNTER_MONOLITH_PLACEMENT_SPACING;
    public static final ConfigValue<Integer> HUNTER_MONOLITH_PLACEMENT_SEPARATION;
    public static final ConfigValue<List<? extends String>> HUNTER_MONOLITH_BIOMES;

    public static final ConfigValue<Integer> WARRIOR_MONOLITH_PLACEMENT_SPACING;
    public static final ConfigValue<Integer> WARRIOR_MONOLITH_PLACEMENT_SEPARATION;
    public static final ConfigValue<List<? extends String>> WARRIOR_MONOLITH_BIOMES;

    public static final ConfigValue<Integer> SENTINEL_MONOLITH_PLACEMENT_SPACING;
    public static final ConfigValue<Integer> SENTINEL_MONOLITH_PLACEMENT_SEPARATION;
    public static final ConfigValue<List<? extends String>> SENTINEL_MONOLITH_BIOMES;

    public static final ConfigValue<Integer> CHERRY_MONUMENT_PLACEMENT_SPACING;
    public static final ConfigValue<Integer> CHERRY_MONUMENT_PLACEMENT_SEPARATION;
    public static final ConfigValue<List<? extends String>> CHERRY_MONUMENT_BIOMES;

    public static final ConfigValue<Integer> OAKWOOD_MONUMENT_PLACEMENT_SPACING;
    public static final ConfigValue<Integer> OAKWOOD_MONUMENT_PLACEMENT_SEPARATION;
    public static final ConfigValue<List<? extends String>> OAKWOOD_MONUMENT_BIOMES;

    public static final ConfigValue<Integer> CONIFEROUS_MONUMENT_PLACEMENT_SPACING;
    public static final ConfigValue<Integer> CONIFEROUS_MONUMENT_PLACEMENT_SEPARATION;
    public static final ConfigValue<List<? extends String>> CONIFEROUS_MONUMENT_BIOMES;

    public static final HashSet<ConfigValue<?>> VALUES;

    public static void init() {}

    static {
        final var builder = ConfigBuilder.create("obscuria/ancient_remnants-common.toml");

        builder.comment(
                "=========[ Ancient Remnants Common Config ]=========",
                " Please prefer editing the config through the in-game ",
                " configuration screen (available via the mods list),",
                " as it includes many hints and quality-of-life improvements.",
                "====================================================");

        HUNTER_MONOLITH_PLACEMENT_SPACING = builder.defineInt("hunter_monolith_placement_spacing", 100, 0, Integer.MAX_VALUE);
        HUNTER_MONOLITH_PLACEMENT_SEPARATION = builder.defineInt("hunter_monolith_placement_separation", 20, 0, Integer.MAX_VALUE);
        HUNTER_MONOLITH_BIOMES = builder.defineList("hunter_monolith_biomes", List.of("minecraft:badlands"), () -> "");

        WARRIOR_MONOLITH_PLACEMENT_SPACING = builder.defineInt("warrior_monolith_placement_spacing", 100, 0, Integer.MAX_VALUE);
        WARRIOR_MONOLITH_PLACEMENT_SEPARATION = builder.defineInt("warrior_monolith_placement_separation", 20, 0, Integer.MAX_VALUE);
        WARRIOR_MONOLITH_BIOMES = builder.defineList("warrior_monolith_biomes", List.of("minecraft:ice_spikes"), () -> "");

        SENTINEL_MONOLITH_PLACEMENT_SPACING = builder.defineInt("sentinel_monolith_placement_spacing", 100, 0, Integer.MAX_VALUE);
        SENTINEL_MONOLITH_PLACEMENT_SEPARATION = builder.defineInt("sentinel_monolith_placement_separation", 20, 0, Integer.MAX_VALUE);
        SENTINEL_MONOLITH_BIOMES = builder.defineList("sentinel_monolith_biomes", List.of("minecraft:deep_frozen_ocean"), () -> "");

        CHERRY_MONUMENT_PLACEMENT_SPACING = builder.defineInt("cherry_monument_placement_spacing", 100, 0, Integer.MAX_VALUE);
        CHERRY_MONUMENT_PLACEMENT_SEPARATION = builder.defineInt("cherry_monument_placement_separation", 20, 0, Integer.MAX_VALUE);
        CHERRY_MONUMENT_BIOMES = builder.defineList("cherry_monument_biomes", List.of("minecraft:cherry_grove"), () -> "");

        OAKWOOD_MONUMENT_PLACEMENT_SPACING = builder.defineInt("oakwood_monument_placement_spacing", 100, 0, Integer.MAX_VALUE);
        OAKWOOD_MONUMENT_PLACEMENT_SEPARATION = builder.defineInt("oakwood_monument_placement_separation", 20, 0, Integer.MAX_VALUE);
        OAKWOOD_MONUMENT_BIOMES = builder.defineList("oakwood_monument_biomes", List.of("#minecraft:is_forest"), () -> "");

        CONIFEROUS_MONUMENT_PLACEMENT_SPACING = builder.defineInt("coniferous_monument_placement_spacing", 100, 0, Integer.MAX_VALUE);
        CONIFEROUS_MONUMENT_PLACEMENT_SEPARATION = builder.defineInt("coniferous_monument_placement_separation", 20, 0, Integer.MAX_VALUE);
        CONIFEROUS_MONUMENT_BIOMES = builder.defineList("coniferous_monument_biomes", List.of("#minecraft:is_taiga"), () -> "");

        VALUES = builder.buildCommon(AncientRemnants.MOD_ID);
    }
}
