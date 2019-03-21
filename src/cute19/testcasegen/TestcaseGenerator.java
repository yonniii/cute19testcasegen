package cute19.testcasegen;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Created by eschough on 2019-03-13.
 * 
 * class TestcaseGenerator : Actual Test Case Generation Functions
 */
public class TestcaseGenerator {
    /*    program -> expr
     * 	  expr -> list | const | id
     * 	  const -> intconst | boolconst
     * 	  boolconst -> #T | #F
     * 	  list -> 	'(' binop expr expr ')' | '(' 'define' expr expr ')' | '(' 'eq?' expr expr ')'
     * 				| '(' cond expr expr expr ')'
     * 				| '(' 'not' expr ')' |  (atom?' expr ')' | '(' 'null?' expr ')'
     * 				| '(' 'lambda' expr list ')'
     * 				| '(' car list ')'  | '(' cdr list ')'
     * 				| '(' cons expr list ')'
     * 				| '(' ')'
     * 				| '(' expr')'
     * 				| '(' expr list ')'
     * 
     * 	todo: genIDList, genBoolExpr, genIntExpr 분리? type 생각해서?
    */
    
	static final FileWriter CONSOLE_OUTPUT = (FileWriter) null;
	FileWriter output = CONSOLE_OUTPUT;

	BasicsGenerator basicsGen 	= new BasicsGenerator(this);
	ExprGenerator 	exprGen 	= new ExprGenerator(this);
	ConstnIdGenerator constIdGen = new ConstnIdGenerator(this);
	ListGenerator 	listGen 	= new ListGenerator(this);
	FuncGenerator 	funcGen 	= new FuncGenerator(this);
	
	public TestcaseGenerator(FileWriter filewriter) {
		this.output = filewriter;
	}
	
	public TestcaseGenerator() { }	// console output will shows test cases
	
	public void generate() {
		genProg();	
		genToken("\n"); 
	}

	// same as genExpr except the probabilities of individual cases
	void genProg() {
		CasesGroup cases = CasesGroup.getInstance();
		
    	cases.onCases(Probability.Percent5, ()-> genId())
                .onCases(Probability.Percent5, ()-> genConst())
                .otherwise(Probability.Percent90,()->listGen.genList());
	}
    
    // delegates
    void blank() 				{basicsGen.blank();}
    void genToken(String s) 	{basicsGen.genToken(s);}
    void genId() 				{constIdGen.genId();}
	void genConst() 			{constIdGen.genConst();}
	void genBoolConst() 		{constIdGen.genBoolConst();}
	void genIntConst() 			{constIdGen.genIntConst();}
    void genExpr()				{exprGen.genExpr();}
	void genBinExpr() 			{exprGen.genBinExpr();}
	void genPredefinedOps() 	{exprGen.genPredefinedOps();}
	void genPredicates() 		{exprGen.genPredicates();}

	void genList() 				{listGen.genList(); }
    void genPlainList() 		{listGen.genPlainList();}
    void genQuotedPlainList() 	{listGen.genQuotedPlainList();}
    void genFunDef(String s) 	{funcGen.genFunDef(s);}
    void genFuncApplication() 	{funcGen.genFuncApplication();}
    
}

	