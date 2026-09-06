package dev.obscuria.remnants.config.reference;

import com.mojang.serialization.Codec;
import dev.obscuria.fragmentum.v2.api.config.ConfigValue;
import dev.obscuria.remnants.config.CommonConfig;
import net.minecraft.util.StringRepresentable;

public enum IntConfigReference implements StringRepresentable {

    HUNTER_MONOLITH_PLACEMENT_SPACING(CommonConfig.HUNTER_MONOLITH_PLACEMENT_SPACING),
    HUNTER_MONOLITH_PLACEMENT_SEPARATION(CommonConfig.HUNTER_MONOLITH_PLACEMENT_SEPARATION),
    WARRIOR_MONOLITH_PLACEMENT_SPACING(CommonConfig.WARRIOR_MONOLITH_PLACEMENT_SPACING),
    WARRIOR_MONOLITH_PLACEMENT_SEPARATION(CommonConfig.WARRIOR_MONOLITH_PLACEMENT_SEPARATION),
    SENTINEL_MONOLITH_PLACEMENT_SPACING(CommonConfig.SENTINEL_MONOLITH_PLACEMENT_SPACING),
    SENTINEL_MONOLITH_PLACEMENT_SEPARATION(CommonConfig.SENTINEL_MONOLITH_PLACEMENT_SEPARATION),
    CHERRY_MONUMENT_PLACEMENT_SPACING(CommonConfig.CHERRY_MONUMENT_PLACEMENT_SPACING),
    CHERRY_MONUMENT_PLACEMENT_SEPARATION(CommonConfig.CHERRY_MONUMENT_PLACEMENT_SEPARATION),
    OAKWOOD_MONUMENT_PLACEMENT_SPACING(CommonConfig.OAKWOOD_MONUMENT_PLACEMENT_SPACING),
    OAKWOOD_MONUMENT_PLACEMENT_SEPARATION(CommonConfig.OAKWOOD_MONUMENT_PLACEMENT_SEPARATION),
    CONIFEROUS_MONUMENT_PLACEMENT_SPACING(CommonConfig.CONIFEROUS_MONUMENT_PLACEMENT_SPACING),
    CONIFEROUS_MONUMENT_PLACEMENT_SEPARATION(CommonConfig.CONIFEROUS_MONUMENT_PLACEMENT_SEPARATION);

    public static final Codec<IntConfigReference> CODEC = StringRepresentable.fromEnum(IntConfigReference::values);

    private final ConfigValue<Integer> value;

    IntConfigReference(ConfigValue<Integer> value) {
        this.value = value;
    }

    public int get() {
        return value.get();
    }

    @Override
    public String getSerializedName() {
        return "@" + name();
    }
}
