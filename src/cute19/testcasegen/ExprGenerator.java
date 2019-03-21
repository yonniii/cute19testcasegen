package cute19.testcasegen;

public class ExprGenerator {
	private TestcaseGenerator gen;
	
	ExprGenerator(TestcaseGenerator testcasegenerator) {
		this.gen = testcasegenerator;
	}
	
	void genExpr() {
		CasesGroup cases = CasesGroup.getInstance();
    	cases.onCases(Probability.Percent40, ()-> genId())
                .onCases(Probability.Percent40, ()-> genConst())
                .otherwise(Probability.Percent20,()->genList());
    	blank();
	}
	
	void genIntExpr() {
		CasesGroup cases = CasesGroup.getInstance();
    	cases.onCases(Probability.Percent40, ()-> genId())
                .onCases(Probability.Percent40, ()-> genIntConst())
                .otherwise(Probability.Percent20,()->genFuncApplication());
    	blank();
	}
	
	void genBoolExpr() {
		CasesGroup cases = CasesGroup.getInstance();
    	cases.onCases(Probability.Percent40, ()-> genId())
                .onCases(Probability.Percent40, ()-> genBoolConst())
                .otherwise(Probability.Percent20,()->genFuncApplication());
    	blank();
	}
	
	
	void genBinExpr() {
        CasesGroup cases = CasesGroup.getInstance();
        cases.onCases(Probability.Percent20, () -> {genToken("+"); genIntExpr(); genIntExpr();})
                .onCases(Probability.Percent20, () -> {genToken("-"); genIntExpr(); genIntExpr();})
                .onCases(Probability.Percent20, () -> {genToken("*"); genIntExpr(); genIntExpr();})
                .onCases(Probability.Percent10, () -> {genToken("/"); genIntExpr(); genIntExpr();})
                .onCases(Probability.Percent10, () -> {genToken(">"); genIntExpr(); genIntExpr();})
                .onCases(Probability.Percent10, () -> {genToken("<"); genIntExpr(); genIntExpr();})
                .otherwise(Probability.Percent10,()-> {genToken("="); genExpr(); genExpr();}); // 20%
        blank();
	}

	void genPredefinedOps() {
		CasesGroup cases = CasesGroup.getInstance();
        cases.onCases(Probability.Percent15, () -> {genFunDef("define"); })
        		.onCases(Probability.Percent10, () -> {genFunDef("lambda"); })
                .onCases(Probability.Percent15, () -> {genToken("cond"); genBoolExpr(); genExpr(); genExpr();})
                .onCases(Probability.Percent10, () -> {genToken("not");  genBoolExpr();})
                .onCases(Probability.Percent10, () -> {genToken("quote");genList();})
                
                .onCases(Probability.Percent15, () -> {genToken("car");	genList();})
                .onCases(Probability.Percent15, () -> {genToken("cdr"); genPlainList();})
                .otherwise(Probability.Percent10,()-> {genToken("cons"); genExpr(); genPlainList();});
        blank(); 
	}
	
	void genPredicates() {
		CasesGroup cases = CasesGroup.getInstance();
        cases.onCases(Probability.Percent40, 	() -> {genToken("eq?"); genExpr(); genExpr();})
                .onCases(Probability.Percent30, () -> {genToken("atom?"); genExpr();})
                .otherwise(Probability.Percent30,()-> {genToken("null?"); genExpr();}); 
        blank();
	}
	
	// delegates
	void genId () 			{gen.genId();}
	void genConst() 		{gen.genConst();}
	void genBoolConst() 	{gen.genBoolConst();}
	void genIntConst() 		{gen.genIntConst();}
	
	void genList() 			{gen.genList();}
	void genPlainList() 	{gen.genPlainList();}
	
	void genFunDef(String s){gen.genFunDef(s);}
	void genFuncApplication(){gen.genFuncApplication();}
	
	void genToken(String s)	{gen.genToken(s);}
	void blank() 			{gen.blank();}
}
