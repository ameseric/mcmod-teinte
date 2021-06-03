package witherwar.faction2.structures;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import witherwar.utility.Pair;
import witherwar.utility.noise.MidpointNoiseMap2D;
import witherwar.utility.noise.NoiseMap2D;
import witherwar.utility.noise.NoiseMap3D;
import witherwar.utility.noise.SimplexNoiseMap2D;
import witherwar.utility.noise.SimplexNoiseMap3D;

public class JodhHome extends Home{

	private NoiseMap3D patternmap;
	private NoiseMap2D heightmap;
	private NoiseMap2D buildmap;
	
	private Shape boundary;
	private Pattern pattern;
	
	private int size;
	
	
	public enum Shape{
		RING
		,TOROID
	}
	
	
	public enum Pattern{
		CROSS
	}

	
	
	
	public JodhHome( BlockPos pos ,Shape s ,Pattern p ,int size) {
		super( pos);
		this.size = size;
		this.buildmap = new SimplexNoiseMap2D( 6 ,0.02 ,25 ,true);
		
		//expensive, consider zooming out and mapping 4:1 block ratio again 
		//Done, much better performance
		this.patternmap = new SimplexNoiseMap3D( 6 ,6 ,75 ,0.06); 
		this.heightmap = new SimplexNoiseMap2D( 8 ,0.04 ,75 ,true);
		this.heightmap.lowerValues( 0.9f);
		this.heightmap.spikeRandomValues( 8 ,this.size*2 ,0.8 ,18);
		this.boundary = s;
		this.pattern = p;
	}
	
	
	
	public boolean isValidPosition( BlockPos pos) {
		return validPatternBlock( pos) && (validBoundaryBlock( pos));// && buildArea( pos);
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
	
	
	
	public boolean buildArea( BlockPos pos) {
		double value = this.getBuildMapValue( pos.getX() ,pos.getZ());
		return value < 0.9;
	}
		
	
	
	//using cross pattern
	public boolean validPatternBlock( BlockPos pos) {
		double mapValue = this.getPatternMapValue( pos.getX() ,pos.getY() ,pos.getZ());
		
//		if( mapValue < 0.75 && mapValue > 0.7) {
//			return pos.getY() < spike( pos.getX() ,pos.getZ());
//		}
		
		int mod = this.getMod( pos);		
		return emptyBoxPattern( pos ,mod);
	}
	
		
	
	private int getMod( BlockPos pos) {
		
		double mapValue = this.getPatternMapValue( pos.getX() ,pos.getY() ,pos.getZ());
		
		if( mapValue < 0.3) return 4;
		if( mapValue < 0.7) return 6;
		
//		return (int) (Math.round(mapValue * 10) + 2);
		return 8;
	}
	
	
	
	
	
//	public double[][] getPMap(){
//		return this.patternmap.getMap();
//	}
	
	public double[][] getHMap(){
		return this.heightmap.getMap();
	}
	
	public double[][] getBMap(){
		return this.buildmap.getMap();
	}
	
	
	
	private boolean withinHeightMap( BlockPos pos) {
		double mapValue = this.getHeightMapValue( pos.getX() ,pos.getZ());
		//1.0 = y 200?
		
		return pos.getY() < (mapValue * 100); //TODO: set max height via constructor
	}
	
	
	
	private double getPatternMapValue( int x ,int y ,int z) {
		
//		int xtx = correction + x >> 2;
//		int ztx = correction + z >> 2;
		
//		int xtx = this.patternmap.getHalfwidth() + x;
//		int ztx = this.patternmap.getHalfwidth() + z;
		return this.patternmap.get( new BlockPos( x>>2 ,y>>2 ,z>>2) ,true);
	}
	
	private double getHeightMapValue( int x ,int z) {
		int xtx = this.heightmap.getHalfwidth() + x;
		int ztx = this.heightmap.getHalfwidth() + z;
		return this.heightmap.getMap()[xtx][ztx];
	}
	
	private double getBuildMapValue( int x ,int z) {
		int xtx = this.buildmap.getHalfwidth() + x;
		int ztx = this.buildmap.getHalfwidth() + z;
		return this.buildmap.getMap()[xtx][ztx];
	}
	
	
	
	//============== Patterns ==========//

	
	
	
	
	
	
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
	
	
	
	public static int spike( int x ,int z) {
		int a = 4;
		double logA = Math.log( Math.pow( x ,2) + Math.pow( z ,2));
		double logB = Math.log( 0.5);
//		return (int) Math.round( 2 * a * ( logA / logB) );		
		return (int) Math.round( (logA / logB) + 100);
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
