package witherwar.hermetics;

import java.util.ArrayList;


public class ElementalFluid {

	private ArrayList<Element> elements;
	private ArrayList<ElementalBond> bonds;
	
	
	
	
	public ElementalFluid( ArrayList<Element> elements) {
		this.elements = elements;
		this.calculateBonds();
	}
	
	public ElementalFluid() {
		this.elements = new ArrayList<>();
		this.bonds = new ArrayList<>();
	}
	
	public ElementalFluid( Element[] arr) {
		this();
		this.add( arr);
	}
	
	
	
	
	
	private void calculateBonds() {
		this.bonds = ElementalBond.getAllPossibleBonds( (ArrayList<Element>) this.elements.clone());
	}
	
	
	
	
	public void add( ElementalFluid f) {
		for( Element e : f.getElements()) {
			this.add(e ,false);
		}
		this.calculateBonds();
	}
	
	public void add( Element[] arr) {
		for( Element e : arr) {
			this.add( e ,false);
		}
		this.calculateBonds();
	}
	
	public void add( Element e) {
		this.add( e ,true);
	}
	
	private void add( Element e ,boolean bondCheck) {
		if( !this.elements.contains(e)) {
			this.elements.add(e);
		}
		if( bondCheck) {
			this.calculateBonds();
		}
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
		return this.bonds.toString();
	}
	
	
	
	public static ElementalFluid random() {
		ElementalFluid f = new ElementalFluid();
		for( Element e : Element.values()) {
			double rand = Math.random();
			if( rand < 0.7) {
				f.add( e ,false);
			}
		}
		f.calculateBonds();
		return f;
	}
	
	public static ElementalFluid empty() {
		return new ElementalFluid();
	}

	
}
