package witherwar.utility;

public class MiscUtil {
	
	private MiscUtil() {}
	
	public static int limit( int value) {
	    return Math.max( 0 ,Math.min( value ,10));
	}
}
