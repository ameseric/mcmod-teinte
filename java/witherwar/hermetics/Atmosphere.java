package witherwar.hermetics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import witherwar.MCForge;
import witherwar.TEinTE;
import witherwar.network.ClientFogUpdate;
import witherwar.tilelogic.TileLogic;

public class Atmosphere {
	
	private HashMap<ChunkPos,AtmosCell> cells = new HashMap<>();
	private Iterator< ChunkPos> iter = null;
	
	private HashMap<EntityPlayerMP ,ChunkPos> players = new HashMap<>();
	
	public static final Vec3d DEFAULT_COLOR = new Vec3d( 0.95 ,0.95 ,0.95);
	
	private int bound = 35;
	private int xIndex = -bound;
	private int zIndex = -bound;
	private int interval = 10;
	
	private int activeCellUpdateLimit = 20;
	private int totalCellUpdateLimit = 1000;
	
	
	
	//iterate through chunks and prepopulate HashMap (ignore dynamic option for now)
	//this should change if world type is Pillar, since sizes can vary. -> do later, for now r = 1024
	//to start, we won't subdivide the cells on the y axis. Just xz.
		
		

	
	
	
	public void _tick( int tickcount) {
	
		//change, store chunk location of players (similar to region) and send update
		//based on that.
		if( tickcount%5 == 0) {
			for( EntityPlayerMP player : MCForge.getAllPlayersOnServer()) {
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

		
//		iterateByInterval();
		
		
		iterateByAddition();
		
		
		
	}
	
	
	
	private void iterateByInterval() {
		for( int x=this.xIndex; x<this.xIndex+1; x++) {
			for( int z=this.zIndex; z<this.zIndex+this.interval; z++) {
				averageCell( x ,z);
			}
		}
		this.zIndex += this.interval;		
		if( this.zIndex > this.bound) {
			this.zIndex = -this.bound;
			this.xIndex++;
		}

		if( this.xIndex > this.bound) {
			this.xIndex = -this.bound;
//			System.out.println( "Full cycle.");
		}
	}
	
	
	
	private void iterateByAddition() {
		if( iter == null || !iter.hasNext()) {
			iter = this.cells.keySet().iterator();
//			System.out.println( "Full cycle.");
		}
		
		int cellsUpdated = 0;
		int cellsChecked = 0;
		for( iter=iter; iter.hasNext();) {
			ChunkPos pos = iter.next();
			AtmosCell c = getCell( pos);
			if( c.needsUpdated) {
				averageCell( pos.x ,pos.z);
				c.needsUpdated = false;
				cellsUpdated++;
			}
			cellsChecked++;
			if( cellsUpdated > this.activeCellUpdateLimit || cellsChecked > this.totalCellUpdateLimit) {
				break;
			}
		}
		
	}
	
	
	
	
	public void addCell( ChunkPos pos) {
		this.cells.put( pos ,new AtmosCell());
	}
	
	
	public void addPlayer( EntityPlayerMP player) {
		this.players.put( player ,new ChunkPos( player.getPosition()));	
	}
	
	
	public void addMuir( ChunkPos pos ,Muir m) {
		AtmosCell c = getCell( pos);
		c.muir.add( m);
		c.needsUpdated = true;
	}
	
	
	
	public Muir getMuir( ChunkPos pos) {
		AtmosCell c = getCell( pos);
		if( c != null) {
			return c.muir;
		}
		return null;
	}
	
	public Muir getMuir( BlockPos pos) {
		return getMuir( new ChunkPos( pos));
	}
	
	private AtmosCell getCell( ChunkPos pos) {
		return this.cells.get( pos);
	}
	
	private AtmosCell getCell( BlockPos pos) {
		return getCell( new ChunkPos( pos));
	}
	
	public boolean acceptingMuir( BlockPos pos ,int pressure) { 
		return getMuir( pos).getTotalAmount() < pressure;
	}
	
	

	
	public void setupInitialCellMap() {
		BlockPos pos = MCForge.getOverworld().getSpawnPoint();
		int bound = this.bound<<4;
		System.out.println( pos);
		for( int x=-bound; x<bound; x=x+16) {
			for( int z=-bound; z<bound; z=z+16) {
				this.cells.put( new ChunkPos( pos.add( x ,0 ,z)) ,new AtmosCell());
			}
		}
	}
	
	
	
	
	private Vec3d getFogColor( BlockPos pos) {
//		return this.DEFAULT_COLOR.add( getMuir(pos).getColor());
		Muir m = getMuir(pos);
		Vec3d color = m.getColor();
		float scale =  m.getTotalAmount() / (RuleBook.PRESSURE_THRESHOLD_A * 1f);
		if( scale > 1.0f) { scale = 1.0f;}
		color = color.scale( scale);
		return color;
	}
	
	
	private float getFogDensity( BlockPos pos) {
		float totalAmount = getMuir( pos).getTotalAmount();
		float density = ((totalAmount / RuleBook.PRESSURE_THRESHOLD_B));
		if( density > 1.0f) { density = 1.0f;}
		return density;
	}
	
	
	
	private void averageCell( int x ,int z) {
		BlockPos pos = new BlockPos( x<<4 ,0 ,z<<4);
//		pos = pos.add( MCForge.getOverworld().getSpawnPoint());
		Muir m = getMuir( pos);
		if( m == null) {
			return;
		}
		
		for( EnumFacing face : EnumFacing.HORIZONTALS) {
			Vec3i v = face.getDirectionVec();
			AtmosCell c = getCell( pos.add( v.getX()*16 ,0 ,v.getZ()*16));
			if( c != null) {
//				m.averageWith( c.muir);
				boolean valueChanged = m.exchangeWith( c.muir);
				c.needsUpdated = valueChanged;

			}
		}
		
	}
	

	
	
	

}



class AtmosCell{
	
	public boolean needsUpdated = false;
	public Muir muir = Muir.empty();
	
}



