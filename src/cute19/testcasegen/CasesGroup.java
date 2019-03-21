package cute19.testcasegen;

import java.util.Random;

import cute19.util.MyMsg;

/**
 * Created by eschough on 2019-03-16.
 * 
 * class CasesGroup: represents a set of ranges {[min1, max1], [min2, max2]...}
 *  which max1 == min2, max2 == min3, ....
 *  and max_N == 100.
 *  Thus, it is modeled by sliding window of [min, max], starting from [0, M]
 */
class CasesGroup{
    private int randnum  = -1;
    
    Range currentRange = new Range();
    boolean alreadyGenerated = false; 
    
    private  CasesGroup() {
        randnum = new Random().nextInt(100);
    }
    
    public static CasesGroup getInstance(){
        return new CasesGroup();
    }
    
    static final boolean FAILED = false;
    static final boolean SUCCEEDED = true;
    	
    /**
    * usage  : CasesGroup.getInstance(..).onCases(..).onCases(..)...otherwise(..);
    */
    CasesGroup onCases(Probability p, GenFunction genFunc) {    	
    	if (!p.isProperValue()) {
        	MyMsg.note("## Percent should be larger than 0 and smaller than 100");
            return this;
        }    	
   
        if (currentRange.slide(p.percent()) == FAILED) {	
        	MyMsg.debuggingLog("## min of the current range should be smaller than max");
        	return this;
        }
    
        if (!alreadyGenerated) {
	        if (currentRange.contains(randnum)) {
	            genFunc.gen();
	            alreadyGenerated = true; 
	        } 
        }
        return this;
    }
    
	void otherwise(Probability p, GenFunction genFunc) {
		if (currentRange.slide(p.percent()) == FAILED) {	
        	MyMsg.debuggingLog("## min should be smaller than max: " + currentRange + ", givenP=" + p.percent());
        	return;
        }
		
    	if (! currentRange.full(100)) { 
    		MyMsg.note("## shold be 100::"+ currentRange + ", givenP=" + p.percent());
    		return;
    	}
    	
    	if (currentRange.contains(randnum)) {
	            genFunc.gen();
	            alreadyGenerated = true; // actually, unnecessary
	    } 
    }
    
    void otherwiseDoNothing(Probability p){
    	otherwise(p, ()->{});
    }
}