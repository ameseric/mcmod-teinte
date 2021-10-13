package teinte.hermetics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.WorldServer;
import teinte.MCForge;
import teinte.TEinTE;
import teinte.disk.NBTSaveObject;
import teinte.network.ClientFogUpdate;
import teinte.tilelogic.TileLogic;

public class Atmosphere extends NBTSaveObject{
	
	private HashMap<ChunkPos,AtmosCell> cells = new HashMap<>();
	private Iterator< ChunkPos> iter = null;
//	private NBTTagCompound localnbt = new NBTTagCompound();
	
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
		
		for( EntityPlayerMP player : MCForge.getAllPlayersOnServer()) {
			BlockPos pos = player.getPosition();
//			if( pos.getY() > TEinTE.DEPTH_CUTOFF  && tickcount%5 == 0) {
			if( tickcount%5 == 0) {
				AtmosCell cell = getCell( pos);
				float density = 0f;
				Vec3d unadjColor = new Vec3d(0,0,0);
				float colorScale = 0f;
				if( cell != null) {
					density = getFogDensity( pos);
					unadjColor = getUnadjustedFogColor( pos);
					colorScale = getColorAdjustment(pos);
					System.out.println( getMuir( pos));
				}
				TEinTE.networkwrapper.sendTo( new ClientFogUpdate( unadjColor ,colorScale ,density) ,player );
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
		BlockPos pos = MCForge.getOverworldServer().getSpawnPoint();
		int bound = this.bound<<4;
		System.out.println( pos);
		for( int x=-bound; x<bound; x=x+16) {
			for( int z=-bound; z<bound; z=z+16) {
				this.cells.put( new ChunkPos( pos.add( x ,0 ,z)) ,new AtmosCell());
			}
		}
	}
	
	
	
	
	private Vec3d getAdjustedFogColor( BlockPos pos) {
		Muir m = getMuir(pos);
		Vec3d color = m.getColor();		
		color = color.scale( getColorAdjustment( pos));
		return color;
	}
	
	
	private float getColorAdjustment( BlockPos pos) {
		Muir m = getMuir( pos);
		float scale =  m.getTotalAmount() / (RuleBook.PRESSURE_THRESHOLD_A * 0.85f);
		if( scale > 1.0f) { scale = 1.0f;}
		return scale;
	}
	
	
	private Vec3d getUnadjustedFogColor( BlockPos pos) {
		return getMuir(pos).getColor();
	}
	
	
	
	private float getFogDensity( BlockPos pos) {
		float totalAmount = getMuir( pos).getTotalAmount();
		float density = ((totalAmount / RuleBook.PRESSURE_THRESHOLD_B));
		if( density > 1.0f) { density = 1.0f;}
		return density;
	}
	
	
	
	public static void displayAtmosphericParticles( Entity player ,float fogDensity) {
			BlockPos pos = player.getPosition();
			WorldServer world = MCForge.getOverworldServer();
			float start = 0;
			
			for( int i=0; i<30; i++) {
//				start += 0.2;
//				if( start <= fogDensity) {
					world.spawnParticle( EnumParticleTypes.SUSPENDED_DEPTH 
							,pos.getX()+(TEinTE.RNG.nextGaussian()*16) ,pos.getY()+(TEinTE.RNG.nextGaussian()*3) ,pos.getZ()+(TEinTE.RNG.nextGaussian()*16) 
							,1 ,0 ,0 ,0 ,0 ,null);
//				}
			}

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
//				c.needsSaved = valueChanged;

			}
		}
		this.markDirty();
//		getCell( pos).needsSaved = true;
	}



	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
//		nbt = this.localnbt;
		
		nbt.setInteger( "numOfCells" ,this.cells.size());
		int i = 0;
		for( ChunkPos pos : this.cells.keySet()) {
			AtmosCell cell = getCell( pos);
//			if( cell.needsSaved) {
				NBTTagCompound cellnbt = new NBTTagCompound();
				cellnbt.setBoolean( "update" ,cell.needsUpdated);
				cellnbt.setIntArray( "chunk" ,new int[] { pos.x ,pos.z});
				cellnbt = cell.muir.writeToNBT( cellnbt);
				nbt.setTag( "Cell"+i ,cellnbt);
//				cell.needsSaved = false;
//			}
			i++;
		}
		return nbt;
	}



	@Override
	public void readFromNBT(NBTTagCompound nbt) {
//		this.localnbt = nbt;
		int size = nbt.getInteger( "numOfCells");
		for( int i=0; i<size; i++) {
			NBTTagCompound cellnbt = nbt.getCompoundTag( "Cell"+i);
			int[] chunk = cellnbt.getIntArray( "chunk");
			ChunkPos pos = new ChunkPos( chunk[0] ,chunk[1]);
			AtmosCell cell = getCell( pos);
			cell.muir.readFromNBT( cellnbt);
			cell.needsUpdated = cellnbt.getBoolean( "update");
		}
		
	}



	@Override
	public String getDataName() {
		return "atmosphere";
	}
	

	
	
	

}



class AtmosCell{	
	public boolean needsUpdated = false;
	public Muir muir = Muir.empty();
//	public boolean needsSaved = false;
	
	

	
}



