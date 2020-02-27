package witherwar.worlds;

import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;


//static class, no instance
public class WorldManager {
	
	public static final int PILLAR_DIMENSION_ID = findOpenDimensionID();
	public final static DimensionType PILLAR_WORLD_TYPE;
	public static final int MAX_ID_NUM = 260;
	
	//WorldType instance is not directly used - the object creation triggers necessary registration in the super class
	private static final WorldTypeTeinte worldTypePillar = new WorldTypeTeinte();

	static {
		PILLAR_WORLD_TYPE = DimensionType.register( "pillar" ,"_pillar" ,PILLAR_DIMENSION_ID ,WorldProviderPillar.class ,true);
	}

	
	
	private WorldManager() {}
	
	
	
	//can move to more appropriate location
	public static void registerDimensions() {
		DimensionManager.registerDimension( PILLAR_DIMENSION_ID ,PILLAR_WORLD_TYPE);
	}
	
	
//	public static void registerWorldStructureGenerators() {
//		GameRegistry.registerWorldGenerator(new WorldGenShrine(), 10);
//	}
	
	

	public static int findOpenDimensionID() {
		
		int id = 5;
		while( DimensionManager.isDimensionRegistered( id)){
			++id;
		}
		
		return id;
	}
	
	
}
