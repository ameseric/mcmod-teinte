package witherwar.faction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import witherwar.entity.EntityFactionFlying;

public class UnitCategory {
	
	public HashMap< Object ,List<Entity>> jobAssignments;
	private ArrayList< Entity> units;
	
	public UnitCategory() {
		
	}
	
	public void add( EntityFactionFlying e){
		this.units.add(e);
	}
	
	
	
	public int size() {
		return this.units.size();
	}
	
}
