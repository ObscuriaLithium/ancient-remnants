package dev.obscuria.remnants.integration;

import dev.obscuria.fragmentum.v2.api.config.ConfigValue;
import dev.obscuria.remnants.config.CommonConfig;
import dev.obscuria.remnants.registry.tags.AncientRemnantsBiomeTags;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public final class TagPostProcessors {

    private static final Map<TagKey<?>, List<Postprocessor>> POSTPROCESSORS = new ConcurrentHashMap<>();
    private static final Map<Identifier, String> IRREGULAR_DIRECTORIES;

    public static void register(TagKey<?> tagKey, Postprocessor postprocessor) {
        POSTPROCESSORS.computeIfAbsent(tagKey, key -> new CopyOnWriteArrayList<>()).add(postprocessor);
    }

    public static void register(TagKey<?> tagKey, ConfigValue<List<? extends String>> configValue) {
        register(tagKey, () -> configValue.get().stream().map(Entry::parse).collect(Collectors.toList()));
    }

    public static void registerStatic(TagKey<?> tagKey, Collection<Entry> entries) {
        register(tagKey, () -> entries);
    }

    public static void registerStaticRaw(TagKey<?> tagKey, Collection<String> rawStrings) {
        register(tagKey, () -> parseAll(rawStrings));
    }

    public static List<Entry> parseAll(Collection<String> rawStrings) {
        return rawStrings.stream().map(Entry::parse).collect(Collectors.toList());
    }

    public static Map<TagKey<?>, List<Postprocessor>> view() {
        return POSTPROCESSORS;
    }

    public static String directoryFor(ResourceKey<? extends Registry<?>> registryKey) {
        String irregular = IRREGULAR_DIRECTORIES.get(registryKey.identifier());
        if (irregular != null) {
            return irregular;
        }
        return "tags/" + registryKey.identifier().getPath();
    }

    static {
        IRREGULAR_DIRECTORIES = Map.of(
                Identifier.withDefaultNamespace("block"), "tags/blocks",
                Identifier.withDefaultNamespace("item"), "tags/items",
                Identifier.withDefaultNamespace("entity_type"), "tags/entity_types",
                Identifier.withDefaultNamespace("fluid"), "tags/fluids",
                Identifier.withDefaultNamespace("game_event"), "tags/game_events");

        register(AncientRemnantsBiomeTags.HAS_HUNTER_MONOLITH, CommonConfig.HUNTER_MONOLITH_BIOMES);
        register(AncientRemnantsBiomeTags.HAS_WARRIOR_MONOLITH, CommonConfig.WARRIOR_MONOLITH_BIOMES);
        register(AncientRemnantsBiomeTags.HAS_SENTINEL_MONOLITH, CommonConfig.SENTINEL_MONOLITH_BIOMES);
        register(AncientRemnantsBiomeTags.HAS_CHERRY_MONUMENT, CommonConfig.CHERRY_MONUMENT_BIOMES);
        register(AncientRemnantsBiomeTags.HAS_OAKWOOD_MONUMENT, CommonConfig.OAKWOOD_MONUMENT_BIOMES);
        register(AncientRemnantsBiomeTags.HAS_CONIFEROUS_MONUMENT, CommonConfig.CONIFEROUS_MONUMENT_BIOMES);
    }

    @FunctionalInterface
    public interface Postprocessor {

        Collection<Entry> collect();
    }

    public sealed interface Entry {

        record Element(Identifier id) implements Entry {}

        record Tag(Identifier id) implements Entry {}

        static Entry element(Identifier id) {
            return new Element(id);
        }

        static Entry tag(Identifier id) {
            return new Tag(id);
        }

        static Entry parse(String raw) {
            return raw.startsWith("#")
                    ? tag(Identifier.parse(raw.substring(1)))
                    : element(Identifier.parse(raw));
        }
    }
}