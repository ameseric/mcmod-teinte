package witherwar.particle;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MuirParticle extends ParticleCustom{
	
	
	
	

	public MuirParticle(TextureDefinition parTexDef, World parWorld, double parX, double parY, double parZ ,Vec3d color) {
		super(parTexDef, parWorld, parX, parY, parZ ,0 ,0.01 ,0);
//		this.setFinalAlpha(0f);
		this.setLifeSpan( (int)(40.0D / (Math.random() * 0.8D + 0.2D)));
		this.setScale( 0.25f);
		
		float red = (float) (1.0f + color.x);
		float green = (float) (1.0f + color.y);
		float blue = (float) (1.0f + color.z);
		
		this.setTintColor( red ,green ,blue);
	}
	
	
	
	
	
	
	

}
