package dev.obscuria.remnants.registry;

import dev.obscuria.fragmentum.v2.api.common.registry.FragmentumRegistry;
import dev.obscuria.fragmentum.v2.api.common.registry.Registrar;
import dev.obscuria.remnants.AncientRemnants;
import dev.obscuria.remnants.common.ResearchEntry;
import dev.obscuria.remnants.common.blessing.Blessing;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class AncientRemnantsRegistries {

    static final Registrar REGISTRAR = FragmentumRegistry.registrar(AncientRemnants.MOD_ID);

    public static final Registry<Blessing> BLESSING = REGISTRAR.createRegistry(Keys.BLESSING);

    public static final class Keys {

        public static final ResourceKey<Registry<Blessing>> BLESSING = key("blessing");
        public static final ResourceKey<Registry<ResearchEntry>> RESEARCH = key("research");

        private static <T> ResourceKey<Registry<T>> key(String name) {
            return ResourceKey.createRegistryKey(AncientRemnants.id(name));
        }
    }

    public static void init() {

        REGISTRAR.createSyncedDataRegistry(Keys.RESEARCH, () -> ResearchEntry.DIRECT_CODEC);

        AncientRemnantsSoundEvents.init();
        AncientRemnantsBlessings.init();
        AncientRemnantsEffects.init();
        AncientRemnantsComponents.init();
        AncientRemnantsEntityTypes.init();
        AncientRemnantsParticleTypes.init();
        AncientRemnantsItems.init();
        AncientRemnantsRecipeSerializers.init();
    }
}
