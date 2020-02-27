package witherwar.worlds;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldTypeTeinte extends WorldType {

	public WorldTypeTeinte() {
		super( "pillar"); //TODO change
	}
	
	
	@Override
	public IChunkGenerator getChunkGenerator( World world ,String generatorOptions) {
		return new ChunkGeneratorPillar( world ,WorldManager.PILLAR_DIMENSION_ID ,false 
				,new ChunkGeneratorSettings.Factory().toString()); 
	}

}
