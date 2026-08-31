package dev.obscuria.remnants.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.obscuria.remnants.AncientRemnants;
import dev.obscuria.remnants.client.model.ModelElderheart;
import dev.obscuria.remnants.common.entity.Elderheart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class ElderheartRenderer extends EntityRenderer<Elderheart, ElderheartRenderState> {

    private static final Identifier TEXTURE = AncientRemnants.id("textures/entity/elderheart.png");
    private final ModelElderheart model;

    public ElderheartRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ModelElderheart(context.bakeLayer(ModelElderheart.LAYER));
    }

    @Override
    public ElderheartRenderState createRenderState() {
        return new ElderheartRenderState();
    }

    @Override
    public void extractRenderState(Elderheart entity, ElderheartRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
    }

    @Override
    public void submit(ElderheartRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        super.submit(state, poseStack, collector, camera);
        this.model.setupAnim(state);
        poseStack.pushPose();
        poseStack.translate(0, state.boundingBoxHeight * 0.5f, 0);
        poseStack.scale(1.5f, 1.5f, 1.5f);
        poseStack.pushPose();
        collector.submitModel(model, state, poseStack,
                RenderTypes.entityCutoutCull(TEXTURE), state.lightCoords,
                OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();
        collector.submitModel(model, state, poseStack,
                model.renderType(TEXTURE), state.lightCoords,
                OverlayTexture.NO_OVERLAY, -1, null);
        poseStack.popPose();
    }
}