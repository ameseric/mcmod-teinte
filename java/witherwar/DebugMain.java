package witherwar;

import java.util.ArrayList;
import java.util.Iterator;

public class DebugMain {

	
	static ArrayList<String> strings;
	
	
	public static void main(String args[]){
		
		System.out.println( "Hello world");


		strings = new ArrayList<>();
		strings.add( "Hello");
		strings.add( "Testing");
		
		Iterator<String> iter;
		for( iter = strings.iterator(); iter.hasNext();) {
			System.out.println( iter.next());
			iter.remove();
		}
		
		System.out.println( strings.size());


	}
	
	
	
	
	
	
	
}
