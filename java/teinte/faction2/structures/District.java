package teinte.faction2.structures;

import java.util.HashSet;

import net.minecraft.util.math.BlockPos;

public class District {
	
	private int[] globalSegments;
	private int height = 16;
	private int layers = 1;
	private String name;
	private HashSet<Structure> structures;
	/* Number of blocks per segment*/
	private int segmentSize;
	
//	private final static int SEGMENT_SIZE = 16;
	
	
	public District( String name ,int[] segments ,int homeSize) {
		this.globalSegments = segments;
		this.name = name;
		this.segmentSize = homeSize / 5;
		//generate noisemap -> place structures in HashList?
	}
	
	
	
	public BlockPos getBuildSpot( Structure s) {
		BlockPos size = s.getSize();
		
		return null;
	}
	
	
	
	
	
	
	public void addStructure( Structure s) {
		this.structures.add( s);
	}
	
	
	public HashSet<Structure> getStructures() {
		return this.structures;
	}
	
	
	public boolean hasStructure( Structure s) {
		return this.structures.contains( s);
	}
	
	
	public int getHeight() {
		return this.height;
	}
	
	
	public int[] getSegments() {
		return this.globalSegments;
	}
	
	
	public String getName() {
		return this.name;
	}
	
	
	public District setLayers(int i) {
		this.layers = i;
		return this;
	}
	
	
	public District setHeight( int i) {
		this.height = i;
		return this;
	}
	
	
//	public boolean containsSegment( int n) {
//		return this.segments[n] == 1;
//	}
	
	
	

}
