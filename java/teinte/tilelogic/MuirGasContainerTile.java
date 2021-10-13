package teinte.tilelogic;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import teinte.hermetics.Muir;
import teinte.hermetics.MuirContainer;
import teinte.hermetics.MuirGasContainer;

public abstract class MuirGasContainerTile extends TileLogic implements MuirGasContainer{


	protected Muir contents = new Muir();
	public static final int DEFAULT_MUIR_LIMIT = 5000;
	
	
	public MuirGasContainerTile(BlockPos pos, Block homeblock, boolean active ,int ticksUntilUpdate) {
		super( pos ,homeblock ,active ,ticksUntilUpdate);			
	}
	public MuirGasContainerTile() {}
	
	
	
	
	public void averageWith( Muir m ,BlockPos requester) {
		this.contents.averageWith( m);
	}
	
	
	public void add( Muir m) {
		this.contents.add( m);
	}
	
	
	public Muir contents() {
		return this.contents;
	}
	



}
