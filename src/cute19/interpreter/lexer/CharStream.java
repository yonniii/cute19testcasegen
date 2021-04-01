package cute19.interpreter.lexer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;

class CharStream {
	private final StringReader reader;
	private Character cache;
	
	static CharStream from(String input) throws FileNotFoundException {
		return new CharStream(new StringReader(input));
	}
	
	CharStream(StringReader reader) {
		this.reader = reader;
		this.cache = null;
	}
	
	Char nextChar() {
		if ( cache != null ) {
			char ch = cache;
			cache = null;
			
			return Char.of(ch);
		}
		else {
			try {
				int ch = reader.read();
				if ( ch == -1 ) {
					return Char.end();
				}
				else {
					return Char.of((char)ch);
				}
			}
			catch ( IOException e ) {
				throw new ScannerException("" + e);
			}
		}
	}
	
	void pushBack(char ch) {
		cache = ch;
	}
}
