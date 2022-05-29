package com.bokmcdok.cat.objects.renderers;

import com.bokmcdok.cat.objects.entities.living.Butterfly;
import com.bokmcdok.cat.objects.models.ButterflyModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * The renderer for the butterfly
 */
public class ButterflyRenderer extends MobRenderer<Butterfly, ButterflyModel> {
    //  The texture locations
    public static ResourceLocation[] TEXTURE = {
            new ResourceLocation("cat:textures/entity/butterfly/butterfly_blue.png"),
            new ResourceLocation("cat:textures/entity/butterfly/butterfly_nyan.png"),
            new ResourceLocation("cat:textures/entity/butterfly/butterfly_purple.png"),
            new ResourceLocation("cat:textures/entity/butterfly/butterfly_purple_trim.png"),
            new ResourceLocation("cat:textures/entity/butterfly/butterfly_rainbow.png"),
            new ResourceLocation("cat:textures/entity/butterfly/butterfly_red.png"),
            new ResourceLocation("cat:textures/entity/butterfly/butterfly_seethru.png"),
            new ResourceLocation("cat:textures/entity/butterfly/butterfly_sword.png"),
            new ResourceLocation("cat:textures/entity/butterfly/butterfly_white.png"),
    };

    /**
     * Bakes a new model for the renderer
     * @param context The current rendering context
     */
    public ButterflyRenderer(EntityRendererProvider.Context context) {
        super(context, new ButterflyModel(context.bakeLayer(ButterflyModel.LAYER_LOCATION)), 0.2F);
    }

    /**
     * Gets the texture to use
     * @param entity The butterfly entity
     * @return The texture to use for this entity
     */
    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Butterfly entity) {
        return TEXTURE[entity.getVariant()];
    }

    /**
     * Scale the entity down
     * @param entity The butterfly entity
     * @param poses The current entity pose
     * @param scale The scale that should be applied
     */
    @Override
    protected void scale(@NotNull Butterfly entity, PoseStack poses, float scale) {
        poses.scale(0.35F, 0.35F, 0.35F);
    }
}
