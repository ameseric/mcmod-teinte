package witherwar.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import witherwar.ObjectCatalog;
import witherwar.alchemy.Fluid;
import witherwar.alchemy.FluidContainer;
import witherwar.block.DirectionalBlock;
import witherwar.block.FluidContainerBlock;

public class RitualBlockEntity extends BlockEntity implements FluidContainer {

	
	private Fluid fuelType = new Fluid();
	private Fluid contents;
	
	private EnumFacing facing;
	private BlockPos inputPos;
	private BlockPos outputPos;
	private BlockPos fuelPos;
	
	
	public RitualBlockEntity(BlockPos pos ,World world) {
		super(pos, ObjectCatalog.FLESH ,1); 
		this.facing = world.getBlockState( this.getPos()).getValue( DirectionalBlock.FACING);
		this.outputPos = new BlockPos( this.facing.rotateY().getDirectionVec()).add( this.getPos());
		this.inputPos = new BlockPos( this.facing.rotateYCCW().getDirectionVec()).add( this.getPos());
		this.fuelPos = new BlockPos( this.facing.getDirectionVec()).add( this.getPos());
	}

	
	
	public Fluid pullFluid(FluidContainer requester, BlockPos pos, World world) {
		
		//TODO: Consume fuel
		//TODO: check if ready to pass fluid, if still working
		
		Block inputBlock = world.getBlockState( this.inputPos).getBlock();
		Fluid input = new Fluid();
		if( inputBlock instanceof FluidContainerBlock) {
			input.add( ((FluidContainerBlock) inputBlock).pullFluid(requester, this.inputPos, world));
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

	
	public Fluid cycleFluid( Fluid input) {
		Fluid output = this.contents;
		this.contents = input;
		return output;
	}

	
	public String getDataName() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void ticklogic( World world) {
//		IBlockState a = world.getBlockState( this.getPos());
//		
//		System.out.println( a.getValue( DirectionalBlock.FACING));
		
	}
	
	
	
	public BlockPos getOutputPos( World world) {
		return this.outputPos;
	}
	
	
	public BlockPos getInputPos( World world) {
		return this.inputPos;
	}
	
	

	
	
	
	
	
	
}
