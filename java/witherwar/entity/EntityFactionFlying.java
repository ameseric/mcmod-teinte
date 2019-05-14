package witherwar.entity;

import net.minecraft.entity.EntityFlying;
import net.minecraft.world.World;

public class EntityFactionFlying extends EntityFlying implements IFactionUnit{

	public EntityFactionFlying(World worldIn) {
		super(worldIn);
	}

}
