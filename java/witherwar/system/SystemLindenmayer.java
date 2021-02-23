package witherwar.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import witherwar.system.SystemLindenmayer.Rule;
import witherwar.util.Symbol;
import witherwar.util.WeightedChoice;




class RuleSet{
	public ArrayList<Symbol> axiom;
	public Rule[] rules;
}



public class SystemLindenmayer{
		
	protected ArrayList<Symbol> axiom;
	protected HashMap<Symbol ,Rule> alphabet;
	protected int recursionDepth;	
	
	protected interface Rule{
		ArrayList<Symbol> exe();
	}
	
	
	public enum Rulesets{
		WEEPING( new Weeping());
		
		public RuleSet ruleset;
		
		private Rulesets( RuleSet ruleset) {
			this.ruleset = ruleset;
		}
	}

	

	public SystemLindenmayer( RuleSet ruleset ,int recursionDepth) {
		setRules( ruleset.rules);
		axiom = ruleset.axiom;		
	}
	
	
	public Symbol[] grow() {
		List<Symbol> output = grow( this.recursionDepth ,this.axiom);
		Symbol[] branch = new Symbol[ output.size()];
		return output.toArray( branch);
	}
	
	
	private ArrayList<Symbol> grow( int depth ,ArrayList<Symbol> construct) {
		if( depth < 0) {
			return construct;
		}
		--depth;
		
		ArrayList<Symbol> nextConstruct = new ArrayList<Symbol>();
		for( Symbol symbol : construct) {
			nextConstruct.addAll( this.alphabet.get( symbol).exe());
		}
		return grow( depth ,nextConstruct);
	}
	
	
	protected void setRules( Rule[] rules) {
		// if rules.length != 7, THROW ERROR!
		this.alphabet = new HashMap< Symbol ,Rule>( rules.length);
		
		int i=0;
		for( Symbol symbol : Symbol.values()) {
			this.alphabet.put( symbol ,rules[i]);
			++i;
		}
	}

}






class Weeping extends RuleSet{

	public Weeping() {		
		
		WeightedChoice<?> c = new WeightedChoice<Object>( 
				new Symbol[]{ Symbol.XP ,Symbol.XN ,Symbol.ZP ,Symbol.ZN} 
				,new int[]{ 1 ,1 ,1 ,1});
		this.axiom = new ArrayList<Symbol>( Arrays.asList( Symbol.YP ,Symbol.YP ,(Symbol)c.pick() ,Symbol.YN ,Symbol.YN));
		
		
		Rule yp = () -> {
			WeightedChoice<?> a = new WeightedChoice<Object>( 
					new Symbol[]{ Symbol.YP ,Symbol.XP ,Symbol.XN ,Symbol.ZP ,Symbol.ZN ,Symbol.NU} 
					,new int[]{ 12 ,2 ,2 ,2 ,2 ,1});
			return new ArrayList<Symbol>( Arrays.asList( Symbol.YP ,Symbol.YP ,(Symbol)a.pick() ));};
			
		Rule yn = () -> {
			WeightedChoice<?> a = new WeightedChoice<Object>( 
					new Symbol[]{ Symbol.YN ,Symbol.XP ,Symbol.XN ,Symbol.ZP ,Symbol.ZN ,Symbol.NU} 
					,new int[]{ 12 ,2 ,2 ,2 ,2 ,1});
			return new ArrayList<Symbol>( Arrays.asList( Symbol.YN ,(Symbol)a.pick() ,Symbol.YN));};
			
		Rule xp = () -> {
			WeightedChoice<?> a = new WeightedChoice<Object>( 
					new Symbol[]{ Symbol.XP ,Symbol.YP ,Symbol.YN ,Symbol.NU} 
					,new int[]{ 5 ,1 ,1 ,1});
			return new ArrayList<Symbol>( Arrays.asList( Symbol.XP ,(Symbol)a.pick() ,Symbol.XP ));};	
			
		Rule xn = () -> {
			WeightedChoice<?> a = new WeightedChoice<Object>( 
					new Symbol[]{ Symbol.XN ,Symbol.YP ,Symbol.YN ,Symbol.NU} 
					,new int[]{ 5 ,1 ,1 ,1});
			return new ArrayList<Symbol>( Arrays.asList( Symbol.XN ,(Symbol)a.pick() ,Symbol.XN ));};
			
		Rule zp = () -> {
			WeightedChoice<?> a = new WeightedChoice<Object>( 
					new Symbol[]{ Symbol.ZP ,Symbol.YP ,Symbol.YN ,Symbol.NU} 
					,new int[]{ 5 ,1 ,1 ,1});
			return new ArrayList<Symbol>( Arrays.asList( Symbol.ZP ,(Symbol)a.pick() ,Symbol.ZP ));};
			
			
		Rule zn = () -> {
			WeightedChoice<?> a = new WeightedChoice<Object>( 
					new Symbol[]{ Symbol.ZN ,Symbol.YP ,Symbol.YN ,Symbol.NU} 
					,new int[]{ 5 ,1 ,1 ,1});
			return new ArrayList<Symbol>( Arrays.asList( Symbol.ZN ,(Symbol)a.pick() ,Symbol.ZN ));};
			
		Rule nu = () -> {
			return new ArrayList<Symbol>( Arrays.asList( Symbol.NU ));};
		
		this.rules = new Rule[] { yp ,yn ,xp ,xn ,zp ,zn ,nu};

		
	}
}


/**
class ClawingSystem extends LSystem{

	public ClawingSystem( int recursionDepth) {
		//HashMap<String ,Symbol> symbols = this.defaultSymbolSetup();
		
		this.recursionDepth = recursionDepth;
		
		WeightedChoice c = new WeightedChoice( 
				new Symbol[]{ Symbol.XP ,Symbol.XN ,Symbol.ZP ,Symbol.ZN} 
				,new int[]{ 1 ,1 ,1 ,1});
		this.setAxiom( new ArrayList<Symbol>( Arrays.asList( Symbol.YP ,c.pick() ,Symbol.YP)));
		
		
		Rule yp = () -> {
			WeightedChoice a = new WeightedChoice( 
					new Symbol[]{ Symbol.YP ,Symbol.XP ,Symbol.XN ,Symbol.ZP ,Symbol.ZN ,Symbol.NU} 
					,new int[]{ 3 ,1 ,1 ,1 ,1 ,1});
			//WeightedChoice b = new WeightedChoice( new Symbol[]{ Symbol.ZP ,Symbol.ZN ,Symbol.NU} ,new int[]{4,4,1});
			return new ArrayList<Symbol>( Arrays.asList( Symbol.YP ,a.pick() ));};
			
		Rule yn = () -> {
			return new ArrayList<Symbol>( Arrays.asList( Symbol.YN));};	
			
		Rule xp = () -> {
			WeightedChoice a = new WeightedChoice( new Symbol[]{ Symbol.XP ,Symbol.YP ,Symbol.NU} ,new int[]{2,3,1});
			return new ArrayList<Symbol>( Arrays.asList( Symbol.XP ,a.pick() ));};	
			
		Rule xn = () -> {
			WeightedChoice a = new WeightedChoice( new Symbol[]{ Symbol.XN ,Symbol.YP ,Symbol.NU} ,new int[]{2,3,1});
			return new ArrayList<Symbol>( Arrays.asList( Symbol.XN ,a.pick() ));};
			
		Rule zp = () -> {
			WeightedChoice a = new WeightedChoice( new Symbol[]{ Symbol.ZP ,Symbol.YP ,Symbol.NU} ,new int[]{2,3,1});
			return new ArrayList<Symbol>( Arrays.asList( Symbol.ZP ,a.pick() ));};
			
		Rule zn = () -> {
			WeightedChoice a = new WeightedChoice( new Symbol[]{ Symbol.ZN ,Symbol.YP ,Symbol.NU} ,new int[]{2,3,1});
			return new ArrayList<Symbol>( Arrays.asList( Symbol.ZN ,a.pick() ));};
			
		Rule nu = () -> {
			return new ArrayList<Symbol>( Arrays.asList( Symbol.NU ));};
		
		Rule[] rules = new Rule[] { yp ,yn ,xp ,xn ,zp ,zn ,nu};
		this.setRules( rules);

		
	}
}



class SampleSys extends LSystem{

	public SampleSys( int recursionDepth) {
		
		this.recursionDepth = recursionDepth;
		
		this.setAxiom( new ArrayList<Symbol>( Arrays.asList( Symbol.YP ,Symbol.XP ,Symbol.YP)));
		
		
		Rule yp = () -> {
			WeightedChoice a = new WeightedChoice( 
					new Symbol[]{ Symbol.YP ,Symbol.XP ,Symbol.XN ,Symbol.ZP ,Symbol.ZN ,Symbol.NU} 
					,new int[]{ 3 ,1 ,1 ,1 ,1 ,1});
			//WeightedChoice b = new WeightedChoice( new Symbol[]{ Symbol.ZP ,Symbol.ZN ,Symbol.NU} ,new int[]{4,4,1});
			return new ArrayList<Symbol>( Arrays.asList( Symbol.YP ,Symbol.YP ,Symbol.XP));};
			
		Rule yn = () -> {
			return new ArrayList<Symbol>( Arrays.asList( Symbol.YN));};	
			
		Rule xp = () -> {
			//WeightedChoice a = new WeightedChoice( new Symbol[]{ Symbol.XP ,Symbol.YP ,Symbol.NU} ,new int[]{2,3,1});
			return new ArrayList<Symbol>( Arrays.asList( Symbol.XP ,Symbol.XP ,Symbol.YP));};	
			
		Rule xn = () -> {
			WeightedChoice a = new WeightedChoice( new Symbol[]{ Symbol.XN ,Symbol.YP ,Symbol.NU} ,new int[]{2,3,1});
			return new ArrayList<Symbol>( Arrays.asList( Symbol.XN ,a.pick() ));};
			
		Rule zp = () -> {
			WeightedChoice a = new WeightedChoice( new Symbol[]{ Symbol.ZP ,Symbol.YP ,Symbol.NU} ,new int[]{2,3,1});
			return new ArrayList<Symbol>( Arrays.asList( Symbol.ZP ,a.pick() ));};
			
		Rule zn = () -> {
			WeightedChoice a = new WeightedChoice( new Symbol[]{ Symbol.ZN ,Symbol.YP ,Symbol.NU} ,new int[]{2,3,1});
			return new ArrayList<Symbol>( Arrays.asList( Symbol.ZN ,a.pick() ));};
			
		Rule nu = () -> {
			return new ArrayList<Symbol>( Arrays.asList( Symbol.NU ));};
		
		Rule[] rules = new Rule[] { yp ,yn ,xp ,xn ,zp ,zn ,nu};
		this.setRules( rules);

		
	}
}



**/






