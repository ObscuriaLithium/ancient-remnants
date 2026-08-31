package dev.obscuria.remnants.integration.hook;

import dev.obscuria.remnants.common.entity.Elderheart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.ServerLevelAccessor;

public final class HookStructureTemplate {

    public static void onEntityPlaced(ServerLevelAccessor level, Entity entity) {
        if (!(entity instanceof Elderheart elderheart)) return;
        elderheart.finalizeSpawn(level, EntitySpawnReason.STRUCTURE);
    }
}
