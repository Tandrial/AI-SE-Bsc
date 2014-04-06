#ifndef DEFS_H
#define DEFS_H

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#include "mpc.h"

/* lVal Types */
enum { LVAL_ERR, LVAL_NUM, LVAL_SYM, LVAL_SEXPR, LVAL_QEXPR };

/* Error Types */
enum { LERR_DIV_ZERO, LERR_BAD_OP, LERR_BAD_NUM };

/* lval struct */
typedef struct lval {
	int type;
	double num;

	char* err;
	char* sym;

	int count;
	struct lval** cell;

} lval;

#define LASSERT(args, cond, err) if (!(cond)) { lval_del(args); return lval_err(err, ""); }

// parser.c
extern void setupParser();
extern void parserCleanUp();
extern void parse(char* input);

// parser.c
extern lval eval(mpc_ast_t* t);
extern lval eval_op(lval x, char* op, lval y);

// lval.c
extern lval* lval_num(double x);
extern lval* lval_err(char* m, char* func);
extern lval* lval_sym(char* s);
extern lval* lval_sexpr(void);

extern void lval_del(lval* v);

extern lval* lval_add(lval* v, lval* x);
extern lval* lval_read_num(mpc_ast_t* t);
extern lval* lval_read(mpc_ast_t* t);

extern lval* lval_eval(lval* v);
extern lval* lval_eval_sexpr(lval* v);

extern lval* lval_take(lval* v, int i);
extern lval* lval_pop(lval* v, int i);

extern void lval_print(lval* v);
extern void lval_println(lval* v);
extern void lval_expr_print(lval* v, char open, char close);

// ops.c 
extern lval* builtin(lval* a, char* func);
extern lval* builtin_op(lval* a, char* op);
extern lval* builtin_head(lval* a);
extern lval* builtin_tail(lval* a);
extern lval* builtin_list(lval* a);
extern lval* builtin_eval(lval* a);
extern lval* builtin_join(lval* a);
extern lval* lval_join(lval* x, lval* y);
extern lval* builtin_cons(lval* a);
extern lval* builtin_len(lval* a);
extern lval* builtin_init(lval* a);
extern lval* builtin_last(lval* a);


#endif
