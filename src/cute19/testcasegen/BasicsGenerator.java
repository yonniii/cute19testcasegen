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
    	if (gen.output == TestcaseGenerator.CONSOLE_OUTPUT) {	// to console
    		System.out.print(s);
    	}
		else {							// to a file
			try {
				gen.output.write(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
}
