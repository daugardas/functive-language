package org.functive;

import java.util.ArrayList;

import functive.functiveBaseVisitor;
import functive.functiveParser;

public class FunctiveVisitorImplementation extends functiveBaseVisitor<Object> {
    public FunctiveSymbolsTable symbolsTable = new FunctiveSymbolsTable();

    @Override
    public Object visitVarDeclaration(functiveParser.VarDeclarationContext ctx) {
        // check if the var declaration has a value
        if (ctx.expression() != null) {
            // var declaration with value
            // System.out.println("Visited VarDeclaration with value: " + ctx.IDENTIFIER());
            if (symbolsTable.currentTable.containsKey(ctx.IDENTIFIER().getText())) {
                throw new RuntimeException("Variable already declared: " + ctx.IDENTIFIER().getText());
            }

            // System.out.println("TYPE: " + ctx.TYPE().getText());
            switch (ctx.TYPE().getText()) {
                case "int" -> {
                    // because this should be an integer, we will try to convert the value to an
                    // integer
                    try {
                        Integer intValue = Integer.parseInt(ctx.expression().getText());
                        // System.out.println("intValue: " + intValue);
                        symbolsTable.currentTable.put(ctx.IDENTIFIER().getText(), intValue);
                    } catch (NumberFormatException e) {
                        throw new RuntimeException("Invalid value for int: " + ctx.expression().getText());
                    }
                }
                case "float" -> {
                    // because this should be a float, we will try to convert the value to a float
                    try {
                        Float floatValue = Float.parseFloat(ctx.expression().getText());
                        // System.out.println("floatValue: " + floatValue);
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
                        // System.out.println("boolValue: " + boolValue);
                        symbolsTable.currentTable.put(ctx.IDENTIFIER().getText(), boolValue);
                    } else {
                        throw new RuntimeException("Invalid value for boolean: " + ctx.expression().getText());
                    }
                }
                case "string" -> {
                    // because this should be a string, we will try to convert the value to a string
                    String stringValue = ctx.expression().getText().replace("\"", "");
                    // System.out.println("stringValue: " + stringValue);
                    symbolsTable.currentTable.put(ctx.IDENTIFIER().getText(), stringValue);
                }
                default -> {
                    throw new RuntimeException("Unknown type: " + ctx.TYPE().getText());
                }
            }
        } else {
            // System.out.println("Visited VarDeclaration without value: " +
            // ctx.IDENTIFIER());

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
        // System.out.println("Visited ArrayDeclaration: " + ctx.getText());

        Object array;
        // check if the array declaration has an expression list
        if (ctx.expressionList() != null) {
            // array declaration with expression list
            // System.out.println("Visited ArrayDeclaration with expression list: " +
            // ctx.IDENTIFIER());

            // check if the variable is already declared
            if (symbolsTable.currentTable.containsKey(ctx.IDENTIFIER().getText())) {
                throw new RuntimeException("Variable already declared: " + ctx.IDENTIFIER().getText());
            }

            // System.out.println("TYPE: " + ctx.TYPE().getText());
            switch (ctx.TYPE().getText()) {
                case "int" -> {
                    array = new ArrayList<Integer>();
                    for (var expression : ctx.expressionList().expression()) {
                        try {
                            Integer intValue = Integer.parseInt(expression.getText());
                            // System.out.println("intValue: " + intValue);
                            ((ArrayList<Integer>) array).add(intValue);
                        } catch (NumberFormatException e) {
                            throw new RuntimeException("Invalid value for int: " + expression.getText());
                        }
                    }
                }
                case "float" -> {
                    array = new ArrayList<Float>();
                    for (var expression : ctx.expressionList().expression()) {
                        try {
                            Float floatValue = Float.parseFloat(expression.getText());
                            // System.out.println("floatValue: " + floatValue);
                            ((ArrayList<Float>) array).add(floatValue);
                        } catch (NumberFormatException e) {
                            throw new RuntimeException("Invalid value for float: " + expression.getText());
                        }
                    }
                }
                case "boolean" -> {
                    array = new ArrayList<Boolean>();
                    for (var expression : ctx.expressionList().expression()) {
                        if (expression.getText().equals("true") || expression.getText().equals("false")) {
                            Boolean boolValue = Boolean.parseBoolean(expression.getText());
                            // System.out.println("boolValue: " + boolValue);
                            ((ArrayList<Boolean>) array).add(boolValue);
                        } else {
                            throw new RuntimeException("Invalid value for boolean: " + expression.getText());
                        }
                    }
                }
                case "string" -> {
                    array = new ArrayList<String>();
                    for (var expression : ctx.expressionList().expression()) {
                        String stringValue = expression.getText().replace("\"", "");
                        // System.out.println("stringValue: " + stringValue);
                        ((ArrayList<String>) array).add(stringValue);
                    }
                }
                default -> {
                    throw new RuntimeException("Unknown type: " + ctx.TYPE().getText());
                }
            }
        } else {
            // System.out.println("Visited ArrayDeclaration without expression list: " +
            // ctx.IDENTIFIER());
            // check type
            switch (ctx.TYPE().getText()) {
                case "int" -> {
                    array = new ArrayList<Integer>();
                }
                case "float" -> {
                    array = new ArrayList<Float>();
                }
                case "boolean" -> {
                    array = new ArrayList<Boolean>();
                }
                case "string" -> {
                    array = new ArrayList<String>();
                }
                default -> {
                    throw new RuntimeException("Unknown type: " + ctx.TYPE().getText());
                }
            }

        }
        symbolsTable.currentTable.put(ctx.IDENTIFIER().getText(), array);
        return null;
    }

    @Override
    public Object visitAssignment(functiveParser.AssignmentContext ctx) {
        System.out.println("Visited Assignment: " + ctx.getText());

        // check if the variable is declared
        if (!symbolsTable.currentTable.containsKey(ctx.IDENTIFIER().getText())) {
            throw new RuntimeException("Variable not declared: " + ctx.IDENTIFIER().getText());
        }

        // assignment to variable
        // System.out.println("Assignment to variable: " + ctx.IDENTIFIER().getText() +
        // " with value: "
        // + ctx.expression().getText());

        // check the type of the variable and the expression, and assign the value
        Object variable = symbolsTable.currentTable.get(ctx.IDENTIFIER().getText());

        if (variable instanceof Integer) {
            // check if expression is an integer
            try {
                Integer intValue = Integer.parseInt(ctx.expression().getText());
                symbolsTable.currentTable.put(ctx.IDENTIFIER().getText(), intValue);
            } catch (NumberFormatException e) {
                throw new RuntimeException(
                        "Invalid value for " + ctx.IDENTIFIER().getText() + ": " + ctx.expression().getText());
            }
        } else if (variable instanceof Float) {
            // check if expression is a float
            try {
                Float floatValue = Float.parseFloat(ctx.expression().getText());
                symbolsTable.currentTable.put(ctx.IDENTIFIER().getText(), floatValue);
            } catch (NumberFormatException e) {
                throw new RuntimeException(
                        "Invalid value for " + ctx.IDENTIFIER().getText() + ": " + ctx.expression().getText());
            }
        } else if (variable instanceof Boolean) {
            // check if expression is a boolean
            if (ctx.expression().getText().equals("true") || ctx.expression().getText().equals("false")) {
                Boolean boolValue = Boolean.parseBoolean(ctx.expression().getText());
                symbolsTable.currentTable.put(ctx.IDENTIFIER().getText(), boolValue);
            } else {
                throw new RuntimeException(
                        "Invalid value for " + ctx.IDENTIFIER().getText() + ": " + ctx.expression().getText());
            }
        } else if (variable instanceof String) {
            // check if expression is a string
            String stringValue = ctx.expression().getText().replace("\"", "");
            symbolsTable.currentTable.put(ctx.IDENTIFIER().getText(), stringValue);
        } else {
            throw new RuntimeException("Unknown type: " + variable.getClass());
        }

        return null;
        // return super.visitAssignment(ctx);
    }

    @Override
    public Object visitArrayAssignment(functiveParser.ArrayAssignmentContext ctx) {
        // check if the variable is declared
        if (!symbolsTable.currentTable.containsKey(ctx.IDENTIFIER().getText())) {
            throw new RuntimeException("Variable not declared: " + ctx.IDENTIFIER().getText());
        }
        // print out the array saved in the table
        // for (var value : (ArrayList<?>)
        // symbolsTable.currentTable.get(ctx.IDENTIFIER().getText())) {
        // System.out.println(value);
        // }
        // System.out.println("Visited ArrayAssignment: " + ctx.getText());
        Object array = symbolsTable.currentTable.get(ctx.IDENTIFIER().getText());
        Class<?> arrayType = null;
        if (!((ArrayList<?>) array).isEmpty()) {
            arrayType = ((ArrayList<?>) array).get(0).getClass();
        }

        Object newValues = visit(ctx.expressionList());
        Class<?> newValuesType = null;
        if (!((ArrayList<?>) newValues).isEmpty()) {
            newValuesType = ((ArrayList<?>) newValues).get(0).getClass();
        }

        // System.out.println(arrayType);

        if (arrayType != null && arrayType != newValuesType)
            throw new RuntimeException("Invalid type for array: " + ctx.IDENTIFIER().getText());

        symbolsTable.currentTable.put(ctx.IDENTIFIER().getText(), newValues);

        // print out the array saved in the table
        // for (var value : (ArrayList<?>)
        // symbolsTable.currentTable.get(ctx.IDENTIFIER().getText())) {
        // System.out.println(value);
        // }
        return null;
    }

    @Override
    public Object visitArrayAccessAssignment(functiveParser.ArrayAccessAssignmentContext ctx) {
        // check if the variable is declared
        if (!symbolsTable.currentTable.containsKey(ctx.IDENTIFIER().getText())) {
            throw new RuntimeException("Variable not declared: " + ctx.IDENTIFIER().getText());
        }
        // System.out.println("Visited ArrayAccessAssignment: " + ctx.getText());
        Object array = symbolsTable.currentTable.get(ctx.IDENTIFIER().getText());
        Class<?> arrayType = null;
        if (!((ArrayList<?>) array).isEmpty()) {
            arrayType = ((ArrayList<?>) array).get(0).getClass();
        }
        Integer index = (Integer) visit(ctx.arrayAccess().expression());
        Object value = visit(ctx.expression());

        // System.out.println(arrayType);

        if (arrayType != null && arrayType != value.getClass())
            throw new RuntimeException("Invalid type of assignment for " + ctx.IDENTIFIER().getText() + ": "
                    + ctx.IDENTIFIER().getText());

        if (value instanceof Integer)
            ((ArrayList<Integer>) array).set(index, (Integer) value);
        else if (value instanceof Float)
            ((ArrayList<Float>) array).set(index, (Float) value);
        else if (value instanceof Boolean)
            ((ArrayList<Boolean>) array).set(index, (Boolean) value);
        else if (value instanceof String)
            ((ArrayList<String>) array).set(index, (String) value);
        else
            throw new RuntimeException("Unknown type: " + value.getClass());

        symbolsTable.currentTable.put(ctx.IDENTIFIER().getText(), array);
        return null;
    }

    @Override
    public Object visitIfStatement(functiveParser.IfStatementContext ctx) {
        System.out.println("Visited IfStatement: " + ctx.getText());

        // Get the condition expression and evaluate it
        Object condition = visit(ctx.expression());
        System.out.println("Condition: " + condition);

        // // If the condition is true, visit the if block
        // if (conditionResult) {
        // return visit(ctx.ifBlock);
        // }
        // // Otherwise, visit the else block (if it exists)
        // else if (ctx.elseBlock != null) {
        // return visit(ctx.elseBlock);
        // }

        return null;
    }

    @Override
    public Object visitSwitchStatement(functiveParser.SwitchStatementContext ctx) {
        // Retrieve the switch expression and case statement
        return null;
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
    public Object visitGreaterThanExpression(functiveParser.GreaterThanExpressionContext ctx) {
        System.out.println("Visited GreaterThanExpression: " + ctx.getText());
        return super.visitGreaterThanExpression(ctx);
    }

    @Override
    public Object visitGreaterThanEqualExpression(functiveParser.GreaterThanEqualExpressionContext ctx) {
        System.out.println("Visited GreaterThanEqualExpression: " + ctx.getText());
        return super.visitGreaterThanEqualExpression(ctx);
    }

    @Override
    public Object visitNotEqualExpression(functiveParser.NotEqualExpressionContext ctx) {
        System.out.println("Visited NotEqualExpression: " + ctx.getText());
        return super.visitNotEqualExpression(ctx);
    }

    @Override
    public Object visitEqualExpression(functiveParser.EqualExpressionContext ctx) {
        // Visit the left and right expressions
        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));

        // Compare the values of the left and right expressions
        return left.equals(right);
    }

    @Override
    public Object visitLessThanExpression(functiveParser.LessThanExpressionContext ctx) {
        System.out.println("Visited LessThanExpression: " + ctx.getText());
        return super.visitLessThanExpression(ctx);
    }

    @Override
    public Object visitLessThanEqualExpression(functiveParser.LessThanEqualExpressionContext ctx) {
        System.out.println("Visited LessThanEqualExpression: " + ctx.getText());
        return super.visitLessThanEqualExpression(ctx);
    }

    @Override
    public Object visitOrExpression(functiveParser.OrExpressionContext ctx) {
        System.out.println("Visited OrExpression: " + ctx.getText());
        return super.visitOrExpression(ctx);
    }

    @Override
    public Object visitAndExpression(functiveParser.AndExpressionContext ctx) {
        System.out.println("Visited AndExpression: " + ctx.getText());
        return super.visitAndExpression(ctx);
    }

    // visits the literal expression, which is just one value which is either int,
    // float, boolean, or string
    @Override
    public Object visitLiteralExpression(functiveParser.LiteralExpressionContext ctx) {
        if (ctx.literal().INTEGER() != null)
            return Integer.parseInt(ctx.literal().INTEGER().getText());
        else if (ctx.literal().FLOAT() != null)
            return Float.parseFloat(ctx.literal().FLOAT().getText());
        else if (ctx.literal().BOOLEAN() != null)
            return Boolean.parseBoolean(ctx.literal().BOOLEAN().getText());
        else if (ctx.literal().STRING() != null)
            return ctx.literal().STRING().getText().replace("\"", "");

        return null;
    }

    @Override
    public Object visitExpressionList(functiveParser.ExpressionListContext ctx) {
        // System.out.println("Visited ExpressionList: " + ctx.getText());
        // System.out.println(ctx.expression());
        // get the first elements type
        Object firstElement = visit(ctx.expression(0));
        Class<?> firstElementType = firstElement.getClass();

        // cycle through the rest of the elements and check if they are the same type,
        // and if they are, add them to the list, otherwise throw an error
        if (ctx.expression().isEmpty()) {
            return null;
        }

        if (firstElementType == Integer.class) {
            ArrayList<Integer> list = new ArrayList<Integer>();
            for (functiveParser.ExpressionContext expressionContext : ctx.expression()) {
                Object element = visit(expressionContext);
                if (element.getClass() != firstElementType)
                    throw new RuntimeException("Invalid type of assignment for " + ctx.getText());
                list.add((Integer) element);
            }
            return list;
        } else if (firstElementType == Float.class) {
            ArrayList<Float> list = new ArrayList<Float>();
            for (functiveParser.ExpressionContext expressionContext : ctx.expression()) {
                Object element = visit(expressionContext);
                if (element.getClass() != firstElementType)
                    throw new RuntimeException("Invalid type of assignment for " + ctx.getText());
                list.add((Float) element);
            }
            return list;
        } else if (firstElementType == Boolean.class) {
            ArrayList<Boolean> list = new ArrayList<Boolean>();
            for (functiveParser.ExpressionContext expressionContext : ctx.expression()) {
                Object element = visit(expressionContext);
                if (element.getClass() != firstElementType)
                    throw new RuntimeException("Invalid type of assignment for " + ctx.getText());
                list.add((Boolean) element);
            }
            return list;
        } else if (firstElementType == String.class) {
            ArrayList<String> list = new ArrayList<String>();
            for (functiveParser.ExpressionContext expressionContext : ctx.expression()) {
                Object element = visit(expressionContext);
                if (element.getClass() != firstElementType)
                    throw new RuntimeException("Invalid type of assignment for " + ctx.getText());
                list.add((String) element);
            }
            return list;
        }

        return null;
    }
}
