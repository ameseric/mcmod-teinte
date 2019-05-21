package witherwar.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	
	public WeightedHashMap( Object[] array) {
		this.map = new HashMap<Object ,Integer>();
		for( Object o : array) {
			this.map.put( o ,0);
		}
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
	
	
	private HashMap<Object,Integer> allocate( int numOfObj) {
		HashMap<Object,Integer> allocation = new HashMap<>();
		
		for( Object o : this.map.keySet()) {
			int weight = this.map.get(o);
			int numOfAllocations = (int) (( Math.ceil( (weight*1.0) / this.max)) * numOfObj );
			allocation.put( o ,numOfAllocations);
		}
		System.out.println( "Calculated Allocation: " + allocation);
		return allocation;
	}
	
	
	/**
	 * First implementation of reallocating Objects based on the current HashMap weights. Crude, will probably
	 * revisit.
	 * @param numOfObj
	 * @param oldAllocation
	 * @return oldAllocation
	 */
	public HashMap<Object,List<Object>> allocate( int numOfObj ,HashMap< Object ,List<Object>> oldAllocation){
		HashMap<Object ,Integer> newAllocation = this.allocate( numOfObj);
		ArrayList<Object> reassign = new ArrayList<Object>();
		
		for( Object o : oldAllocation.keySet()) {
			int diff = newAllocation.get(o) - oldAllocation.get(o).size();
			if( diff < 0) {
				for( int i=0; i>diff; i--) {
					int index = Math.abs(i);
					reassign.add( oldAllocation.get(o).get( index));
					oldAllocation.get(o).remove( index);
				}
			}
		}
		
		for( Object o : oldAllocation.keySet()) {
			int diff = newAllocation.get(o) - oldAllocation.get(o).size();
			if( diff > 0) {
				for( int i=0; i<diff; i++) {
					oldAllocation.get(o).add( reassign.remove(0));
				}
			}
		}
		
		return oldAllocation;
	}
	
	
	
}
