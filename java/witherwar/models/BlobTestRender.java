package witherwar.models;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelGhast;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
    
    
    
    @Override
    public void doRender(FactionDroneEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
    	if( true) {    	
            float f = (float)entity.innerRotation + partialTicks;
            float f1 = MathHelper.sin(f * 0.2F) / 2.0F + 0.5F;
            f1 = f1 * f1 + f1;
            
    		BlockPos blockpos = entity.getBeamTarget();
    		this.bindTexture(RenderDragon.ENDERCRYSTAL_BEAM_TEXTURES);
            float targetX = (float)blockpos.getX() + 0.5F;
            float targetY = (float)blockpos.getY() + 0.5F;
            float targetZ = (float)blockpos.getZ() + 0.5F;
            double d0 = (double)targetX - entity.posX;
            double d1 = (double)targetY - entity.posY;
            double d2 = (double)targetZ - entity.posZ;
//    		this.renderCrystalBeams(relX, relY, relZ, partialTicks, targetX, targetY, targetZ, ???, entityX, entityY, entityZ);
    	    renderCrystalBeams(x + d0, y - 0.3D + (double)(f1 * 0.4F) + d1, z + d2, partialTicks, targetX, targetY, targetZ, entity.innerRotation, entity.posX, entity.posY-0.5, entity.posZ);

    	}
    	
    	super.doRender(entity, x, y, z, entityYaw, partialTicks);

    }

    
//    RenderDragon.renderCrystalBeams(x + d0, y - 0.3D + (double)(f1 * 0.4F) + d1, z + d2, partialTicks, (double)f2, (double)f3, (double)f4, entity.innerRotation, entity.posX, entity.posY, entity.posZ);

    /**
     * 
     * @param relX
     * @param relY
     * @param relZ
     * @param partialTicks
     * @param targetX
     * @param targetY
     * @param targetZ
     * @param entityRotation
     * @param entityX
     * @param entityY
     * @param entityZ
     */
    public static void renderCrystalBeams(double relX, double relY, double relZ, float partialTicks, double targetX, double targetY, double targetZ, int entityRotation, double entityX, double entityY, double entityZ)
    {
        float f = (float)(entityX - targetX);
        float f1 = (float)(entityY - 1.0D - targetY);
        float f2 = (float)(entityZ - targetZ);
        float f3 = MathHelper.sqrt(f * f + f2 * f2);
        float f4 = MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)relX, (float)relY + 2.0F, (float)relZ);
        GlStateManager.rotate((float)(-Math.atan2((double)f2, (double)f)) * (180F / (float)Math.PI) - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float)(-Math.atan2((double)f3, (double)f1)) * (180F / (float)Math.PI) - 90.0F, 1.0F, 0.0F, 0.0F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        GlStateManager.shadeModel(7425);
        float f5 = 0.0F - ((float)entityRotation + partialTicks) * 0.01F;
        float f6 = MathHelper.sqrt(f * f + f1 * f1 + f2 * f2) / 32.0F - ((float)entityRotation + partialTicks) * 0.01F;
        bufferbuilder.begin(5, DefaultVertexFormats.POSITION_TEX_COLOR);

        for (int j = 0; j <= 8; ++j)
        {
            float f7 = MathHelper.sin((float)(j % 8) * ((float)Math.PI * 2F) / 8.0F) * 0.75F;
            float f8 = MathHelper.cos((float)(j % 8) * ((float)Math.PI * 2F) / 8.0F) * 0.75F;
            float f9 = (float)(j % 8) / 8.0F;
//            bufferbuilder.pos( (double)(f7*0.2F) ,(double)(f8*0.2F) ,0.0D).tex( (double)f9 ,(double)f5).color( 0 ,0 ,0 ,255).endVertex();
            bufferbuilder.pos( (double)(f7*0.2F) ,(double)(f8*0.2F) ,0.0D).tex( (double)f9 ,(double)f5).color( 200 ,200 ,255 ,200).endVertex();
            bufferbuilder.pos( (double)(f7*0.2F) ,(double)(f8*0.2F) ,(double)f4).tex( (double)f9, (double)f6).color( 200 ,200 ,255 ,200).endVertex();
//            bufferbuilder.pos( (double)(f7) ,(double)(f8) ,(double)f4).tex( (double)f9, (double)f6).color( 100 ,100 ,250 ,100).endVertex();

        }

        tessellator.draw();
        GlStateManager.enableCull();
        GlStateManager.shadeModel(7424);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    
    
//	/**
//	 * Renders a beam in the world, similar to the dragon healing beam
//	 * @param relX startX relative to the player
//	 * @param relY
//	 * @param relZ
//	 * @param centerX startX in world
//	 * @param centerY
//	 * @param centerZ
//	 * @param targetX targetX in world
//	 * @param targetY
//	 * @param targetZ
//	 * @param tickStuff used to move the beam, use the last param of {@link #renderTileEntityAt(TileEntity, double, double, double, float)} for that
//	 * @param beacon whether it should be a beacon or a dragon style beam
//	 */
//	private void renderBeam(double relX, double relY, double relZ, double centerX, double centerY, double centerZ, double targetX, double targetY, double targetZ, float tickStuff, boolean beacon) {
//		float f2 = 50000;
//		float f3 = MathHelper.sin(f2 * 0.2F) / 2.0F + 0.5F;
//		f3 = (f3 * f3 + f3) * 0.2F;
//		float wayX = (float) (targetX - centerX);
//		float wayY = (float) (targetY - centerY);
//		float wayZ = (float) (targetZ - centerZ);
//		float distFlat = MathHelper.sqrt(wayX * wayX + wayZ * wayZ);
//		float dist = MathHelper.sqrt(wayX * wayX + wayY * wayY + wayZ * wayZ);
//		GlStateManager.pushMatrix();
//		GlStateManager.translate((float) relX, (float) relY, (float) relZ);
//		GlStateManager.rotate((float) (-Math.atan2(wayZ, wayX)) * 180.0F / (float) Math.PI - 90.0F, 0.0F, 1.0F, 0.0F);
//		GlStateManager.rotate((float) (-Math.atan2(distFlat, wayY)) * 180.0F / (float) Math.PI - 90.0F, 1.0F, 0.0F, 0.0F);
//		Tessellator tessellator = Tessellator.getInstance();
//		RenderHelper.disableStandardItemLighting();
//		GlStateManager.disableCull();
//		if (beacon) {
//			this.bindTexture(beaconBeamTexture);
//		} else {
//			this.bindTexture(enderDragonCrystalBeamTextures);
//		}
//		GlStateManager.color(1.0F, 0.0F, 0.0F);
//		GL11.glShadeModel(GL11.GL_SMOOTH);
//		float f9 = -(tickStuff * 0.005F);
//		float f10 = MathHelper.sqrt(wayX * wayX + wayY * wayY + wayZ * wayZ) / 32.0F + f9;
////		tessellator.startDrawing(5);
//		tessellator.draw();
//		//Add all 2*8 vertex/corners
//		byte b0 = 8;
//		for (int i = 0; i <= b0; ++i) {
//			float f11 = 0.2F * (MathHelper.sin(i % b0 * (float) Math.PI * 2.0F / b0) * 0.75F);
//			float f12 = 0.2F * (MathHelper.cos(i % b0 * (float) Math.PI * 2.0F / b0) * 0.75F);
//			float f13 = i % b0 * 1.0F / b0;
//			tessellator.setColorOpaque(255, 0, 0);
//			tessellator.addVertexWithUV((f11), (f12), 0.0D, f13, f10);
//			if (!beacon) {
//				tessellator.setColorOpaque_I(16777215);
//			}
//
//			tessellator.addVertexWithUV(f11, f12, dist, f13, f9);
//		}
//
//		tessellator.draw();
//		GL11.glEnable(GL11.GL_CULL_FACE);
//		GL11.glShadeModel(GL11.GL_FLAT);
//		RenderHelper.enableStandardItemLighting();
//		GL11.glPopMatrix();
//	}    

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
