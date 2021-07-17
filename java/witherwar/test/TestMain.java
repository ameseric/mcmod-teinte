package witherwar.test;



public class TestMain extends Test{
	
	
	@Override
	public void run() {
		
		System.out.print("\n\n");
		
		
		
		DefaultTest testA = new DefaultTest();
		testA.run();
		
		
		
		System.out.print("\n\n");
		
	}
	
	
	
	
}





abstract class Test {
	
	
	public abstract void run();	
	
	
	public void p( String name ,Object result) {
//		String sResult = "TRUE";
//		if( result) { sResult = "FALSE";}
		System.out.println( this.getClass().getName() + " | " + name + " | " + result );
	}


	
	
}



class DefaultTest extends Test{
	
	
	public void testA() {
		String name = "true boolean is true";
		p( name ,true);
	}
	
	public void testB() {
		String name = "equivalent interger is equivalent";
		int i = 9;
		p( name ,i == 9);
	}
	
	

	@Override
	public void run() {
		testA();
		testB();
	}
	
	
}



//class MustBeImplementedException extends Exception{
//
//	private static final long serialVersionUID = 1L;
//	
//	
//	public MustBeImplementedException() {
//		super();
//	}
//	
//}