package dev.obscuria.remnants.client;

import dev.obscuria.fragmentum.v2.api.client.FragmentumClientRegistry;
import dev.obscuria.remnants.AncientRemnants;
import dev.obscuria.remnants.client.model.ModelElderheart;
import dev.obscuria.remnants.client.particle.CrystalParticle;
import dev.obscuria.remnants.client.renderer.ElderheartRenderer;
import dev.obscuria.remnants.client.screen.MonolithCodexScreen;
import dev.obscuria.remnants.client.sound.ElderheartSoundInstance;
import dev.obscuria.remnants.common.entity.Elderheart;
import dev.obscuria.remnants.registry.AncientRemnantsEntityTypes;
import dev.obscuria.remnants.registry.AncientRemnantsParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;

public final class AncientRemnantsClient {

    public static void init() {
        var registrar = FragmentumClientRegistry.registrar(AncientRemnants.MOD_ID);
        registrar.registerModelLayer(ModelElderheart.LAYER, ModelElderheart::createBodyLayer);
        registrar.registerEntityRenderer(AncientRemnantsEntityTypes.ELDERHEART, ElderheartRenderer::new);
        registrar.registerTexturedParticleRenderer(AncientRemnantsParticleTypes.CRYSTAL, CrystalParticle.Provider::new);
    }

    public static void openMonolithCodexScreen(ItemStack stack, RegistryAccess registryAccess) {
        Minecraft.getInstance().setScreen(new MonolithCodexScreen(stack, registryAccess));
    }

    public static void playElderheartSound(Elderheart elderheart, SoundEvent soundEvent) {
        ElderheartSoundInstance.play(elderheart, soundEvent);
    }
}
