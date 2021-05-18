package witherwar.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class AIFactionHarvest extends EntityAIBase{
	private boolean active = false;
	private FactionEntityLiving parent;
	private BlockPos center;
	private BlockPos target;
	
	
	
	public AIFactionHarvest( BlockPos center) {
		this.center = center;
		this.setMutexBits( 3);
	}
	
	
	
	@Override
	public void startExecuting() {
		
		BlockPos pos = this.parent.getFaction().getNextHarvestBlock( this.center ,this.parent.world);
		this.parent.getMoveHelper().setMoveTo( pos.getX() ,pos.getY() ,pos.getZ() ,1.0 );
		this.target = pos;
	}
	
	
	
	@Override
	public void updateTask() {
		if( this.parent.getDistanceSq( this.target) < 0.5) {
			this.parent.world.setBlockState( this.target ,Blocks.AIR.getDefaultState());
			this.parent.getMoveHelper().setMoveTo( this.center.getX() ,this.center.getY() ,this.center.getZ() ,1.0 );
		}
		
		if( this.parent.getDistanceSq( this.center) < 0.5) {
			this.target = this.parent.getFaction().getNextHarvestBlock(this.center ,this.parent.world);
			this.parent.getMoveHelper().setMoveTo( this.target.getX() ,this.target.getY() ,this.target.getZ() ,1.0 );
		}
	}
	
	
	
	
	
    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return false;
    }
	
	
	
	@Override
	public boolean shouldExecute() {
		return this.active;
	}	
	
	
	
	public void setActive( boolean active) {
		this.active = active;
	}
	
	public void setEntity( FactionEntityLiving e) {
		this.parent = e;
	}
	
	

}
