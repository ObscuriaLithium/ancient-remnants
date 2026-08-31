package dev.obscuria.remnants.registry;

import dev.obscuria.remnants.AncientRemnants;
import dev.obscuria.remnants.common.component.StoredResearch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;

import java.util.function.UnaryOperator;

public final class AncientRemnantsComponents {

    public static final DataComponentType<StoredResearch> STORED_RESEARCH;

    static {
        STORED_RESEARCH = register("stored_research", builder -> builder.persistent(StoredResearch.CODEC).networkSynchronized(StoredResearch.STREAM_CODEC).cacheEncoding());
    }

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        var type = builder.apply(DataComponentType.builder()).build();
        AncientRemnantsRegistries.REGISTRAR.register(Registries.DATA_COMPONENT_TYPE, AncientRemnants.id(name), () -> type);
        return type;
    }

    static void init() {}
}
