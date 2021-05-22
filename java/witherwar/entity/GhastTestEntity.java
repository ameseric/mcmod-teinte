package witherwar.entity;

import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.world.World;

public class GhastTestEntity extends EntityGhast{
	
	private int explosionStrength = 3;

	public GhastTestEntity(World worldIn) {
		super(worldIn);
	}
	
	
	@Override
	public int getFireballStrength() {
		return this.explosionStrength;
	}
	
	
	
}