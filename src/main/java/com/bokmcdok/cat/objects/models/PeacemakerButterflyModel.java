package com.bokmcdok.cat.objects.models;

import com.bokmcdok.cat.CatMod;
import com.bokmcdok.cat.objects.entities.PeacemakerButterfly;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class PeacemakerButterflyModel extends HierarchicalModel<PeacemakerButterfly> {

    // This layer location should be baked with EntityRendererProvider.Context
    // in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(new ResourceLocation(CatMod.MOD_ID, "peacemaker_butterfly"), "main");

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart right_wing;
    private final ModelPart left_wing;
    private final ModelPart body;
    private final ModelPart legs;

    public PeacemakerButterflyModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.right_wing = root.getChild("right_wing");
        this.left_wing = root.getChild("left_wing");
        this.body = root.getChild("body");
        this.legs = root.getChild("legs");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-2.0F, -5.0F, -6.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(1.0F, 23.0F, -1.0F));

        PartDefinition antannae = head.addOrReplaceChild("antennae", CubeListBuilder.create()
                .texOffs(6, 2).addBox(-2.0F, -7.0F, -9.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 2).addBox(0.0F, -7.0F, -9.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_wing = partdefinition.addOrReplaceChild("right_wing", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-12.0F, -5.0F, -6.0F, 10.0F, 0.0F, 17.0F, new CubeDeformation(0.0F)),
                PartPose.offset(1.0F, 23.0F, 0.0F));

        PartDefinition left_wing = partdefinition.addOrReplaceChild("left_wing", CubeListBuilder.create()
                .texOffs(0, 17).addBox(0.0F, -5.0F, -6.0F, 10.0F, 0.0F, 17.0F, new CubeDeformation(0.0F)),
                PartPose.offset(1.0F, 23.0F, 0.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 24).addBox(-2.0F, -5.0F, -3.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)),
                PartPose.offset(1.0F, 23.0F, 0.0F));

        PartDefinition legs = partdefinition.addOrReplaceChild("legs", CubeListBuilder.create()
                .texOffs(0, 4).addBox(1.0F, -4.0F, -2.0F, 0.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.0F, -4.0F, -2.0F, 0.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(@NotNull PeacemakerButterfly entity,
                          float limbSwing,
                          float limbSwingAmount,
                          float ageInTicks,
                          float netHeadYaw,
                          float headPitch) {

    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack,
                               @NotNull VertexConsumer vertexConsumer,
                               int packedLight,
                               int packedOverlay,
                               float red,
                               float green,
                               float blue,
                               float alpha) {
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        right_wing.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        left_wing.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        legs.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public @NotNull ModelPart root() {
        return this.root;
    }
}
