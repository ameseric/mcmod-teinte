package teinte.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import teinte.entity.FactionEntity;
import teinte.faction2.FactionAITask;

public abstract class FactionEntityTask extends EntityAIBase implements FactionAITask{

	protected FactionEntity taskHolder;
	protected boolean active = true;
	
	
	@Override
	public boolean shouldExecute() {
		return false;
	}
	
	
	
	public void setEntity( FactionEntity e) {
		this.taskHolder = e;
	}



	@Override
	public void setActive(boolean active) {
		 this.active = active;		
	}
	
	

}
