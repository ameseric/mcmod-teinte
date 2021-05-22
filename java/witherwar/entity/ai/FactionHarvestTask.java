package witherwar.entity.ai;

import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;

public class FactionHarvestTask extends FactionEntityTask{
	private boolean active = true;
	private BlockPos center;
	private BlockPos target;
	private boolean collecting = false;
	
	
	
	public FactionHarvestTask( BlockPos center) {
		this.center = center;
		this.setMutexBits( 3);
	}
	
	
	
	
	
	@Override
	public void startExecuting() {
		System.out.println( "Starting execution...");
		this.target = this.taskHolder.getFaction().getNextHarvestBlock( this.center ,this.taskHolder.world);
//		this.taskHolder.getMoveHelper().setMoveTo( this.target.getX() ,this.target.getY()+2 ,this.target.getZ() ,1.0 );
		
		boolean hasPath = this.taskHolder.getNavigator().tryMoveToXYZ( this.target.getX(), this.target.getY()+2 ,this.target.getZ() ,1.0);
		System.out.println( hasPath);
		
		this.collecting = true;
	}
	
	//the smaller the entity size, the lower the render range. Pop-in is big problem for small entities
	
	@Override
	public void updateTask() {

//		System.out.println( "Updating task...");
//		this.taskHolder.getMoveHelper().setMoveTo( 0 ,100 ,0 ,1.0D); 
		//MoveTo does not work if movePos interferes with moveHelper notColliding method
		

		
		PathNavigate nav = this.taskHolder.getNavigator();
		if( nav.noPath()) {
			System.out.println( "Path is no more.");
			if( this.collecting) {
				nav.tryMoveToXYZ( this.target.getX(), this.target.getY()+2 ,this.target.getZ() ,1.0);				
			}else {
				nav.tryMoveToXYZ( this.center.getX() ,this.center.getY()+2 ,this.center.getZ() ,1.0);
			}


		}
		
		if( this.collecting && this.taskHolder.getDistanceSq( this.target) < 7) {
			this.taskHolder.setBeamTarget( this.target.add(0 ,-2 ,0));			
		}

		if( this.collecting && this.taskHolder.getDistanceSq( this.target) < 6) {
			System.out.println( "Finished collecting...");
			this.taskHolder.world.setBlockState( this.target ,Blocks.AIR.getDefaultState());
			this.taskHolder.getNavigator().tryMoveToXYZ( this.center.getX() ,this.center.getY()+2 ,this.center.getZ() ,1.0 );
			this.taskHolder.stopBeamRender();
			this.collecting = false;
		}

		if(!this.collecting && this.taskHolder.getDistanceSq( this.center) < 6) {
			this.target = this.taskHolder.getFaction().getNextHarvestBlock(this.center ,this.taskHolder.world);
			this.taskHolder.getNavigator().tryMoveToXYZ( this.target.getX() ,this.target.getY()+2 ,this.target.getZ() ,1.0 );
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
