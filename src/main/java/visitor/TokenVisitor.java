package visitor;

import token.brace.Brace;
import token.number.NumberToken;
import token.operation.Operation;

public interface TokenVisitor {

    void visit(NumberToken token);

    void visit(Brace token);

    void visit(Operation token);
}
