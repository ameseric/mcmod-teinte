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
	
	
	
	//using toroid shape
	public static boolean validShapeBlock( BlockPos pos) {
		int a = 30; //radius of tube
		int c = 40; //dist from center to tube center
		
		double xzsq = Math.pow( pos.getX() ,2.0) + Math.pow( pos.getZ() ,2.0); 
		
		double ysq = Math.pow( a ,2.0) - Math.pow(  c - Math.sqrt(xzsq), 2.0);		
		
		int y = (int) Math.round( Math.sqrt( ysq));
		
		
		return pos.getY() <= y; 
	}
	
	
	
	public static boolean validBlock( BlockPos pos) {
		return validPatternBlock( pos) && validShapeBlock( pos);
	}
	
	
	
	public void thing() {
		
		
		
	}
	
	
	
    private boolean blockInBoundary( int x ,int z) {
    	int r = this.bound;
    	
    	int ax = Math.abs(x);
    	int az = Math.abs(z);
    	double axz = Math.pow( ax ,2.0) + Math.pow( az ,2.0);
    	
    	return axz <= Math.pow( r ,2.0)  ;
    }
	
	
	
}
