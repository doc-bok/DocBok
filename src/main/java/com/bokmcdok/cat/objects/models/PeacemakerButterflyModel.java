package com.bokmcdok.cat.objects.models;

import com.bokmcdok.cat.CatMod;
import com.bokmcdok.cat.objects.entities.PeacemakerButterfly;
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
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

/**
 * The model for a Peacemaker butterfly.
 */
public class PeacemakerButterflyModel extends HierarchicalModel<PeacemakerButterfly> {

    //  The layers used for the model
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(new ResourceLocation(CatMod.MOD_ID, "peacemaker_butterfly"), "main");

    //  The parts of the model
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart right_wing;
    private final ModelPart left_wing;

    /**
     * Create the model for the peacemaker butterfly
     * @return The complete model
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(0, 24).addBox(-2.0F, -5.0F, -3.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)),
                PartPose.offset(1.0F, 23.0F, 0.0F));

        body.addOrReplaceChild("left_wing", CubeListBuilder.create()
                        .texOffs(0, 17).addBox(0.0F, 0.0F, -6.0F, 10.0F, 0.0F, 17.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -5.0F, 0.0F));

        body.addOrReplaceChild("right_wing", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-12.0F, 0.0F, -6.0F, 10.0F, 0.0F, 17.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -5.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-1.0F, -0.5F, -4.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-1.0F, -4.5F, -3.0F));

        head.addOrReplaceChild("antennae", CubeListBuilder.create()
                        .texOffs(6, 2).addBox(-2.0F, -7.0F, -9.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 2).addBox(0.0F, -7.0F, -9.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offset(1.0F, 4.5F, 2.0F));

        partdefinition.addOrReplaceChild("legs", CubeListBuilder.create()
                        .texOffs(0, 4).addBox(1.0F, -4.0F, -2.0F, 0.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 0).addBox(-1.0F, -4.0F, -2.0F, 0.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    /**
     * Create a model
     * @param root The root to attach the model to
     */
    public PeacemakerButterflyModel(ModelPart root) {
        this.root = root;
        ModelPart body = root.getChild("body");
        this.head = body.getChild("head");
        this.right_wing = body.getChild("right_wing");
        this.left_wing = body.getChild("left_wing");
    }

    /**
     * Get the root of the model
     * @return The root to render
     */
    @Override
    public @NotNull ModelPart root() {
        return this.root;
    }

    /**
     * Animate the model.
     * @param entity The butterfly entity
     * @param limbSwing Unused
     * @param limbSwingAmount Unused
     * @param ageInTicks The current age of the entity in ticks
     * @param netHeadYaw unused
     * @param headPitch unused
     */
    @Override
    public void setupAnim(@NotNull PeacemakerButterfly entity,
                          float limbSwing,
                          float limbSwingAmount,
                          float ageInTicks,
                          float netHeadYaw,
                          float headPitch) {
        //  Head
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);

        //  Wings
        this.right_wing.zRot = Mth.sin(ageInTicks * 0.85F) * Mth.PI * 0.25F;
        this.left_wing.zRot = -right_wing.zRot;

    }
}
