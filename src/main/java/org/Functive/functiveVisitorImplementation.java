package org.functive;

import java.util.ArrayList;
import java.util.List;

import functive.functiveBaseVisitor;
import functive.functiveParser;

public class functiveVisitorImplementation extends functiveBaseVisitor<Object> {
    public FunctiveSymbolsTable symbolsTable = new FunctiveSymbolsTable();

    @Override
    public Object visitVarDeclaration(functiveParser.VarDeclarationContext ctx) {
        // check if the var declaration has a value
        if (ctx.expression() != null) {
            // var declaration with value
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
        return null;
    }

    @Override
    public Object visitArrayDeclaration(functiveParser.ArrayDeclarationContext ctx) {

        Object array;
        // check if the array declaration has an expression list
        if (ctx.expressionList() != null) {
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
        // check if the variable is declared
        if (!symbolsTable.currentTable.containsKey(ctx.IDENTIFIER().getText())) {
            throw new RuntimeException("Variable not declared: " + ctx.IDENTIFIER().getText());
        }

        Object newValue = visit(ctx.expression());
        Object prevValue = symbolsTable.currentTable.get(ctx.IDENTIFIER().getText());

        // check if prevValue is the same type as newValue
        if (prevValue instanceof Integer && newValue instanceof Integer) {
            symbolsTable.currentTable.put(ctx.IDENTIFIER().getText(), newValue);
        } else if (prevValue instanceof Float && newValue instanceof Float) {
            symbolsTable.currentTable.put(ctx.IDENTIFIER().getText(), newValue);
        } else if (prevValue instanceof Boolean && newValue instanceof Boolean) {
            symbolsTable.currentTable.put(ctx.IDENTIFIER().getText(), newValue);
        } else if (prevValue instanceof String && newValue instanceof String) {
            symbolsTable.currentTable.put(ctx.IDENTIFIER().getText(), newValue);
        } else {
            throw new RuntimeException("Invalid type for variable: " + ctx.IDENTIFIER().getText());
        }

        try {
            // put the newValue to the variable
            symbolsTable.currentTable.put(ctx.IDENTIFIER().getText(), newValue);
        } catch (UnsupportedOperationException e) {
            throw new RuntimeException("UnsupportedOperationException: " + ctx.IDENTIFIER().getText());
        } catch (ClassCastException e) {
            throw new RuntimeException("ClassCastException: " + ctx.IDENTIFIER().getText());
        } catch (NullPointerException e) {
            throw new RuntimeException("NullPointerException: " + ctx.IDENTIFIER().getText());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("IllegalArgumentException: " + ctx.IDENTIFIER().getText());
        } catch (Exception e) {
            throw new RuntimeException("Exception: " + ctx.IDENTIFIER().getText());
        }

        return null;
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
    public Object visitIfElseIfElseStatement(functiveParser.IfElseIfElseStatementContext ctx) {
        // if the ifStatement visitor returns null, then the condition was false
        Object condition = visit(ctx.ifStatement());

        if ((boolean) condition == false) {
            // there can be multiple elseif statements
            for (var elseifStatement : ctx.elseifStatement()) {
                // visit the elseif statement
                condition = visit(elseifStatement); // will return null if none of the elseif conditions are ture
                if ((boolean) condition == true) {
                    return null;
                }
            }
            if (ctx.elseStatement() != null)
                visit(ctx.elseStatement());
        }

        return null;
    }

    @Override
    public Object visitIfStatement(functiveParser.IfStatementContext ctx) {
        // System.out.println("Visited IfStatement: " + ctx.getText());

        // Get the condition expression and evaluate it
        Object condition = visit(ctx.expression());
        // System.out.println("Condition: " + condition);

        if ((boolean) condition) {
            visit(ctx.block());
            return true;
        }

        return false;
    }

    @Override
    public Object visitElseifStatement(functiveParser.ElseifStatementContext ctx) {
        // System.out.println("Visited elseifStatement: " + ctx.getText());

        // Get the condition expression and evaluate it
        Object condition = visit(ctx.expression());
        // System.out.println("Condition: " + condition);

        if ((boolean) condition) {
            visit(ctx.block());
            return true;
        }

        return false;
    }

    @Override
    public Object visitElseStatement(functiveParser.ElseStatementContext ctx) {
        // System.out.println("Visited elseStatement: " + ctx.getText());
        return visit(ctx.block());
    }

    @Override
    public Object visitSwitchStatement(functiveParser.SwitchStatementContext ctx) {
        System.out.println("Visited SwitchStatement: " + ctx.getText());

        // Retrieve the switch expression
        Object switchExpr = visit(ctx.expression());

        // if (switchExpr != null) {
        // // Convert switch expression to the appropriate type if necessary
        // if (switchExpr instanceof String) {
        // switchExpr = switchExpr.toString(); // Convert to string
        // } else if (switchExpr instanceof Double) {
        // switchExpr = ((Double) switchExpr).intValue(); // Convert to int
        // } else if (switchExpr instanceof Float) {
        // switchExpr = ((Float) switchExpr).intValue(); // Convert to int
        // }

        // // Visit each case statement
        // List<CaseStatementContext> caseStatements = ctx.caseStatement();
        // boolean isMatched = false;
        // for (CaseStatementContext caseCtx : caseStatements) {
        // Object caseValue = visit(caseCtx.expression());

        // if (caseValue != null) {
        // // Convert case value to the appropriate type if necessary
        // if (caseValue instanceof String) {
        // caseValue = caseValue.toString(); // Convert to string
        // } else if (caseValue instanceof Double) {
        // caseValue = ((Double) caseValue).intValue(); // Convert to int
        // } else if (caseValue instanceof Float) {
        // caseValue = ((Float) caseValue).intValue(); // Convert to int
        // }

        // if (switchExpr.equals(caseValue)) {
        // isMatched = true;
        // // Execute the statements in the matched case
        // List<StatementContext> statements = caseCtx.statement();
        // for (StatementContext statementCtx : statements) {
        // visitStatement(statementCtx);
        // }
        // break; // Exit the loop after the first match
        // }
        // }
        // }

        // // If no case matches, check for the default statement
        // if (!isMatched) {
        // DefaultStatementContext defaultCtx = ctx.defaultStatement();
        // if (defaultCtx != null) {
        // // Execute the statements in the default case
        // List<StatementContext> statements = defaultCtx.statement();
        // for (StatementContext statementCtx : statements) {
        // visitStatement(statementCtx);
        // }
        // }
        // // }
        // }

        return null; // Modify this line to return the desired object
    }

    @Override
    public Object visitCaseStatement(functiveParser.CaseStatementContext ctx) {
        System.out.println("Visited CaseStatement: " + ctx.getText());

        // Retrieve the case expression
        Object caseExpression = visit(ctx.expression());

        // Perform necessary operations with the case expression
        if (caseExpression != null) {
            System.out.println("Case Expression: " + caseExpression);

            // Example operations: check if the case expression matches a specific value
            if (caseExpression instanceof Integer) {
                int value = (int) caseExpression;
            } else if (caseExpression instanceof String) {
                String value = (String) caseExpression;
            }

            // Add your specific code logic here
            // For example:
            // - Perform calculations or comparisons based on the case expression
            // - Call other methods or perform actions specific to your language or
            // application
            // - Manipulate data or variables based on the case expression

            // Example: Print a custom message based on the case expression
            System.out.println("Custom message based on the case expression: " + caseExpression);
        }

        return null; // Modify this line to return the desired object
    }

    @Override
    public Object visitDefaultStatement(functiveParser.DefaultStatementContext ctx) {
        System.out.println("Visited DefaultStatement: " + ctx.getText());
        // Visit each statement in the default block
        // for (StatementContext statementCtx : ctx.statement()) {
        // visitStatement(statementCtx);
        // }
        return null;
    }

    @Override
    public Object visitForLoop(functiveParser.ForLoopContext ctx) {
        // enter the block scope
        symbolsTable.enterBlock();
        // Visit the initialization expression
        boolean isVarDeclaration = ctx.forControl().varDeclaration() != null;
        if (isVarDeclaration)
            visit(ctx.forControl().varDeclaration());
        else
            visit(ctx.forControl().assignment(0));

        // Visit the control expression
        Object controlExpression = visit(ctx.forControl().expression());

        // visit the block expression, if the control expression is true
        if (controlExpression instanceof Boolean) {
            while ((Boolean) controlExpression) {
                visit(ctx.block());
                if (isVarDeclaration)
                    visit(ctx.forControl().assignment(0));
                else
                    visit(ctx.forControl().assignment(1));
                controlExpression = visit(ctx.forControl().expression());
            }
        }

        // exit the block scope
        symbolsTable.exitBlock();
        return null;
    }

    @Override
    public Object visitWhileLoop(functiveParser.WhileLoopContext ctx) {
        // System.out.println("Visited WhileLoop: " + ctx.getText());
        Object controlExpression = visit(ctx.expression());
        if (controlExpression instanceof Boolean) {
            while ((Boolean) controlExpression) {
                visit(ctx.block());
                controlExpression = visit(ctx.expression());
            }
        }
        return null;
    }

    @Override
    public Object visitFunctionDeclaration(functiveParser.FunctionDeclarationContext ctx) {
        System.out.println("Visited FunctionDeclaration: " + ctx.getText());

        // Retrieve function details
        String returnType = ctx.TYPE() != null ? ctx.TYPE().getText() : "void";
        String functionName = ctx.IDENTIFIER().getText();

        // Visit function parameters TODO

        // Visit function body statements TODO

        // - Process function details and parameters
        // - Perform any necessary actions or validations

        return null;
    }

    @Override
    public Object visitFunctionCall(functiveParser.FunctionCallContext ctx) {
        System.out.println("Visited FunctionCall: " + ctx.getText());
        // Retrieve function call details
        String functionName = ctx.IDENTIFIER().getText();

        // Visit function call arguments

        return null;
    }

    @Override
    public Object visitPrint(functiveParser.PrintContext ctx) {
        System.out.println(visit(ctx.expression()));
        return null;
    }

    @Override
    public Object visitReturnStatement(functiveParser.ReturnStatementContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public Object visitGreaterThanExpression(functiveParser.GreaterThanExpressionContext ctx) {
        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));

        // check that left and right are numbers
        if (!(left instanceof Number) || !(right instanceof Number))
            throw new RuntimeException("Invalid type for comparison: " + ctx.expression(0) + " > " + ctx.expression(1));

        // Compare the values of the left and right expressions
        return ((Number) left).doubleValue() > ((Number) right).doubleValue();
    }

    @Override
    public Object visitGreaterThanEqualExpression(functiveParser.GreaterThanEqualExpressionContext ctx) {
        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));

        // check that left and right are numbers
        if (!(left instanceof Number) || !(right instanceof Number))
            throw new RuntimeException(
                    "Invalid type for comparison: " + ctx.expression(0) + " >= " + ctx.expression(1));

        // Compare the values of the left and right expressions
        return ((Number) left).doubleValue() >= ((Number) right).doubleValue();
    }

    @Override
    public Object visitNotEqualExpression(functiveParser.NotEqualExpressionContext ctx) {
        // Visit the left and right expressions
        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));

        // Compare the values of the left and right expressions
        return !left.equals(right);
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
        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));

        // check that left and right are numbers
        if (!(left instanceof Number) || !(right instanceof Number))
            throw new RuntimeException("Invalid type for comparison: " + ctx.expression(0) + " < " + ctx.expression(1));

        // Compare the values of the left and right expressions
        return ((Number) left).doubleValue() < ((Number) right).doubleValue();
    }

    @Override
    public Object visitIdentifierExpression(functiveParser.IdentifierExpressionContext ctx) {
        Object identifierValue = symbolsTable.currentTable.get(ctx.IDENTIFIER().getText());
        if (identifierValue == null) {
            throw new RuntimeException("Variable " + ctx.IDENTIFIER().getText() + " not defined");
        }
        return identifierValue;
    }

    @Override
    public Object visitLessThanEqualExpression(functiveParser.LessThanEqualExpressionContext ctx) {
        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));

        // check that left and right are numbers
        if (!(left instanceof Number) || !(right instanceof Number))
            throw new RuntimeException(
                    "Invalid type for comparison: " + ctx.expression(0) + " <= " + ctx.expression(1));

        // Compare the values of the left and right expressions
        return ((Number) left).doubleValue() <= ((Number) right).doubleValue();
    }

    @Override
    public Object visitOrExpression(functiveParser.OrExpressionContext ctx) {
        // System.out.println("Visited AndExpression: " + ctx.getText());

        // Visit the left and right expressions
        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));

        // System.out.println("Left: " + left + ", Right: " + right);

        // check if left is 0 or 1 and right is 0 or 1, then cast them to boolean
        if (left instanceof Integer && right instanceof Integer) {
            if ((int) left == 0)
                left = false;
            else if ((int) left == 1)
                left = true;
            else
                throw new RuntimeException(
                        "Invalid type for OR operation: " + ctx.expression(0) + " && " + ctx.expression(1));

            if ((int) right == 0)
                right = false;
            else if ((int) right == 1)
                right = true;
            else
                throw new RuntimeException(
                        "Invalid type for OR operation: " + ctx.expression(0) + " && " + ctx.expression(1));
        }

        if (!(left instanceof Boolean) || !(right instanceof Boolean))
            throw new RuntimeException(
                    "Invalid type for OR operation: " + ctx.expression(0) + " && " + ctx.expression(1));

        // Compute the result of the AND operation
        boolean result = (boolean) left || (boolean) right;

        return result;
    }

    @Override
    public Object visitAndExpression(functiveParser.AndExpressionContext ctx) {
        // System.out.println("Visited AndExpression: " + ctx.getText());

        // Visit the left and right expressions
        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));

        // System.out.println("Left: " + left + ", Right: " + right);

        // check if left is 0 or 1 and right is 0 or 1, then cast them to boolean
        if (left instanceof Integer && right instanceof Integer) {
            if ((int) left == 0)
                left = false;
            else if ((int) left == 1)
                left = true;
            else
                throw new RuntimeException(
                        "Invalid type for AND operation: " + ctx.expression(0) + " && " + ctx.expression(1));

            if ((int) right == 0)
                right = false;
            else if ((int) right == 1)
                right = true;
            else
                throw new RuntimeException(
                        "Invalid type for AND operation: " + ctx.expression(0) + " && " + ctx.expression(1));
        }

        if (!(left instanceof Boolean) || !(right instanceof Boolean))
            throw new RuntimeException(
                    "Invalid type for AND operation: " + ctx.expression(0) + " && " + ctx.expression(1));

        // Compute the result of the AND operation
        boolean result = (boolean) left && (boolean) right;

        return result;
    }

    // visits the literal expression, which is just one value which is either int,
    // float, boolean, or string
    @Override
    public Object visitLiteralExpression(functiveParser.LiteralExpressionContext ctx) {
        // check

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

    @Override
    public Object visitBlock(functiveParser.BlockContext ctx) {
        // System.out.println("Visited Block: " + ctx.getText());
        // printCurrentBlockVariablesAndValues();
        symbolsTable.enterBlock();
        // printCurrentBlockVariablesAndValues();
        // visit all the statements in the block
        for (functiveParser.StatementContext statementContext : ctx.statement()) {
            // System.out.println("Visiting statement: " + statementContext.getText());
            visit(statementContext);
        }
        // printCurrentBlockVariablesAndValues();
        symbolsTable.exitBlock();
        // printCurrentBlockVariablesAndValues();
        return null;
    }

    private void printCurrentBlockVariablesAndValues() {
        System.out.println("Current block variables and values:");
        for (String variable : symbolsTable.currentTable.keySet()) {
            System.out.println(variable + ": " + symbolsTable.currentTable.get(variable));
        }
    }

    @Override
    public Object visitParenthesisExpression(functiveParser.ParenthesisExpressionContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public Object visitModulusExpression(functiveParser.ModulusExpressionContext ctx) {
        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));

        if (left instanceof Integer && right instanceof Integer) {
            return (int) left % (int) right;
        } else if (left instanceof Float || right instanceof Float) {
            return (float) left % (float) right;
        } else {
            throw new RuntimeException(
                    "Invalid type for modulus operation: " + left.getClass() + " % " + right.getClass());
        }
    }

    @Override
    public Object visitMultiplyExpression(functiveParser.MultiplyExpressionContext ctx) {
        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));

        if (left instanceof Integer && right instanceof Integer) {
            return (int) left * (int) right;
        } else if (left instanceof Float || right instanceof Float) {
            return (float) left * (float) right;
        } else {
            throw new RuntimeException(
                    "Invalid type for multiply operation: " + left.getClass() + " * " + right.getClass());
        }
    }

    @Override
    public Object visitDivideExpression(functiveParser.DivideExpressionContext ctx) {
        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));

        if (left instanceof Integer && right instanceof Integer) {
            return (int) left / (int) right;
        } else if (left instanceof Float || right instanceof Float) {
            return (float) left / (float) right;
        } else {
            throw new RuntimeException(
                    "Invalid type for divide operation: " + left.getClass() + " / " + right.getClass());
        }
    }

    @Override
    public Object visitAddExpression(functiveParser.AddExpressionContext ctx) {
        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));

        if (left instanceof Integer && right instanceof Integer) {
            return (int) left + (int) right;
        } else if (left instanceof String || right instanceof String) {
            return left.toString() + right.toString();
        } else if (left instanceof Float || right instanceof Float) {
            return (float) left + (float) right;
        } else if (left instanceof Boolean && right instanceof Boolean) {
            return (boolean) left || (boolean) right;
        } else {
            throw new RuntimeException(
                    "Invalid type for add operation: " + left.getClass() + " + " + right.getClass());
        }
    }

    @Override
    public Object visitSubtractExpression(functiveParser.SubtractExpressionContext ctx) {
        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));

        if (left instanceof Integer && right instanceof Integer) {
            return (int) left - (int) right;
        } else if (left instanceof Float || right instanceof Float) {
            return (float) left - (float) right;
        } else {
            throw new RuntimeException(
                    "Invalid type for subtract operation: " + left.getClass() + " - " + right.getClass());
        }
    }
}
