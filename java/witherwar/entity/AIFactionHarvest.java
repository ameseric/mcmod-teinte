package witherwar.entity;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class AIFactionHarvest extends AIFactionEntityBase{
	private boolean active = true;
	private BlockPos center;
	private BlockPos target;
	private boolean collecting = false;
	
	
	
	public AIFactionHarvest( BlockPos center) {
		this.center = center;
		this.setMutexBits( 3);
	}
	
	
	
	
	
	@Override
	public void startExecuting() {
		System.out.println( "Starting execution...");
		this.target = this.taskHolder.getFaction().getNextHarvestBlock( this.center ,this.taskHolder.world);
		this.taskHolder.getMoveHelper().setMoveTo( this.target.getX() ,this.target.getY()+2 ,this.target.getZ() ,1.0 );
//		this.taskHolder.getMoveHelper().setMoveTo( 0 ,90 ,0 ,1.0 );
		this.taskHolder.setBeamTarget( this.target);
		this.taskHolder.renderBeam = true;
		this.collecting = true;
	}
	
	//viable, need to massage entity collision/entity shape/etc to make this reliable.
	//the smaller the entity size, the lower the render range. Pop-in is big problem for small entities
	
	@Override
	public void updateTask() {

//		System.out.println( "Updating task...");
//		this.taskHolder.getMoveHelper().setMoveTo( 0 ,100 ,0 ,1.0D); 
		//MoveTo does not work if movePos interferes with moveHelper notColliding method

		if( this.collecting && this.taskHolder.getDistanceSq( this.target) < 4) {
			System.out.println( "Finished collecting...");
			this.taskHolder.world.setBlockState( this.target ,Blocks.AIR.getDefaultState());
			this.taskHolder.getMoveHelper().setMoveTo( this.center.getX() ,this.center.getY()+2 ,this.center.getZ() ,1.0 );
			this.collecting = false;
		}

		if(!this.collecting && this.taskHolder.getDistanceSq( this.center) < 4) {
			this.target = this.taskHolder.getFaction().getNextHarvestBlock(this.center ,this.taskHolder.world);
			this.taskHolder.getMoveHelper().setMoveTo( this.target.getX() ,this.target.getY()+2 ,this.target.getZ() ,1.0 );
			this.collecting = true;
		}

	}
	
	@Override //Testing
    public boolean isInterruptible()
    {
        return true;
    }
	
	
	
	
	
    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     * 
     * updateTask still runs at least once.
     */
    public boolean shouldContinueExecuting()
    {
        return true;
    }
	
	
	
	@Override
	public boolean shouldExecute() {
		return this.active;
	}	
	
	
	
	public void setActive( boolean active) {
		this.active = active;
	}
	

	
	

}
