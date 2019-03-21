package cute19.testcasegen;

/** 
 * collection of test functions for class CasesGroup 
 */
public class UnitTest4CasesGroup {
	public static void UnitTestCasesGroup() {
		CasesGroup cases = CasesGroup.getInstance();
    	cases.onCases(Probability.Percent10,  ()-> System.out.println("0-10"))
                .onCases(Probability.Percent10, ()-> System.out.println("10-20"))
                .onCases(Probability.Percent30, ()-> System.out.println("20-50"))
                .otherwise(Probability.Percent50, ()->System.out.println("50-100"));
	}
}
