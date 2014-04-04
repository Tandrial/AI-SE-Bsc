#include "defs.h"

mpc_parser_t* Number;
mpc_parser_t* Operator;
mpc_parser_t* Expr;
mpc_parser_t* Lispy;

mpc_result_t r;


void setupParser() {
	/* Create Some Parsers */
	Number   = mpc_new("number");
	Operator = mpc_new("operator");
	Expr     = mpc_new("expr");
	Lispy    = mpc_new("lispy");

	/* Define them with the following Language */
	mpca_lang(MPC_LANG_DEFAULT,
  	"                                                       \
	    number   : /-?[0-9]+/ ;                             \
	    operator : '+' | '-' | '*' | '/' | '\%' | '^' | \"min\" | \"max\" ;            \
	    expr     : <number> | '(' <operator> <expr>+ ')' ;  \
	    lispy    : /^/ <operator> <expr>+ /$/ ;             \
  	",
  	Number, Operator, Expr, Lispy);

}

void parserCleanUp() {
	mpc_cleanup(4, Number, Operator, Expr, Lispy);
}

void parse(char* input) {

	if(mpc_parse("<stdin>", input, Lispy, &r)) {
		/* On Success */
		long result = eval(r.output);
		printf("%li\n", result);
		mpc_ast_delete(r.output);
	} else {
		/* Error */
		mpc_err_print(r.error);
		mpc_err_delete(r.error);
	}
}


long eval(mpc_ast_t* t) {

	/* If tagged as number return it directly, otherwise ==> Expr */
	if (strstr(t->tag, "number")) { 
		return atoi(t->contents);
	}

	/* The Operator is the second child */
	char* op = t->children[1]->contents;

	/* Third Child either 2nd number or Expr */
	long x = eval(t->children[2]);

	/* Iterate over remaining children, combine with operator */
	int i = 3;
	while(strstr(t->children[i]->tag, "expr")) {
		x = eval_op(x, op, eval(t->children[i++]));		
	}
	return x;
}

long eval_op(long x, char* op, long y) {
	if (strcmp(op, "+") == 0) { return x + y;}
	if (strcmp(op, "-") == 0) { return x - y;}
	if (strcmp(op, "*") == 0) { return x * y;}
	if (strcmp(op, "/") == 0) { return x / y;}
	if (strcmp(op, "\%") == 0) { return x % y;}
	if (strcmp(op, "^") == 0) { return pow(x, y);}
	if (strcmp(op, "min") == 0) { return x < y ? x : y;}
	if (strcmp(op, "max") == 0) { return x > y ? x : y;}
	return 0;
}