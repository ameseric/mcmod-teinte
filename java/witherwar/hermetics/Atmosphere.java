package witherwar.hermetics;

import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import witherwar.MCForge;
import witherwar.TEinTE;
import witherwar.network.ClientFogUpdate;

public class Atmosphere {
	
	private HashMap<ChunkPos,Muir> cells = new HashMap<>();
	
	private HashMap<EntityPlayerMP ,ChunkPos> players = new HashMap<>();
	
	
	
	//iterate through chunks and prepopulate HashMap (ignore dynamic option for now)
	//this should change if world type is Pillar, since sizes can vary. -> do later, for now r = 1024
	//to start, we won't subdivide the cells on the y axis. Just xz.
		
		

	
	
	
	public void _tick( int tickcount) {
	
		//change, store chunk location of players (similar to region) and send update
		//based on that.
		for( EntityPlayerMP player : MCForge.getAllPlayersOnServer()) {
			ChunkPos pos = new ChunkPos( player.getPosition());
			if( !(pos.equals( this.players.get(player)))) { //TODO rewrite for clarity
				this.players.put( player ,pos);
				Vec3d colors = getFogColor( player.getPosition());
				float density = getFogDensity( player.getPosition());
				TEinTE.networkwrapper.sendTo( new ClientFogUpdate( colors ,density) ,player );
			}
		}
	}
	
	
	
	
	public void addCell( ChunkPos pos) {
		this.cells.put( pos ,Muir.empty());
	}
	
	
	public void addPlayer( EntityPlayerMP player) {
		this.players.put( player ,new ChunkPos( player.getPosition()));	
	}
	
	
	public void addMuir( ChunkPos pos ,Muir m) {
		getMuir( pos).add( m);
	}
	
	
	
	public Muir getMuir( ChunkPos pos) {
		return this.cells.get( pos);
	}
	
	
	public Vec3d getFogColor( BlockPos pos) {
		return new Vec3d(0.4 ,0.4 ,0.6);
	}
	
	
	public float getFogDensity( BlockPos pos) {
		return 0.5f;
	}
	
	
	
	public void setupInitialCellMap() {
		BlockPos pos = MCForge.getOverworld().getSpawnPoint();
		for( int x=-544; x<544; x=x+16) {
			for( int z=-544; z<544; z=z+16) {
				this.cells.put( new ChunkPos( pos.add( x ,0 ,z)) ,Muir.empty());
			}
		}
	}
	
	
	

}



