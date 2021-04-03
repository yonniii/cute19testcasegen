package cute19.main;

import cute19.testcasegen.main.TestMain;
import cute19.interpreter.interpreter.CuteInterpreter;
import cute19.prover.ProveMain;

import java.io.IOException;

class main{
    public static void main(String[] args){
        String genedFilename = "out";
        TestMain.main(new String[]{genedFilename});
        ProveMain.main(new String[]{genedFilename});
    }
}