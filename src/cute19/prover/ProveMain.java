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

    public static void main(String args[]){

        target.setInOut(new Object[] {1,2,3}, new Object[] {2,3,4});
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

        programToRun = target.getPrecondition();
        programToRun.add(genedCode);

        CuteInterpreter.callInterpreter(programToRun);

        return null;
    }
}

class Program{

    Object[] in;
    Object[] out;
    String program;

    public boolean setInOut(Object[] in, Object[] out){
        if(in.length != out.length){
            return false;
        }
        this.in = new Object[in.length];
        this.out = new Object[out.length];
        for (int i = 0; i < in.length; i++) {
            this.in[i] = in[i];
            this.out[i] = out[i];
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

    public List<String> getPrecondition(){
        String prame = "( %s %s %s )";
        List<String> codes = new ArrayList<>();

        if(in == null){
            return null;
        }

        for (int i = 0; i < in.length; i++) {
            codes.add(String.format(prame, "define", (char)('a'+i), in[i]));
        }
        return codes;
    }

}