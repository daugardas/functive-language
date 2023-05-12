grammar functive;

// Main rule
program: statement* EOF;

// Statement
statement
    : varDeclaration ';'
    | arrayDeclaration ';'
    | assignment ';'
    | ifStatement
    | switchStatement
    | forLoop
    | whileLoop
    | functionDeclaration
    | functionCall ';'
    | print ';'
    | returnStatement ';'
    ;

// Variable Declaration
varDeclaration: (TYPE | 'String') IDENTIFIER '=' expression;

// Array Declaration
arrayDeclaration: TYPE ('[' ']') IDENTIFIER '=' '{' expressionList '}';

// Assignment
assignment: IDENTIFIER '=' expression;

// If statement
ifStatement: 'if' '(' expression ')' '{' statement* '}' ('else' '{' statement* '}')?;

// Switch statement
switchStatement: 'switch' '(' expression ')' '{' caseStatement* defaultStatement? '}';

caseStatement: 'case' expression ':' statement*;

defaultStatement: 'default' ':' statement*;

// For loop
forLoop: 'for' '(' (assignment | varDeclaration) ';' expression ';' expression ')' '{' statement* '}';

// While loop
whileLoop: 'while' '(' expression ')' '{' statement* '}';

// Function Declaration
functionDeclaration: 'phoonk' (TYPE | 'void') IDENTIFIER '(' (TYPE IDENTIFIER (',' TYPE IDENTIFIER)*)? ')' '{' statement* '}';

// Function Call
functionCall: IDENTIFIER '(' expressionList? ')';

// Print
print: 'print' '(' (expression | STRING_LITERAL) ')';

// Return statement
returnStatement: 'return' expression?;

// Expression
expression
    : '(' expression ')'
    | '!' expression
    | '-' expression
    | expression ('*'|'/'|'%'|'+'|'-'|'<'|'<='|'>'|'>='|'=='|'!='|'&&'|'||') expression
    | INT_LITERAL
    | FLOAT_LITERAL
    | CHAR_LITERAL
    | IDENTIFIER ('[' expression ']')?
    | functionCall
    ;

expressionList: expression (',' expression)*;

// Tokens
TYPE: ('Int' | 'Float' | 'Char');
IDENTIFIER: [a-zA-Z_] [a-zA-Z_0-9]*;
INT_LITERAL: [0-9]+;
FLOAT_LITERAL: [0-9]+ '.' [0-9]+;
CHAR_LITERAL: '\'' . '\'';
STRING_LITERAL: '"' .*? '"';
LINE_COMMENT: '//' ~[\r\n]* -> skip;
BLOCK_COMMENT: '/*' .*? '*/' -> skip;
WS: [ \t\r\n]+ -> skip;