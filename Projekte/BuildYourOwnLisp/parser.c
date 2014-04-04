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
	    operator : '+' | '-' | '*' | '/' ;                  \
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
		mpc_ast_print(r.output);
		mpc_ast_delete(r.output);
	} else {
		/* Error */
		mpc_err_print(r.error);
		mpc_err_delete(r.error);
	}
}