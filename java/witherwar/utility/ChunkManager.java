package witherwar.utility;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager.OrderedLoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import witherwar.tileentity.deprecated.TileEntitySerpentmind;


/**
 * 
 * @author Pancake
 * 
 * Taken from Pancake's code: http://www.minecraftforge.net/forum/topic/25632-solved-1710-chunkloading-block/
 * They, in turn, adapted it from ( I believe ) the Quarry object in BuildCraft.
 *
 */
public class ChunkManager implements OrderedLoadingCallback{

	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world) {
		for (Ticket ticket : tickets) {
			int mx = ticket.getModData().getInteger("mx");
			int my = ticket.getModData().getInteger("my");
			int mz = ticket.getModData().getInteger("mz");
			TileEntitySerpentmind entity = (TileEntitySerpentmind) world.getTileEntity( new BlockPos( mx ,my ,mz));
			entity.forceChunkLoading(ticket);
		}
	}
	
	@Override
	public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount) {
		List<Ticket> validTickets = new ArrayList();
		for (Ticket ticket : tickets) {
			int mx = ticket.getModData().getInteger("mx");
			int my = ticket.getModData().getInteger("my");
			int mz = ticket.getModData().getInteger("mz");
			IBlockState blockstate = world.getBlockState( new BlockPos( mx ,my ,mz));
//			if (block == Singularity.BlockMainframeCore)
//				validTickets.add(ticket);
		}
		return validTickets;
	}
}