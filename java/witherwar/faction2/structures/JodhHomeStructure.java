package witherwar.faction2.structures;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import witherwar.utility.Pair;
import witherwar.utility.noise.MidpointNoiseMap;
import witherwar.utility.noise.NoiseMap;
import witherwar.utility.noise.SimplexNoiseMap;

public class JodhHomeStructure extends Structure{

	private BlockPos start;
	private BlockPos index;
	private int bound = 1;
	private NoiseMap patternmap;
	private NoiseMap heightmap;
	
	private Shape boundary;
	private Pattern pattern;
	
	private int size;
	
	private final int P_BOUND = 4; //4,8,16,32
	
	
	public enum Shape{
		RING
		,TOROID
	}
	
	
	public enum Pattern{
		CROSS
	}

	
	
	
	public JodhHomeStructure( BlockPos pos ,Shape s ,Pattern p ,int size) {
		this.start = pos;
		this.size = size;
		this.patternmap = new SimplexNoiseMap( 8 ,0.06 ,75 ,true);
		this.heightmap = new SimplexNoiseMap( 8 ,0.02 ,75 ,true);
		this.heightmap.lowerValues( 0.8f);
		this.heightmap.spikeRandomValues(8 ,this.size*2);
		this.boundary = s;
		this.pattern = p;
	}
	
	
	
	public boolean validBlock( BlockPos pos) {
		return validPatternBlock( pos) && validBoundaryBlock( pos);
	}
	
	

	public boolean validBoundaryBlock( BlockPos pos) {
		
		int y = 0;
		
		switch( this.boundary) {
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
		
		double mapValue = this.getNoiseMapValue( pos.getX() ,pos.getZ() ,false);
		
		if( mapValue < 0.4) return 4;
		if( mapValue < 0.8) return 8;
		
//		return (int) (Math.round(mapValue * 10) + 2);
		return 12;
	}
	
	
	
	
	
	public double[][] getPMap(){
		return this.patternmap.getMap();
	}
	
	public double[][] getHMap(){
		return this.heightmap.getMap();
	}
	
	
	
	private boolean withinHeightMap( BlockPos pos) {
		double mapValue = this.getNoiseMapValue( pos.getX() ,pos.getZ() ,true);
		//1.0 = y 200?
		
		return pos.getY() < (mapValue * 100); //TODO: set max height via constructor
	}
	
	
	
	private double getNoiseMapValue( int x ,int z ,boolean height) {
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
	
	
	
	public static int ring( int size) {
		int r = size;
		return (int) Math.pow(r, 2.0);
	}
	public boolean withinRing( BlockPos pos) {
    	double xz = Math.pow( pos.getX() ,2.0) + Math.pow( pos.getZ() ,2.0);
		
		return this.withinHeightMap( pos) && xz < ring( this.size);
	}



	@Override
	public ArrayList<Pair<BlockPos, Block>> getNextSegment() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	

	
	
	
	
}
