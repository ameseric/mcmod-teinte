package witherwar.entity;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import witherwar.faction2.Faction2;

public class FactionEntityLiving extends EntityLiving{

	private int cost = 10;
	private ArrayList<Block> heldblocks = new ArrayList<>();
	private Faction2 faction;
	
	
	
	public FactionEntityLiving(WorldServer world ,Faction2 faction) {
		super(world);
		this.faction = faction;
	}
	
	
	
	public void factionTick( int tickcount ,WorldServer world) {
		System.out.println( this.isLoaded(world));
		
		this.pickTask();
		
		if( this.isLoaded(world)){
			return; //do not perform any tasks, allow MC to tick
		}
		
		
		
	}
	
	
	
	public void pickTask() {
		
	}
	
	
	
	public void setCost( int cost) {
		this.cost = cost;
	}
	
	public int getCost() {
		return this.cost;
	}
	
	
	
	public void setPosition( Vec3d v) {
		this.setPosition( v.x ,v.y ,v.z);
	}
	
	
	
	public Faction2 getFaction() {
		return this.faction;
	}
	
	
	
	public boolean isLoaded( WorldServer world) {
		return world.isBlockLoaded( this.getPosition());
	}
	

}
