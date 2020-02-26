package witherwar.worlds;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;

public class WorldProviderPillar extends WorldProvider{
	
	public WorldProviderPillar() {
		super();
	}

	@Override
	public DimensionType getDimensionType() {
		return WorldManager.getPillarType();
	}

}
