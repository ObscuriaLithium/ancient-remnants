package dev.obscuria.remnants.registry;

import com.mojang.serialization.MapCodec;
import dev.obscuria.fragmentum.v2.api.common.registry.DeferredParticle;
import dev.obscuria.remnants.AncientRemnants;
import dev.obscuria.remnants.common.particle.CrystalParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Supplier;

public final class AncientRemnantsParticleTypes {

    public static final DeferredParticle<CrystalParticleOptions> CRYSTAL = register("crystal", true,
            () -> CrystalParticleOptions.CODEC,
            () -> CrystalParticleOptions.STREAM_CODEC);

    private static <T extends ParticleOptions> DeferredParticle<T> register(
            String name, boolean overrideLimiter,
            Supplier<MapCodec<T>> codecSupplier,
            Supplier<StreamCodec<RegistryFriendlyByteBuf, T>> streamCodecSupplier
    ) {
        return AncientRemnantsRegistries.REGISTRAR.registerParticle(AncientRemnants.id(name), () -> new ParticleType<>(overrideLimiter) {

            @Override
            public MapCodec<T> codec() {
                return codecSupplier.get();
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
                return streamCodecSupplier.get();
            }
        });
    }

    static void init() {}
}
