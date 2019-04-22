package witherwar.entity;

import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.world.World;

public class EntityMotusGhast extends EntityGhast{
	
	private int explosionStrength = 3;

	public EntityMotusGhast(World worldIn) {
		super(worldIn);
	}
	
	
	@Override
	public int getFireballStrength() {
		return this.explosionStrength;
	}
	
	
	
}