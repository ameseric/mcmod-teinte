package witherwar.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import witherwar.ObjectCatalog;
import witherwar.TEinTE;
import witherwar.alchemy.Fluid;
import witherwar.alchemy.FluidContainer;
import witherwar.block.DirectionalBlock;
import witherwar.block.FluidContainerBlock;

public class RitualBlockTile extends FluidContainerTile {

	
	private EnumFacing facing;
	private BlockPos inputPos;
	private BlockPos outputPos;
	private BlockPos fuelPos;
	
	private final int WORKTICKLENGTH = 4;
	private int worktick = 0;
	private boolean busy = false;
	

	private Fluid fuelType = new Fluid();
	
	
	public RitualBlockTile(BlockPos pos ,World world) {
		super(pos, ObjectCatalog.RITUALBLOCK ,TileLogic.RITUALBLOCK_ID ,true); 
		this.facing = world.getBlockState( this.getPos()).getValue( DirectionalBlock.FACING);
		this.outputPos = new BlockPos( this.facing.rotateY().getDirectionVec()).add( this.getPos());
		this.inputPos = new BlockPos( this.facing.rotateYCCW().getDirectionVec()).add( this.getPos());
		this.fuelPos = new BlockPos( this.facing.getDirectionVec()).add( this.getPos());
	}


	
	public void ticklogic( World world) {
		if( this.canWork( world)) {
			this.performWork();
		}else if( this.hasNoOutput( world)) {
			Fluid f = this.pullFluid( this.getPos() ,this.getPos() ,world);
			System.out.println( f);
		}
	}
	
	
	@Override
	public Fluid pullFluid(BlockPos requesterPos, BlockPos myPos, World world) {
		if( !requesterPos.equals( this.outputPos) || !this.hasInputProduct()) {
			return Fluid.empty();
		}
		
		
		Block inputBlock = world.getBlockState( this.inputPos).getBlock();
		Fluid input = new Fluid();
		if( inputBlock instanceof FluidContainer) {
			input.add( ((FluidContainer) inputBlock).pullFluid(requesterPos, this.inputPos, world));
		}

		return this.cycleFluid(input);
	}
	
	
	
	public Fluid pullFuel() {
		return null;
	}	
	
	
	private void performWork() {
		this.pullFuel();
		this.worktick += 1;
		if( this.worktick == this.WORKTICKLENGTH) {
			this.busy = false;
		}
	}
	
	
	private void operationSplit() {
		
	}
	
	
	private void operationFuseTrinary() {
		
	}
	
	
	
	public boolean hasFuel( World world) {
		TileLogic tl = TEinTE.instance.getTileLogic( this.fuelPos);
		if( tl instanceof FluidContainerTile) {
			return ((FluidContainerTile) tl).getContents().equals( this.fuelType);
		}
		return false;
	}	
	
	public boolean canWork( World world) {
		return this.hasFuel( world) && this.hasInputProduct();
	}

	public boolean hasFluid() {
		return false;
	}
	
	
	public boolean hasInputProduct() {
		return true;
	}
	
	
	public boolean hasNoOutput( World world) {
		return !( world.getBlockState( this.outputPos).getBlock() instanceof FluidContainerBlock);
	}

	
	
	public Fluid cycleFluid( Fluid input) {
		Fluid output = this.getContents();
		this.setContents( input);
		return output;
	}

	
	public String getDataName() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	public BlockPos getOutputPos( World world) {
		return this.outputPos;
	}
	
	
	public BlockPos getInputPos( World world) {
		return this.inputPos;
	}


	
	

	
	
	
	
	
	
}
