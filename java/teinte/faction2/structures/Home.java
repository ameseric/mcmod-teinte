package teinte.faction2.structures;

import java.util.ArrayList;
import java.util.List;
import java.util.ArrayDeque;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.Template;
import teinte.utility.Pair;

public abstract class Home {

	
	protected BlockPos center;
	protected ArrayDeque< Structure> queue = new ArrayDeque<>();
	protected List<District> districts = new ArrayList<>();
	/* the width and length of the Home in blocks */
	protected int size;

	
	
	
	
	public Home( BlockPos center ,int size) {
		this.center = center;
		this.size = size;
	}
	
	
	
	
	
	
	public boolean canBuild( Structure s) {
		return false;
	}
	
	
	
	private BlockPos getBuildSpot( Structure s) {
		for( District d : this.districts) {
			if( d.hasStructure( s)) {
				return d.getBuildSpot( s);
			}
		}
		return null;
	}
	
	
	
	public abstract boolean isValidPosition( BlockPos pos);
	
	
	
	public ArrayList< Template.BlockInfo> getNextSegment(){
//		Class<? extends Structure> s = this.nextStructure();
//		BlockPos buildspot = this.getBuildSpot( s);
//		if( buildspot == null) {
//			return this.getDistrictBuildSegment( s);
//		}
		return null;//s.getBlocks();
	}
	
	
	
	
	public Structure nextStructure() {
		return this.queue.peek();
	}
	
	
	
	protected static boolean emptyBoxPattern( BlockPos pos ,int mod) {
		int x = pos.getX() % mod;
		int y = pos.getY() % mod;
		int z = pos.getZ() % mod;
		
		int ubound = mod - 1;
		
		if( (( x == ubound || x == 0) && (z == ubound || z == 0))  ||  (y == 0 || y == ubound)) {
			return true;
		}		
		return false;
	}
	
	
}
