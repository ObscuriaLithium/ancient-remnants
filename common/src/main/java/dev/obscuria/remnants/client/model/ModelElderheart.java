package dev.obscuria.remnants.client.model;

import dev.obscuria.remnants.AncientRemnants;
import dev.obscuria.remnants.client.renderer.ElderheartRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;

import java.util.List;

public class ModelElderheart extends EntityModel<ElderheartRenderState> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(AncientRemnants.id("elderheart"), "main");

    private final ModelPart main;
    private final ModelPart shard_1, shard_1_1, shard_1_2;
    private final ModelPart shard_2, shard_2_1, shard_2_2;
    private final ModelPart shard_3, shard_3_1, shard_3_2;
    private final ModelPart shard_4, shard_4_1, shard_4_2;
    private final ModelPart shard_5, shard_5_1, shard_5_2;
    private final ModelPart shard_6, shard_6_1, shard_6_2;

    private final List<ModelPart> shards;

    public ModelElderheart(ModelPart root) {
        super(root, RenderTypes::eyes);
        this.main = root.getChild("main");
        this.shard_1 = this.main.getChild("shard_1");
        this.shard_1_1 = this.shard_1.getChild("shard_1_1");
        this.shard_1_2 = this.shard_1_1.getChild("shard_1_2");
        this.shard_2 = this.main.getChild("shard_2");
        this.shard_2_1 = this.shard_2.getChild("shard_2_1");
        this.shard_2_2 = this.shard_2_1.getChild("shard_2_2");
        this.shard_3 = this.main.getChild("shard_3");
        this.shard_3_1 = this.shard_3.getChild("shard_3_1");
        this.shard_3_2 = this.shard_3_1.getChild("shard_3_2");
        this.shard_4 = this.main.getChild("shard_4");
        this.shard_4_1 = this.shard_4.getChild("shard_4_1");
        this.shard_4_2 = this.shard_4_1.getChild("shard_4_2");
        this.shard_5 = this.main.getChild("shard_5");
        this.shard_5_1 = this.shard_5.getChild("shard_5_1");
        this.shard_5_2 = this.shard_5_1.getChild("shard_5_2");
        this.shard_6 = this.main.getChild("shard_6");
        this.shard_6_1 = this.shard_6.getChild("shard_6_1");
        this.shard_6_2 = this.shard_6_1.getChild("shard_6_2");
        this.shards = List.of(shard_1_1, shard_2_1, shard_3_1, shard_4_1, shard_5_1, shard_6_1);
    }

    public static LayerDefinition createBodyLayer() {
        var meshDefinition = new MeshDefinition();
        var partDefinition = meshDefinition.getRoot();

        var main = partDefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        var shard_1 = main.addOrReplaceChild("shard_1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        var shard_1_1 = shard_1.addOrReplaceChild("shard_1_1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        shard_1_1.addOrReplaceChild("shard_1_2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.6295F, -0.2112F, -0.6295F));
        var shard_2 = main.addOrReplaceChild("shard_2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));
        var shard_2_1 = shard_2.addOrReplaceChild("shard_2_1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        shard_2_1.addOrReplaceChild("shard_2_2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.6295F, -0.2112F, -0.6295F));
        var shard_3 = main.addOrReplaceChild("shard_3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));
        var shard_3_1 = shard_3.addOrReplaceChild("shard_3_1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        shard_3_1.addOrReplaceChild("shard_3_2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.6295F, -0.2112F, -0.6295F));
        var shard_4 = main.addOrReplaceChild("shard_4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));
        var shard_4_1 = shard_4.addOrReplaceChild("shard_4_1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        shard_4_1.addOrReplaceChild("shard_4_2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.6295F, -0.2112F, -0.6295F));
        var shard_5 = main.addOrReplaceChild("shard_5", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 1.5708F));
        var shard_5_1 = shard_5.addOrReplaceChild("shard_5_1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        shard_5_1.addOrReplaceChild("shard_5_2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.6295F, -0.2112F, -0.6295F));
        var shard_6 = main.addOrReplaceChild("shard_6", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 1.5708F));
        var shard_6_1 = shard_6.addOrReplaceChild("shard_6_1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        shard_6_1.addOrReplaceChild("shard_6_2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.6295F, -0.2112F, -0.6295F));

        return LayerDefinition.create(meshDefinition, 32, 32);
    }

    @Override
    public void setupAnim(ElderheartRenderState state) {
        super.setupAnim(state);

        main.xRot = 16f * Mth.cos(state.ageInTicks * 0.01f);
        main.yRot = 8f * Mth.cos(state.ageInTicks * 0.02f);
        main.zRot = 4f * Mth.cos(state.ageInTicks * 0.03f);

        for (ModelPart shard : shards) {
            var animationStage = Mth.sin(state.ageInTicks * 0.1f) * Mth.cos(state.ageInTicks * 0.01f);
            shard.yScale = 1.4f + 0.8f * animationStage;
            shard.y = -16f + 12f * animationStage;
        }
    }
}