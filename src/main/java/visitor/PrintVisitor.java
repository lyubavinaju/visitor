package visitor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import token.Token;
import token.brace.Brace;
import token.brace.LeftBrace;
import token.brace.RightBrace;
import token.number.NumberToken;
import token.operation.Div;
import token.operation.Minus;
import token.operation.Mul;
import token.operation.Operation;
import token.operation.Plus;

public class PrintVisitor implements TokenVisitor {
    private OutputStream outputStream;

    private static final Map<Class<? extends Operation>, Character> operations =
        Map.of(Plus.class, '+',
            Minus.class, '-',
            Mul.class, '*',
            Div.class, '/');

    private static final Map<Class<? extends Brace>, Character> braces =
        Map.of(LeftBrace.class, '(',
            RightBrace.class, ')');

    @Override
    public void visit(NumberToken token) {
        try {
            outputStream.write(String.valueOf(token.getNumber()).getBytes(StandardCharsets.UTF_8));
            outputStream.write(' ');
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(Brace token) {
        try {
            outputStream.write(braces.get(token.getClass()));
            outputStream.write(' ');
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(Operation token) {
        try {
            outputStream.write(operations.get(token.getClass()));
            outputStream.write(' ');
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public OutputStream print(List<Token> tokens) {
        outputStream = new ByteArrayOutputStream();
        tokens.forEach(token -> token.accept(this));
        return outputStream;
    }
}
