package teinte.hermetics;

import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class RuleBook {
	
	public final static int PRESSURE_THRESHOLD_A = 10000;
	public final static int PRESSURE_THRESHOLD_B = 15000;
	public final static int DEPOSITION_POINT = 8000;
//	public final static int DIFFUSION_STEP = 100; //per element
	
	
	
	public static final HashSet<Block> BINDERS = new HashSet<Block>();
	public static final HashSet<Block> FOCI = new HashSet<Block>();	
	static {
		BINDERS.add( Blocks.STONE);
		BINDERS.add( Blocks.SANDSTONE);
		BINDERS.add( Blocks.COBBLESTONE);
		BINDERS.add( Blocks.END_STONE);
		BINDERS.add( Blocks.PURPUR_BLOCK);
		BINDERS.add( Blocks.PURPUR_PILLAR);
		BINDERS.add( Blocks.NETHERRACK);
		BINDERS.add( Blocks.OBSIDIAN);
		BINDERS.add( Blocks.PRISMARINE);
		BINDERS.add( Blocks.LOG);
		BINDERS.add( Blocks.LOG2);
		BINDERS.add( Blocks.BONE_BLOCK); //maybe switch
		BINDERS.add( Blocks.GLASS);
		
		//glowstone goes where?
		
		FOCI.add( Blocks.EMERALD_BLOCK);
		FOCI.add( Blocks.REDSTONE_BLOCK);
		FOCI.add( Blocks.LAPIS_BLOCK);
		FOCI.add( Blocks.DIAMOND_BLOCK);
		FOCI.add( Blocks.GOLD_BLOCK);
		FOCI.add( Blocks.IRON_BLOCK);
		FOCI.add( Blocks.QUARTZ_BLOCK);
		FOCI.add( Blocks.COAL_BLOCK);
	}
	
	
	

	
	private RuleBook() {}
	
	
	
	
	
	
	
	
	
}
