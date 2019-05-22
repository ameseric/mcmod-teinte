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
import witherwar.util.Symbol;




/**
 * 
 * @author Guiltygate
 *
 * Can be used without Chunk loading by not calling super.update()
 * 
 */
public abstract class TileEntityCustomTickingBase extends TileEntity implements ITickable{
	
	
	protected Ticket ticket;
	private boolean triedToAssignTicket = false;
	
	protected interface BlockFilter{
		boolean scan( Block bs);
	}
	
	
	
	
	//GENERIC BLOCK METHODS	

	protected BlockPos searchForTouchingBlock( BlockPos currentPos ,BlockFilter returnBlock ,BlockFilter traversableBlock ,int depth ,boolean random) {
		
		ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
		positions.add( currentPos);
		
		Symbol[] values;
		if( random) {
			values = Symbol.randomValues();
		}else {
			values = Symbol.values();
		}
		
		return searchForTouchingBlock( positions ,returnBlock ,traversableBlock ,0 ,depth ,values);
	}
	
	
	
	private BlockPos searchForTouchingBlock( ArrayList<BlockPos> positions ,BlockFilter returnBlock ,BlockFilter traversableBlock 
			,int currentDepth ,final int maxDepth ,Symbol[] values) {
		BlockPos dead = new BlockPos(0,0,0);
		if( currentDepth >= maxDepth || positions.size() <= currentDepth) { 
			return dead;}
		
		HashMap<BlockPos,Block> map = getNeighborBlocks( positions.get( currentDepth) ,values);
		
		for( BlockPos pos : map.keySet()) {
			Block b = map.get(pos);
			if( traversableBlock.scan(b)) {
				if( returnBlock.scan( b)) {
					return pos;
				}else if( !positions.contains( pos)) { //inefficient, think about using HashSet?
					positions.add( pos);
				}
			}
		}
		
		return searchForTouchingBlock( positions ,returnBlock ,traversableBlock ,++currentDepth ,maxDepth ,values);
	}
	
	
	protected boolean onlyTouchingBlockTypes( BlockPos currentPos ,Block[] acceptedBlocks) {
		HashMap<BlockPos,Block> map = getNeighborBlocks( currentPos);
		
		for( Block b : map.values()) {
			if( !isOfBlockSet( b ,acceptedBlocks)) {
				return false;
			}
		}
		return true;
	}
	

	
	public HashMap<BlockPos,Block> getNeighborBlocks( BlockPos pos){
		return getNeighborBlocks( pos ,Symbol.values());
	}
	
	public HashMap<BlockPos,Block> getNeighborBlocks( BlockPos pos ,Symbol[] array){
		HashMap<BlockPos,Block> map = new HashMap<BlockPos,Block>();
		
		for( Symbol direction : array) {
			BlockPos currentPos = pos.add( direction.mod);
			map.put( currentPos ,this.world.getBlockState( currentPos).getBlock());
		}
		return map;
	}
	
	
	
	
	protected boolean isOfBlockSet( Block b ,ArrayList<Block> list) {
		for( Block setB : list) {
			if( b == setB) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean isOfBlockSet( Block b ,Block[] array) {
		return isOfBlockSet( b ,new ArrayList<Block>( Arrays.asList( array)));
	}
	
	protected boolean isOfBlockSet( Block b ,HashMap<Block ,BlockPos> map) {		
		return isOfBlockSet( b ,new ArrayList<Block>( map.keySet()));
	}
	
	
	
	
	
	
	protected Symbol[] getPath( BlockPos a ,BlockPos b) {
		int x = b.getX() - a.getX();
		int y = b.getY() - a.getY();
		int z = b.getZ() - a.getZ();
//		int totalPool = Math.abs(x) + Math.abs(y) + Math.abs(z);

		ArrayList<Symbol> path = new ArrayList<Symbol>();
		
		if( x > 0) {
			for( int i=0; i<x; i++) {
				path.add( Symbol.XP);
			}
		}else {
			for( int i=0; i>x; i--) {
				path.add( Symbol.XN);
			}			
		}
		
		if( y > 0) {
			for( int i=0; i<y; i++) {
				path.add( Symbol.YP);
			}
		}else {
			for( int i=0; i>y; i--) {
				path.add( Symbol.YN);
			}			
		}
		
		if( z > 0) {
			for( int i=0; i<z; i++) {
				path.add( Symbol.ZP);
			}
		}else {
			for( int i=0; i>z; i--) {
				path.add( Symbol.ZN);
			}			
		}

		/**
		Symbol[] path = new Symbol[totalPool];
		Random rand = new Random();
		int choice;
		
		Symbol dirX = Symbol.XP;
		if( x <= 0) { 
			dirX = Symbol.XN;
			x = Math.abs(x);
		}
		
		Symbol dirY = Symbol.YP;
		if( y <= 0) { 
			dirY = Symbol.YN;
			y = Math.abs(y);
		}
		
		Symbol dirZ = Symbol.ZP;
		if( z <= 0) { 
			dirZ = Symbol.ZN;
			z = Math.abs(z);
		}
		
		
		while( totalPool > 0) {
			--totalPool;
			choice = rand.nextInt(3);

			while( true) {
				if( choice == 0) {
					if( x > 0) {
						path[totalPool] = dirX;
						--x;
						break;
					}else {	choice = 1;	}
				}
				
				if( choice == 1) {
					if( y > 0) {
						path[totalPool] = dirY;
						--y;
						break;
					}else { choice = 2;}
				}
				
				if( choice == 2) {
					if( z > 0) {
						path[totalPool] = dirZ;
						--z;
						break;
					}else { choice = 0;}
				}
			}
		}**/
		Collections.shuffle( path);
		return  path.toArray( new Symbol[ path.size()]);
	}
	
	
	
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