package witherwar.tileentity;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.utility.Symbol;




public class MonumentCell extends GrowingTile{
	
	
	
	
	
	

	public MonumentCell(BlockPos pos) {
		super(pos ,Blocks.OBSIDIAN ,TileLogic.MONUMENT_ID ,40);
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
	public void readFromNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
	
	

}
