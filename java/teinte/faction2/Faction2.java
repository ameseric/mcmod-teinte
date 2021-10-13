package teinte.faction2;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import teinte.ObjectCatalog;
import teinte.entity.DroneEntity;
import teinte.entity.FactionEntity;
import teinte.entity.ai.FactionEntityTask;
import teinte.entity.ai.FactionHarvestTask;
import teinte.faction2.structures.Home;
import teinte.utility.Tickable;



public abstract class Faction2 implements Tickable{
	
//	Resources will depend on faction
//	but start with Stone?
	
//	Task priority for units is derived from the priority set by the Faction
//	and the distance from a particular unit?
	
	
	protected int tickrate = 40;
	protected int resourceCount = 100;
	protected ArrayList<FactionEntity> units = new ArrayList<>();
	protected ChunkPos harvestTest = new ChunkPos(0,0);
	protected BlockPos index = new BlockPos(16,255,15);
	protected final Block homeblockType = ObjectCatalog.FLESH;
	protected BlockPos homeblockPos;
	protected ArrayList<FactionEntityTask> tasks = new ArrayList<>();
	protected Home home;
	private boolean firstrun = true;
	
	
	
	
	public Faction2( BlockPos pos) {
		this.homeblockPos = pos;
	}
	
	
	
	@Override
	public void _tick( int tickcount ,WorldServer world) {
		if( tickcount%this.tickrate != 0){
			return;
		}
		
		if( this.firstrun) {
			this.parentInit();
			this.onFirstTickCycle( world);
			this.firstrun = false;
		}
		
		this.updateTaskList();
				
		
		this.ticklogic( world ,tickcount);
		
	}
	
	
	
	private void updateTaskList() {
		
	}
	
	
	
	private void parentInit() {
		//TODO stuff
	}
	
	protected abstract void onFirstTickCycle( World world);
	
	
	protected abstract void ticklogic( WorldServer world ,int tickcount);
	
	
	
	
	//========== Boolean Checks =================
	@Override
	public boolean isDead() {
		return !this.hasUnits() && !this.hasCoreEntity();
	}
	
	
	public boolean hasUnits() {
		return this.units.size() > 0;
	}
	
	
	public abstract boolean hasCoreEntity();
	
	
	
	
	//============= Internal Helpers
	protected void createUnit( BlockPos pos ,World world) {
		FactionEntity newUnit = new DroneEntity( world ,this); //TODO not sure what to do with Entity types
		this.subtractResource( newUnit.getCost());
//		newUnit.setPosition( this.homeblockPos.getX() ,this.homeblockPos.getY()+2 ,this.homeblockPos.getZ());
		newUnit.setPosition( pos.getX() , pos.getY() ,pos.getZ());
		this.units.add( newUnit);
		world.spawnEntity( newUnit);
	}
	
	
	
	
	protected void subtractResource(int i) {
		this.resourceCount -= i;
	}
	protected void addResource(int i) {
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
