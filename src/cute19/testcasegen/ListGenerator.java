package cute19.testcasegen;

class ListGenerator {
	int listLength = 0;    	// length for one list 
	int listDepth = 0; 		// testcase-wide maximum depth
	static public final int MAXLENGTH = 7;
	static public final int MAXDEPTH = 5;
	
	TestcaseGenerator gen;
	
	public ListGenerator(TestcaseGenerator testcaseGenerator) {
		this.gen = testcaseGenerator;
	}

	private void genOneElement() {
		CasesGroup cases = CasesGroup.getInstance();
		cases.onCases(Probability.Percent10, ()-> genToken("()"))	// null , the last element in this list
			.otherwise(Probability.Percent90, ()-> genExpr());				// one element, the last element in this list
	}
	
	// generate list of elements without (, )
	private void genPlainListTail() {
		if (listLength > MAXLENGTH)
			return;
		listLength++;

		genOneElement();
		
		CasesGroup cases = CasesGroup.getInstance();
    	cases.onCases(Probability.Percent80, ()->{blank(); genPlainListTail();})	// more elements (recursion)
    	.otherwiseDoNothing(Probability.Percent20);
		blank();
	}	        
	
    // generate one list with (, )
    // this includes '()'
	// this does not include (+ a c), (define ..), that is, predefined list is not allowed.
	// so, this might be free style list ( a b c d e 1 2 ) which does not have meaning without quote
	// or might be function application (custom funtion call with actual parameters) eg. (f a b)
	void genPlainList() {
		listLength = 0;
		gen.genToken("("); listDepth++;
		if (listDepth < MAXDEPTH) {
			genPlainListTail();
		}
		gen.genToken(")"); listDepth--;
		gen.blank();
	}
	
	void genQuotedPlainList(){
		gen.genToken("'");
		genPlainList();
	}
	
	// includes binary arithmetic expr, predefinedops, predicates as well as plain lists. 
	void genList() {
		listLength = 0;
		genToken("("); listDepth++;
		if (listDepth < MAXDEPTH) {
			CasesGroup cases = CasesGroup.getInstance();
	    	cases.onCases(Probability.Percent25, ()-> genBinExpr())
	              .onCases(Probability.Percent25, ()-> genPredefinedOps())
	              .onCases(Probability.Percent20, ()->genPredicates())
	               .otherwise(Probability.Percent30,()->genPlainList());
		}
    	genToken(")"); listDepth--;
    	blank();
	}
	
	// delegated methods
	void blank()			{ gen.blank(); }
	void genToken(String s) { gen.genToken(s);}
	
	void genExpr()			{ gen.genExpr();}
	void genBinExpr() 		{ gen.genBinExpr();}
	void genPredefinedOps() { gen.genPredefinedOps();}
	void genPredicates() 	{ gen.genPredicates();}
	
}
