package visitor;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import token.Token;
import token.brace.Brace;
import token.number.NumberToken;
import token.operation.Div;
import token.operation.Minus;
import token.operation.Mul;
import token.operation.Operation;
import token.operation.Plus;

public class CalcVisitor implements TokenVisitor {
    private Deque<NumberToken> deque;

    private static final Map<Class<? extends Operation>, BiFunction<NumberToken, NumberToken, Integer>> operations
        = Map.of(Plus.class, (NumberToken a, NumberToken b) -> a.getNumber() + b.getNumber(),
        Minus.class, (a, b) -> a.getNumber() - b.getNumber(),
        Mul.class, (a, b) -> a.getNumber() * b.getNumber(),
        Div.class, (a, b) -> a.getNumber() / b.getNumber());

    @Override

    public void visit(NumberToken token) {
        deque.add(token);
    }

    @Override
    public void visit(Brace token) {
        throw new IllegalStateException();
    }

    @Override
    public void visit(Operation token) {
        if (deque.isEmpty()) {
            throw new IllegalStateException();
        }
        NumberToken tok2 = deque.removeLast();
        if (deque.isEmpty()) {
            throw new IllegalStateException();
        }

        NumberToken tok1 = deque.removeLast();
        BiFunction<NumberToken, NumberToken, Integer> function =
            operations.get(token.getClass());

        deque.add(new NumberToken(function.apply(tok1, tok2)));
    }

    public int calc(List<Token> tokens) {
        deque = new ArrayDeque<>();
        for (Token token : tokens) {
            token.accept(this);
        }
        if (deque.size() != 1) {
            throw new IllegalStateException();
        }

        return deque.removeLast().getNumber();
    }
}
