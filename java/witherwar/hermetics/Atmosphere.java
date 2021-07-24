package witherwar.hermetics;

import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import witherwar.MCForge;
import witherwar.TEinTE;
import witherwar.network.ClientFogUpdate;

public class Atmosphere {
	
	private HashMap<ChunkPos,Muir> cells = new HashMap<>();
	
	private HashMap<EntityPlayerMP ,ChunkPos> players = new HashMap<>();
	
	public static final float MAX_DEFAULT = 10000;
	public static final Vec3d DEFAULT_COLOR = new Vec3d( 0.95 ,0.95 ,0.95);
	
	private int bound = 35;
	private int xIndex = -bound;
	private int zIndex = -bound;
	private int interval = 10;
	
	
	
	//iterate through chunks and prepopulate HashMap (ignore dynamic option for now)
	//this should change if world type is Pillar, since sizes can vary. -> do later, for now r = 1024
	//to start, we won't subdivide the cells on the y axis. Just xz.
		
		

	
	
	
	public void _tick( int tickcount) {
	
		//change, store chunk location of players (similar to region) and send update
		//based on that.
		ChunkPos ppos = null;
		if( tickcount%10 == 0) {
			for( EntityPlayerMP player : MCForge.getAllPlayersOnServer()) {
				ppos = new ChunkPos(player.getPosition());
//				ChunkPos pos = new ChunkPos( player.getPosition());
//				if( !(pos.equals( this.players.get(player)))) { //TODO rewrite for clarity
//					this.players.put( player ,pos);
					Vec3d colors = getFogColor( player.getPosition());
					float density = getFogDensity( player.getPosition());
					TEinTE.networkwrapper.sendTo( new ClientFogUpdate( colors ,density) ,player );
					System.out.println( getMuir( player.getPosition()));
//					System.out.println( colors);
//					System.out.println( density);
//				}
			}
		}
		for( int x=this.xIndex; x<this.xIndex+1; x++) {
			for( int z=this.zIndex; z<this.zIndex+this.interval; z++) {
				averageCell( x ,z);
				if( ppos != null && ppos.x == x && ppos.z == z) {
					System.out.println( "Averaging your values...");
					System.out.println( getMuir(ppos));
				}
			}
		}
		this.zIndex += this.interval;		
		if( this.zIndex > this.bound) {
			this.zIndex = -this.bound;
			this.xIndex++;
		}

		if( this.xIndex > this.bound) {
			this.xIndex = -this.bound;
			System.out.println( "Full cycle.");
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
	
	public Muir getMuir( BlockPos pos) {
		return getMuir( new ChunkPos( pos));
	}
	
	public boolean acceptingMuir( BlockPos pos) {
		return getMuir( pos).getTotalAmount() < MAX_DEFAULT;
	}
	
	

	
	public void setupInitialCellMap() {
		BlockPos pos = MCForge.getOverworld().getSpawnPoint();
		int bound = this.bound<<4;
		System.out.println( pos);
		for( int x=-bound; x<bound; x=x+16) {
			for( int z=-bound; z<bound; z=z+16) {
				this.cells.put( new ChunkPos( pos.add( x ,0 ,z)) ,Muir.empty());
			}
		}
	}
	
	
	
	
	private Vec3d getFogColor( BlockPos pos) {
//		return this.DEFAULT_COLOR.add( getMuir(pos).getColor());
		return getMuir(pos).getColor();
	}
	
	
	private float getFogDensity( BlockPos pos) {
		float totalAmount = getMuir( pos).getTotalAmount();
		float density = ((totalAmount / this.MAX_DEFAULT) * 0.12f) + 0.01f;
		return density;
	}
	
	
	
	private void averageCell( int x ,int z) {
		BlockPos pos = new BlockPos( x<<4 ,0 ,z<<4);
		pos = pos.add( MCForge.getOverworld().getSpawnPoint());
		Muir m = getMuir( pos);
		if( m == null) {
			return;
		}
		
		for( EnumFacing face : EnumFacing.HORIZONTALS) {
			Vec3i v = face.getDirectionVec();
			Muir f = getMuir( pos.add( v.getX()*16 ,0 ,v.getZ()*16));
			if( f != null) {
				m.averageWith( f);
			}
		}
		
	}
	
	
	

}



