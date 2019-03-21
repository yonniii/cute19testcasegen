package cute19.testcasegen;

class FuncGenerator {
	int NumOfArgs = 5; // for the formal argument list for lambda's and define's
		
	TestcaseGenerator gen;
	public FuncGenerator(TestcaseGenerator testcaseGenerator) {
		gen = testcaseGenerator;
	}

	// formal arguments
	private void genArgListTail() {
		if (NumOfArgs < 0)  {
			NumOfArgs = 5;
			return;
		}
		NumOfArgs--;
		blank(); genId();
		CasesGroup cases = CasesGroup.getInstance();
		cases.onCases(Probability.Percent30, ()->{blank(); genArgListTail();})
			.otherwiseDoNothing(Probability.Percent70);
	}
	
	private void genArgList() {

		genToken("(");
		genId(); genArgListTail();
		genToken(")");
    	blank();
	}
	

	private void genFormalArgs() {
		CasesGroup cases = CasesGroup.getInstance();
        cases.onCases(Probability.Percent20, () -> genId() )
                .otherwise(Probability.Percent80,()-> genArgList());
        blank(); 		
	}
	
	// currently we do not consider actual arguments in detailed : to do!
	private void genActualArgs() {
		genPlainList();
	}
	
	// function definition with define or lambda
	void genFunDef() {
		CasesGroup cases = CasesGroup.getInstance();
        cases.onCases(Probability.Percent70, () -> genFunDef("define") )
                .otherwise(Probability.Percent30,()-> genFunDef("lambda"));
	}
	
	void genFunDef(String keyword) {
		if (keyword == "define" || keyword == "lambda")
			return;
		genToken(keyword);
		genFormalArgs(); 
		genExpr();
	}
	
	// function application, 
	// that is, function definition or function id actualArgList
	public void genFuncApplication() {
		CasesGroup cases = CasesGroup.getInstance();
        cases.onCases(Probability.Percent20, () -> genFunDef() )
                .otherwise(Probability.Percent80,()-> genId());
        
        genActualArgs();
	}
	
	// delegated methods
	void blank()			{ gen.blank(); }
	void genToken(String s) { gen.genToken(s);}
	void genExpr()			{ gen.genExpr();}
	void genId() 			{ gen.genId();}
	void genPlainList()		{ gen.genPlainList();}
	
}	