package token.brace;

import token.Token;
import visitor.TokenVisitor;

public abstract class Brace implements Token {

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }
}
