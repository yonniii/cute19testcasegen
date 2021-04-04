package cute19.prover;

import cute19.interpreter.interpreter.CuteInterpreter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ProveMain {

    private static Program target = new Program();
    private static int eachInputSize = 3;
    private static int totalInputSize = 4;
    public static void main(String[] args){


        Object[][] input = {{1,1,1},{2,2,2},{3,3,3},{4,4,4}};
        Object[][] output = {{2},{3},{4},{5}};

        target.setInOut(input, output, eachInputSize,1);
        target.setProgram("x + 1");
        try {
            checker(args[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String checker(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filename),
                StandardCharsets.UTF_8);
        List<String> programToRun;
        String genedCode = lines.get(0).trim();

        for (int i = 0; i < totalInputSize; i++) {
            programToRun = target.getPrecondition(i);
            if(programToRun == null){
                continue;
            }
            programToRun.add(genedCode);
            List<String> results;
            try{
                results = CuteInterpreter.callInterpreter(programToRun);
            } catch (Exception e){
//                e.printStackTrace();
                continue;
            }
            String lastResult;
            if(results.size()>0){
                lastResult = results.get(results.size()-1).trim();
            } else {
                continue;
            }

            if(isReasonable(lastResult, (target.out[i][0]).toString())){
//                System.out.println(genedCode);
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

    private static boolean isReasonable(String gened, String target) {
        return gened.equals(target);
    }
}

class Program{

    Object[][] in;
    Object[][] out;
    String program;
    int eachInputSize;
    int eachOutputSize;

    public boolean setInOut(Object[][] in, Object[][] out, int eachInputSize, int eachOuputSize){
        if(in.length != out.length){
            return false;
        }
        this.eachInputSize = eachInputSize;
        this.eachOutputSize = eachOuputSize;
        this.in = new Object[in.length][eachInputSize];
        this.out = new Object[out.length][eachOuputSize];
        for (int i = 0; i < in.length; i++) {
            System.arraycopy(in[i],0, this.in[i],0,eachInputSize);
            System.arraycopy(out[i],0, this.out[i],0,eachOuputSize);
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

    public List<String> getPrecondition(int ioCount){
        String prame = "( %s %s %s )";
        List<String> codes = new ArrayList<>();

        if(in == null){
            return null;
        }

        for (int i = 0; i < this.eachInputSize; i++) {
            codes.add(String.format(prame, "define", (char)('a'+i), in[ioCount][i]));
        }
        return codes;
    }

}