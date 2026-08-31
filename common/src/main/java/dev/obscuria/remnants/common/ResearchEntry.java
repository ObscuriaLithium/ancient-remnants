package dev.obscuria.remnants.common;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.remnants.registry.AncientRemnantsRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public record ResearchEntry(
        TagKey<Biome> biomes,
        Component header,
        Component content,
        Identifier texture,
        boolean rightPage
) {

    public static final Codec<ResearchEntry> DIRECT_CODEC;
    public static final Codec<Holder<ResearchEntry>> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<ResearchEntry>> STREAM_CODEC;

    static {
        DIRECT_CODEC = RecordCodecBuilder.create(builder -> builder.group(
                TagKey.codec(Registries.BIOME).fieldOf("biomes").forGetter(ResearchEntry::biomes),
                ComponentSerialization.CODEC.fieldOf("header").forGetter(ResearchEntry::header),
                ComponentSerialization.CODEC.fieldOf("content").forGetter(ResearchEntry::content),
                Identifier.CODEC.fieldOf("texture").forGetter(ResearchEntry::texture),
                Codec.BOOL.optionalFieldOf("right_page", false).forGetter(ResearchEntry::rightPage)
        ).apply(builder, ResearchEntry::new));
        CODEC = RegistryFixedCodec.create(AncientRemnantsRegistries.Keys.RESEARCH);
        STREAM_CODEC = ByteBufCodecs.holderRegistry(AncientRemnantsRegistries.Keys.RESEARCH);
    }
}
