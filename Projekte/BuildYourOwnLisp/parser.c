#include "defs.h"

mpc_parser_t* Number;
mpc_parser_t* Symbol;
mpc_parser_t* Sexpr;
mpc_parser_t* Qexpr;
mpc_parser_t* Expr;
mpc_parser_t* Lispy;

lenv* env;

int running  = 1;

void setupParser() {
	/* Create Some Parsers */
	Number 	= mpc_new("number");
	Symbol 	= mpc_new("symbol");
	Sexpr  	= mpc_new("sexpr");
	Qexpr	= mpc_new("qexpr");
	Expr 	= mpc_new("expr");
	Lispy 	= mpc_new("lispy");

	/* Define them with the following Language */
	
	mpca_lang(MPC_LANG_DEFAULT,
	"	number	: /-?([0-9]+\\.)?[0-9]+/					;\
		symbol	: /[a-zA-Z0-9_+\\-*\\/\\\\=<>!&^\%]+/		;\
		sexpr	: '(' <expr>* ')'							;\
		qexpr	: '{' <expr>* '}'							;\
		expr	: <number> | <symbol> | <sexpr>	| <qexpr>	;\
		lispy	: /^/ <expr>* /$/							;"
	,	Number, Symbol, Sexpr, Qexpr, Expr, Lispy);

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

// ---- liest einen Double aus dem AST aus
lval* lval_read_num(mpc_ast_t* t) {
	double x = strtod(t->contents,&t->contents);
	return errno != ERANGE ? lval_num(x) : lval_err("Invalid number: %s", t->contents);
}

// ---- Wandelt einen AST in eine lVAL um
lval* lval_read(mpc_ast_t* t) {
	if (strstr(t->tag, "number")) { return lval_read_num(t); }
	if (strstr(t->tag, "symbol")) { return lval_sym(t->contents); }

	lval* x = NULL;
	if (strcmp(t->tag, ">") == 0) { x = lval_sexpr(); }
	if (strstr(t->tag, "sexpr"))  { x = lval_sexpr(); }
	if (strstr(t->tag, "qexpr"))  { x = lval_qexpr(); }

	for (int i = 0; i < t->children_num; i++) {
		if (strcmp(t->children[i]->contents, "(") == 0) { continue; }
		if (strcmp(t->children[i]->contents, ")") == 0) { continue; }
		if (strcmp(t->children[i]->contents, "{") == 0) { continue; }
		if (strcmp(t->children[i]->contents, "}") == 0) { continue; }
		if (strcmp(t->children[i]->tag, "regex")  == 0) { continue; }

		x = lval_add(x, lval_read(t->children[i]));
	}
	return x;
}

