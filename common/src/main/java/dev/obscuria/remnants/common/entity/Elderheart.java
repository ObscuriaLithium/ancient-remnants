package dev.obscuria.remnants.common.entity;

import dev.obscuria.fragmentum.v2.api.common.network.FragmentumNetworking;
import dev.obscuria.remnants.AncientRemnantsHelper;
import dev.obscuria.remnants.client.AncientRemnantsClient;
import dev.obscuria.remnants.common.particle.CrystalParticleOptions;
import dev.obscuria.remnants.common.blessing.Blessing;
import dev.obscuria.remnants.network.ClientboundBlessingAcquiredPayload;
import dev.obscuria.remnants.registry.AncientRemnantsSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

public class Elderheart extends Entity {

    private static final int BLESSING_DISTANCE = 16;

    @Getter @Setter private @Nullable TagKey<Blessing> possibleBlessings;
    @Getter @Setter private @Nullable Holder<Blessing> blessing;

    public Elderheart(EntityType<?> type, Level level) {
        super(type, level);
    }

    public void finalizeSpawn(ServerLevelAccessor level, EntitySpawnReason reason) {
        if (reason != EntitySpawnReason.STRUCTURE) return;
        if (possibleBlessings == null) return;
        level.registryAccess()
                .lookupOrThrow(possibleBlessings.registry())
                .getRandomElementOf(possibleBlessings, random)
                .ifPresent(this::setBlessing);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide()) {
            this.handleClientPlayerEffects();
            this.emitBlockRays();
        }

        if (tickCount % 100 == 0 && blessing != null) {
            @Nullable var player = level().getNearestPlayer(this, BLESSING_DISTANCE);
            if (player instanceof ServerPlayer serverPlayer) {
                if (Objects.equals(blessing, AncientRemnantsHelper.getBlessing(serverPlayer))) return;
                serverPlayer.connection.send(new ClientboundSetTitleTextPacket(Component.translatable("title.ancient_remnants.blessing_acquired")));
                serverPlayer.connection.send(new ClientboundSetSubtitleTextPacket(blessing.value().getDisplayName().copy().withStyle(ChatFormatting.LIGHT_PURPLE)));
                serverPlayer.connection.send(new ClientboundSetTitlesAnimationPacket(40, 80, 40));
                FragmentumNetworking.sendTo(serverPlayer, ClientboundBlessingAcquiredPayload.SHARED);
                AncientRemnantsHelper.setBlessing(serverPlayer, blessing);
            }
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float v) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        input.read("PossibleBlessings", Blessing.TAG_KEY_CODEC).ifPresent(this::setPossibleBlessings);
        input.read("Blessing", Blessing.CODEC).ifPresent(this::setBlessing);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.storeNullable("PossibleBlessings", Blessing.TAG_KEY_CODEC, possibleBlessings);
        output.storeNullable("Blessing", Blessing.CODEC, blessing);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    private void handleClientPlayerEffects() {
        if (!level().isClientSide()) return;
        @Nullable var player = AncientRemnantsClient.localPlayer();
        if (player == null || distanceTo(player) > 256) return;
        AncientRemnantsClient.playElderheartSound(this, AncientRemnantsSoundEvents.AMBIENT_MONOLITH);

        if (distanceTo(player) > BLESSING_DISTANCE) return;
        var direction = player.getEyePosition().vectorTo(getEyePosition().add(
                random.triangle(0, 1.5f),
                random.triangle(0, 1.5f),
                random.triangle(0, 1.5f)));
        this.level().addParticle(
                ParticleTypes.ENCHANT,
                player.getX(), player.getY() + player.getBbHeight(), player.getZ(),
                direction.x, direction.y, direction.z);
    }

    private void emitBlockRays() {
        if (tickCount % 2 == 0) return;

        var origin = position();
        Vec3 direction;
        do {
            direction = new Vec3(
                    random.nextGaussian(),
                    random.nextGaussian(),
                    random.nextGaussian());
        } while (direction.lengthSqr() < 1.0E-6);

        direction = direction.normalize();
        var end = origin.add(direction.scale(10.0));
        var hit = level().clip(new ClipContext(origin, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (hit.getType() != HitResult.Type.BLOCK) return;

        var blockState = level().getBlockState(hit.getBlockPos());
        if (blockState.getRenderShape() == RenderShape.INVISIBLE) return;
        var hitPos = hit.getLocation();
        var motion = origin.subtract(hitPos).normalize().scale(10);

        this.level().addParticle(
                CrystalParticleOptions.of(getX(), getY() + getBbHeight() * 0.5f, getZ()),
                hitPos.x, hitPos.y, hitPos.z,
                motion.x, motion.y, motion.z);
    }
}
