package dev.obscuria.remnants.integration.hook;

import dev.obscuria.remnants.integration.TagPostProcessors;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class HookTagLoader {

    public static void build(String directory, Map<Identifier, List<TagLoader.EntryWithSource>> builders) {
        for (var registration : TagPostProcessors.view().entrySet()) {
            var tagKey = registration.getKey();
            var expectedDirectory = TagPostProcessors.directoryFor(tagKey.registry());
            if (!expectedDirectory.equals(directory)) continue;

            var entries = builders.computeIfAbsent(tagKey.location(), id -> new ArrayList<>());

            for (var postprocessor : registration.getValue()) {
                try {
                    var additions = postprocessor.collect();
                    for (var addition : additions) {
                        if (addition instanceof TagPostProcessors.Entry.Element(Identifier id)) {
                            entries.add(new TagLoader.EntryWithSource(
                                    TagEntry.optionalElement(id),
                                    "aquamirae:config"));
                        } else if (addition instanceof TagPostProcessors.Entry.Tag(Identifier id)) {
                            entries.add(new TagLoader.EntryWithSource(
                                    TagEntry.optionalTag(id),
                                    "aquamirae:config"));
                        }
                    }
                } catch (Exception ignored) {}
            }
        }
    }
}
