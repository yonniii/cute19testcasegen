package cute19.util;

public class MyMsg {
	// to be deleted at deployment
	public static void debuggingLog(String msg) {
		//System.out.println(msg);
		
	}

	// to remain even at deployment
	public static void note(String msg) {
		System.out.println(msg);		
	}

}
