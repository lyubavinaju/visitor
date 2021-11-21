import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import token.Token;
import tokenizer.Tokenizer;
import visitor.CalcVisitor;
import visitor.ParserVisitor;
import visitor.PrintVisitor;

public class Calculator {

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        String s;
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            s = scanner.nextLine();
        } else {
            throw new RuntimeException("input string expected");
        }
        calculator.run(s);
    }

    public void run(String s) {
        InputStream inputStream = new ByteArrayInputStream(
            s.getBytes(StandardCharsets.UTF_8));

        Tokenizer tokenizer = new Tokenizer();
        List<Token> tokens = tokenizer.getTokens(inputStream);
        if (tokenizer.hasError()) {
            throw new RuntimeException("Tokenizer failed");
        }

        ParserVisitor parserVisitor = new ParserVisitor();
        List<Token> parsedTokens = parserVisitor.parse(tokens);

        PrintVisitor printVisitor = new PrintVisitor();
        OutputStream outputStream = printVisitor.print(parsedTokens);
        System.out.println(outputStream);

        CalcVisitor calcVisitor = new CalcVisitor();
        int calc = calcVisitor.calc(parsedTokens);
        System.out.println(calc);
    }
}
