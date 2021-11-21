package visitor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import token.Token;
import token.brace.Brace;
import token.brace.LeftBrace;
import token.number.NumberToken;
import token.operation.Minus;
import token.operation.Operation;
import token.operation.Plus;

public class ParserVisitor implements TokenVisitor {
    private Deque<Token> deque;
    private List<Token> result;

    @Override
    public void visit(NumberToken token) {
        result.add(token);
    }

    @Override
    public void visit(Brace token) {
        if (token instanceof LeftBrace) {
            deque.add(token);
        } else {
            while (!(deque.peekLast() instanceof LeftBrace)) {
                if (deque.isEmpty()) {
                    throw new IllegalStateException();
                }

                result.add(deque.removeLast());
            }
            deque.removeLast();
        }
    }

    @Override
    public void visit(Operation token) {
        if (deque.isEmpty()) {
            deque.add(token);
            return;
        }

        if (deque.peekLast() instanceof LeftBrace) {
            deque.add(token);
            return;
        }

        if (!(deque.peekLast() instanceof Operation)) {
            throw new IllegalStateException();
        }

        while (!deque.isEmpty() && lowerOrEqPriority(token, (Operation) deque.peekLast())) {
            result.add(deque.removeLast());
        }

        deque.add(token);
    }

    private boolean lowerOrEqPriority(Operation op1, Operation op2) {
        if (op1 instanceof Plus || op1 instanceof Minus) {
            return true;
        }
        if (op2 instanceof Plus || op2 instanceof Minus) {
            return false;
        }
        return true;
    }

    public List<Token> parse(List<Token> tokens) {
        deque = new ArrayDeque<>();
        result = new ArrayList<>();
        for (Token token : tokens) {
            token.accept(this);
        }
        Iterator<Token> tokenIterator = deque.descendingIterator();
        while (tokenIterator.hasNext()) {
            Token next = tokenIterator.next();
            if (next instanceof LeftBrace) {
                throw new IllegalStateException();
            }
            result.add(next);
        }
        return result;
    }
}
