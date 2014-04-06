#include "defs.h"

mpc_parser_t* Number;
mpc_parser_t* Symbol;
mpc_parser_t* Sexpr;
mpc_parser_t* Qexpr;
mpc_parser_t* Expr;
mpc_parser_t* Lispy;

lenv* env;

void setupParser() {
	/* Create Some Parsers */
	Number 	= mpc_new("number");
	Symbol 	= mpc_new("symbol");
	Sexpr  	= mpc_new("sexpr");
	Qexpr	= mpc_new("qexpr");
	Expr 	= mpc_new("expr");
	Lispy 	= mpc_new("lispy");

	/* Define them with the following Language */

	//TODO cons len init
	mpca_lang(MPC_LANG_DEFAULT,
  	"   number 	: /-?([0-9]+\\.)?[0-9]+/																						;\
	    symbol 	: /[a-zA-Z0-9_+\\-*\\/\\\\=<>!&^\%]+/																			;\
	    sexpr   : '(' <expr>* ')'																								;\
	    qexpr   : '{' <expr>* '}' 																								;\
	    expr    : <number> | <symbol> | <sexpr>	| <qexpr>																		;\
	    lispy   : /^/ <expr>* /$/																								;"
  	, 	Number, Symbol, Sexpr, Qexpr, Expr, Lispy);

	env = lenv_new();
	lenv_add_builtins(env);
}

void parserCleanUp() {
	mpc_cleanup(6, Number, Symbol, Sexpr, Qexpr, Expr, Lispy);
	lenv_del(env);
}

void parse(char* input) {
	mpc_result_t r;
	if (mpc_parse("<stdin>", input, Lispy, &r)) {
		/* On Success */
		lval* x = lval_eval(env, lval_read(r.output));		
		lval_println(x);
		lval_del(x);		
		//mpc_ast_print(r.output);
		mpc_ast_delete(r.output);
	} else {
		/* Error */
		mpc_err_print(r.error);
		mpc_err_delete(r.error);
	}
}
