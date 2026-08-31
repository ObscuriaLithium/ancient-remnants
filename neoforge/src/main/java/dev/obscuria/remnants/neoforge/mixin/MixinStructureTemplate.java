package dev.obscuria.remnants.neoforge.mixin;

import dev.obscuria.remnants.integration.hook.HookStructureTemplate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StructureTemplate.class)
public abstract class MixinStructureTemplate {

    @Inject(method = "lambda$addEntitiesToWorld$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ServerLevelAccessor;addFreshEntityWithPassengers(Lnet/minecraft/world/entity/Entity;)V", shift = At.Shift.BEFORE))
    private static void placeEntities$finalize(
            StructurePlaceSettings placementIn, Vec3 pos,
            ServerLevelAccessor level, Entity entity, CallbackInfo ci
    ) {
        HookStructureTemplate.onEntityPlaced(level, entity);
    }
}
