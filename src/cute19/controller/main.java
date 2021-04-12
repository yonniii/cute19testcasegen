package cute19.controller;

import cute19.testcasegen.main.TestMain;
import cute19.interpreter.interpreter.CuteInterpreter;
import cute19.prover.ProveMain;

import java.io.IOException;

class main{
    public static void main(String[] args){
//        String genedFilename = "out";
        String genedProgram;
        String reasonableProgram = null;
        long start = System.nanoTime();
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
        long elapsedTime = System.nanoTime() - start;
        System.out.println("time to solve : " + (elapsedTime/1000000000.0) + "sec" );
    }
}