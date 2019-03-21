package cute19.testcasegen;

import java.util.Random;

public class ConstnIdGenerator {
	private TestcaseGenerator gen;
	
	ConstnIdGenerator(TestcaseGenerator gen) {
		this.gen = gen;
	}
	
	void genConst() {
		CasesGroup cases = CasesGroup.getInstance();
    	cases.onCases(Probability.Percent30, ()-> genIntConst())
    			.onCases(Probability.Percent20, ()-> genBoolConst())
                .otherwise(Probability.Percent50,()-> genSimpleQuoted());
	}

    void genSimpleQuoted() {
    	genToken("'");
    	CasesGroup cases = CasesGroup.getInstance();
    	cases.onCases(Probability.Percent50, ()-> genId())		
                .otherwise(Probability.Percent50,()-> genList());
    	blank();
	}
    
    void genIntConst(){
        int i = new Random().nextInt(100);
        genToken(((Integer)i).toString());
        blank();
    }
    
    void genBoolConst(){
        CasesGroup cases = CasesGroup.getInstance();
        cases.onCases(Probability.Percent50, () -> genToken("#F"))
                .otherwise(Probability.Percent50,()-> genToken("#T"));
        blank();
    }
    
	void genId(){
        int i = new Random().nextInt(26);
        char c = (char) (i + (int)'a');
        genToken(((Character)c).toString());
        blank();
    }
    
	// delegates
	void blank()			{gen.blank(); }
	void genToken(String s) {gen.genToken(s);}
	void genList() 			{gen.genList();}
}
