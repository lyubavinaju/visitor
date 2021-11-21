package token.operation;

import token.Token;
import visitor.TokenVisitor;

public abstract class Operation implements Token {

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }
}
