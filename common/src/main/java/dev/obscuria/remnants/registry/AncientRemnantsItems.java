package dev.obscuria.remnants.registry;

import dev.obscuria.fragmentum.v2.api.common.registry.DeferredItem;
import dev.obscuria.remnants.AncientRemnants;
import dev.obscuria.remnants.common.item.MonolithCodexItem;
import dev.obscuria.remnants.common.item.ResearchScrapItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.function.Function;

public final class AncientRemnantsItems {

    public static final DeferredItem<Item> MONOLITH_CODEX = register("monolith_codex", properties -> new MonolithCodexItem(properties.rarity(Rarity.RARE).stacksTo(1)));
    public static final DeferredItem<Item> RESEARCH_SCRAP = register("research_scrap", properties -> new ResearchScrapItem(properties.rarity(Rarity.UNCOMMON)));

    private static DeferredItem<Item> register(String name, Function<Item.Properties, Item> function) {
        return AncientRemnantsRegistries.REGISTRAR.registerItem(AncientRemnants.id(name), () -> {
            var resourceKey = ResourceKey.create(Registries.ITEM, AncientRemnants.id(name));
            return function.apply(new Item.Properties().setId(resourceKey));
        });
    }

    static void init() {}
}
