//package witherwar.models;
//
//import net.minecraft.client.model.ModelBase;
//import net.minecraft.client.model.ModelRenderer;
//import net.minecraft.entity.Entity;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//@SideOnly(Side.CLIENT)
//
//public class BlobModel extends ModelBase{
//
//	public ModelRenderer body;
//	public ModelRenderer fin;
//	
//	public final int textureWidth = 64;
//	public final int textureHeight = 32;
//	
//	private double distanceMovedTotal = 0.0D;
//	
//	private static final double CYCLES_PER_BLOCK = 3.0D;
//	private int cycleIndex = 0;
//	
//	
//	
//	public BlobModel() {
//		
//		this.body = new ModelRenderer( this ,0 ,0);
////		this.body.addBox(offX, offY, offZ, width, height, depth);
//		this.body.addBox( -2.5F ,-1F ,-5F ,5 ,2 ,5);
////		this.body.setRotationPoint(rotationPointXIn, rotationPointYIn, rotationPointZIn);
//		this.body.setRotationPoint( 0F ,23F ,-8F);
//		this.body.setTextureSize( this.textureWidth ,this.textureHeight);
//		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
//		
//	}
//	
//	
//	
//	@Override
//	public void render( Entity e ,float parTime ,float parSwingSuppress ,float par4 
//			,float parHeadAngleY ,float parHeadAngleX ,float par7) {
//		
//		//TODO
//		
//	}
//	
//	
//	@Override
//    public void setRotationAngles(float parTime, float parSwingSuppress, float par3, 
//    		float parHeadAngleY, float parHeadAngleX, float par6, Entity parEntity) {
//		
//		//TODO
//		
//		
//	}
//	
//	
//	
//    protected float degToRad(float degrees)
//    {
//        return degrees * (float)Math.PI / 180 ;
//    }
//	
//    protected void spinX(ModelRenderer model)
//    {
//        model.rotateAngleX += degToRad(0.5F);
//    }
//    
//    protected void spinY(ModelRenderer model)
//    {
//        model.rotateAngleY += degToRad(0.5F);
//    }
//    
//    protected void spinZ(ModelRenderer model)
//    {
//        model.rotateAngleZ += degToRad(0.5F);
//    }
//	
//	
//	
//}
