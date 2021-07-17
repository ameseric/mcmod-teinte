package witherwar.hermetics;

import java.util.ArrayList;


public class ElementalFluid {

	private ArrayList<Element> elements;
//	private ArrayList<ElementalBond> bonds;
	
	
	
	
	public ElementalFluid( ArrayList<Element> elements) {
		this.elements = elements;
	}
	
	public ElementalFluid() {
		this.elements = new ArrayList<>();
	}	
	

	
	
	public void add( ElementalFluid f) {
		for( Element e : f.getElements()) {
			add(e);
		}
	}
	
	public void add( Element e) {
		if( e != null && !this.elements.contains(e)) {
			this.elements.add(e);
		}
	}
	
	
	public ElementalFluid remove( Element e) {
		ElementalFluid f = new ElementalFluid();
		if( has( e)) {
			transfer( e ,f);
			
			Element glbe = null;
			if( has( e.getGreaterBondingElement())) {
				glbe = e.getGreaterBondingElement();
			}else if( has( e.getLesserBondingElement())){
				glbe = e.getLesserBondingElement();
			}
			
			if( glbe != null) {
				transfer( glbe ,f);
				Element tbe = e.getTrinaryBondingElement( glbe); 
				if( has( tbe)) {
					transfer( tbe ,f);
				}
			}
		}
		return f;
	}
	
	
	private void transfer( Element e ,ElementalFluid f) {
		getElements().remove( e);
		f.add( e);
	}
	
	
	
	public boolean has( Element e) {
		return getElements().contains( e);
	}
	
	
	public ArrayList<ElementalBond> getBonds(){
//		calculateBonds();
		return ElementalBond.getAllPossibleBonds( (ArrayList<Element>) this.elements.clone());
	}
	
	
	public ArrayList<Element> getElements() {
		return this.elements;
	}
	
	
	public boolean isEmpty() {
		return this.elements.isEmpty();
	}
	

	
	
	
	
	@Override
	public boolean equals( Object o) {
		if( !(o instanceof ElementalFluid)) {
			return false;
		}
		ElementalFluid f = (ElementalFluid) o;
		
		return this.elements == f.getElements();
	}	
	
	
	@Override
	public String toString() {
		return getElements().toString();
	}
	
	
	
	public static ElementalFluid random() {
		ElementalFluid f = new ElementalFluid();
		for( Element e : Element.values()) {
			double rand = Math.random();
			if( rand < 0.7) {
				f.add( e);
			}
		}
		return f;
	}
	
	public static ElementalFluid empty() {
		return new ElementalFluid();
	}
	
	
	
	
//	private void calculateBonds() {
//		this.bonds = ElementalBond.getAllPossibleBonds( (ArrayList<Element>) this.elements.clone());
//	}

	
}
