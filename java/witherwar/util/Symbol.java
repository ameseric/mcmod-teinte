package witherwar.util;


import java.util.Arrays;

import net.minecraft.util.math.BlockPos;


public enum Symbol{ //not a generic Symbol class, which bothers me, but I think it's cleaner in the long-run.
	 YP( new BlockPos(0,1,0) ,0)
	,YN( new BlockPos(0,-1,0) ,1)
	,XP( new BlockPos(1,0,0) ,2)
	,XN( new BlockPos(-1,0,0) ,3)
	,ZP( new BlockPos(0,0,1) ,4)
	,ZN( new BlockPos(0,0,-1) ,5)
	,NU( new BlockPos(0,0,0) ,6);
	
	public BlockPos mod;
	public int tx;
	
	private Symbol( BlockPos pos ,int tx) {
		this.mod = pos;
		this.tx = tx;
	}
	
	public int toInt() {
		return this.tx;
	}
	
	
	public static Symbol[] randomValues() {
		return Symbol.randomValues( 3);
	}
	
	public static Symbol[] randomValues( int numOfValues) {
		if( numOfValues > 6) { numOfValues = 6; }
		
		Symbol[] choices = Symbol.nonNullValues();
		int[] weights = new int[] { 1 ,1 ,1 ,1 ,1 ,1};
		WeightedChoice a = new WeightedChoice( choices ,weights);
		
		Symbol[] output = new Symbol[numOfValues];
		for( int i=0; i<numOfValues; i++) {
			output[i] = (Symbol) a.pick();
		}
		return output;
	}
	
	public static Symbol[] nonNullValues() {
		Symbol[] noNull = Arrays.copyOfRange( Symbol.values() ,0 ,6);
		return noNull;
	}
	
	public static Symbol intToSymbol( int n) {
		switch( n) {
		case 0: return Symbol.YP;
		case 1: return Symbol.YN;
		case 2: return Symbol.XP;
		case 3: return Symbol.XN;
		case 4: return Symbol.ZP;
		case 5: return Symbol.ZN;
		}
		return Symbol.NU;
	}
	
}

