package witherwar.worlds;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldProviderPillar extends WorldProviderSurface{
	
	public WorldProviderPillar() {
		super();
	}

	@Override
	public DimensionType getDimensionType() {
		return WorldManager.PILLAR_WORLD_TYPE;
	}
	
	
	
	
//	@Override
//	public IChunkGenerator createChunkGenerator() {
//		
//		return new ChunkGeneratorPillar( world ,WorldManager.PILLAR_DIMENSION_ID ,false 
//				,new ChunkGeneratorSettings.Factory().toString());
//	}
	
// Jabelar's code comparison
//    public IChunkGenerator createChunkGenerator()
//    {
//        return this.terrainType.getChunkGenerator(world, generatorSettings);
//    }
	
	
// Twilight Forest code comparison	
//    public IChunkProvider createChunkGenerator()
//    {
//    	// save chunk generator?
//    	if (this.chunkProvider == null) {
//	    	this.chunkProvider = new ChunkProviderTwilightForest(worldObj, worldObj.getSeed(), worldObj.getWorldInfo().isMapFeaturesEnabled());
//	        return this.chunkProvider;
//    	} else {
//    		return new ChunkProviderTwilightForest(worldObj, worldObj.getSeed(), worldObj.getWorldInfo().isMapFeaturesEnabled());
//    	}
//    }

}
