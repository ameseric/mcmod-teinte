package witherwar.util;

import java.util.HashMap;
import java.util.Random;

public class WeightedHashMap {
	private HashMap<Object,Integer> map;
	private int max = 0;

	public WeightedHashMap( HashMap<Object,Integer> map) {
		this.map = map;		
		this.updateMaxWeight();		
	}
	
	public WeightedHashMap() {
		this.map = new HashMap<Object ,Integer>();
	}
	
	public void put( Object o ,Integer w) {
		this.map.put( o ,w);
	}
	
	private void updateMaxWeight() {
		for( Integer i : this.map.values()) {
			this.max += i;
		}
	}
	
	public void updateWeight( Object o ,int w) {
		this.map.put( o ,w);
		this.updateMaxWeight();
	}
	
	//Increase weight by 1
	public void increment( Object o) {
		this.updateWeight( o ,this.map.get(o)+1);
	}
	
	//Decrease weight by 1
	public void decrement( Object o) {
		this.updateWeight( o ,this.map.get(o)-1);
	}
	
	public void remove( Object o) {
		this.map.remove( o);
		this.updateMaxWeight();
	}
	
	public Object pick() {
		Object rObj = null;
		
		int randint = new Random().nextInt( this.max);
		int addedWeight = 0;
		
		for( Object o : this.map.keySet()) {
			addedWeight += this.map.get(o);
			if( randint < addedWeight) {
				rObj = o;
				break;
			}
		}
		
		return rObj;
	}
	
	
	
}
