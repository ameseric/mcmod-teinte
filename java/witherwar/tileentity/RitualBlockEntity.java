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
import witherwar.alchemy.Fluid;
import witherwar.alchemy.FluidContainer;
import witherwar.block.DirectionalBlock;
import witherwar.block.FluidContainerBlock;

public class RitualBlockEntity extends FluidContainerBlockEntity {

	
	private Fluid fuelType = new Fluid();
	
	private EnumFacing facing;
	private BlockPos inputPos;
	private BlockPos outputPos;
	private BlockPos fuelPos;
	
	
	public RitualBlockEntity(BlockPos pos ,World world) {
		super(pos, ObjectCatalog.RITUALBLOCK ,BlockEntity.RITUALBLOCK_ID ,true); 
		this.facing = world.getBlockState( this.getPos()).getValue( DirectionalBlock.FACING);
		this.outputPos = new BlockPos( this.facing.rotateY().getDirectionVec()).add( this.getPos());
		this.inputPos = new BlockPos( this.facing.rotateYCCW().getDirectionVec()).add( this.getPos());
		this.fuelPos = new BlockPos( this.facing.getDirectionVec()).add( this.getPos());
	}


	
	public void ticklogic( World world) {
		if( this.hasNoOutput( world)) {
			Fluid f = this.pullFluid( this.getPos() ,this.getPos() ,world);
			System.out.println( f.getElements());
		}
	}
	
	
	@Override
	public Fluid pullFluid(BlockPos requesterPos, BlockPos myPos, World world) {
		//TODO: Consume fuel
		//TODO: check if ready to pass fluid, if still working
		
		Block inputBlock = world.getBlockState( this.inputPos).getBlock();
		Fluid input = new Fluid();
		if( inputBlock instanceof FluidContainer) {
			input.add( ((FluidContainer) inputBlock).pullFluid(requesterPos, this.inputPos, world));
		}

		return this.cycleFluid(input);
	}
	
	
	private Fluid getFuel() {
		return new Fluid();
	}
	

	
	public boolean hasFuel() {
		return true;
	}

	

	public boolean hasFluid() {
		// TODO Auto-generated method stub
		return false;
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
