package cute19.testcasegen;

/**
 * Created by eschough on 2019-03-16.
 * 
 * class Range: simple range with [min, max]
 		a set of ranges divides (and covers) [0-100]
 		which are grouped by a CasesGroup instance 
 */
class Range {
	private int min = 0;
	private int max = 0;
	 
	/**
	 * boolean slide(int givenP)
	 * 	update min and max's which makes a move to right direction of the range
	 *  eg. ---[---]--- => ------[--]-  
	 */
	boolean slide(int givenP) {
    	int prevMax = max;
        min = prevMax;
        max = prevMax + givenP;
        return min < max;
	}
	
	public boolean contains(int value) {
		return min <= value  && value < max;
	}

	public boolean full(int limit) {
		if (max == limit) return true;
		return false;
	}
	
	@Override
	public String toString(){
		return "(" + min + " ," + max + ")"; 		
	}

}