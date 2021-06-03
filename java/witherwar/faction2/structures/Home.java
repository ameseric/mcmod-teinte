package witherwar.faction2.structures;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.Template;
import witherwar.utility.Pair;

public abstract class Home {

	
	protected BlockPos center;
	
	
	public Home( BlockPos center) {
		this.center = center;
	}
	
	
	
	public FunctionalStructure getNextStructure( WorldServer world) {
		return new MuirSpike( world);
	}
	
	
	
	public boolean canBuild( Structure s) {
		return false;
	}
	
	
	
	public abstract boolean isValidPosition( BlockPos pos);
	
	
	
	public ArrayList< Template.BlockInfo> getNextSegment(){
		Structure s = this.nextStructure();
		BlockPos buildspot = this.getBuildSpot( s);
		if( buildspot == null) {
			return this.getDistrictBuildSegment( s);
		}
		return s.getBlocks();
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
