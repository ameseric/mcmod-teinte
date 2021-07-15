package witherwar.hermetics;

import java.util.ArrayList;
import java.util.Collections;


public class ElementalBond {

	private ArrayList<Element> elements; //always sorted due to creation in getAllPossibleBonds
	private BondType type = BondType.MONO;
	
	
	public enum BondType{
		MONO,
		BI,
		TRI;
	}
	
	
	
	private ElementalBond( ArrayList<Element> elements) {
		this.elements = elements;
		this.type = BondType.values()[this.elements.size()-1];
	}
	
	
	public boolean isMonoBond() {
		return this.type == BondType.MONO;
	}
	public boolean isBiBond() {
		return this.type == BondType.BI;
	}
	public boolean isTriBond() {
		return this.type == BondType.TRI;
	}
	public BondType getBondType() {
		return this.type;
	}

	
	
	
	public Element getTrinaryTransform() {
		if( !this.isTriBond()) {
			return null;
		}
		
		return this.elements.get(0).getTrinaryProduct();
	}
	
	
	public static ArrayList<ElementalBond> getAllPossibleBonds( ArrayList<Element> f) {
		Collections.sort( f);
		ArrayList<ElementalBond> bonds = new ArrayList<>();
		
		
		while( !f.isEmpty()) {
			
			ArrayList<Element> bond = new ArrayList<>();
			Element currentElement = f.remove(0);
			bond.add( currentElement);
			
			Element glbe;
			if( currentElement == Element.A && f.contains( Element.A.getGreaterBondingElement())) {
				glbe = Element.A.getGreaterBondingElement();
				f.remove( glbe);
				bond.add( 0 ,glbe);
				
			}else {
				glbe = currentElement.getLesserBondingElement();
				if( f.contains( glbe)) {
					bond.add( glbe);
					f.remove( glbe);
				}
			}
			
			if( bond.size() > 1) {
				Element tbe = currentElement.getTrinaryBondingElement( glbe);
				if( f.contains( tbe)) {
					bond.add( tbe);
					f.remove( tbe);
				}
			}
			
			bonds.add( new ElementalBond( bond));
		}
		return bonds;
	}
	
	
	@Override
	public String toString() {
		return this.elements.toString();
	}
	
	
	
}
