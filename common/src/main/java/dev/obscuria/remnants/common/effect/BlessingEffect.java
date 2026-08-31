package dev.obscuria.remnants.common.effect;

import dev.obscuria.fragmentum.v2.api.common.registry.Deferred;
import dev.obscuria.remnants.common.blessing.Blessing;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

import java.util.function.Supplier;

public class BlessingEffect extends MobEffect {

    private final Supplier<Deferred<Blessing>> blessingSupplier;

    public BlessingEffect(Supplier<Deferred<Blessing>> blessingSupplier) {
        super(MobEffectCategory.BENEFICIAL, 0xffffff);
        this.blessingSupplier = blessingSupplier;
    }

    @Override
    public Component getDisplayName() {
        var blessingName = blessingSupplier.get().get().getDisplayName().copy();
        return blessingName.withStyle(ChatFormatting.LIGHT_PURPLE);
    }

    @Override
    protected String getOrCreateDescriptionId() {
        return blessingSupplier.get().get().getOrCreateDescriptionId();
    }

    @Override
    public void addAttributeModifiers(AttributeMap attributes, int amplifier) {
        super.addAttributeModifiers(attributes, amplifier);
        this.blessingSupplier.get().get().addAttributeModifiers(attributes);
    }

    @Override
    public void removeAttributeModifiers(AttributeMap attributes) {
        super.removeAttributeModifiers(attributes);
        this.blessingSupplier.get().get().removeAttributeModifiers(attributes);
    }
}