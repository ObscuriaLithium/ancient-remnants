package dev.obscuria.remnants.registry;

import com.mojang.serialization.MapCodec;
import dev.obscuria.remnants.AncientRemnants;
import dev.obscuria.remnants.common.structure.ConfigurableStructurePlacement;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

public final class AncientRemnantsStructurePlacements {

    public static final StructurePlacementType<ConfigurableStructurePlacement> CONFIGURABLE_RANDOM_SPREAD = register("configurable_random_spread", ConfigurableStructurePlacement.CODEC);

    private static <T extends StructurePlacement> StructurePlacementType<T> register(String name, MapCodec<T> codec) {
        StructurePlacementType<T> type = () -> codec;
        AncientRemnantsRegistries.REGISTRAR.register(Registries.STRUCTURE_PLACEMENT, AncientRemnants.id(name), () -> type);
        return type;
    }

    static void init() {}
}