package witherwar.utility;

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
	
	
}
