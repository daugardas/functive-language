grammar functive;

// Main rule
program: statement* EOF;

// Statement
statement:
	varDeclaration ';'
	| arrayDeclaration ';'
	| assignment ';'
	| ifStatement
	| switchStatement
	| forLoop
	| whileLoop
	| functionDeclaration
	| functionCall ';'
	| print ';'
	| returnStatement ';';

// Variable Declaration
varDeclaration: (TYPE | 'String') IDENTIFIER ('=' expression)?;

// Array Declaration
arrayDeclaration:
	TYPE ('[' ']') IDENTIFIER ('=' '{' expressionList '}')?;

// Assignment
assignment: IDENTIFIER '=' expression;

// If statement
ifStatement:
	'if' '(' expression ')' '{' statement* '}' (
		'else' '{' statement* '}'
	)?;

// Switch statement
switchStatement:
	'switch' '(' expression ')' '{' caseStatement* defaultStatement? '}';

caseStatement: 'case' expression ':' statement*;

defaultStatement: 'default' ':' statement*;

// For loop
forLoop: 'for' '(' forControl ')' '{' statement* '}';
forControl: (varDeclaration | assignment)? ';' expression? ';' expression?;

// While loop
whileLoop: 'while' '(' expression ')' '{' statement* '}';

// Function Declaration
functionDeclaration:
	'phoonk' (TYPE | 'void') IDENTIFIER '(' parameters? ')' '{' statement* '}';
parameters: parameter (',' parameter)*;
parameter: TYPE IDENTIFIER;

// Function Call
functionCall: IDENTIFIER '(' arguments? ')';
arguments: expression (',' expression)*;

// Print
print: 'print' expression;

// Return statement
returnStatement: 'return' expression?;

// Expression
expression:
	expression ('+' | '-') expression
	| expression ('*' | '/' | '%') expression
	| expression ('<' | '>' | '<=' | '>=') expression
	| expression ('==' | '!=') expression
	| expression ('&&' | '||') expression
	| expression '[' expression ']'
	| expression '.' IDENTIFIER
	| '(' expression ')'
	| literal
	| IDENTIFIER
	| functionCall;
literal: INTEGER | FLOAT | STRING | BOOLEAN;

expressionList: expression (',' expression)*;

// Tokens
TYPE: 'int' | 'float' | 'boolean' | 'void' | 'string';
INTEGER: ('0' | [1-9][0-9]*);
FLOAT: [0-9]* '.' [0-9]+;
STRING: '"' ~[\r\n"]* '"';
BOOLEAN: 'true' | 'false';
IDENTIFIER: [a-zA-Z_] [a-zA-Z_0-9]*;
WS: [ \t\r\n]+ -> skip;