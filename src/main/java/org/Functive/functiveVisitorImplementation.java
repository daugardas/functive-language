package org.functive;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import functive.functiveBaseVisitor;
import functive.functiveParser;

public class functiveVisitorImplementation extends functiveBaseVisitor<Object> {
    public FunctiveSymbolsTable symbolsTable = new FunctiveSymbolsTable();

    @Override
    public Object visitVarDeclaration(functiveParser.VarDeclarationContext ctx) {
        if (symbolsTable.currentTable.containsKey(ctx.IDENTIFIER().getText())) {
            throw new RuntimeException("Variable already declared: " + ctx.IDENTIFIER().getText());
        }

        // check if the var declaration has a value
        if (ctx.expression() != null) {
            Object value = visit(ctx.expression());

            try {
                symbolsTable.currentTable.put(ctx.IDENTIFIER().getText(), value);
                return null;
            } catch (Exception e) {
                throw new RuntimeException("Invalid value for variable: " + ctx.IDENTIFIER().getText());
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
        // System.out.println("Visited SwitchStatement: " + ctx.getText());

        // Retrieve the switch expression
        Object switchExpr = visit(ctx.expression());
        // System.out.println("Switch expression: " + switchExpr);

        // check if case statements exist
        if (ctx.caseStatement(0) == null && ctx.defaultStatement() == null)
            throw new RuntimeException("Used switch statement without any case or default statements");

        // loop through each case statement
        for (var caseStatement : ctx.caseStatement()) {
            // get the case value
            Object caseValue = visit(caseStatement.expression());
            // System.out.println("Case value: " + caseValue);

            // check if the case value is the same as the switch expression
            if (switchExpr.equals(caseValue)) {
                // returns false if break statement is reached, otherwise
                // returns true
                boolean resultOfCase = (boolean) visit(caseStatement);
                // System.out.println("Result of case: " + resultOfCase);
                if (!resultOfCase) {
                    return null;
                }
            }
        }

        // check if there is a default statement
        if (ctx.defaultStatement() != null) {
            // visit the default statement
            visit(ctx.defaultStatement());
        }

        return null;
    }

    @Override
    public Object visitCaseStatement(functiveParser.CaseStatementContext ctx) {
        // System.out.println("Visited CaseStatement: " + ctx.getText());

        // enter block scope
        symbolsTable.enterBlock();

        // visit each statement in the case block
        for (functiveParser.StatementContext statementCtx : ctx.statement()) {
            // check if the statement is a break statement
            if (statementCtx.breakStatement() != null) {
                // exit block scope
                symbolsTable.exitBlock();
                // return false to indicate that a break statement was reached
                return false;
            }
            visitStatement(statementCtx);
        }

        // exit block scope
        symbolsTable.exitBlock();
        return true;
    }

    @Override
    public Object visitDefaultStatement(functiveParser.DefaultStatementContext ctx) {
        // System.out.println("Visited DefaultStatement: " + ctx.getText());
        // enter block scope
        symbolsTable.enterBlock();

        // visit each statement in the case block
        for (functiveParser.StatementContext statementCtx : ctx.statement()) {
            visitStatement(statementCtx);
        }

        // exit block scope
        symbolsTable.exitBlock();
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
        String functionName = ctx.IDENTIFIER().getText();
        String returnType = ctx.TYPE() != null ? ctx.TYPE().getText() : "void";

        List<PhoonkParameter> parameters = new ArrayList<>();
        if (ctx.parameters() != null) {
            for (functiveParser.ParameterContext parameterCtx : ctx.parameters().parameter()) {
                String parameterName = parameterCtx.IDENTIFIER().getText();
                String parameterType = parameterCtx.TYPE().getText();
                parameters.add(new PhoonkParameter(parameterName, parameterType));
            }
        }

        Phoonk phoonk = new Phoonk(functionName, returnType, ctx.block(), parameters);

        symbolsTable.currentTable.put(functionName, phoonk);
        return null;
    }

    @Override
    public Object visitFunctionCall(functiveParser.FunctionCallContext ctx) {
        // check if function exists by checking if function name is in the current table
        // and if it is a Phoonk object
        if (!symbolsTable.currentTable.containsKey(ctx.IDENTIFIER().getText())
                && !(symbolsTable.currentTable.get(ctx.IDENTIFIER().getText()) instanceof Phoonk))
            throw new RuntimeException("Function " + ctx.IDENTIFIER().getText() + " does not exist");

        // get the Phoonk object from the current table
        Phoonk phoonk = (Phoonk) symbolsTable.currentTable.get(ctx.IDENTIFIER().getText());

        // check if the number of arguments passed in the function call is the same as
        // in the function declaration
        if (ctx.arguments() != null && ctx.arguments().argument().size() != phoonk.getParameterCount())
            throw new RuntimeException("Invalid number of arguments passed in function call");

        // enter the function scope
        symbolsTable.enterBlock();

        // add the arguments to the function scope
        if (ctx.arguments() != null) {
            for (int i = 0; i < ctx.arguments().argument().size(); i++) {
                PhoonkParameter parameter = phoonk.getParameter(i);

                // check if argument is identifier or expression
                if (ctx.arguments().argument(i).expression() != null) {
                    Object argumentValue = visit(ctx.arguments().argument(i).expression());
                    symbolsTable.currentTable.put(parameter.getName(), argumentValue);
                } else {
                    String argumentName = ctx.arguments().argument(i).getText();
                    Object argumentValue = symbolsTable.currentTable.get(argumentName);
                    symbolsTable.currentTable.put(parameter.getName(), argumentValue);
                }
            }
        }

        // execute the scoped block
        Object returnValue = visit(phoonk.getBody());

        // exit the function scope
        symbolsTable.exitBlock();

        if (!phoonk.getReturnType().equals("void")) {
            return returnValue;
        }
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

        Object returnValue = null;
        for (functiveParser.StatementContext statementContext : ctx.statement()) {
            // System.out.println("Visiting statement: " + statementContext.getText());
            // check if the current statement is a return statement, if it is, then return

            if (statementContext.returnStatement() != null) {
                returnValue = visit(statementContext.returnStatement());
                break;
            }

            visit(statementContext);
        }
        // printCurrentBlockVariablesAndValues();
        symbolsTable.exitBlock();
        // printCurrentBlockVariablesAndValues();
        return returnValue;
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
