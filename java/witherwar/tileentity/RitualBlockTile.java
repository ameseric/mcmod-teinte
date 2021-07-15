package witherwar.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import witherwar.ObjectCatalog;
import witherwar.TEinTE;
import witherwar.block.DirectionalBlock;
import witherwar.block.FluidContainerBlock;
import witherwar.hermetics.ElementalFluid;
import witherwar.hermetics.ElementalFluidContainer;

public class RitualBlockTile extends ElementalFluidContainerTile {

	
	private EnumFacing facing;
	private BlockPos inputPos;
	private BlockPos outputPos;
	private BlockPos fuelPos;
	
	private final int WORKTICKLENGTH = 4;
	private int worktick = 0;
	private boolean busy = false;
	

	private ElementalFluid fuelType = new ElementalFluid();
	
	
	public RitualBlockTile(BlockPos pos ,World world) {
		super(pos, ObjectCatalog.RITUALBLOCK ,TileLogic.RITUALBLOCK_ID ,true ,1); 
		this.facing = world.getBlockState( this.getPos()).getValue( DirectionalBlock.FACING);
		this.outputPos = new BlockPos( this.facing.rotateY().getDirectionVec()).add( this.getPos());
		this.inputPos = new BlockPos( this.facing.rotateYCCW().getDirectionVec()).add( this.getPos());
		this.fuelPos = new BlockPos( this.facing.getDirectionVec()).add( this.getPos());
	}


	
	public void _ticklogic( World world) {
		if( canWork( world)) {
			performWork();
		}else if( hasNoOutput( world)) {
			ElementalFluid f = _takeFluid( getPos() ,getPos() ,world);
			System.out.println( f);
		}
	}
	
	
	@Override
	public ElementalFluid _takeFluid(BlockPos requesterPos, BlockPos myPos, World world) {
		if( !requesterPos.equals( this.outputPos) || !hasInputProduct()) {
			return ElementalFluid.empty();
		}
		
		
		Block inputBlock = world.getBlockState( this.inputPos).getBlock();
		ElementalFluid input = new ElementalFluid();
		if( inputBlock instanceof ElementalFluidContainer) {
			input.add( ((ElementalFluidContainer) inputBlock)._takeFluid(requesterPos, this.inputPos, world));
		}

		return _cycleFluid(input);
	}
	
	
	
	
	
	
	
	
	
	public boolean hasFuel( World world) {
		TileLogic tl = TEinTE.instance.getTileLogic( this.fuelPos);
		if( tl instanceof ElementalFluidContainerTile) {
			return ((ElementalFluidContainerTile) tl).peekAtContents().equals( this.fuelType);
		}
		return false;
	}	
	
	
	public boolean canWork( World world) {
		return hasFuel( world) && hasInputProduct();
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
	
	
	
	
	
	
	
	
	
	
	private ElementalFluid pullFuel() {
		return null;
	}
	
		
	private void performWork() {
		pullFuel();
		this.worktick += 1;
		if( this.worktick == this.WORKTICKLENGTH) {
			this.busy = false;
		}
	}
	
	
	private void operationSplit() {
		
	}
	
	
	private void operationFuseTrinary() {
		
	}
	
	
	


	
	


	


	@Override
	public void readFromNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		
	}


	
	

	
	
	
	
	
	
}
