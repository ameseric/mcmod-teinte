package witherwar.hermetics;

import java.util.Comparator;

import net.minecraft.util.math.Vec3d;

public enum MuirElement implements Comparator<MuirElement>{

	A( 0 ,true ,true	,new Vec3d( -0.02 ,-0.30 ,-0.3)),
	B( 1 ,true ,true	,new Vec3d( -0.08 ,-0.22 ,-0.3)),
	C( 2 ,false ,true	,new Vec3d( -0.15 ,-0.15 ,-0.3)),
	D( 3 ,false ,true	,new Vec3d( -0.22 ,-0.08 ,-0.3)),
	E( 4 ,false ,false	,new Vec3d( -0.30 ,-0.02 ,-0.3)),
	F( 5 ,false ,false	,new Vec3d( -0.30 ,-0.15 ,-0.15)),
	G( 6 ,true ,false	,new Vec3d( -0.30 ,-0.30 ,-0.02)),
	H( 7 ,true ,false	,new Vec3d( -0.15 ,-0.15 ,-0.3));
	
	
	private static final int NUM = 8;
	private int index;
	private boolean noble;
	private boolean ordered;
	private Vec3d color;
	
	
	private MuirElement( int index ,boolean isNoble ,boolean isOrdered ,Vec3d color) {
		this.index = index;
		this.noble = isNoble;
		this.ordered = isOrdered;
		this.color = color.scale( 1.5f);//using this to try darker colors
	}
	
	
	
	
	public boolean isNoble() {
		return this.noble;
	}
	
	public boolean isOrdered() { //TODO rename, confusing
		return this.ordered;
	}
	
	
	public MuirElement bondTxElement() {
		return this.getLesserBondingElement();
	}
	
	public Vec3d color() {
		return this.color;
	}
	
	
	
	
	
	
	
	
	public static MuirElement getGreaterElement( MuirElement e1 ,MuirElement e2) {
		//TODO change
		if( e1 == MuirElement.A && e2 == MuirElement.G || e1 == MuirElement.G && e2 == MuirElement.A) {
			return MuirElement.G;
		}
		
		return e1.index < e2.index ? e1 : e2;
	}
	
	
	public MuirElement getGreaterBondingElement() {
		int index = this.index - 1;
		if( index < 0) {
			index = MuirElement.NUM - 1;
		}
		return MuirElement.values()[ index];
	}	

	
	public MuirElement getLesserBondingElement() {
		int index = this.index + 1;
		if( index >= MuirElement.NUM) {
			index = 0;
		}
		return MuirElement.values()[index];
	}
	
	
	
	public MuirElement getTrinaryBondingElement( MuirElement partner) {
		MuirElement greater = MuirElement.getGreaterElement( this ,partner);
		int index = (greater.index + 4);
		if( index >= MuirElement.NUM) { index = index - MuirElement.NUM; }
		return MuirElement.values()[ index];
	}
	
	
	
	//Assuming this Element has the greatest energy in the trinary bond
	public MuirElement getBinaryProduct() {
		int index = (this.index + 3) % MuirElement.NUM;
		return MuirElement.values()[ index];
	}

	

	@Override
	public int compare(MuirElement o1, MuirElement o2) {
		return (o1.index - o2.index) * -1;
	}
	
	
}
