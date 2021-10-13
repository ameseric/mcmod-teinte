package teinte.tilelogic;

import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teinte.MCForge;
import teinte.ObjectCatalog;
import teinte.TEinTE;
import teinte.hermetics.ElementalFluidContainer;
import teinte.hermetics.Muir;
import teinte.hermetics.MuirElement;
import teinte.hermetics.MuirGasContainer;
import teinte.hermetics.RuleBook;
import teinte.utility.VectorUtility;

public class RitualBlockTile extends MuirGasContainerTile {

	

	private static final int MAX_STABILTY = 100;
	

	
	private EnumFacing facing;
	private BlockPos inputPos;
	private BlockPos outputPos;
	private BlockPos fuelPos;	
	private Muir fuelType = new Muir();
	private int stability = MAX_STABILTY;
	
	
	
	//may change every tick call
	private int primaryFocusHeight = 0;
	private int secondaryFocusHeight = 0;
	private int focusRadius = 0;
	private int binderDiagonal = 0;
	
	
	
	
	
	
	
	public RitualBlockTile() {
		this( new BlockPos(0,0,0) ,EnumFacing.NORTH);
	}	
	public RitualBlockTile(BlockPos pos ,EnumFacing facing) {
		super(pos, ObjectCatalog.RITUALBLOCK ,true ,100);
		this.facing = facing;
		setupExternalPositions();
	}



	
	public void _ticklogic( World world) {
		calculateRitualValues();
		if( this.primaryFocusHeight > 0) {
			performOperation();
			explodeIfInvalidStructure();
			if( hasOutput()) {
				output().add( contents());
			}
		}
	}
	
	
	@Override
	public void averageWith( Muir m ,BlockPos requester) {
		if( requester.equals( inputPos())) {
			super.averageWith( m ,requester);
		}
	}
	
	
//	@Override
//	public void add( Muir m) {
//		if( requester.equals( inputPos())) {
//			super.add( m);
//		}
//	}
	
	
	

	
	
	
	public String getDataName() {
		return null;
	}
	
	
	
	
//	
//	
//	private boolean hasFuel( World world) {
//		TileLogic tl = TEinTE.instance.getTileLogic( this.fuelPos);
//		if( tl instanceof MuirGasContainer) {
//			return ((MuirGasContainer) tl).contents().equals( this.fuelType);
//		}
//		return false;
//	}	
	
	
	

	
	private boolean hasInput() {
		return MCForge.getTileLogic( inputPos()) instanceof ElementalFluidContainer;
	}
	
	
	private boolean hasOutput() {
		return MCForge.getTileLogic( outputPos()) instanceof ElementalFluidContainer;
	}
	
	
	private MuirGasContainer input() {
		return (MuirGasContainer) MCForge.getTileLogic( inputPos());
	}
	
	
	private MuirGasContainer output() {
		return (MuirGasContainer) MCForge.getTileLogic( outputPos());
	}
	
	
	private BlockPos outputPos() {
		return this.outputPos;
	}
	
	
	private BlockPos inputPos() {
		return this.inputPos;
	}
	
	
	private void calculateRitualValues() {
		setValuesInvalid();
		calculateLocusHeight();
		if( this.primaryFocusHeight > 0) {
			calculateLocusRadius();
			calculateDiagonalRadius();
		}
	}
	
	
	//TODO allow multiple locus values, and only pick the last one as the "primary" to determine height & fn
	private void calculateLocusHeight() {
		BlockPos locusPos = pos().add( EnumFacing.UP.getDirectionVec());
		Block b = MCForge.getBlock( locusPos);
		if(!RuleBook.BINDERS.contains( b)) {
			return;
		}		
		
		boolean foundSecondary = false;
		while( true) {
			
			if( locusPos.getY() > 250 || VectorUtility.distance( locusPos , pos()) > 100 ) {
				return;
			}else if( RuleBook.BINDERS.contains(b)) {
				locusPos = locusPos.add( EnumFacing.UP.getDirectionVec());
			}else if( RuleBook.FOCI.contains( b)) {
				if( !foundSecondary) {
					this.secondaryFocusHeight = locusPos.getY() - pos().getY();
					foundSecondary = true;
					locusPos = locusPos.add( EnumFacing.UP.getDirectionVec());
				}else {
					this.primaryFocusHeight = locusPos.getY() - pos().getY();
					break;
				}
			}else {
				return;
			}
			
			b = MCForge.getBlock( locusPos);
		}
	}
	
	
	
	private void calculateLocusRadius() {
		BlockPos searchPos = pos().add( 0 ,this.secondaryFocusHeight ,1);
		Block b = MCForge.getBlock( searchPos);
		
		while( true) {
			if( VectorUtility.distance( searchPos , pos()) > 20) {
				return;
			}else if( RuleBook.BINDERS.contains(b)) {
				searchPos = searchPos.add( EnumFacing.SOUTH.getDirectionVec());
			}else if( RuleBook.FOCI.contains( b)) {
				this.focusRadius = searchPos.getZ() - posZ();
				break;
			}else {
				return;
			}
			
			b = MCForge.getBlock( searchPos);
		}

		//followup foci checks for symmetry
		
	}
	
	
	
	private void calculateDiagonalRadius() {
		int searchLevel = TEinTE.RNG.nextInt( this.primaryFocusHeight-1) + 1;
		BlockPos searchPos = pos().add( 1 ,searchLevel ,1);
		Block b = MCForge.getBlock( searchPos);
		
		while( true) {
			
			if( VectorUtility.distance( searchPos , pos()) > 20) {				
				System.out.println( "Too far away.");
				break;
			}else if( RuleBook.BINDERS.contains(b)) {
				System.out.println( "Iterating in...");
				searchPos = searchPos.add( EnumFacing.SOUTH.getDirectionVec()).add( EnumFacing.EAST.getDirectionVec());
			}else {
				System.out.println( "Non-binder, quitting.");
				this.binderDiagonal = searchPos.getZ() - posZ();
				break;
			}
			
			b = MCForge.getBlock( searchPos);			
		}

		
		//followup diagonal checks
	}
	
	
	//I think we'll have Stability, Magnitude, and Efficiency?
	private void calculateStability() {
		
	}
	
	

	
	
	private void setValuesInvalid() {
		this.primaryFocusHeight = 0;
		this.secondaryFocusHeight = 0;
		this.focusRadius = 0;
		this.binderDiagonal = 0;
	}
	
	
	private void explodeIfInvalidStructure() {
		int r = TEinTE.instance.RNG.nextInt(100);
		if( r > this.stability) {
			MCForge.getOverworldServer().createExplosion( null ,posX() ,posY()+this.primaryFocusHeight-2 ,posZ(), 4.0F, true);
			//should explosion location change based on stability issue?
			//need to record stabilty issue then
		}
	}
	
	
	
//	private void blockIterate( Vec3i direction ,int limit ,BlockPos start) {
//		
//		while( true) {
//			
//			if( start.getY() > limit) {
//				
//			}
//			
//		}
//		
//	}
	
	
	
	
	
	
	
	private Muir pullFuel() {
		return null;
	}

	
	
	private void operationSplit() {
		MuirElement a = MuirElement.A;
		contents().subtract( a ,10);
	}
	
	
	
	private void performOperation() {
		operationSplit();
	}
	
	
	
	
	private void setupExternalPositions() {
		this.outputPos = new BlockPos( this.facing.rotateY().getDirectionVec()).add( this.pos());
		this.inputPos = new BlockPos( this.facing.rotateYCCW().getDirectionVec()).add( this.pos());
		this.fuelPos = new BlockPos( this.facing.getDirectionVec()).add( this.pos());
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
