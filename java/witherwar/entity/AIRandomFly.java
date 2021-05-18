package witherwar.entity;

import java.util.Random;

import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;


/**
 * 
 * @author Mojang
 *
 * Taken from Ghast AIRandomFly
 *
 */
public class AIRandomFly extends EntityAIBase{
	
    private final FactionEntityFlying parent;
    
    
    

    public AIRandomFly( FactionEntityFlying e)
    {
        this.parent = e;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        EntityMoveHelper entitymovehelper = this.parent.getMoveHelper();

        if (!entitymovehelper.isUpdating())
        {
            return true;
        }
        else
        {
            double d0 = entitymovehelper.getX() - this.parent.posX;
            double d1 = entitymovehelper.getY() - this.parent.posY;
            double d2 = entitymovehelper.getZ() - this.parent.posZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            return d3 < 1.0D || d3 > 3600.0D;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return false;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        Random random = this.parent.getRNG();
        double d0 = this.parent.posX + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
        double d1 = this.parent.posY + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
        double d2 = this.parent.posZ + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
        this.parent.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
    }
}
