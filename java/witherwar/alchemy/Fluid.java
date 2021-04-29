package witherwar.alchemy;

import java.util.ArrayList;


public class Fluid {

	private ArrayList<Element> elements;
	
	
	public Fluid( ArrayList<Element> elements) {
		this.elements = elements;
	}
	
	public Fluid() {
		this.elements = new ArrayList<>();
	}
	
	public Fluid( Element[] arr) {
		this();
		for( Element e : arr) {
			this.elements.add( e);
		}
	}
	
	
	public void add( Fluid f) {
		for( Element e : f.getElements()) {
			if( !this.elements.contains(e)) {
				this.elements.add(e);
			}
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

	
}
