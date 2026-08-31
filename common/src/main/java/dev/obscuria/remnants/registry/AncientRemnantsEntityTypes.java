package dev.obscuria.remnants.registry;

import dev.obscuria.fragmentum.v2.api.common.registry.DeferredEntity;
import dev.obscuria.remnants.AncientRemnants;
import dev.obscuria.remnants.common.entity.Elderheart;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public final class AncientRemnantsEntityTypes {

    public static final DeferredEntity<Elderheart> ELDERHEART = register("elderheart",
            () -> EntityType.Builder.of(Elderheart::new, MobCategory.MISC)
                    .sized(2, 2)
                    .clientTrackingRange(256)
                    .fireImmune());

    private static <T extends Entity> DeferredEntity<T> register(String name, Supplier<EntityType.Builder<T>> supplier) {
        return AncientRemnantsRegistries.REGISTRAR.registerEntity(AncientRemnants.id(name), () -> {
            var resourceKey = ResourceKey.create(Registries.ENTITY_TYPE, AncientRemnants.id(name));
            return supplier.get().build(resourceKey);
        });
    }

    static void init() {}
}
