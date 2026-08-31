package dev.obscuria.remnants.common.blessing;

import com.mojang.serialization.Codec;
import dev.obscuria.fragmentum.v2.api.common.registry.Deferred;
import dev.obscuria.remnants.integration.ValueContext;
import dev.obscuria.remnants.registry.AncientRemnantsRegistries;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Util;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;

public class Blessing {

    public static final Codec<Holder<Blessing>> CODEC;
    public static final Codec<TagKey<Blessing>> TAG_KEY_CODEC;

    private final Map<Holder<Attribute>, AttributeTemplate> attributeModifiers;
    private final Supplier<Deferred<MobEffect>> effectSupplier;
    private @Nullable String descriptionId;

    public Blessing(Supplier<Deferred<MobEffect>> effectSupplier) {
        this.effectSupplier = effectSupplier;
        this.attributeModifiers = new Object2ObjectOpenHashMap<>();
    }

    public void onAdded(Player player) {
        this.onTick(player);
    }

    public void onTick(Player player) {
        if (player.hasEffect(effectSupplier.get().holder())) return;
        player.addEffect(new MobEffectInstance(effectSupplier.get().holder(), -1, 0, false, false, true));
    }

    public void onRemoved(Player player) {
        player.removeEffect(effectSupplier.get().holder());
    }

    public void modifyIncomingDamage(Player player, DamageSource source, ValueContext context) {}

    public void modifyOutgoingDamage(Player player, LivingEntity victim, DamageSource source, ValueContext context) {}

    public void modifyArmorValue(Player player, DamageSource source, ValueContext context) {}

    public void addAttributeModifier(Holder<Attribute> attribute, Identifier id, double amount, AttributeModifier.Operation operation) {
        this.attributeModifiers.put(attribute, new AttributeTemplate(id, amount, operation));
    }

    public void addAttributeModifiers(AttributeMap attributes) {
        for (var entry : attributeModifiers.entrySet()) {
            @Nullable var attribute = attributes.getInstance(entry.getKey());
            if (attribute == null) return;
            attribute.removeModifier(entry.getValue().id());
            attribute.addPermanentModifier(entry.getValue().create());
        }
    }

    public void removeAttributeModifiers(AttributeMap attributes) {
        for (var entry : attributeModifiers.entrySet()) {
            @Nullable var attribute = attributes.getInstance(entry.getKey());
            if (attribute == null) continue;
            attribute.removeModifier(entry.getValue().id());
        }
    }

    public Component getDisplayName() {
        return Component.translatable(getOrCreateDescriptionId());
    }

    public String getOrCreateDescriptionId() {
        if (descriptionId != null) return descriptionId;
        this.descriptionId = Util.makeDescriptionId("blessing", AncientRemnantsRegistries.BLESSING.getKey(this));
        return descriptionId;
    }

    static {
        CODEC = AncientRemnantsRegistries.BLESSING.holderByNameCodec();
        TAG_KEY_CODEC = TagKey.codec(AncientRemnantsRegistries.Keys.BLESSING);
    }

    private record AttributeTemplate(Identifier id, double amount, AttributeModifier.Operation operation) {

        public AttributeModifier create() {
            return new AttributeModifier(id, amount, operation);
        }
    }
}