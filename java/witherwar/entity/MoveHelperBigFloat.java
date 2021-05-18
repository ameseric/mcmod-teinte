package witherwar.entity;

import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

public class MoveHelperBigFloat extends EntityMoveHelper{
	
	private final FactionEntityFlying parent;
    private int courseChangeCooldown;
    
    

    public MoveHelperBigFloat(FactionEntityFlying e){
        super( e);
        this.parent = e;
    }
    
    

    public void onUpdateMoveHelper()
    {
        if (this.action == EntityMoveHelper.Action.MOVE_TO)
        {
            double d0 = this.posX - this.parent.posX;
            double d1 = this.posY - this.parent.posY;
            double d2 = this.posZ - this.parent.posZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;

            if (this.courseChangeCooldown-- <= 0)
            {
                this.courseChangeCooldown += this.parent.getRNG().nextInt(5) + 2;
                d3 = (double)MathHelper.sqrt(d3);

                if (this.isNotColliding(this.posX, this.posY, this.posZ, d3))
                {
                    this.parent.motionX += d0 / d3 * 0.1D;
                    this.parent.motionY += d1 / d3 * 0.1D;
                    this.parent.motionZ += d2 / d3 * 0.1D;
                }
                else
                {
                    this.action = EntityMoveHelper.Action.WAIT;
                }
            }
        }
    }
    
    

    /**
     * Checks if entity bounding box is not colliding with terrain
     */
    private boolean isNotColliding(double x, double y, double z, double p_179926_7_)
    {
        double d0 = (x - this.parent.posX) / p_179926_7_;
        double d1 = (y - this.parent.posY) / p_179926_7_;
        double d2 = (z - this.parent.posZ) / p_179926_7_;
        AxisAlignedBB axisalignedbb = this.parent.getEntityBoundingBox();

        for (int i = 1; (double)i < p_179926_7_; ++i)
        {
            axisalignedbb = axisalignedbb.offset(d0, d1, d2);

            if (!this.parent.world.getCollisionBoxes(this.parent, axisalignedbb).isEmpty())
            {
                return false;
            }
        }

        return true;
    }
    
    
}
	
	

