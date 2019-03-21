package cute19.testcasegen.main;

import java.io.FileWriter;
import java.io.IOException;

import cute19.testcasegen.TestcaseGenerator;
import cute19.util.MyMsg;

/**
 * Created by eschough on 2019-03-16.
 * 
 * class TestMain : main function for test case generation
 */
public class TestMain {
	/**												
	* file output									
	*/
	private static void testCaseGeneration2File(int numOfTCases, String filename) {
		try (FileWriter filewriter = new FileWriter(filename)) {
			for (int  i = 0; i < numOfTCases ; i++) {
				new TestcaseGenerator(filewriter).generate();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**												
	* console output								
	*/
	private static void testCaseGeneration2Console(int numOfTCases) {
		for (int  i = 0; i< numOfTCases ; i++) {
			new TestcaseGenerator().generate();
		}
	}

	public static void main(String args[]){
		int numOfTCases = 10;
    	
    	if (args.length == 0)		// to console
    		testCaseGeneration2Console(numOfTCases);
    	else if (args.length == 1)	// to a file
			testCaseGeneration2File(numOfTCases, args[0]);
    	else
    		MyMsg.note("## Usage of commandline arguments: for file output, specify the file name; for standard output, do not specify any argument.");
    }
    
  
    
}
