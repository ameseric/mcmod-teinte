package witherwar.utility;

import net.minecraft.util.math.Vec2f;

public class Vec2 extends Vec2f{

	public Vec2(float xIn, float yIn) {
		super(xIn, yIn);
	}
	
	
	public Vec2 subtract( Vec2 v) {
		return new Vec2( this.x - v.x ,this.y-v.y);
	}
	
	public double distance( Vec2 v) {
		Vec2 a = this.subtract( v);
		return Math.sqrt( Math.pow( a.x ,2) + Math.pow( a.y ,2));
	}
	
//	public static double distance( Vec2 v ,double x ,double y) {
//		
//	}

}
