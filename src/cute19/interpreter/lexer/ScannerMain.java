package cute19.interpreter.lexer;

public class ScannerMain {
    public static final void main(String... args) throws Exception {
//        ClassLoader cloader = ScannerMain.class.getClassLoader();
//        File file = new File(cloader.getResource("as04.txt").getFile());
//        testTokenStream(file);
    }

    // use tokens as a Stream
//    private static void testTokenStream(File file) throws FileNotFoundException {
//        Stream<Token> tokens = Scanner.stream(file);

//        try(FileWriter writer = new FileWriter("output04.txt");
//            PrintWriter pw  = new PrintWriter(writer))
//        {
//
//            tokens.map(ScannerMain::toString).forEach(pw::println);
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//    }

    private static String toString(Token token) {
        return String.format("%-3s: %s", token.type().name(), token.lexme());
    }
}
