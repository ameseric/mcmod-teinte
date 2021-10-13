package teinte.tilelogic;


import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teinte.ObjectCatalog;
import teinte.TEinTE;
import teinte.system.GrowthSystem;
import teinte.utility.BlockTypeCollection;
import teinte.utility.BlockUtil;
import teinte.utility.HashBlockCollection;



@Deprecated
public class KaliCoreTile extends TileLogic{

	private final IBlockState SKIN = Blocks.OBSIDIAN.getDefaultState();
	
	private final BlockTypeCollection dontChange = new HashBlockCollection();
	
	private GrowthSystem growth = new GrowthSystem();
	
	
	
	static {
		TileLogicManager.registerClass( new KaliCoreTile());
	}
	
	
	public KaliCoreTile() {}
	
	public KaliCoreTile(BlockPos pos) {
		super(pos ,ObjectCatalog.TERRA_KALI ,true ,10);
		
		this.dontChange.add( Blocks.OBSIDIAN);
		this.dontChange.add( Blocks.AIR);
		this.dontChange.add( ObjectCatalog.TERRA_KALI);
		this.dontChange.add( ObjectCatalog.ASH_REPL_BLOCK);
		this.dontChange.add( Blocks.BEDROCK);
		
		this.growth.add( EnumFacing.UP);
		this.growth.add( EnumFacing.UP);
		ArrayList<EnumFacing> segment = new ArrayList<>();
		segment.add(EnumFacing.EAST);
		segment.add( EnumFacing.WEST);
		this.growth.add( segment);
	}


	
	
	@Override
	public void _ticklogic( World world) {
		HashMap<BlockPos,Block> neighbors = BlockUtil.getNeighborBlocks( this.pos() ,world);
		
		for( BlockPos pos : neighbors.keySet() ) {
			Block neighbor = neighbors.get( pos); 
			if( !this.dontChange.includes( neighbor)) {
				world.setBlockState( pos ,ObjectCatalog.ASH_REPL_BLOCK.getDefaultState());
				ReplicatingTile child = (ReplicatingTile) TEinTE.instance.getTileLogic( pos);
				child.setParent( this);
			}
		}
		
	}



	public String getDataName() {
		return "Serpentmind";
	}


	@Override
	protected NBTTagCompound __writeToNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void __readFromNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		
	}






	
	
}
