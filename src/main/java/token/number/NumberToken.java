package token.number;

import lombok.AllArgsConstructor;
import lombok.Getter;
import token.Token;
import visitor.TokenVisitor;

@AllArgsConstructor
public class NumberToken implements Token {
    @Getter
    private final int number;

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }
}
