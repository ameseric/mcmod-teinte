package witherwar.util;


import scala.util.Random;


public class WeightedChoice{
		public int[] weights;
		public Object[] choices;
		private int max = 0;
		
		public WeightedChoice( Object[] choices ,int[] weights ) {
			this.weights = weights;
			this.choices = choices;
			
			for( int i=0; i < weights.length; i++) {
				max += weights[i];
			}
		}
		
		public Object pick() {
			int randint = new Random().nextInt( max);
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