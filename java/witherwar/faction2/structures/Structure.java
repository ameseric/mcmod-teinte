package witherwar.faction2.structures;

import java.util.ArrayList;
import net.minecraft.world.gen.structure.template.Template;



public abstract class Structure {

	
	
	public abstract ArrayList<Template.BlockInfo> getBlocks() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException;
	
	
	
}
