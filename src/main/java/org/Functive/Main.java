package org.functive;

import functive.functiveLexer;
import functive.functiveParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        try {
            Path path = Paths.get("functive-code/test.functive");
            System.out.println(path.toAbsolutePath().toString());
            execute(CharStreams.fromFileName(path.toAbsolutePath().toString()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Object execute(CharStream stream) {
        functiveLexer lexer = new functiveLexer(stream);
        functiveParser parser = new functiveParser(new CommonTokenStream(lexer));
        parser.setBuildParseTree(true);
        ParseTree tree = parser.program();

        functiveVisitorImplementation visitor = new functiveVisitorImplementation();
        return visitor.visit(tree);
    }
}
