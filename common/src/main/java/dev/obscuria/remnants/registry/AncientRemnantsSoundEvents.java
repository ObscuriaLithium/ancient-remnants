package dev.obscuria.remnants.registry;

import dev.obscuria.remnants.AncientRemnants;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;

public final class AncientRemnantsSoundEvents {

    public static final SoundEvent AMBIENT_MONOLITH = register("ambient.monolith");
    public static final SoundEvent AMBIENT_MONUMENT = register("ambient.monument");
    public static final SoundEvent EFFECT_WIND_LOOP = register("effect.wind_loop");
    public static final SoundEvent EFFECT_BLESSING = register("effect.blessing");

    private static SoundEvent register(String name) {
        var soundEvent = SoundEvent.createVariableRangeEvent(AncientRemnants.id(name));
        AncientRemnantsRegistries.REGISTRAR.register(Registries.SOUND_EVENT, AncientRemnants.id(name), () -> soundEvent);
        return soundEvent;
    }

    static void init() {}
}
