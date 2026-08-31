package dev.obscuria.remnants.registry;

import dev.obscuria.fragmentum.v2.api.common.registry.Deferred;
import dev.obscuria.remnants.AncientRemnants;
import dev.obscuria.remnants.common.blessing.*;

import java.util.function.Supplier;

public final class AncientRemnantsBlessings {

    public static final Deferred<Blessing> HUNTER_INSTINCTS = register("hunter_instincts",
            () -> new HunterInstinctsBlessing(() -> AncientRemnantsEffects.HUNTER_INSTINCTS));
    public static final Deferred<Blessing> WARRIOR_WRATH = register("warrior_wrath",
            () -> new WarriorWrathBlessing(() -> AncientRemnantsEffects.WARRIOR_WRATH));
    public static final Deferred<Blessing> SENTINEL_AEGIS = register("sentinel_aegis",
            () -> new SentinelAegisBlessing(() -> AncientRemnantsEffects.SENTINEL_AEGIS));
    public static final Deferred<Blessing> DESPERATE_VITALITY = register("desperate_vitality",
            () -> new DesperateVitalityBlessing(() -> AncientRemnantsEffects.DESPERATE_VITALITY));
    public static final Deferred<Blessing> ARCHITECT_REACH = register("architect_reach",
            () -> new ArchitectReachBlessing(() -> AncientRemnantsEffects.ARCHITECT_REACH));
    public static final Deferred<Blessing> BOUNTIFUL_HARVEST = register("bountiful_harvest",
            () -> new BountifulHarvestBlessing(() -> AncientRemnantsEffects.BOUNTIFUL_HARVEST));

    private static Deferred<Blessing> register(String name, Supplier<Blessing> supplier) {
        return AncientRemnantsRegistries.REGISTRAR.register(
                AncientRemnantsRegistries.BLESSING,
                AncientRemnants.id(name), supplier);
    }

    static void init() {}
}
