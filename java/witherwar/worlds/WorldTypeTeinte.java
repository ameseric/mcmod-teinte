package witherwar.worlds;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldTypeTeinte extends WorldType {

	public WorldTypeTeinte( String name) {
		super( name); //TODO change name
	}
	
	
	@Override
	public IChunkGenerator getChunkGenerator( World world ,String generatorOptions) {
		return new ChunkGeneratorPillar( world ,WorldCatalog.PILLAR_DIMENSION_ID ,false 
				,new ChunkGeneratorSettings.Factory().toString()); 
	}

	
	

    
    public double voidFadeMagnitude()
    {
        return 1.0D;
    }
}
