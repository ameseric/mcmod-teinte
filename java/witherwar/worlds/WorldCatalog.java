package witherwar.worlds;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.DimensionManager;


//stateless singleton that manages TEINTE custom dimensions
public class WorldCatalog {
	
	public static final int PILLAR_DIMENSION_ID = findOpenDimensionID();
	public final static DimensionType PILLAR_DIMENSION_TYPE;
	public static final int MAX_ID_NUM = 260;

	static {
		PILLAR_DIMENSION_TYPE = DimensionType.register( "pillar" ,"_pillar" ,PILLAR_DIMENSION_ID ,WorldProviderPillar.class ,false);
	}	
	
	//WorldType instance is not directly used - the object creation triggers necessary registration in the super class
	public static final WorldType worldTypePillar = new WorldType( "pillar");

	
	
	private WorldCatalog() {}
	
	
	
	//can move to more appropriate location
	public static void registerDimensions() {
		DimensionManager.registerDimension( PILLAR_DIMENSION_ID ,PILLAR_DIMENSION_TYPE);
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
