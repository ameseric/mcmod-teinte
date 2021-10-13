package teinte.system;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import teinte.ObjectCatalog;

public class InvasionSystem {

	
	private final double SPAWN_INC = 0.0001;
	private double spawnChance = SPAWN_INC;
	

/*
 * 
 *   ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(MobEffects.WITHER, 200));
 *    
 * 
 */
	
	
	
	public InvasionSystem() {
	}
	
	
	
	
	public void tick( int tickcount ,WorldServer world) {
		if( tickcount % 100 != 0) { return; }
		double rand = Math.random();
		System.out.println( "Spawn chance: " + this.spawnChance + " " + rand);

		
		if( this.spawnChance > rand) {
			this.placeInvader( world);
			this.spawnChance = this.SPAWN_INC;
			
		}else {
			this.spawnChance += this.SPAWN_INC;
		}
		
		
	}
	
	
	
	
	private void placeInvader( WorldServer world) {
		int y = 1;
		long x = Math.round(900 * Math.random());
		long z = Math.round( 900 * Math.random());

		System.out.println( "SPAWNING..." + x + " " + y + " " + z);
		
		world.setBlockState( new BlockPos(x ,y ,z) ,ObjectCatalog.TERRA_KALI.getDefaultState());
		
	}
	
	
	
	
}
