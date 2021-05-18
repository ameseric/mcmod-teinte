package witherwar.models;
import net.minecraft.client.model.ModelGhast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import witherwar.entity.FactionDroneEntity;
import witherwar.entity.EntityMotusGhast;


public class BlobTestRender extends RenderLiving<FactionDroneEntity>{
//    private static final ResourceLocation GHAST_TEXTURES = new ResourceLocation("textures/entity/ghast/ghast.png");
    private static final ResourceLocation GHAST_TEXTURES = new ResourceLocation("witherwar:textures/entity/test_blob/ghast_yellow.png");
    private static final ResourceLocation GHAST_SHOOTING_TEXTURES = new ResourceLocation("textures/entity/ghast/ghast_shooting.png");

    public BlobTestRender(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new BlobModel(), 0.5F);
    }


    protected ResourceLocation getEntityTexture(FactionDroneEntity entity)
    {
        return GHAST_TEXTURES;
    }

    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
//    protected void preRenderCallback(BlobFlyingTestEntity entitylivingbaseIn, float partialTickTime)
//    {
//        float f = 1.0F;
//        float f1 = 4.5F;
//        float f2 = 4.5F;
//        GlStateManager.scale(4.5F, 4.5F, 4.5F);
//        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//    }



}
