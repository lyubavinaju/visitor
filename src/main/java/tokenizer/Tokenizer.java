package tokenizer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import token.Token;
import token.brace.LeftBrace;
import token.brace.RightBrace;
import token.number.NumberToken;
import token.operation.Div;
import token.operation.Minus;
import token.operation.Mul;
import token.operation.Plus;

public class Tokenizer {
    @Getter
    @Setter
    private State state;
    private static final int END_CHAR = -1;

    private enum State {
        START, END, ERROR, NUMBER
    }

    private static final Map<Character, Token> operations =
        Map.of('+', new Plus(),
            '-', new Minus(),
            '*', new Mul(),
            '/', new Div());

    private static final Map<Character, Token> braces =
        Map.of('(', new LeftBrace(),
            ')', new RightBrace());

    public List<Token> getTokens(InputStream inputStream) {
        List<Token> tokens = new ArrayList<>();
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        setState(State.START);
        try {
            int r = reader.read();
            if (r == END_CHAR) {
                setState(State.END);
            }
            StringBuilder number = new StringBuilder();
            while (state != State.END && state != State.ERROR) {
                if (Character.isWhitespace(r)) {
                    r = reader.read();
                    continue;
                }
                char curChar = (char) r;
                switch (state) {
                    case START:
                        if (operations.containsKey(curChar)) {
                            tokens.add(operations.get(curChar));
                            r = reader.read();
                        } else if (braces.containsKey(curChar)) {
                            tokens.add(braces.get(curChar));
                            r = reader.read();
                        } else if (Character.isDigit(curChar)) {
                            setState(State.NUMBER);
                            number = new StringBuilder();
                        } else {
                            setState(State.ERROR);
                        }
                        break;
                    case NUMBER:
                        if (Character.isDigit(curChar)) {
                            number.append(curChar);
                            r = reader.read();
                        } else {
                            tokens.add(new NumberToken(Integer.parseInt(number.toString())));
                            setState(State.START);
                            number = new StringBuilder();
                        }
                        break;
                    default:
                        throw new IllegalStateException();
                }

                if (r == END_CHAR) {
                    setState(State.END);
                }
            }
            if (state == State.END && (number.length() > 0)) {
                tokens.add(new NumberToken(Integer.parseInt(number.toString())));
            }
        } catch (IOException e) {
            state = State.ERROR;
            tokens.clear();
        }
        return tokens;
    }

    public boolean hasError() {
        return state == State.ERROR;
    }
}
