package teinte.faction2.structures;








public class MuirSpike extends FunctionalStructure{
	
	
	private final static String FILENAME = "muir_spike";

	
	
	public MuirSpike() {
		super( FILENAME);
	}



	public static Structure getInertInstance() {
		return new MuirSpike().setInert();
	}



	

	
	

	
	
	
	
	

}
