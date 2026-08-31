package dev.obscuria.remnants.common.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.remnants.registry.AncientRemnantsParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record CrystalParticleOptions(
        float radius,
        float height,
        double coreX,
        double coreY,
        double coreZ
) implements ParticleOptions {

    public static final MapCodec<CrystalParticleOptions> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, CrystalParticleOptions> STREAM_CODEC;

    public static CrystalParticleOptions of(double coreX, double coreY, double coreZ) {
        return new CrystalParticleOptions(2.5F, 0.1F, coreX, coreY, coreZ);
    }

    public static CrystalParticleOptions of(float radius, float height, double coreX, double coreY, double coreZ) {
        return new CrystalParticleOptions(radius, height, coreX, coreY, coreZ);
    }

    @Override
    public ParticleType<CrystalParticleOptions> getType() {
        return AncientRemnantsParticleTypes.CRYSTAL.get();
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.FLOAT.fieldOf("radius").forGetter(o -> o.radius),
                Codec.FLOAT.fieldOf("height").forGetter(o -> o.height),
                Codec.DOUBLE.fieldOf("core_x").forGetter(o -> o.coreX),
                Codec.DOUBLE.fieldOf("core_y").forGetter(o -> o.coreY),
                Codec.DOUBLE.fieldOf("core_z").forGetter(o -> o.coreZ)
        ).apply(instance, CrystalParticleOptions::new));
        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.FLOAT, o -> o.radius,
                ByteBufCodecs.FLOAT, o -> o.height,
                ByteBufCodecs.DOUBLE, o -> o.coreX,
                ByteBufCodecs.DOUBLE, o -> o.coreY,
                ByteBufCodecs.DOUBLE, o -> o.coreZ,
                CrystalParticleOptions::new);
    }
}