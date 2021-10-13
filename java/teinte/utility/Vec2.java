package teinte.utility;

import net.minecraft.util.math.BlockPos;
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
	
	public double distance( BlockPos pos) {
		return this.distance( new Vec2( pos.getX() ,pos.getZ()));
	}
	
	
	public static double distance( int x1 ,int y1 ,int x2 ,int y2) {
		int dx = x1 - x2;
		int dy = y1 - y2;
		
		return Math.sqrt( Math.pow( dx ,2) + Math.pow( dy ,2));
	}
	

	
	
//	public static double distance( Vec2 v ,double x ,double y) {
//		
//	}

}
