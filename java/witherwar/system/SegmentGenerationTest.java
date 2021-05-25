package witherwar.system;

import net.minecraft.util.math.BlockPos;
import witherwar.utility.MidpointNoiseMap;

public class SegmentGenerationTest {

	private BlockPos start;
	private BlockPos index;
	private int bound = 1;
	private MidpointNoiseMap noisemap;
	
	private final int P_BOUND = 4; //4,8,16,32
	
	
	
	
	public SegmentGenerationTest( BlockPos pos) {
		this.start = pos;
		this.noisemap = new MidpointNoiseMap( 10 ,0.6f);
	}
	
	
	
	//using cross pattern
	public boolean validPatternBlock( BlockPos pos) {
		int mod = this.getMod( pos);
		
		return emptyBoxPattern( pos ,mod);
	}
	
	
	
	private static boolean emptyBoxPattern( BlockPos pos ,int mod) {
		int x = pos.getX() % mod;
		int y = pos.getY() % mod;
		int z = pos.getZ() % mod;
		
		int ubound = mod - 1;
		
		if( (( x == ubound || x == 0) && (z == ubound || z == 0))  ||  (y == 0 || y == ubound)) {
			return true;
		}		
		return false;
	}
	
	
	
	private int getMod( BlockPos pos) {
		
		int x = Math.abs(pos.getX()) >> 2;
		int z = Math.abs( pos.getZ()) >> 2;
		int mid = this.noisemap.getHalfwidth();
		
		
		float mapValue = this.noisemap.getMap()[x+mid][z+mid];
		
		if( mapValue < 0.35) return 4;
		if( mapValue < 0.55) return 8;
		if( mapValue < 0.8) return 16;
		
		return 16;
	}
	
	
	
	public static int toroid( int x ,int z) {
		int a = 30;
		int c = 40;
		
		double xzsq = Math.pow( x ,2.0) + Math.pow( z ,2.0); 
		double ysq = Math.pow( a ,2.0) - Math.pow(  c - Math.sqrt(xzsq), 2.0);		
		return (int) Math.round( Math.sqrt( ysq));		
	}
	
	
	

	public boolean validShapeBlock( BlockPos pos) {
		int y = SegmentGenerationTest.toroid( pos.getX() ,pos.getZ());
		return pos.getY() < y; 
	}
	
	
	
	public boolean validBlock( BlockPos pos) {
		return validPatternBlock( pos) && validShapeBlock( pos);
	}
	
	
	public float[][] getMap(){
		return this.noisemap.getMap();
	}
	
	
	
    private boolean blockInBoundary( int x ,int z) {
    	int r = this.bound;
    	
    	int ax = Math.abs(x);
    	int az = Math.abs(z);
    	double axz = Math.pow( ax ,2.0) + Math.pow( az ,2.0);
    	
    	return axz <= Math.pow( r ,2.0);
    }
	
	
	
}
