package dev.obscuria.remnants.fabric.mixin;

import dev.obscuria.remnants.integration.hook.HookStructureTemplate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StructureTemplate.class)
public abstract class MixinStructureTemplate {

    @Inject(method = "lambda$placeEntities$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ServerLevelAccessor;addFreshEntityWithPassengers(Lnet/minecraft/world/entity/Entity;)V", shift = At.Shift.BEFORE))
    private static void placeEntities$finalize(
            Rotation rotation, Mirror mirror, Vec3 pos, boolean finalizeEntities,
            ServerLevelAccessor level, Entity entity, CallbackInfo ci
    ) {
        HookStructureTemplate.onEntityPlaced(level, entity);
    }
}
