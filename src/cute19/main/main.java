package cute19.main;

import cute19.testcasegen.main.TestMain;
import cute19.interpreter.interpreter.CuteInterpreter;

import java.io.IOException;

class main{
    public static void main(String[] args){
        String genedFilename = "out";
        TestMain.main(new String[]{genedFilename});
        try {
            CuteInterpreter.callInterpreter(genedFilename);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}