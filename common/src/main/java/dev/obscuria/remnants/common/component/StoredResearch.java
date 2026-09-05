package dev.obscuria.remnants.common.component;

import com.mojang.serialization.Codec;
import dev.obscuria.remnants.common.ResearchEntry;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public record StoredResearch(List<Holder<ResearchEntry>> entries) {

    public static final StoredResearch EMPTY;
    public static final Codec<StoredResearch> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, StoredResearch> STREAM_CODEC;

    private static final Comparator<Holder<ResearchEntry>> ORDER;

    public static StoredResearch of(Stream<Holder.Reference<ResearchEntry>> entries) {
        return new StoredResearch(entries.<Holder<ResearchEntry>>map(Function.identity()).toList());
    }

    public static StoredResearch of(Holder<ResearchEntry> entry) {
        return new StoredResearch(List.of(entry));
    }

    public StoredResearch {
        entries = entries.stream().distinct().sorted(ORDER).toList();
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public boolean contains(Holder<ResearchEntry> entry) {
        return entries.contains(entry);
    }

    public StoredResearch with(Holder<ResearchEntry> entry) {
        if (entries.contains(entry)) return this;
        var result = new ArrayList<>(entries);
        result.add(entry);
        return new StoredResearch(result);
    }

    public StoredResearch without(Holder<ResearchEntry> entry) {
        if (!entries.contains(entry)) return this;
        var result = new ArrayList<>(entries);
        result.remove(entry);
        return result.isEmpty() ? EMPTY : new StoredResearch(result);
    }

    public StoredResearch merge(StoredResearch other) {
        if (other.isEmpty()) return this;
        if (isEmpty()) return other;

        var result = new ArrayList<>(entries);
        for (var entry : other.entries) {
            if (!result.contains(entry)) {
                result.add(entry);
            }
        }

        return new StoredResearch(result);
    }

    static {
        ORDER = Comparator.comparingInt(Holder::hashCode);
        EMPTY = new StoredResearch(List.of());
        CODEC = ResearchEntry.CODEC.listOf().xmap(StoredResearch::new, StoredResearch::entries);
        STREAM_CODEC = StreamCodec.composite(
                ResearchEntry.STREAM_CODEC.apply(ByteBufCodecs.list()), StoredResearch::entries,
                StoredResearch::new);
    }
}