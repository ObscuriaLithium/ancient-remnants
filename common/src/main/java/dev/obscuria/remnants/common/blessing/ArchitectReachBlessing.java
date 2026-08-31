package dev.obscuria.remnants.common.blessing;

import dev.obscuria.fragmentum.v2.api.common.registry.Deferred;
import dev.obscuria.remnants.AncientRemnants;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.function.Supplier;

public class ArchitectReachBlessing extends Blessing {

    public ArchitectReachBlessing(Supplier<Deferred<MobEffect>> effectSupplier) {
        super(effectSupplier);
        this.addAttributeModifier(
                Attributes.BLOCK_INTERACTION_RANGE,
                AncientRemnants.id("architect_reach"),
                3, AttributeModifier.Operation.ADD_VALUE);
    }
}
