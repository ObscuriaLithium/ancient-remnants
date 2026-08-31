package dev.obscuria.remnants.registry;

import dev.obscuria.fragmentum.v2.api.common.registry.Deferred;
import dev.obscuria.remnants.AncientRemnants;
import dev.obscuria.remnants.common.blessing.Blessing;
import dev.obscuria.remnants.common.effect.BlessingEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;

import java.util.function.Supplier;

public final class AncientRemnantsEffects {

    public static final Deferred<MobEffect> HUNTER_INSTINCTS = register("hunter_instincts",
            () -> AncientRemnantsBlessings.HUNTER_INSTINCTS);
    public static final Deferred<MobEffect> WARRIOR_WRATH = register("warrior_wrath",
            () -> AncientRemnantsBlessings.WARRIOR_WRATH);
    public static final Deferred<MobEffect> SENTINEL_AEGIS = register("sentinel_aegis",
            () -> AncientRemnantsBlessings.SENTINEL_AEGIS);
    public static final Deferred<MobEffect> DESPERATE_VITALITY = register("desperate_vitality",
            () -> AncientRemnantsBlessings.DESPERATE_VITALITY);
    public static final Deferred<MobEffect> ARCHITECT_REACH = register("architect_reach",
            () -> AncientRemnantsBlessings.ARCHITECT_REACH);
    public static final Deferred<MobEffect> BOUNTIFUL_HARVEST = register("bountiful_harvest",
            () -> AncientRemnantsBlessings.BOUNTIFUL_HARVEST);

    private static Deferred<MobEffect> register(String name, Supplier<Deferred<Blessing>> blessingSupplier) {
        return AncientRemnantsRegistries.REGISTRAR.register(
                Registries.MOB_EFFECT, AncientRemnants.id(name),
                () -> new BlessingEffect(blessingSupplier));
    }

    static void init() {}
}
