package witherwar.util;



import scala.util.Random;


public class WeightedChoice<T>{
	private static final Random rng = new Random();
	
	public int[] weights;
	public T[] choices;
	private int max = 0;
	
	public WeightedChoice( T[] choices ,int[] weights ) {
		this.weights = weights;
		this.choices = choices;
		
		for( int i=0; i < weights.length; i++) {
			max += weights[i];
		}
	}
	
	public T pick() {
		int randint = rng.nextInt( max);
		int fullWeight = 0;
		
		for( int i=0; i < weights.length; i++) {
			fullWeight += weights[i];
			if( randint < fullWeight) {
				return choices[i];
			}
		}
		return choices[ choices.length]; //causes error, because this should never happen
	}
}



//public class WeightedChoice{
//	private int[] weights;
//	private Object[] choices;
//	private ArrayList<Object> list;
//	
//	public WeightedChoice( Object[] choices ,int[] weights) {
//		this.weights = weights;
//		this.choices = choices;
//	}
//	
//	public Object pick() {
//		
//		for( int i=0; i<this.weights.length; i++) {
//			int weight = this.weights[i];
//			for( int j=0; j<weight; j++) {
//				list.add( this.choices[i]);
//			}
//		}
//		
//		int randint = ThreadLocalRandom.current().nextInt(0,list.size());
//		return this.list.get( randint);
//	}
//	
//	
//}


