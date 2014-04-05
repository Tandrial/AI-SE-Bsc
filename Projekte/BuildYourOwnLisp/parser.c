#include "defs.h"

mpc_parser_t* Number;
mpc_parser_t* Symbol;
mpc_parser_t* Sexpr;
mpc_parser_t* Expr;
mpc_parser_t* Lispy;

mpc_result_t r;


void setupParser() {
	/* Create Some Parsers */
	Number   = mpc_new("number");
	Symbol   = mpc_new("symbol");
	Sexpr    = mpc_new("sexpr");
	Expr     = mpc_new("expr");
	Lispy    = mpc_new("lispy");

	/* Define them with the following Language */
	mpca_lang(MPC_LANG_DEFAULT,
  	"  	                        \
	    number 	: /-?([0-9]+\\.)?[0-9]+/									;\
	    symbol 	: '+' | '-' | '*' | '/' | '\%' | '^' | \"min\" | \"max\"	;\
	    sexpr   : '(' <expr>* ')'											;\
	    expr    : <number> | <symbol> | <sexpr>								;\
	    lispy   : /^/ <expr>* /$/											;\
  	",
  	Number, Symbol, Sexpr, Expr, Lispy);

}

void parserCleanUp() {
	mpc_cleanup(4, Number, Symbol, Sexpr, Expr, Lispy);
}

void parse(char* input) {

	if (mpc_parse("<stdin>", input, Lispy, &r)) {
		/* On Success */
		//lval result = eval(r.output);
		//lval_println(result);
		lval* x = lval_eval(lval_read(r.output));
		lval_println(x);
		lval_del(x);
		mpc_ast_delete(r.output);
	} else {
		/* Error */
		mpc_err_print(r.error);
		mpc_err_delete(r.error);
	}
}
