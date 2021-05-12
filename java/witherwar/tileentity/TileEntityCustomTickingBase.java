package witherwar.tileentity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import witherwar.TEinTE;
import witherwar.utility.Symbol;




/**
 * 
 * @author Guiltygate
 *
 * Can be used without Chunk loading by not calling super.update()
 * 
 * In the process of tearing this apart, most functions are being ripped into a separte block searcher class.
 * 
 */
public abstract class TileEntityCustomTickingBase extends TileEntity implements ITickable{	
	
	protected Ticket ticket;
	private boolean triedToAssignTicket = false;	
	
	
	
	public EntityPlayer getRandomPlayer() {
		List<EntityPlayer> players = world.playerEntities;
		EntityPlayer player = null;
		if( players.size() > 0) {
			int choice = world.rand.nextInt( players.size());
			player = players.get(choice);
		}
		return player;
	}
	
	
	public BlockPos getRandomPlayerLocation() {
		return this.getRandomPlayer().getPosition();
	}
	
	
	
	// Pretty particles
	// doesn't work right now?
	public void castParticle() {
		this.world.spawnParticle( EnumParticleTypes.SPELL_WITCH, this.pos.getX() ,this.pos.getY(),this.pos.getZ() ,0 ,0 ,0);
	}
	
	
	public void playSound( BlockPos pos ,SoundEvent se) {
		this.world.playSound( (EntityPlayer)null ,pos ,se ,SoundCategory.NEUTRAL ,1.0F ,1.0F );
	}
	
	
	
	
	
	
	// CHUNK LOADING SUPPORT
	@Override
	public void invalidate() {
		ForgeChunkManager.releaseTicket( ticket);
		super.invalidate();
	}
	
	
	public void forceChunkLoading( Ticket ticket) {
		if( this.ticket == null)
			this.ticket = ticket;
		ChunkPos chunk = new ChunkPos( this.pos.getX() >> 4 ,this.pos.getZ() >> 4);
		ForgeChunkManager.forceChunk( ticket ,chunk);
	}
	
	
	//call with super() if you want ChunkLoading
	@Override
	public void update() {
		if (!this.hasWorld() || world.isRemote) return; // no client-side work

		
		if( !triedToAssignTicket){
			triedToAssignTicket = true;
			if(ticket==null)
				ticket = ForgeChunkManager.requestTicket( TEinTE.instance ,world ,Type.NORMAL);
			if(ticket==null)
				System.out.println( "No tickets left!");
			else {
				ticket.getModData().setInteger("mx" ,this.pos.getX());
				ticket.getModData().setInteger("my" ,this.pos.getY());
				ticket.getModData().setInteger("mz" ,this.pos.getZ());
				ForgeChunkManager.forceChunk( ticket ,new ChunkPos( this.pos.getX()>>4 ,this.pos.getZ()>>4));
			}
		}
		
	}	
	
	
	
	
}