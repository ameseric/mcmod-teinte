package teinte.tilelogic;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teinte.TEinTE;
import teinte.utility.Symbol;




public class MonumentCell extends GrowingTile{
	
	
	static {
		TileLogicManager.registerClass( new MonumentCell());
	}
	
	
	
	public MonumentCell() {}
	
	public MonumentCell(BlockPos pos) {
		super(pos ,Blocks.OBSIDIAN ,40);
		this.celltype = CellType.SEED;
	}
	
	


	
	
	
	
	
	@Override
	protected void grow(World world) {
		
		switch( this.celltype) {
			case SEED:
				//get neighbors, set as leaves
				//get above, set as stem
				//die?
				break;
			
			case LEAF:
				//grow outward up to limit
				break;
				
			case STEM:
				//same as seed
				break;
		
		
		}
		
		
	}






	@Override
	protected boolean canGrow() {
		return true;
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
