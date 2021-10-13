package teinte.faction2.structures;

import java.util.ArrayList;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.template.Template;



public abstract class Structure {
	
	private boolean inert;
	
	
	public abstract ArrayList<Template.BlockInfo> getBlocks() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException;
	

	public abstract BlockPos getSize();
	
	
	public boolean isInert() {
		return this.inert;
	};
	
	
	protected Structure setInert() {
		this.inert = true;
		return this;
	}
	

	
}
