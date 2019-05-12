package witherwar.tileentity;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.WitherWar;

public class TileEntityCataromotus extends TileEntityCustomTickingBase{
	
	private int ticks = 0;
	private int minWarpTime = 120;
	private int maxWarpTime = 600;
	private int timer = 0;
	private boolean timeToWarp = false; //due to timer, player distance, or lack of food
	private ArrayList<TileEntityMaw> maws = new ArrayList<TileEntityMaw>(); 
	private BlockPos[] form = null;
	
	Block deadSkin = WitherWar.newBlocks.get("dead_ash").block;
	ArrayList<String> markedPlayerIDs = new ArrayList<String>(); 
	
	// NBT TAG SUPPORT
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		
		//save timeToWarp, maws, form level, marked player names, timer.
		
		return compound;
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

	}	
	
	
	
	
	
	
	
	public void update() {
		super.update();
		
		//if newly placed, grow Form before everything else.
		
		++ticks;
		if( ticks == 5) {
			ticks = 0;
			++timer;
			
			boolean playerInStrikingDistance = lookAtSurroundings(); //might split into near/far
			checkForWarpConditions( playerInStrikingDistance);
			if( this.timeToWarp) {
				boolean stillFeeding = tellMawsToRetract();
				if( !stillFeeding) {
					warp();
					return;
				}
			}	
			//evolve form after enough time has passed / blocks eaten.
		}
	}
	
	
	private boolean lookAtSurroundings() {
		//check EntityPlayers within cortex reach.
		//check EntityPlayers within sight, mark, message.
		//this.markedPlayerIDs.add( player.getUniqueID());
		//player.sendMessage(new TextComponentString("This is sendmessage example string."));
		return false;
	}
	
	
	private void checkForWarpConditions( boolean danger) {
		if( 	timer > this.maxWarpTime ||
				(timer > this.minWarpTime && !this.isFeeding()) ||
				danger) {
			this.timeToWarp = true;
		}
	}
	
	
	private boolean isFeeding() {
		boolean allRetracted = true;
		for( TileEntityMaw maw : this.maws) {
			allRetracted = allRetracted && maw.branchless();
		}
		return allRetracted;
	}
	
	
	private boolean tellMawsToRetract() {
		boolean allRetracted = true;
		for( TileEntityMaw maw : this.maws) {
			maw.retract();
			allRetracted = allRetracted && maw.branchless();
		}
		return allRetracted;		
	}
	
	
	private void warp() {
		BlockPos warpPos = checkForTargetLocation();
		if( warpPos != null) {
			int choice = this.world.rand.nextInt( 3);
			if( choice < 2) {	warpPos = null;}
		}
		
		if( warpPos == null) {
			//pick random location centered on random player
		}
		this.world.setBlockState( warpPos ,this.blockType.getDefaultState());
		this.destroyForm( true);
		this.playWarpSound( this.pos);
		this.playWarpSound( warpPos);
	}
	
	
	private BlockPos checkForTargetLocation() {
		//check if we have targets
		//if so, randomly select one
		return null;
	}
	
	
	public void destroyForm( boolean noTrace) {
		for( BlockPos pos : this.form) {
			if( noTrace) {
				this.world.setBlockState( pos ,Blocks.AIR.getDefaultState());	
			}else {
				this.world.setBlockState( pos ,this.deadSkin.getDefaultState());
			}
		}
	}
	
	
	private void playWarpSound( BlockPos pos) {
		this.world.playSound( (EntityPlayer)null ,pos ,SoundEvents.ENTITY_ENDERMEN_TELEPORT ,SoundCategory.NEUTRAL ,1.0F ,1.0F );
	}
	
}