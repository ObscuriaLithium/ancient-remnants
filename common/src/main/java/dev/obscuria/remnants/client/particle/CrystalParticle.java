package dev.obscuria.remnants.client.particle;

import dev.obscuria.fragmentum.v2.api.common.Easing;
import dev.obscuria.fragmentum.v2.api.common.EasingFunction;
import dev.obscuria.remnants.common.particle.CrystalParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class CrystalParticle extends SingleQuadParticle {

    private static final int LIFETIME_TICKS = 120;
    private static final EasingFunction SCALE_EASING;

    private static final double RADIUS_JITTER_FRACTION = 0.025D;
    private static final double HEIGHT_JITTER_BLOCKS = 0.025D;

    private final SpriteSet spriteSet;

    private final double coreX;
    private final double coreY;
    private final double coreZ;

    private final float maxRadius;
    private final float riseHeight;

    private final double radiusJitter;
    private final double heightJitter;
    private final float size;

    private double angle;
    private final double angularDirection;

    protected CrystalParticle(ClientLevel level, double x, double y, double z,
                              CrystalParticleOptions options, SpriteSet spriteSet, RandomSource random) {

        super(level, x, y, z, spriteSet.get(random));
        this.spriteSet = spriteSet;

        this.coreX = options.coreX();
        this.coreY = options.coreY();
        this.coreZ = options.coreZ();

        this.maxRadius = Math.max(0.1F, options.radius());
        this.riseHeight = options.height();


        this.radiusJitter = (this.random.nextDouble() - 0.5D) * 2.0D * (this.maxRadius * RADIUS_JITTER_FRACTION);
        this.heightJitter = (this.random.nextDouble() - 0.5D) * 2.0D * HEIGHT_JITTER_BLOCKS;

        this.angle = this.random.nextDouble() * Math.PI * 2.0D;
        this.angularDirection = this.random.nextBoolean() ? 1.0D : -1.0D;

        this.size = 0.12F + this.random.nextFloat() * 0.08F;
        this.quadSize = size;
        this.scale(0);
        this.lifetime = LIFETIME_TICKS;

        this.hasPhysics = false;
        this.gravity = 0.0F;

        this.alpha = 1.0F;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age % 20 == 0) {
            level.addParticle(ParticleTypes.FIREFLY, x, y, z, xd, yd, zd);
        }

        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        this.updateTornadoMotion();
        this.move(this.xd, this.yd, this.zd);
        this.setSpriteFromAge(this.spriteSet);
    }

    @Override
    public void move(double xa, double ya, double za) {
        this.setBoundingBox(this.getBoundingBox().move(xa, ya, za));
        this.setLocationFromBoundingbox();
    }

    private void updateTornadoMotion() {
        float progress = Mth.clamp((float) this.age / (float) this.lifetime, 0.0F, 1.0F);

        @Nullable Vec3 target = null;

        for (Player player : level.getEntitiesOfClass(Player.class, getBoundingBox().inflate(8))) {
            target = player.position().add(0, player.getBbHeight() * 0.5, 0);
        }

        this.quadSize = size;
        this.scale(SCALE_EASING.compute(progress));

        double angularSpeed = (0.05D + progress * 0.1D) * this.angularDirection;
        this.angle += angularSpeed;

        double shrinkProgress = Mth.clamp(progress * 2.5D, 0.0D, 1.0D);
        double baseTargetRadius = Mth.lerp(shrinkProgress, this.maxRadius * 2.0D, this.maxRadius * 2D);
        if (target != null) baseTargetRadius *= 0.5;
        double targetRadius = Math.max(0.05D, baseTargetRadius + this.radiusJitter * shrinkProgress);

        double targetX = (target != null ? target.x : this.coreX) + Math.cos(this.angle) * targetRadius;
        double targetZ = (target != null ? target.z : this.coreZ) + Math.sin(this.angle) * targetRadius;
        double targetY = (target != null ? target.y : this.coreY) + progress * this.riseHeight
                + (target != null ? heightJitter * 10 : this.heightJitter)
                + Math.sin(this.age * 0.35D) * 0.05D;

        double pull = Mth.lerp(progress, 0.05, 0.3);
        double nx = Mth.lerp(pull, this.x, targetX);
        double ny = Mth.lerp(pull, this.y, targetY);
        double nz = Mth.lerp(pull, this.z, targetZ);

        this.xd = nx - this.x;
        this.yd = ny - this.y;
        this.zd = nz - this.z;
    }

    @Override
    protected int getLightCoords(float partialTick) {
        float progress = Mth.clamp(((float) this.age + partialTick) / (float) this.lifetime, 0.0F, 1.0F);
        float emission = progress * progress;
        return LightCoordsUtil.addSmoothBlockEmission(super.getLightCoords(partialTick), emission);
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    static {
        SCALE_EASING = Easing.EASE_OUT_CUBIC.mergeOut(Easing.EASE_IN_CUBIC, 0.33F);
    }

    public static class Provider implements ParticleProvider<CrystalParticleOptions> {

        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(
                CrystalParticleOptions options, ClientLevel level,
                double x, double y, double z,
                double xSpeed, double ySpeed, double zSpeed,
                RandomSource random
        ) {
            return new CrystalParticle(level, x, y, z, options, this.spriteSet, random);
        }
    }
}