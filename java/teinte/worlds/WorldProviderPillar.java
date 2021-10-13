package teinte.worlds;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderPillar extends WorldProvider{
	
	
	public WorldProviderPillar() {
		super();
	}

	@Override
	public DimensionType getDimensionType() {
		return WorldCatalog.PILLAR_DIMENSION_TYPE;
	}
	
	@Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        return 0.95F;
    }	

    
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean isSkyColored(){
        return false;
    }    

    
    

	
	
	
	@Override
	public IChunkGenerator createChunkGenerator() {
		
		return new ChunkGeneratorPillar( world ,WorldCatalog.PILLAR_DIMENSION_ID ,false 
				,new ChunkGeneratorSettings.Factory().toString());
	}
	
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
