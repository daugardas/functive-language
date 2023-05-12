package org.Functive;

import functive.functiveBaseVisitor;
import functive.functiveParser;

public class functiveVisitorImplementation extends functiveBaseVisitor<Object> {

    @Override
    public Object visitProgram(functiveParser.ProgramContext ctx) {
        System.out.println("Visited Program: " + ctx.getText());
        return super.visitProgram(ctx);
    }

    @Override
    public Object visitStatement(functiveParser.StatementContext ctx) {
        Object result = super.visitStatement(ctx);
        System.out.println("Visited Statement: " + ctx.getText());
        return super.visitStatement(ctx);
    }

    @Override
    public Object visitVarDeclaration(functiveParser.VarDeclarationContext ctx) {
        System.out.println("Visited VarDeclaration: " + ctx.getText());
        return super.visitVarDeclaration(ctx);
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
