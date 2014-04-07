#ifndef DEFS_H
#define DEFS_H

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#include "mpc.h"

/* lVal Types */
enum { LVAL_ERR, LVAL_NUM, LVAL_SYM, LVAL_FUN, LVAL_SEXPR, LVAL_QEXPR };

struct lval;
struct lenv;
typedef struct lval lval;
typedef struct lenv lenv;

typedef lval*(*lbuiltin)(lenv*, lval*);

struct lval {
	int type;

	double num;
	char* err;
	char* sym;
	lbuiltin fun;

	int count;
	lval** cell;

};

struct lenv {
	int count;
	char** syms;
	lval** vals;
};


#define LASSERT(args, cond, err) if (!(cond)) { lval_del(args); return lval_err(err, ""); }

// parser.c
extern void setupParser(void);
extern void parserCleanUp(void);
extern void parse(char* input);

// parser.c
extern lval eval(mpc_ast_t* t);
extern lval eval_op(lval x, char* op, lval y);

// lval.c
extern lval* lval_num(double x);
extern lval* lval_err(char* m, char* func);
extern lval* lval_sym(char* s);
extern lval* lval_fun(lbuiltin func);
extern lval* lval_sexpr(void);
extern lval* lval_qexpr(void);

extern void lval_del(lval* v);

extern lval* lval_add(lval* v, lval* x);
extern lval* lval_copy(lval* v);
extern lval* lval_read_num(mpc_ast_t* t);
extern lval* lval_read(mpc_ast_t* t);

extern lval* lval_eval(lenv* e, lval* v);
extern lval* lval_eval_sexpr(lenv* e, lval* v);

extern lval* lval_take(lval* v, int i);
extern lval* lval_pop(lval* v, int i);

extern void lval_print(lval* v);
extern void lval_println(lval* v);
extern void lval_expr_print(lval* v, char open, char close);

// lenv.c

extern lenv* lenv_new(void);
extern void lenv_del(lenv* e);

extern lval* lenv_get(lenv* e, lval* k);
extern void lenv_put(lenv* e, lval* k, lval* v);

extern void lenv_add_builtin(lenv* e, char*name, lbuiltin func);
extern void lenv_add_builtins(lenv* e);

// ops.c 

extern lval* builtin(lval* a, char* func);
extern lval* builtin_op(lenv*e, lval* a, char* op);

extern lval* builtin_add(lenv* e, lval* a);
extern lval* builtin_sub(lenv* e, lval* a);
extern lval* builtin_mul(lenv* e, lval* a);
extern lval* builtin_div(lenv* e, lval* a);

extern lval* builtin_mod(lenv* e, lval* a);
extern lval* builtin_pow(lenv* e, lval* a);
extern lval* builtin_min(lenv* e, lval* a);
extern lval* builtin_max(lenv* e, lval* a);

extern lval* builtin_head(lenv* e, lval* a);
extern lval* builtin_tail(lenv* e, lval* a);
extern lval* builtin_list(lenv* e, lval* a);
extern lval* builtin_eval(lenv* e, lval* a);
extern lval* builtin_join(lenv* e, lval* a);
extern lval* lval_join(lval* x, lval* y);
extern lval* builtin_cons(lenv* e, lval* a);
extern lval* builtin_len(lenv* e, lval* a);
extern lval* builtin_init(lenv* e, lval* a);
extern lval* builtin_last(lenv* e, lval* a);


#endif
