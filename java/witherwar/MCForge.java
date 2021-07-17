package witherwar;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import witherwar.tilelogic.TileLogic;
import witherwar.utility.Tile;




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
	
	
	public static Tile getTile( BlockPos pos) {
		return getTile( getOverworld() ,pos);
	}
	
	
	public static Block getBlock( BlockPos pos) {
		return getBlockState( pos).getBlock();
	}
	
	
	public static TileLogic getTileLogic( BlockPos pos) {
		return TEinTE.instance.getTileLogic( pos);
	}
	
	
	public static IBlockState getBlockState( BlockPos pos) {
		return getOverworld().getBlockState( pos);
	}
	
	
	
	public static World getOverworld() {
		return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
	}
	
	
	//TODO: Move to appropriate class
	public static HashMap<BlockPos,Tile> getNeighbors( BlockPos pos){
		HashMap<BlockPos,Tile> neighbors = new HashMap<>();
		
		for( EnumFacing direction : EnumFacing.VALUES) {
			BlockPos npos = pos.add( direction.getDirectionVec());
			neighbors.put( npos ,getTile( npos));
		}
		return neighbors;
	}
	
	
	
//	public static void setBlock( Block b ,BlockPos pos ,World world) {
//		world.setBlockState( pos ,b.getDefaultState());
//		TEinTE.instance.spawnTileLogicIfNeeded( b ,pos);
//	}
	
	
	

	
	

}




