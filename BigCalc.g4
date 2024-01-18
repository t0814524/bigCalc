grammar BigCalc;

program
        : statement+ EOF
        ;

statement
        : expressionStatement
        | assignmentStatement
        ;

assignmentStatement
        : assignment ';'
        ;

assignment
        : Variable '=' expression
        ;

expressionStatement
        : expression ';'
        ;

expression  
        : LPAREN expression RPAREN              # paren
        | expression op=('*' | '/') expression  # mulDiv
        | expression op=('+' | '-') expression  # addSub
        | Variable                              # var
        | Number                                # num
        ;

Number  
        : Digit* '.' Digit+
        | Digit+
        ;

Variable 
        : Char Digit*
        ;

Char   
        : [A-Za-z]
        ;

Digit   
        : [0-9]
        ;

LPAREN : '(' 
        ;

RPAREN : ')' 
        ;

WS      : [ \t\r\n\u000C]+ -> skip  
        ;

COMMENT
        :   '/*' .*? '*/' -> skip
        ;

LINE_COMMENT
        : '//' ~[\r\n]* -> skip 
        ;


