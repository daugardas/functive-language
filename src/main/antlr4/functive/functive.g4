grammar functive;

// Main rule
program: statement* EOF;

// Statement
statement:
	varDeclaration ';'
	| arrayDeclaration ';'
	| arrayAssignment ';'
	| arrayAccessAssignment ';'
	| assignment ';'
	| ifElseIfElseStatement
	| switchStatement
	| forLoop
	| whileLoop
	| functionDeclaration
	| functionCall ';'
	| print ';'
	| returnStatement ';'
	| breakStatement ';';

// Variable Declaration
varDeclaration: (TYPE | 'String') IDENTIFIER ('=' expression)?;

// Array Declaration
arrayDeclaration:
	TYPE ('[' ']') IDENTIFIER ('=' '[' expressionList ']')?;

// Assignment
arrayAccessAssignment: IDENTIFIER arrayAccess '=' expression;
arrayAccess: '[' expression ']';
arrayAssignment: IDENTIFIER '=' '[' expressionList ']';
assignment: IDENTIFIER '=' expression;
// arrayAccess is optional, because it means that we are specifying the index of the array

// If statement
ifElseIfElseStatement:
	ifStatement elseifStatement* elseStatement?;
ifStatement: 'if' '(' expression ')' block;
elseifStatement: 'else' 'if' '(' expression ')' block;
elseStatement: 'else' block;

// Switch statement
switchStatement:
	'switch' '(' expression ')' '{' caseStatement* defaultStatement? '}';

caseStatement: 'case' expression ':' statement*;
breakStatement: 'break';

defaultStatement: 'default' ':' statement*;

// For loop
forLoop: 'for' '(' forControl ')' block;
forControl: (varDeclaration | assignment) ';' expression ';' assignment;

// While loop
whileLoop: 'while' '(' expression ')' block;

// Function Declaration
functionDeclaration:
	'phoonk' (TYPE | 'void') IDENTIFIER '(' parameters? ')' block;
parameters: parameter (',' parameter)*;
parameter: TYPE IDENTIFIER;

// Function Call
functionCall: IDENTIFIER '(' arguments? ')';
arguments: argument (',' argument)*;
argument: IDENTIFIER | expression;

// Block
block: '{' statement* '}';

// Print
print: 'print' expression;

// Return statement
returnStatement: 'return' expression?;

// Expression
expression:
	expression '+' expression		# addExpression
	| expression '-' expression		# subtractExpression
	| expression '*' expression		# multiplyExpression
	| expression '/' expression		# divideExpression
	| expression '%' expression		# modulusExpression
	| expression '<' expression		# lessThanExpression
	| expression '>' expression		# greaterThanExpression
	| expression '<=' expression	# lessThanEqualExpression
	| expression '>=' expression	# greaterThanEqualExpression
	| expression '==' expression	# equalExpression
	| expression '!=' expression	# notEqualExpression
	| expression '&&' expression	# andExpression
	| expression '||' expression	# orExpression
	| expression '[' expression ']'	# arrayAccessExpression
	//| expression '.' IDENTIFIER		# objectAccessExpression // we don't have objects
	| '(' expression ')'	# parenthesisExpression
	| literal				# literalExpression
	| IDENTIFIER			# identifierExpression
	| functionCall			# functionCallExpression;
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

// Lexer rules
SINGLE_LINE_COMMENT: '//' ~[\r\n]* -> skip;

MULTI_LINE_COMMENT: '/*' .*? '*/' -> skip;