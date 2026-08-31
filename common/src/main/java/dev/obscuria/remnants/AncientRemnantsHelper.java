package dev.obscuria.remnants;

import dev.obscuria.remnants.common.ResearchEntry;
import dev.obscuria.remnants.common.blessing.Blessing;
import dev.obscuria.remnants.common.component.StoredResearch;
import dev.obscuria.remnants.integration.extension.IPlayerExtension;
import dev.obscuria.remnants.registry.AncientRemnantsComponents;
import dev.obscuria.remnants.registry.AncientRemnantsItems;
import dev.obscuria.remnants.registry.AncientRemnantsRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public final class AncientRemnantsHelper {

    public static @Nullable Holder<Blessing> getBlessing(Player player) {
        return ((IPlayerExtension) player).ancientRemnants$getBlessing();
    }

    public static void setBlessing(Player player, @Nullable Holder<Blessing> blessing) {
        ((IPlayerExtension) player).ancientRemnants$setBlessing(blessing);
    }

    public static ItemStack createCompleteCodex(HolderLookup.Provider provider) {
        var result = AncientRemnantsItems.MONOLITH_CODEX.instantiate();
        result.set(AncientRemnantsComponents.STORED_RESEARCH, StoredResearch.of(listResearchEntries(provider)));
        return result;
    }

    public static ItemStack createResearchScrap(Holder<ResearchEntry> entry) {
        var result = AncientRemnantsItems.RESEARCH_SCRAP.instantiate();
        result.set(AncientRemnantsComponents.STORED_RESEARCH, StoredResearch.of(entry));
        return result;
    }

    public static List<ItemStack> createAllResearchScraps(HolderLookup.Provider provider) {
        return listResearchEntries(provider).map(AncientRemnantsHelper::createResearchScrap).toList();
    }

    public static Stream<Holder.Reference<ResearchEntry>> listResearchEntries(HolderLookup.Provider provider) {
        return provider.lookupOrThrow(AncientRemnantsRegistries.Keys.RESEARCH).listElements();
    }

    public static Stream<Holder.Reference<ResearchEntry>> listResearchEntries(HolderLookup.Provider provider, Holder<Biome> biome) {
        return listResearchEntries(provider).filter(it -> biome.is(it.value().biomes()));
    }
}