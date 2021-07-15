package witherwar.hermetics;

import java.util.Comparator;

public enum Element implements Comparator<Element>{

	A( 0),
	B( 1),
	C( 2),
	D( 3),
	E( 4),
	F( 5),
	G( 6);
	
	
	private int index; 
	private static final int NUM = 7;
	
	
	private Element( int index) {
		this.index = index;
	}
	
	
	public static Element getGreaterElement( Element e1 ,Element e2) {
		//TODO change
		if( e1 == Element.A && e2 == Element.G || e1 == Element.G && e2 == Element.A) {
			return Element.G;
		}
		
		return e1.index < e2.index ? e1 : e2;
	}
	
	
	public Element getGreaterBondingElement() {
		int index = this.index - 1;
		if( index < 0) {
			index = Element.NUM - 1;
		}
		return Element.values()[ index];
	}	

	
	public Element getLesserBondingElement() {
		int index = this.index + 1;
		if( index >= Element.NUM) {
			index = 0;
		}
		return Element.values()[index];
	}
	
	
	
	public Element getTrinaryBondingElement( Element partner) {
		Element greater = Element.getGreaterElement( this ,partner);
		int index = (greater.index + 4);
		if( index >= Element.NUM) { index = index - Element.NUM; }
		return Element.values()[ index];
	}
	
	
	
	//Assuming this Element has the greatest energy in the trinary bond
	public Element getTrinaryProduct() {
		int index = this.index + 2;
		if( index >= Element.NUM) { 
			index = index - Element.NUM;
		}
		return Element.values()[ this.index - 5];
	}

	

	@Override
	public int compare(Element o1, Element o2) {
		return (o1.index - o2.index) * -1;
	}
	
	
}
