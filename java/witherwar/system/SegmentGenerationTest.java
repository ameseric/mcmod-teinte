package witherwar.system;

import net.minecraft.util.math.BlockPos;

public class SegmentGenerationTest {

	private BlockPos start;
	private BlockPos index;
	private int bound = 1;
	
	
	
	
	public SegmentGenerationTest( BlockPos pos) {
		this.start = pos;
	}
	
	
	//using cross pattern
	public static boolean validPatternBlock( BlockPos pos) {
		int x = pos.getX() % 8;
		int y = pos.getY() % 8;
		int z = pos.getZ() % 8;
		if( (( x == 7 || x == 0) && (z == 7 || z == 0))  ||  (y == 0 || y == 7)) {
			return true;
		}
		return false;
	}
	
	
	
	public static int toroid( int x ,int z) {
		int a = 30;
		int c = 40;
		
		double xzsq = Math.pow( x ,2.0) + Math.pow( z ,2.0); 
		double ysq = Math.pow( a ,2.0) - Math.pow(  c - Math.sqrt(xzsq), 2.0);		
		return (int) Math.round( Math.sqrt( ysq));		
	}
	
	
	

	public static boolean validShapeBlock( BlockPos pos) {
		int y = SegmentGenerationTest.toroid( pos.getX() ,pos.getZ());		
		return pos.getY() <= y; 
	}
	
	
	
	public static boolean validBlock( BlockPos pos) {
		return validPatternBlock( pos) && validShapeBlock( pos);
	}
	
	
	
	
	
    private boolean blockInBoundary( int x ,int z) {
    	int r = this.bound;
    	
    	int ax = Math.abs(x);
    	int az = Math.abs(z);
    	double axz = Math.pow( ax ,2.0) + Math.pow( az ,2.0);
    	
    	return axz <= Math.pow( r ,2.0);
    }
	
	
	
}
