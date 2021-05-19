package witherwar.entity;

import net.minecraft.entity.ai.EntityAIBase;
import witherwar.faction2.FactionAITask;

public abstract class AIFactionEntityBase extends EntityAIBase implements FactionAITask{

	protected FactionEntityLiving taskHolder;
	protected boolean active = true;
	
	
	@Override
	public boolean shouldExecute() {
		return false;
	}
	
	
	
	public void setEntity( FactionEntityLiving e) {
		this.taskHolder = e;
	}



	@Override
	public void setActive(boolean active) {
		 this.active = active;		
	}
	
	

}
