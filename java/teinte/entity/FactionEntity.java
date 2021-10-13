package teinte.entity;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import teinte.entity.ai.FactionEntityTask;
import teinte.faction2.Faction2;


//TODO: build faction entity interface based on this
public abstract class FactionEntity extends EntityLiving{

	private int cost = 10;
	private ArrayList<Block> heldblocks = new ArrayList<>();
	private Faction2 faction;

	
    private static final DataParameter<Boolean> RENDER_BEAM = 
    		EntityDataManager.<Boolean>createKey(FactionEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<BlockPos> BEAM_TARGET = 
    		EntityDataManager.<BlockPos>createKey(FactionEntity.class, DataSerializers.BLOCK_POS);


	
	
	public FactionEntity(World world ,Faction2 faction) {
		super(world);
		this.faction = faction;
	}
	
	public FactionEntity(World world) {
		super(world);
	}	

	
	@Override
    protected void entityInit() {
		super.entityInit();
		this.dataManager.register( RENDER_BEAM ,Boolean.valueOf(false));
		this.dataManager.register( BEAM_TARGET ,new BlockPos(0,0,0));
	}
	
	
	
	public void factionTick( int tickcount ,WorldServer world) {
		System.out.println( this.isLoaded(world));
		
		this.pickTask();
		
		if( this.isLoaded(world)){
			return; //do not perform any tasks, allow MC to tick
		}
		
		
		
	}
	
	
	
    public BlockPos getBeamTarget() {
    	return this.dataManager.get( BEAM_TARGET);
    }
    
    public void setBeamTarget( BlockPos pos) {
    	this.dataManager.set( BEAM_TARGET ,pos);
    	this.dataManager.set( RENDER_BEAM ,true);
    }
    
    public boolean shouldRenderBeam() {
    	return this.dataManager.get( RENDER_BEAM);
    }
    
    public void stopBeamRender() {
    	this.dataManager.set( RENDER_BEAM ,false);
    }
	
	
	
	public void pickTask() {
		
	}
	
	
	
	public void addTask( FactionEntityTask task) {
		task.setEntity( this);
		this.tasks.addTask( 1 ,task);
	}
	
//	public boolean hasTask( EntityAIBase task) {
//		this.tasks
//	}
//	
	
	
	
	public void setCost( int cost) {
		this.cost = cost;
	}
	
	public int getCost() {
		return this.cost;
	}
	
	
	
	public void setPosition( Vec3d v) {
		this.setPosition( v.x ,v.y ,v.z);
	}
	
	
	
	public Faction2 getFaction() {
		return this.faction;
	}
	
	
	
	public boolean isLoaded( WorldServer world) {
		return world.isBlockLoaded( this.getPosition());
	}
	

}
