package witherwar.faction2;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import witherwar.ObjectCatalog;
import witherwar.entity.DroneEntity;
import witherwar.entity.FactionEntity;
import witherwar.entity.ai.FactionEntityTask;
import witherwar.entity.ai.FactionHarvestTask;



public class Faction2 {
	
//	Resources will depend on faction
//	but start with Stone?
	
//	Task priority for units is derived from the priority set by the Faction
//	and the distance from a particular unit?
	
	
	private int tickrate = 40;
	private int resourceCount = 100;
	private ArrayList<FactionEntity> units = new ArrayList<>();
	private ChunkPos harvestTest = new ChunkPos(0,0);
	private BlockPos index = new BlockPos(16,255,15);
	
	
	private final Block homeblockType = ObjectCatalog.FLESH;
	private BlockPos homeblockPos;
	
	private ArrayList<FactionEntityTask> tasks = new ArrayList<>();
	
	
	public Faction2( BlockPos pos) {
		this.homeblockPos = pos;
	}
	
	
	
	public void tick( int tickcount ,WorldServer world) {
		if( tickcount%this.tickrate != 0){
			return;
		}
		
		System.out.println( "Ticking!");

		if( this.units.size() < 3) {
			this.createUnit( this.homeblockPos ,world);
		}
		
		
		
//		if( this.resourceCount < 200) {
//			this.createNewHarvestTask();
//		}
		
		
		
		
		
	}
	
	
	
	public void createUnit( BlockPos pos ,World world) {
		FactionEntity newUnit = new DroneEntity( world ,this); //TODO not sure what to do with Entity types
		this.subtractResource( newUnit.getCost());
		newUnit.setPosition( this.homeblockPos.getX() ,this.homeblockPos.getY()+2 ,this.homeblockPos.getZ());
		this.units.add( newUnit);
		world.spawnEntity( newUnit);
	}
	
	
	
	
	public void subtractResource(int i) {
		this.resourceCount -= i;
	}
	public void addResource(int i) {
		this.resourceCount += i;
	}
	
	
	public void claimTask( FactionEntity e) {
		
	}
	
	
	
	//x -> z -> y
	public BlockPos getNextHarvestBlock( BlockPos ipos ,World world) {
		System.out.println( "Getting harvest block..");
		Block b;
		BlockPos worldPos;
		do {
			this.index = getNext( this.index);
			worldPos = this.harvestTest.getBlock( this.index.getX() ,this.index.getY() ,this.index.getZ());			
			b = world.getBlockState( worldPos).getBlock();			
			
		}while(b == Blocks.AIR);
		
		
		System.out.println( this.index);
		System.out.println( worldPos);
		

		return worldPos;
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
