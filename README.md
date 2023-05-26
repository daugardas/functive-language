# Functive Language Compiler

This is a compiler for the Functive programming language, written in Java with ANTLR4.

## Features

- Access all function results through the array of the same name as the function.
- Array index larger than the array applies the modulus operation.
- Negative array access returns results from the end of the array.

## Getting Started

1. Clone the repository.
2. Compile the project with MAVEN:
``mvn compile -f <path to pom.xml>``
3. Generate the compiler:
``mvn package -f <path to pom.xml>``
4. Launch the compiler with your given functive code:
``java -jar .\target\functive-1.0-SNAPSHOT.jar <relative path to the functive code> ``