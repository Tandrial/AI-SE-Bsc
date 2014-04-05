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
  	"  	                        \
	    number   : /-?([0-9]+\\.)?[0-9]+/ ;                             \
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
		lval result = eval(r.output);
		lval_println(result);
		mpc_ast_delete(r.output);
	} else {
		/* Error */
		mpc_err_print(r.error);
		mpc_err_delete(r.error);
	}
}


lval eval(mpc_ast_t* t) {

	/* If tagged as number return it directly, otherwise ==> Expr */
	if (strstr(t->tag, "number")) { 
		double x = strtod(t->contents,&t->contents);
		return errno != ERANGE ? lval_num(x) : lval_err(LERR_BAD_NUM);
	}

	/* The Operator is the second child */
	char* op = t->children[1]->contents;	
	lval x = eval(t->children[2]);

	/* Iterate over remaining children, combine with operator */
	int i = 3;
	while(strstr(t->children[i]->tag, "expr")) {
		x = eval_op(x, op, eval(t->children[i++]));		
	}
	return x;
}

lval eval_op(lval x, char* op, lval y) {

	if (x.type == LVAL_ERR) return x;
	if (y.type == LVAL_ERR) return y;

	if (strcmp(op, "+") == 0) { return lval_num(x.num + y.num); }
	if (strcmp(op, "-") == 0) { return lval_num(x.num - y.num); }
	if (strcmp(op, "*") == 0) { return lval_num(x.num * y.num); }
	if (strcmp(op, "/") == 0) { 
		return y.num == 0 ? lval_err(LERR_DIV_ZERO) : lval_num(x.num / y.num); }
	if (strcmp(op, "\%") == 0) { return lval_num((int)x.num % (int)y.num); }
	if (strcmp(op, "^") == 0) { return lval_num(pow(x.num, y.num)); }
	if (strcmp(op, "min") == 0) { return x.num < y.num ? lval_num(x.num) : lval_num(y.num);}
	if (strcmp(op, "max") == 0) { return x.num > y.num ? lval_num(x.num) : lval_num(y.num);}

	return lval_err(LERR_BAD_OP);
}