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
	public TestcaseGenerator(FileWriter filewriter) {
		this.output = filewriter;
	}
	
	public TestcaseGenerator() { }	// console output will shows test cases
	
	public void generate() {
		genProg();	
		genToken("\n"); 
	}

	// same as genExpr except the probabilities of individual cases
	private void genProg() {
		CasesGroup cases = CasesGroup.getInstance();
		
    	cases.onCases(Probability.Percent5, ()-> genId())
                .onCases(Probability.Percent5, ()-> genConst())
                .otherwise(Probability.Percent90,()->genList());

	}

	private void genExpr() {
		CasesGroup cases = CasesGroup.getInstance();
    	cases.onCases(Probability.Percent40, ()-> genId())
                .onCases(Probability.Percent40, ()-> genConst())
                .otherwise(Probability.Percent20,()->genList());
    	blank();
	}
	
	private void genIntExpr() {
		CasesGroup cases = CasesGroup.getInstance();
    	cases.onCases(Probability.Percent40, ()-> genId())
                .onCases(Probability.Percent40, ()-> genIntConst())
                .otherwise(Probability.Percent20,()->genList());
    	blank();
	}
	
	private void genBoolExpr() {
		CasesGroup cases = CasesGroup.getInstance();
    	cases.onCases(Probability.Percent40, ()-> genId())
                .onCases(Probability.Percent40, ()-> genBoolConst())
                .otherwise(Probability.Percent20,()->genList());
    	blank();
	}
	
	int listDepth = 10;
	private void genList() {
		if (listDepth <= 0) {
			genToken("()");
			listDepth = 10;
			return;
		}
			
		genToken("(");
        listDepth--;
    	CasesGroup cases = CasesGroup.getInstance();
    	cases.onCases(Probability.Percent25, ()-> genBinExpr())
              .onCases(Probability.Percent25, ()-> genKeyword())
              .onCases(Probability.Percent20, ()->genPredicates())
               .otherwise(Probability.Percent30,()->genList());
    	
    	genToken(")");
    	blank();
	}
	
	int listItemNumber = 5; // for the formal argument list for lambda's and define's
	
	private void genIdListTail() {
		if (listItemNumber < 0)  {
			listItemNumber = 5;
			return;
		}
		listItemNumber--;
		blank(); genId();
		CasesGroup cases = CasesGroup.getInstance();
		cases.onCases(Probability.Percent30, ()->{blank(); genIdListTail();})
			.otherwiseDoNothing(Probability.Percent70);
	}
	
	private void genIdList() {

		genToken("(");
		genId(); genIdListTail();
		genToken(")");
    	blank();
	}
	

	private void genFormalArgs() {
		CasesGroup cases = CasesGroup.getInstance();
        cases.onCases(Probability.Percent20, () -> genId() )
                .otherwise(Probability.Percent80,()-> genIdList());
        blank(); 		
	}

	private void genBinExpr() {
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

	private void genKeyword() {
		CasesGroup cases = CasesGroup.getInstance();
        cases.onCases(Probability.Percent15, () -> { genToken("define"); genFormalArgs(); genExpr();} )
                .onCases(Probability.Percent15, () -> {genToken("cond"); genBoolExpr(); genExpr(); genExpr();})
                .onCases(Probability.Percent10, () -> {genToken("not");  genExpr();})
                .onCases(Probability.Percent10, () -> {genToken("quote");genList();})
                .onCases(Probability.Percent10, () -> {genToken("lambda"); genFormalArgs(); genList();})
                .onCases(Probability.Percent15, () -> {genToken("car");	genList();})
                .onCases(Probability.Percent15, () -> {genToken("cdr"); genList();})
                .otherwise(Probability.Percent10,()-> {genToken("cons"); genExpr(); genList();});
        blank(); 
	}


	private void genPredicates() {
		CasesGroup cases = CasesGroup.getInstance();
        cases.onCases(Probability.Percent40, 	() -> {genToken("eq?"); genExpr(); genExpr();})
                .onCases(Probability.Percent30, () -> {genToken("atom?"); genExpr();})
                .otherwise(Probability.Percent30,()-> {genToken("null?"); genExpr();}); 
        blank();
	}

	private void genId(){
        int i = new Random().nextInt(26);
        char c = (char) (i + (int)'a');
        print(((Character)c).toString());
        blank();
    }
	
	private void genConst() {
		CasesGroup cases = CasesGroup.getInstance();
    	cases.onCases(Probability.Percent30, ()-> genIntConst())
    			.onCases(Probability.Percent20, ()-> genBoolConst())
                .otherwise(Probability.Percent50,()-> genQuotedConst());
	}

    private void genQuotedConst() {
    	genToken("'");
    	CasesGroup cases = CasesGroup.getInstance();
    	cases.onCases(Probability.Percent50, ()-> genId())		
                .otherwise(Probability.Percent50,()-> genList());
    	blank();
	}
    
    private void genIntConst(){
        int i = new Random().nextInt(100);
        print(((Integer)i).toString());
        blank();
    }
    
    private void genBoolConst(){
        CasesGroup cases = CasesGroup.getInstance();
        cases.onCases(Probability.Percent50, () -> print("#F"))
                .otherwise(Probability.Percent50,()-> print("#T"));
        blank();
    }
    
    private void genToken(String s) {
        print(s);
        blank();
    }
    
    private void blank(){
        print(" ");
    }
    
    private void print(String s) {
    	if (output == CONSOLE_OUTPUT) {	// to console
    		System.out.print(s);
    	}
		else {							// to a file
			try {
				output.write(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
    
}

	