package witherwar.hermetics;

import java.util.Comparator;

public enum MuirElement implements Comparator<MuirElement>{

	A( 0),
	B( 1),
	C( 2),
	D( 3),
	E( 4),
	F( 5),
	G( 6);
	
	
	private int index; 
	private static final int NUM = 7;
	
	
	private MuirElement( int index) {
		this.index = index;
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
	public MuirElement getTrinaryProduct() {
		int index = this.index + 2;
		if( index >= MuirElement.NUM) { 
			index = index - MuirElement.NUM;
		}
		return MuirElement.values()[ this.index - 5];
	}

	

	@Override
	public int compare(MuirElement o1, MuirElement o2) {
		return (o1.index - o2.index) * -1;
	}
	
	
}
