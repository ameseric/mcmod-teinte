package witherwar.utility;

import java.util.HashMap;
import java.util.Random;


public class WeightedHashMap<T> {
	private static final Random rng = new Random();
	
	private HashMap<T,Integer> map;
	private int max = 0;

	
	public WeightedHashMap() {
		this.map = new HashMap<T ,Integer>();
	}
	
	public WeightedHashMap( T[] array) {
		this.map = new HashMap<T ,Integer>();
		for( T t : array) {
			this.map.put( t ,0);
		}
	}
	
	public void put( T t ,Integer w) {
		this.map.put( t ,w);
	}
	
	private void updateMaxWeight() {
		for( Integer i : this.map.values()) {
			this.max += i;
		}
	}
	
	/**
	 * Change weight at specified hash index to new provided weight.
	 * 
	 * @param t
	 * @param weight
	 */
	public void update( T t ,int weight) {
		this.map.put( t ,weight);
		this.updateMaxWeight();
	}
	
	//Increase weight by 1
	public void increment( T t) {
		this.update( t ,this.map.get( t)+1);
	}
	
	//Decrease weight by 1
	public void decrement( T t) {
		this.update( t ,this.map.get(t)-1);
	}
	
	public void remove( T t) {
		this.map.remove( t);
		this.updateMaxWeight();
	}
	
	public T pick() {
		T rt = null;
		
		int randint = rng.nextInt( this.max);
		int addedWeight = 0;
		
		for( T t : this.map.keySet()) {
			addedWeight += this.map.get(t);
			if( randint < addedWeight) {
				rt = t;
				break;
			}
		}
		
		return rt;
	}
	
	
	public HashMap<T,Integer> allocate( int numOfObj) {
		HashMap<T,Integer> allocation = new HashMap<>();
		
		for( T  t : this.map.keySet()) {
			int weight = this.map.get(t);
			int numOfAllocations = (int) (( Math.ceil( (weight*1.0) / this.max)) * numOfObj );
			allocation.put( t ,numOfAllocations);
		}
		System.out.println( "Calculated Allocation: " + allocation);
		return allocation;
	}
	
	

//	public HashMap<T ,List<Entity>> allocate( int numOfObj ,HashMap< T ,List<Entity>> oldAllocation){
//		HashMap<T ,Integer> newAllocation = this.allocate( numOfObj);
//		ArrayList<Entity> reassign = new ArrayList<Entity>();
//		
//		for( T t : oldAllocation.keySet()) {
//			int diff = newAllocation.get(t) - oldAllocation.get(t).size();
//			if( diff < 0) {
//				for( int i=0; i>diff; i--) {
//					int index = Math.abs(i);
//					reassign.add( oldAllocation.get(t).get( index));
//					oldAllocation.get(t).remove( index);
//				}
//			}
//		}
//		
//		for( T t : oldAllocation.keySet()) {
//			int diff = newAllocation.get(t) - oldAllocation.get(t).size();
//			if( diff > 0) {
//				for( int i=0; i<diff; i++) {
//					oldAllocation.get(t).add( reassign.remove(0));
//				}
//			}
//		}
//		
//		return oldAllocation;
//	}
	
	
	
}
