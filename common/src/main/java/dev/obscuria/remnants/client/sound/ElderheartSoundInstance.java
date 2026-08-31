package dev.obscuria.remnants.client.sound;

import dev.obscuria.fragmentum.v2.api.common.Easing;
import dev.obscuria.remnants.common.entity.Elderheart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ElderheartSoundInstance extends AbstractTickableSoundInstance {

    private static final Map<UUID, ElderheartSoundInstance> INSTANCES = new HashMap<>();
    private final UUID uuid;
    private final Vec3 pos;

    public static void play(Elderheart elderheart, SoundEvent event) {
        var instance = INSTANCES.get(elderheart.getUUID());
        if (instance != null && !instance.isStopped()) return;
        instance = new ElderheartSoundInstance(elderheart, event);
        INSTANCES.put(elderheart.getUUID(), instance);
        Minecraft.getInstance().getSoundManager().play(instance);
    }

    protected ElderheartSoundInstance(Elderheart elderheart, SoundEvent event) {
        super(event, SoundSource.AMBIENT, elderheart.getRandom());
        this.uuid = elderheart.getUUID();
        this.pos = elderheart.position();
        this.looping = true;
        this.volume = 0f;
    }

    @Override
    public void tick() {
        if (this.updateVolume()) return;
        this.stopAndClear();
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    private boolean updateVolume() {
        @Nullable var player = Minecraft.getInstance().player;
        if (player == null) return false;
        var distanceFactor = Mth.clamp((float) pos.distanceTo(player.position()) / 256f, 0f, 1f);
        if (distanceFactor >= 1f) return false;
        this.volume = 1f - Easing.EASE_OUT_CUBIC.compute(distanceFactor);
        return true;
    }

    private void stopAndClear() {
        INSTANCES.remove(uuid);
        this.stop();
    }
}
