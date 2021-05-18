package witherwar.faction2;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import witherwar.entity.FactionEntityLiving;

public class Faction2 {
	
//	Resources will depend on faction
//	but start with Stone?
	
//	Task priority for units is derived from the priority set by the Faction
//	and the distance from a particular unit?
	
	
	private int tickrate = 20;

	private int resourceCount = 100;
		
	private ArrayList<FactionEntityLiving> units = new ArrayList<>();
	
	private ChunkPos harvestTest = new ChunkPos(0,0);
	
	private BlockPos index = new BlockPos(16,255,15);
	
	
	
	public void tick( int tickcount ,WorldServer world) {
		
		if( tickcount%this.tickrate != 0){
			return;
		}
		
		
		if( this.units.size() < 3) {
			FactionEntityLiving newUnit = new FactionEntityLiving( world ,this);
			this.units.add( newUnit);
			world.spawnEntity( newUnit);
		}
		
		
		if( this.resourceCount < 200) {
			
		}
		
		
	}
	
	
	
	
	//x -> z -> y
	public BlockPos getNextHarvestBlock( BlockPos ipos ,World world) {
	
		Block b;
		do {
			this.index = getNext( this.index);
			BlockPos worldPos = this.harvestTest.getBlock( this.index.getX() ,this.index.getY() ,this.index.getZ());			
			b = world.getBlockState( worldPos).getBlock();			
			
		}while(b == Blocks.AIR);
		
		

		
		
		

		return this.index;
	}
	
	
	public BlockPos getNext( BlockPos pos) {
		pos = pos.add( -1 ,0 ,0);
		if( pos.getX() < 0) {
			pos = pos.add( 16 ,0 ,-1);
			if( pos.getZ() < 0) {
				pos = pos.add( 0 ,-1 ,16);
			}
		}
		return pos;
	}
	
	
	
}
