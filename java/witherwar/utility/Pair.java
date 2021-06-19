package witherwar.utility;

import java.util.Objects;

public class Pair<T,K> {

	private T first;
	private K second;
	
	
	public Pair(T a ,K b) {
		this.first = a;
		this.second = b;
	}
	
	
	
	public T first() {
		return this.first;
	}
	
	
	public K second() {
		return this.second;
	}
	
	
	
	
	@Override
	public int hashCode() {
		return Objects.hash( this.first ,this.second);
	}
	
	
	@Override
	public boolean equals( Object o) {
		if( this.getClass() == o.getClass()) {
			Pair<Integer,Integer> p = (Pair<Integer,Integer>) o;
			if( this.first.equals( p.first) && this.second.equals( p.second)) {
				return true;
			}
		}
		
		return false;
	}
	
	
}
