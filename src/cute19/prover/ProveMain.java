package cute19.prover;

import cute19.interpreter.interpreter.CuteInterpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProveMain {

    private static Program target = new Program();
    private static int eachInputSize = 3; // Number of preconditions for one input case
    private static int totalInputSize = 4; //Number of input cases in total specification


    public static void main(String[] args){

        Object[][] input = {{1,1,1},{2,2,2},{3,3,3},{4,4,4}};
        Object[][] output = {{2},{3},{4},{5}};

        target.setInOut(input, output, eachInputSize,1);
        target.setProgram("x + 1");
        try {
            checker(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String prove(String program){
        Object[][] input = {{1,1,1},{2,2,2},{3,3,3},{4,4,4}};
        Object[][] output = {{2},{3},{4},{5}};
        String checked = null;

        target.setInOut(input, output, eachInputSize,1);
        target.setProgram("x + 1");
        try {
            checked = checker(program);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return checked;
    }

    private static String checker(String program)  {
        List<String> programToRun;
        String genedCode = program.trim();

        for (int i = 0; i < totalInputSize; i++) {
            programToRun = target.getPrecondition(i); // get codes to set precondition
            if(programToRun == null){
                continue;
            }
            programToRun.add(genedCode);
            List<String> results;
            try{
                results = CuteInterpreter.callInterpreter(programToRun); // exec interpreter for generated program
            } catch (Exception e){
//                e.printStackTrace();
                continue;
            }
            String lastResult;
            // check whether exec result is valid
            if(results.size()>0){
                lastResult = results.get(results.size()-1).trim();
                // The last element in the execution result list is the result of the generated program.
            } else {
                continue;
            }

            // If the execution result of the generated program is the same as the output specification, the generated program is returned.
            if(isReasonable(lastResult, (target.out.get(i).get(0)).toString())){
//                System.out.println(genedCode);
                // If all cases pass, return the generated program
                if(i == totalInputSize-1){
                    System.out.println(lastResult + "\t" + (target.out[i][0]).toString());
                    return genedCode;
                }
            } else {
                return null;
            }
//            for (String result: results) {
//                System.out.println(result);
//            }
        }

        return null;
    }

    //    Checks whether the result of executing the generated program is the same as the output specification
    private static boolean isReasonable(String gened, String target) {
        return gened.equals(target);
    }
}

class Program{

    List<List<Object>> in;
    List<List<Object>> out;
    String program;
    int eachInputSize;
    int eachOutputSize;

    //    set input and output to field
    public boolean setInOut(Object[][] in, Object[][] out, int eachInputSize, int eachOuputSize){
        if(in.length != out.length){
            return false;
        }
        this.eachInputSize = eachInputSize;
        this.eachOutputSize = eachOuputSize;
        this.in = new ArrayList<>();
        this.out = new ArrayList<>();
        for (int i = 0; i < in.length; i++) {
            this.in.add(Arrays.asList(in[i]));
            this.out.add(Arrays.asList(out[i]));
        }
        return true;
    }

    public boolean setProgram(String p){
        if(p == null){
            return false;
        }
        this.program = p;
        return true;
    }

    //    Before run generated program, define variants for input spec.
    public List<String> getPrecondition(int ioCount){
        String prame = "( %s %s %s )";
        List<String> codes = new ArrayList<>();

        if(in == null){
            return null;
        }

        for (int i = 0; i < this.eachInputSize; i++) {
            codes.add(String.format(prame, "define", (char)('a'+i), in.get(ioCount).get(i)));
        }
        return codes;
    }

}