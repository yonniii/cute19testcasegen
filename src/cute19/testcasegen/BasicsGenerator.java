package cute19.testcasegen;

import java.io.IOException;

public class BasicsGenerator {
	private TestcaseGenerator gen;
	
	BasicsGenerator(TestcaseGenerator gen) {
		this.gen = gen;
	}
	
    void genToken(String s) {
        print(s);
        blank();
    }
    
    void blank(){
        print(" ");
    }
    
    private void print(String s) {
    	if (gen.output != TestcaseGenerator.FILE_OUTPUT) {	// to a file
			try {
				gen.output.write(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	else if(gen.sb_output != TestcaseGenerator.SB_OUTPUT){ // to a String data
    		gen.sb_output.append(s);
		}
		else {							// to console
				System.out.print(s);
		}
    }
}
