package org.functive;

import functive.functiveBaseVisitor;
import functive.functiveParser;

public class FunctiveVisitorImplementation extends functiveBaseVisitor<Object> {
    public FunctiveSymbolsTable symbolsTable = new FunctiveSymbolsTable();

    @Override
    public Object visitProgram(functiveParser.ProgramContext ctx) {
        System.out.println("Visited Program: " + ctx.getText());
        return super.visitProgram(ctx);
    }

    @Override
    public Object visitStatement(functiveParser.StatementContext ctx) {
        System.out.println("Visited Statement: " + ctx.getText());
        return super.visitStatement(ctx);
    }

    @Override
    public Object visitVarDeclaration(functiveParser.VarDeclarationContext ctx) {
        // check if the var declaration has a value
        if (ctx.expression() != null) {
            // var declaration with value
            System.out.println("Visited VarDeclaration with value: " + ctx.IDENTIFIER());
            if (symbolsTable.currentTable.containsKey(ctx.IDENTIFIER().getText())) {
                throw new RuntimeException("Variable already declared: " + ctx.IDENTIFIER().getText());
            }

            System.out.println("TYPE: " + ctx.TYPE().getText());
            switch (ctx.TYPE().getText()) {
                case "int" -> {
                    // because this should be an integer, we will try to convert the value to an
                    // integer
                    try {
                        Integer intValue = Integer.parseInt(ctx.expression().getText());
                        System.out.println("intValue: " + intValue);
                        symbolsTable.currentTable.put(ctx.IDENTIFIER().getText(), intValue);
                    } catch (NumberFormatException e) {
                        throw new RuntimeException("Invalid value for int: " + ctx.expression().getText());
                    }
                }
                case "float" -> {
                    // because this should be a float, we will try to convert the value to a float
                    try {
                        Float floatValue = Float.parseFloat(ctx.expression().getText());
                        System.out.println("floatValue: " + floatValue);
                        symbolsTable.currentTable.put(ctx.IDENTIFIER().getText(), floatValue);
                    } catch (NumberFormatException e) {
                        throw new RuntimeException("Invalid value for float: " + ctx.expression().getText());
                    }
                }
                case "boolean" -> {
                    // because this should be a boolean, we will try to convert the value to a
                    // boolean
                    if (ctx.expression().getText().equals("true") || ctx.expression().getText().equals("false")) {
                        Boolean boolValue = Boolean.parseBoolean(ctx.expression().getText());
                        System.out.println("boolValue: " + boolValue);
                        symbolsTable.currentTable.put(ctx.IDENTIFIER().getText(), boolValue);
                    } else {
                        throw new RuntimeException("Invalid value for boolean: " + ctx.expression().getText());
                    }
                }
                case "string" -> {
                    // because this should be a string, we will try to convert the value to a string
                    String stringValue = ctx.expression().getText().replace("\"", "");
                    System.out.println("stringValue: " + stringValue);
                    symbolsTable.currentTable.put(ctx.IDENTIFIER().getText(), stringValue);
                }
                default -> {
                    throw new RuntimeException("Unknown type: " + ctx.TYPE().getText());
                }
            }
        } else

        {
            System.out.println("Visited VarDeclaration without value: " + ctx.IDENTIFIER());

            // set the default values for the variable
            switch (ctx.TYPE().getText()) {
                case "int" -> symbolsTable.currentTable.put(ctx.IDENTIFIER().getText(), 0);
                case "float" -> symbolsTable.currentTable.put(ctx.IDENTIFIER().getText(), 0.0);
                case "boolean" -> symbolsTable.currentTable.put(ctx.IDENTIFIER().getText(), false);
                case "string" -> symbolsTable.currentTable.put(ctx.IDENTIFIER().getText(), "");
                default -> {
                    throw new RuntimeException("Unknown type: " + ctx.TYPE().getText());
                }
            }
        }
        return null; /* super.visitVarDeclaration(ctx); */
    }

    @Override
    public Object visitArrayDeclaration(functiveParser.ArrayDeclarationContext ctx) {
        System.out.println("Visited ArrayDeclaration: " + ctx.getText());
        return super.visitArrayDeclaration(ctx);
    }

    @Override
    public Object visitAssignment(functiveParser.AssignmentContext ctx) {
        System.out.println("Visited Assignment: " + ctx.getText());
        return super.visitAssignment(ctx);
    }

    @Override
    public Object visitIfStatement(functiveParser.IfStatementContext ctx) {
        System.out.println("Visited IfStatement: " + ctx.getText());
        return super.visitIfStatement(ctx);
    }

    @Override
    public Object visitSwitchStatement(functiveParser.SwitchStatementContext ctx) {
        System.out.println("Visited SwitchStatement: " + ctx.getText());
        return super.visitSwitchStatement(ctx);
    }

    @Override
    public Object visitCaseStatement(functiveParser.CaseStatementContext ctx) {
        System.out.println("Visited CaseStatement: " + ctx.getText());
        return super.visitCaseStatement(ctx);
    }

    @Override
    public Object visitDefaultStatement(functiveParser.DefaultStatementContext ctx) {
        System.out.println("Visited DefaultStatement: " + ctx.getText());
        return super.visitDefaultStatement(ctx);
    }

    @Override
    public Object visitForLoop(functiveParser.ForLoopContext ctx) {
        System.out.println("Visited ForLoop: " + ctx.getText());
        return super.visitForLoop(ctx);
    }

    @Override
    public Object visitWhileLoop(functiveParser.WhileLoopContext ctx) {
        System.out.println("Visited WhileLoop: " + ctx.getText());
        return super.visitWhileLoop(ctx);
    }

    @Override
    public Object visitFunctionDeclaration(functiveParser.FunctionDeclarationContext ctx) {
        System.out.println("Visited FunctionDeclaration: " + ctx.getText());
        return super.visitFunctionDeclaration(ctx);
    }

    @Override
    public Object visitFunctionCall(functiveParser.FunctionCallContext ctx) {
        System.out.println("Visited FunctionCall: " + ctx.getText());
        return super.visitFunctionCall(ctx);
    }

    @Override
    public Object visitPrint(functiveParser.PrintContext ctx) {
        System.out.println("Visited Print: " + ctx.getText());
        return super.visitPrint(ctx);
    }

    @Override
    public Object visitReturnStatement(functiveParser.ReturnStatementContext ctx) {
        System.out.println("Visited ReturnStatement: " + ctx.getText());
        return super.visitReturnStatement(ctx);
    }

    @Override
    public Object visitExpression(functiveParser.ExpressionContext ctx) {
        System.out.println("Visited Expression: " + ctx.getText());
        return super.visitExpression(ctx);
    }

    @Override
    public Object visitExpressionList(functiveParser.ExpressionListContext ctx) {
        System.out.println("Visited ExpressionList: " + ctx.getText());
        return super.visitExpressionList(ctx);
    }
}
