package cute19.testcasegen;

/**
 * 
 * @author Eun-Sun Cho 2019.3.16
 * enum Probability:
 * 	constant PercentX represents a probability in % 
 *
 */
enum Probability {
	Percent5(5),
    Percent10(10),Percent15(15),
    Percent20(20),Percent25(25),
    Percent30(30),Percent35(35),
    Percent40(40),Percent45(45),
    Percent50(50),Percent55(55),
    Percent60(60),Percent65(65),
    Percent70(70),Percent75(75),
    Percent80(80),Percent85(85),
    Percent90(90),Percent95(95),
    Percent0(0),Percent100(100);
	
    private int percent;
    
    Probability(int percent) {
        this.percent = percent;
    }    
    int percent() {
    	return percent;
    }

	public boolean isProperValue() {
		if (percent < 0 || percent > 100) 
			return false;
		return true;
	}
}