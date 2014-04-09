#ifndef DEFS_H
#define DEFS_H

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#include "mpc.h"

/* lVal Types */
enum { LVAL_ERR, LVAL_NUM, LVAL_SYM, LVAL_STR, LVAL_FUN, LVAL_SEXPR, LVAL_QEXPR };

struct lval;
struct lenv;
typedef struct lval lval;
typedef struct lenv lenv;

typedef lval*(*lbuiltin)(lenv*, lval*);

struct lval {
	int type;

	/* Basic */
	double num;
	char* err;
	char* sym;
	char* str;

	/* Function */
	lbuiltin builtin;
	lenv* env;
	lval* formals;
	lval* body;

	/* Expression */
	int count;
	lval** cell;

};

struct lenv {
	lenv* par;
	int count;
	char** syms;
	lval** vals;
};

#define LASSERT(args, cond, fmt, ...) \
	if (!(cond)) { lval* err = lval_err(fmt, ##__VA_ARGS__); lval_del(args); return err; }

#define LASSERT_TYPE(func, args, index, expect) \
	LASSERT(args, args->cell[index]->type == expect, \
		"Function '%s' passed incorrect type for argument %i. Got %s, Expected %s.", \
		func, index, ltype_name(args->cell[index]->type), ltype_name(expect))

#define LASSERT_NUM(func, args, num) \
	LASSERT(args, args->count == num, \
		"Function '%s' passed incorrect number of arguments. Got %i, Expected %i.", \
		func, args->count, num)

#define LASSERT_NOT_EMPTY(func, args, index) \
	LASSERT(args, args->cell[index]->count != 0, \
		"Function '%s' passed {} for argument %i.", func, index);

// parser.c

extern mpc_parser_t* Number;
extern mpc_parser_t* Symbol;
extern mpc_parser_t* String;
extern mpc_parser_t* Comment;
extern mpc_parser_t* Sexpr;
extern mpc_parser_t* Qexpr;
extern mpc_parser_t* Expr;
extern mpc_parser_t* Lispy;
extern lenv* env;

extern void setupParser(void);
extern void parserCleanUp(void);
extern void parse(char* input);

// parser.c
extern lval* lval_read_num(mpc_ast_t* t);
extern lval* lval_read_str(mpc_ast_t* t);
extern lval* lval_read(mpc_ast_t* t);

// lval.c
extern lval* lval_num(double x);
extern lval* lval_err(char* fmt, ...);
extern lval* lval_sym(char* s);
extern lval* lval_str(char* s);
extern lval* lval_builtin(lbuiltin func);
extern lval* lval_sexpr(void);
extern lval* lval_qexpr(void);
extern lval* lval_lambda(lval* formals, lval* body);

extern char* ltype_name(int t);

extern void lval_del(lval* v);

extern lval* lval_add(lval* v, lval* x);
extern lval* lval_copy(lval* v);
extern lval* lval_join(lval* x, lval* y);
extern int lval_eq(lval* x, lval* y);

extern lval* lval_call(lenv* e, lval* f, lval* a);
extern lval* lval_eval(lenv* e, lval* v);
extern lval* lval_eval_sexpr(lenv* e, lval* v);

extern lval* lval_take(lval* v, int i);
extern lval* lval_pop(lval* v, int i);

extern void lval_print(lval* v);
extern void lval_println(lval* v);
extern void lval_expr_print(lval* v, char open, char close);
extern void lval_print_str(lval* v);

// lenv.c

extern lenv* lenv_new(void);
extern void lenv_del(lenv* e);

extern lenv* lenv_copy(lenv* e);

extern void lenv_def(lenv* e, lval* k, lval* v);
extern lval* lenv_get(lenv* e, lval* k);
extern void lenv_put(lenv* e, lval* k, lval* v);

// ops.c 

extern void lenv_add_builtins(lenv* e);
extern void lenv_add_builtin(lenv* e, char*name, lbuiltin func);
extern lval* builtin_load(lenv* e, lval* a);

extern lval* builtin(lval* a, char* func);
extern lval* builtin_op(lenv*e, lval* a, char* op);
extern lval* builtin_ord(lenv* e, lval* a, char* op);
extern lval* builtin_cmp(lenv* e, lval* a, char* op);

extern lval* builtin_error(lenv* e, lval* a);
extern lval* builtin_print(lenv* e, lval* a);

extern lval* builtin_lambda(lenv* e, lval* a);
extern lval* builtin_def(lenv* e, lval* a);
extern lval* builtin_put(lenv* e, lval* a);
extern lval* builtin_var(lenv* e, lval* a, char* func);

extern lval* builtin_add(lenv* e, lval* a);
extern lval* builtin_sub(lenv* e, lval* a);
extern lval* builtin_mul(lenv* e, lval* a);
extern lval* builtin_div(lenv* e, lval* a);
extern lval* builtin_mod(lenv* e, lval* a);

extern lval* builtin_gt(lenv* e, lval* a);
extern lval* builtin_lt(lenv* e, lval* a);
extern lval* builtin_ge(lenv* e, lval* a);
extern lval* builtin_le(lenv* e, lval* a);

extern lval* builtin_eq(lenv* e, lval* a);
extern lval* builtin_ne(lenv* e, lval* a);

extern lval* builtin_if(lenv* e, lval* a);

extern lval* builtin_head(lenv* e, lval* a);
extern lval* builtin_tail(lenv* e, lval* a);
extern lval* builtin_list(lenv* e, lval* a);
extern lval* builtin_eval(lenv* e, lval* a);
extern lval* builtin_join(lenv* e, lval* a);

extern lval* builtin_cons(lenv* e, lval* a);
extern lval* builtin_init(lenv* e, lval* a);

#endif
