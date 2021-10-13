package teinte.hermetics;

import java.util.ArrayList;
import java.util.Collections;


public class ElementalBond {

	private ArrayList<MuirElement> elements; //always sorted due to creation in getAllPossibleBonds
	private BondType type = BondType.MONO;
	
	
	public enum BondType{
		MONO,
		BI,
		TRI;
	}
	
	
	
	private ElementalBond( ArrayList<MuirElement> elements) {
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

	
//	
//	
//	public MuirElement getTrinaryTransform() {
//		if( !this.isTriBond()) {
//			return null;
//		}
//		
//		return this.elements.get(0).getTrinaryProduct();
//	}
	
	
	public static ArrayList<ElementalBond> getAllPossibleBonds( ArrayList<MuirElement> f) {
		Collections.sort( f);
		ArrayList<ElementalBond> bonds = new ArrayList<>();
		
		
		while( !f.isEmpty()) {
			
			ArrayList<MuirElement> bond = new ArrayList<>();
			MuirElement currentElement = f.remove(0);
			bond.add( currentElement);
			
			MuirElement glbe;
			if( currentElement == MuirElement.A && f.contains( MuirElement.A.getGreaterBondingElement())) {
				glbe = MuirElement.A.getGreaterBondingElement();
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
				MuirElement tbe = currentElement.getTrinaryBondingElement( glbe);
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
