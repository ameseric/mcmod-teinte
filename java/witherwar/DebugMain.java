package witherwar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.util.math.BlockPos;
import witherwar.test.TestMain;
import witherwar.worlds.structures.StructureBuilder;

public class DebugMain {

	
	static ArrayList<String> strings;
	
	
	public static void main(String args[]){
		
		System.out.println( "Hello world");
		
		
//		for( int i=0; i<20; i=i+5) {
//			System.out.println( i);
//		}
		
		
		System.out.println( 544>>4);
		System.out.println( 528>>4);
		


//		strings = new ArrayList<>();
//		strings.add( "Hello");
//		strings.add( "Testing");
//		
//		Iterator<String> iter;
//		for( iter = strings.iterator(); iter.hasNext();) {
//			System.out.println( iter.next());
//			iter.remove();
//		}
//		
//		System.out.println( strings.size());
		
		
//		StructureBuilder sg = new StructureBuilder( new BlockPos(0,0,0) ,new BlockPos(50,25,50));


	}
	
	
	
	public static int returnNull() {
		int b = 8;
		Integer t = null;
		if( 7 > 6) {
			b = t;
		}
		return b;
	}
	
	
	
}
