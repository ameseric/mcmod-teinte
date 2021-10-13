package teinte.faction2;

import java.util.ArrayList;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.Template.BlockInfo;
import teinte.faction2.structures.FunctionalStructure;
import teinte.faction2.structures.TestHome;



public class TestFaction extends Faction2{

	
	
	
	
	public TestFaction(BlockPos pos) {
		super(pos);
		this.home = new TestHome( pos);
	}
	
	

	@Override
	protected void ticklogic(WorldServer world, int tickcount) {

		this.testbuild( world);
		
	}
	
	
	private void testbuild( World world) {
		ArrayList<Template.BlockInfo> blocks = this.home.getNextSegment();
		for( BlockInfo bi : blocks) {
			world.setBlockState( bi.pos ,bi.blockState);
		}
	}



	@Override
	public boolean hasCoreEntity() {
		return false;
	}



	@Override
	protected void onFirstTickCycle( World world) {
		this.createUnit( this.homeblockPos.add( 0,2,0) ,world);
		this.createUnit( this.homeblockPos.add( 0,4,0) ,world);
	}

	
	
	
	
	
	
	
	
	
	
	
}
