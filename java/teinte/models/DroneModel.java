package teinte.models;


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class DroneModel extends ModelBase{

	public ModelRenderer body;
	public ModelRenderer fin;
	
	
	private double distanceMovedTotal = 0.0D;
	
	private boolean rotatingPositive = true;
	
	private static final double CYCLES_PER_BLOCK = 3.0D;
	private int cycleIndex = 0;
	
	private float[][] animationCycle = new float[][] {
		 {0 ,0}
		,{0 ,45F}
		,{0 ,45F}
		,{0 ,-45F}
	};
	
	
	
	public DroneModel() {
		
		this.body = new ModelRenderer( this ,0 ,0);
        this.body.addBox(-8 ,-8 ,-8 ,16, 16, 16);
		this.body.setRotationPoint( 0F ,16F ,0F);
//        this.body.rotationPointY += 8.0F;
		this.setRotation( this.body ,0F ,0F ,0F);


		this.fin = new ModelRenderer( this ,0 ,0);
		this.fin.addBox(-1 ,-16 ,0 ,2, 16, 2);
		this.fin.setRotationPoint( 0 ,-8 ,0);
		this.body.addChild( this.fin);
		this.setRotation( this.fin ,0F ,0F ,0F);
		
	}
	
	
	
	@Override
    public void render(Entity e, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw 
    		,float headPitch, float scale) {

		this.renderBlob( e ,limbSwing ,limbSwingAmount ,ageInTicks ,netHeadYaw ,headPitch ,scale);
		
	}
	
	
	
	private void renderBlob(Entity e, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw 
			,float headPitch, float scale) {
		
		this.setRotationAngles( limbSwing ,limbSwingAmount ,ageInTicks ,netHeadYaw ,headPitch ,scale ,e);
		
        GlStateManager.pushMatrix();
//        float sf = 1F; //e.getScaleFactor();
//        GL11.glScalef( sf ,sf ,sf);
        

        
        
		this.body.render( scale); //TODO why is there a scale value, and we also need a scale factor
//		this.fin.render( scale);
		
        GlStateManager.popMatrix();
	}
	
	
	
	@Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw 
    		,float headPitch, float scaleFactor, Entity entityIn) {
		
		

		
		if(this.fin.rotateAngleX >= -3*(Math.PI/8) && this.rotatingPositive) {
			this.fin.rotateAngleX -= 0.001F;
		}else {
			this.rotatingPositive = false;
		}
		
		if(this.fin.rotateAngleX <= -(Math.PI/8) && !this.rotatingPositive) {
			this.fin.rotateAngleX += 0.001F;
		}else {
			this.rotatingPositive = true;
		}
		

//		this.spinY( this.body);
//		this.spinZ( this.body);
		
		
//		this.spinY( this.body);
		
		
	}
	
	
    private void setRotation(ModelRenderer model, float rotX, float rotY, float rotZ){
        model.rotateAngleX = degToRad(rotX);
        model.rotateAngleY = degToRad(rotY);
        model.rotateAngleZ = degToRad(rotZ);        
    }	
	
	
    protected float degToRad(float degrees){
        return degrees * (float)Math.PI / 180;
    }
	
    protected void spinX(ModelRenderer model){
        model.rotateAngleX += degToRad(0.5F);
    }
    
    protected void spinY(ModelRenderer model){
        model.rotateAngleY += degToRad(0.5F);
    }
    
    protected void spinZ(ModelRenderer model){
        model.rotateAngleZ += degToRad(0.5F);
    }
	
	
	
}
