package cute19.interpreter.lexer;

import java.io.FileNotFoundException;


class ScanContext {
	private final CharStream input;
	private StringBuilder builder;
	
	ScanContext(String input) throws FileNotFoundException {
		this.input = CharStream.from(input);
		this.builder = new StringBuilder();
	}
	
	CharStream getCharStream() {
		return input;
	}
	
	String getLexime() {
		String str = builder.toString();
		builder.setLength(0);
		return str;
	}
	
	void append(char ch) {
		builder.append(ch);
	}
}
