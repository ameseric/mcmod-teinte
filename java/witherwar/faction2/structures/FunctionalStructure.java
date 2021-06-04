package witherwar.faction2.structures;

import java.lang.reflect.Field;
import java.util.ArrayList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.Template;
import witherwar.TEinTE;
import witherwar.tileentity.TileLogic;
import witherwar.tileentity.TileLogicContainer;
import witherwar.utility.Tickable;

public abstract class FunctionalStructure extends Structure implements Tickable{

	protected Template template;
	
	
	
	public FunctionalStructure( Template template){
		this.template = template;
	}
	
	
	public FunctionalStructure( String name) {
		this.template = TEinTE.instance.getTemplate( name);
	}
	
	
	

	
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<Template.BlockInfo> getBlocks() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		Field f = this.template.getClass().getDeclaredField("blocks");
		f.setAccessible(true);
		return (ArrayList<Template.BlockInfo>) f.get( this.template);
	}

	
	

	@Override
	public boolean isDead() {
		return false;
	}
	
	
	@Override
	public BlockPos getSize() {
		return this.template.getSize();
	}


	@Override
	public void tick(int tickcount, WorldServer world) {
				
	}
	
	
	
}
