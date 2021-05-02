package witherwar.alchemy;

import java.util.ArrayList;


public class Fluid {

	private ArrayList<Element> elements;
	private ArrayList<ElementalBond> bonds;
	
	
	
	
	public Fluid( ArrayList<Element> elements) {
		this.elements = elements;
		this.calculateBonds();
	}
	
	public Fluid() {
		this.elements = new ArrayList<>();
	}
	
	public Fluid( Element[] arr) {
		this();
		this.add( arr);
	}
	
	
	
	
	
	private void calculateBonds() {
		System.out.println( this.bonds);
		this.bonds = ElementalBond.getAllPossibleBonds( this.elements);
		System.out.println( this.bonds);
	}
	
	
	
	
	public void add( Fluid f) {
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
		if( !(o instanceof Fluid)) {
			return false;
		}
		Fluid f = (Fluid) o;
		
		return this.elements == f.getElements();
	}	
	
	
	@Override
	public String toString() {
		return this.bonds.toString();
	}
	
	
	
	public static Fluid random() {
		Fluid f = new Fluid();
		for( Element e : Element.values()) {
			double rand = Math.random();
			if( rand < 0.7) {
				f.add( e ,false);
			}
		}
		f.calculateBonds();
		return f;
	}
	
	public static Fluid empty() {
		return new Fluid();
	}

	
}
