package witherwar.system;

import net.minecraft.util.math.BlockPos;
import witherwar.utility.MidpointNoiseMap;

public class SegmentGenerationTest {

	private BlockPos start;
	private BlockPos index;
	private int bound = 1;
	private MidpointNoiseMap patternmap;
	private MidpointNoiseMap heightmap;
	
	private Shape shape;
	private Pattern pattern;
	
	private final int P_BOUND = 4; //4,8,16,32
	
	
	public enum Shape{
		RING
		,TOROID
	}
	
	
	public enum Pattern{
		CROSS
	}

	
	
	
	public SegmentGenerationTest( BlockPos pos ,Shape s ,Pattern p) {
		this.start = pos;
		this.patternmap = new MidpointNoiseMap( 10 ,0.4f);
		this.heightmap = new MidpointNoiseMap( 10 ,0.4f);
		this.heightmap.pullValuesAwayFromMean( 0.25f ,0.75f ,2);
		this.shape = s;
		this.pattern = p;
	}
	
	
	
	public boolean validBlock( BlockPos pos) {
		return validPatternBlock( pos) && validBoundaryBlock( pos);
	}
	
	

	public boolean validBoundaryBlock( BlockPos pos) {
		
		int y = 0;
		
		switch( this.shape) {
		case TOROID: 
			y = toroid( pos.getX() ,pos.getZ());
			return pos.getY() < y;
		case RING:
			return this.withinRing( pos);
		}

		
		return false; 
	}
		
	
	
	//using cross pattern
	public boolean validPatternBlock( BlockPos pos) {
		int mod = this.getMod( pos);
		
		return emptyBoxPattern( pos ,mod);
	}
	
		
	
	private int getMod( BlockPos pos) {
		
		float mapValue = this.getNoiseMapValue( pos.getX() ,pos.getZ() ,false);
		
//		if( mapValue < 0.4) return 4;
//		if( mapValue < 0.8) return 8;
		
		return Math.round(mapValue * 10) + 2;
	}
	
	
	
	
	
	public float[][] getMap(){
		return this.patternmap.getMap();
	}
	
	
	
	private boolean withinHeightMap( BlockPos pos) {
		float mapValue = this.getNoiseMapValue( pos.getX() ,pos.getZ() ,true);
		//1.0 = y 200?
		
		return pos.getY() < (mapValue * 100); //TODO: set max height via constructor
	}
	
	
	
	private float getNoiseMapValue( int x ,int z ,boolean height) {
		int correction = this.patternmap.getHalfwidth() << 2;
		int xtx = correction + x >> 2;
		int ztx = correction + z >> 2;
		
		if( height) {
			return this.heightmap.getMap()[xtx][ztx];
		}
		return this.patternmap.getMap()[xtx][ztx];
	}
	
	
	
	//============== Patterns ==========//
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
	
	
	
	//=============== Shapes ============//	
	public static int toroid( int x ,int z) {
		int a = 30;
		int c = 40;
		
		double xzsq = Math.pow( x ,2.0) + Math.pow( z ,2.0); 
		double ysq = Math.pow( a ,2.0) - Math.pow(  c - Math.sqrt(xzsq), 2.0);		
		return (int) Math.round( Math.sqrt( ysq));		
	}
	public boolean withinToroid( BlockPos pos) {
		int y = toroid( pos.getX() ,pos.getZ());
		return pos.getY() < y;
	}
	
	
	
	public static int ring() {
		int r = 40;
		return (int) Math.pow(r, 2.0);
	}
	public boolean withinRing( BlockPos pos) {
    	double xz = Math.pow( pos.getX() ,2.0) + Math.pow( pos.getZ() ,2.0);
		
		return this.withinHeightMap( pos) && xz < ring();
	}
	
	
	
	

	
	
	
	
}
