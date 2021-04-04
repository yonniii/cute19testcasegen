package cute19.main;

import cute19.testcasegen.main.TestMain;
import cute19.interpreter.interpreter.CuteInterpreter;
import cute19.prover.ProveMain;

import java.io.IOException;

class main{
    public static void main(String[] args){
//        String genedFilename = "out";
        String genedProgram;
        String reasonableProgram = null;
        while(true){
            genedProgram = TestMain.testCaseGeneration2String(1);
            try{
                reasonableProgram = ProveMain.prove(genedProgram);
            }catch (Exception e){
                System.out.println("err");
                continue;
            }
            if(reasonableProgram != null){
                System.out.println(reasonableProgram);
                break;
            }
        }
    }
}