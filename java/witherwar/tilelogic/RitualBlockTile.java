package witherwar.tilelogic;

import java.util.HashSet;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.MCForge;
import witherwar.ObjectCatalog;
import witherwar.TEinTE;
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
	
	
	static {
		TileLogicManager.registerClass( new AlchemyGeyserTile());
	}
	
	
	
	
	public RitualBlockTile() {
		this( new BlockPos(0,0,0) ,EnumFacing.NORTH);
	}	
	public RitualBlockTile(BlockPos pos ,EnumFacing facing) {
		super(pos, ObjectCatalog.RITUALBLOCK ,true ,20);
		this.facing = facing;
		setupExternalPositions();
	}



	
	public void _ticklogic( World world) {
		if( isEndOfChain()) {
			System.out.println( "triggering pull...");
			HashSet<BlockPos> traversed = new HashSet<BlockPos>();
			traversed.add( getPos());
			ElementalFluid f = getInput()._takeFluid( getPos() ,traversed);
			_cycleFluid( f);
		}
		System.out.println( this.peekAtContents());
	}
	
	
	@Override
	public ElementalFluid _takeFluid(BlockPos requesterPos ,HashSet<BlockPos> traversed) {
		if( !requesterPos.equals( getOutputPos()) || traversed.contains(getPos())) {
			return ElementalFluid.empty();
		}
		traversed.add( getPos());
		
		ElementalFluid input = ElementalFluid.empty();
		if( hasInput()) {
			 input = getInput()._takeFluid( getPos() ,traversed);
		}

		return _cycleFluid(input);
	}
	
	
	
	
	public String getDataName() {
		return null;
	}
	
	
	
	
	private boolean hasFuel( World world) {
		TileLogic tl = TEinTE.instance.getTileLogic( this.fuelPos);
		if( tl instanceof ElementalFluidContainer) {
			return ((ElementalFluidContainer) tl).peekAtContents().equals( this.fuelType);
		}
		return false;
	}	
	
	
	private boolean canWork( World world) {
		return hasFuel( world) && hasInput();
	}

	
	private boolean hasFluid() {
		return false;
	}
	
	
	private boolean hasInput() {
		return MCForge.getTileLogic( getInputPos()) instanceof ElementalFluidContainer;
	}
	
	
	private boolean hasOutput() {
		return MCForge.getTileLogic( getOutputPos()) instanceof ElementalFluidContainer;
	}
	
	
	private ElementalFluidContainer getInput() {
		return (ElementalFluidContainer) MCForge.getTileLogic( getInputPos());
	}
	
	
	private ElementalFluidContainer getOutut() {
		return (ElementalFluidContainer) MCForge.getTileLogic( getOutputPos());
	}
	
	
	private BlockPos getOutputPos() {
		return this.outputPos;
	}
	
	
	private BlockPos getInputPos() {
		return this.inputPos;
	}
	
	
	private boolean isEndOfChain() {
		return hasInput() && !hasOutput();
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
	
	
	
	
	private void setupExternalPositions() {
		this.outputPos = new BlockPos( this.facing.rotateY().getDirectionVec()).add( this.getPos());
		this.inputPos = new BlockPos( this.facing.rotateYCCW().getDirectionVec()).add( this.getPos());
		this.fuelPos = new BlockPos( this.facing.getDirectionVec()).add( this.getPos());
	}
	


	
	


	



	@Override
	protected NBTTagCompound __writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger( "facing" ,this.facing.getIndex());
		return nbt;
	}



	@Override
	protected void __readFromNBT(NBTTagCompound nbt) {
		this.facing = EnumFacing.VALUES[ nbt.getInteger( "facing")];
		setupExternalPositions();
	}


	
	

	
	
	
	
	
	
}
