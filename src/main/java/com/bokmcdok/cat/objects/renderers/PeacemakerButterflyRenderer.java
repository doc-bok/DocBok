package com.bokmcdok.cat.objects.renderers;

import com.bokmcdok.cat.objects.entities.living.PeacemakerButterfly;
import com.bokmcdok.cat.objects.models.PeacemakerButterflyModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * The renderer for the butterfly
 */
public class PeacemakerButterflyRenderer extends MobRenderer<PeacemakerButterfly, PeacemakerButterflyModel> {
    //  The texture locations
    public static ResourceLocation TEXTURE =
            new ResourceLocation("cat:textures/entity/peacemaker_butterfly/peacemaker_butterfly.png");

    /**
     * Bakes a new model for the renderer
     * @param context The current rendering context
     */
    public PeacemakerButterflyRenderer(EntityRendererProvider.Context context) {
        super(context, new PeacemakerButterflyModel(context.bakeLayer(PeacemakerButterflyModel.LAYER_LOCATION)), 0.2F);
    }

    /**
     * Gets the texture to use
     * @param entity The butterfly entity
     * @return The texture to use for this entity
     */
    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull PeacemakerButterfly entity) {
        return TEXTURE;
    }
}
