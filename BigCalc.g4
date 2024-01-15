grammar BigCalc;

expressionStatement
        : expression ';' EOF
        ;

expression  
        : expression op=('*' | '/') expression  # mulDiv
        | expression op=('+' | '-') expression  # addSub
        | Number                                # num
        ;

Number  
        : Digit* '.' Digit+
        | Digit+
        ;

Digit   
        : [0-9]
        ;

WS      : [ \t\r\n\u000C]+ -> skip  
        ;

COMMENT
        :   '/*' .*? '*/' -> skip
        ;

LINE_COMMENT
        : '//' ~[\r\n]* -> skip 
        ;


