package witherwar.tileentity;


import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.ObjectCatalog;




public class SerpentmindTile extends TileLogic{

	
	
	public SerpentmindTile(BlockPos pos) {
		super(pos ,ObjectCatalog.TERRA_KALI ,0 ,true);
		//this.homeBlock = ObjectCatalog.TERRA_KALI;

	}

	
	
	
	@Override
	public void ticklogic( World world) {
		System.out.println( this.getPos());
	}



	public String getDataName() {
		return "Serpentmind";
	}






	
	
}
