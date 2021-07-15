package witherwar;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import witherwar.tileentity.TileLogic;




/**
 * 
 * @author Guiltygate
 * 
 * This is an attempt to segregate Forge API calls that have a strong chance of breaking in future versions
 * and provide wrapper methods for Forge objects which do not fit our design pattern, but we cannot modify.
 *
 */
public class MCForge {
	
	private MCForge() {}
	
	
	public static List<EntityPlayerMP> getAllPlayersOnServer(){
		return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
	}
	
	
	
	
	public static Tile getTile( World world ,BlockPos pos) {
		IBlockState bs = world.getBlockState( pos);
		TileLogic l = TEinTE.instance.getTileLogic( pos);
		return new Tile( bs ,l);
	}
	
	
	
//	public static void setBlock( Block b ,BlockPos pos ,World world) {
//		world.setBlockState( pos ,b.getDefaultState());
//		TEinTE.instance.spawnTileLogicIfNeeded( b ,pos);
//	}
	
	

}




class Tile{
	
	private IBlockState blockstate;
	private TileLogic logic;
	
	
	public Tile( IBlockState blockstate ,@Nullable TileLogic logic) {
		this.blockstate = blockstate;
		this.logic = logic;
	}
	
	
	public TileLogic logic() {
		return this.logic;
	}
	
	
	public IBlockState blockstate() {
		return this.blockstate;
	}
	
	
	public Block block() {
		return this.blockstate().getBlock();
	}
	
	
	public boolean hasLogic() {
		return this.logic != null;
	}
	
	
}