package witherwar.hermetics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.minecraft.util.math.Vec3d;


public class Muir {
	

	private HashMap<MuirElement,Integer> elements = new HashMap<>();
	private static final int DIFFUSION_THRESHOLD = 5;
//	private ArrayList<ElementalBond> bonds;	
	
	{
		for( MuirElement e : MuirElement.values()) {
			this.elements.put( e ,0);
		}		
	}
	
	
	
	
	
	public Muir( HashMap<MuirElement,Integer> elements) {
		for( MuirElement e : elements.keySet()) {
			add( e ,elements.get(e));
		}
	}	
	public Muir() {}

	

	
	
	public void add( Muir f) {
		for( Map.Entry<MuirElement ,Integer> e : f.elements().entrySet()) {
			add( e.getKey() ,e.getValue());
		}
	}
	
	public void add( MuirElement e ,int amount) {
		int newAmount = amount(e) + amount;
		this.elements.put( e ,newAmount);
	}
	
	
	public void subtract( MuirElement e ,int amount) {
		int newAmount = amount(e) - amount;
		if( newAmount < 0) { newAmount = 0;}
		this.elements.put( e ,newAmount);
	}
	
	
	public Muir remove( MuirElement e ,int amount) {
		Muir f = new Muir();
		
		if( amount(e) <= 0) {
			return f;
		}
		
		int balance = amount(e) - amount;		
		if( balance < 0) {
			amount += balance;
			elements.put( e ,0);
			f.add( e ,amount);
		}
		
		return f;
	}
	
	
	private int amount( MuirElement e) {
		return elements().get( e);
	}
	
	
	
	public ArrayList<ElementalBond> getBonds(){
//		calculateBonds();
		return ElementalBond.getAllPossibleBonds( (ArrayList<MuirElement>) this.elements.clone());
	}
	
	
	public HashMap<MuirElement,Integer> elements() {
		return this.elements;
	}
	
	
	public int getTotalAmount() {		
		int amount = 0;
		for( int amountOfElement : this.elements.values()) {
			amount += amountOfElement;
		}
		return amount;
	}
	
	
	public Vec3d getColor() {
		Vec3d color = new Vec3d(0,0,0);
		float totalAmount = getTotalAmount();
		float amount = 0;
		for( MuirElement e : this.elements.keySet()) {
			amount = this.elements.get(e);
			if( amount > 0) {
				float scale = amount / totalAmount;
				Vec3d elementColor = e.color().scale( scale); 
				color = color.add( elementColor );
			}
		}
		return color;
	}

	
	
	public Muir copy() {
		return new Muir( this.elements);
	}
	
	
//	public boolean exceedAmount( int amount) {
//		for( MuirElement e : MuirElement.values()) {
//			if( amount(e) > amount) {
//				return true;
//			}
//		}
//		return false;
//	}
	
	public void bleedExcess( int cutoff) {
		for( MuirElement e : MuirElement.values()) {
			if( amount(e) > cutoff) {
				subtract( e ,amount(e)-cutoff);
			}
		}
	}
	
	
	/*
	 * Modifies Muir value passed in.
	 */
	public void averageWith( Muir m) {
		for( MuirElement e : MuirElement.values()) {
//			int ourAmount = amount(e);
//			int theirAmount = m.amount(e);
			int diff = amount(e) - m.amount(e);
			if( Math.abs(diff) > DIFFUSION_THRESHOLD) {
				subtract( e ,(diff/2));
				m.add( e ,(diff/2));
			}
//			ourAmount -= (diff/2);
//			theirAmount += (diff/2);
//			add( e ,ourAmount);
//			m.add( e ,theirAmount);
		}
	}
	

	

	
	
	
	
	@Override
	public boolean equals( Object o) { //TODO check accuracy
		if( !(o instanceof Muir)) {
			return false;
		}
		Muir f = (Muir) o;
		
		return this.elements.equals( f.elements());
	}	
	
	
	@Override
	public String toString() {
		return elements().toString();
	}
	
	
	
	public static Muir random( ) {
		Muir f = new Muir();
		for( MuirElement e : MuirElement.values()) {
			double rand = Math.random();
			if( rand < 0.55) {
				f.add( e ,(int)(100*rand));
			}
		}
		return f;
	}
	
	public static Muir empty() {
		return new Muir();
	}
	
	
	
	
	
//	private void calculateBonds() {
//		this.bonds = ElementalBond.getAllPossibleBonds( (ArrayList<Element>) this.elements.clone());
//	}

	
}
