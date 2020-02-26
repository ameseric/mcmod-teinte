package witherwar.worlds;

import net.minecraft.world.DimensionType;

public class WorldManager {

	private static DimensionType pillarWorld;
	
	
	public WorldManager() {	

		pillarWorld = DimensionType.register( "pillar" ,"_pillar" ,5 ,WorldProviderPillar.class ,true);
	}
	
	
	public static DimensionType getPillarType() {
		return pillarWorld;
	}
	
}
