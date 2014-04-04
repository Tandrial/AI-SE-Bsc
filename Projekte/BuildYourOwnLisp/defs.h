#ifndef DEFS_H
#define DEFS_H

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#include "mpc.h"

/* lVal Types */
enum { LVAL_NUM, LVAL_ERR };

/* Error Types */
enum { LERR_DIV_ZERO, LERR_BAD_OP, LERR_BAD_NUM };

/* lval struct */
typedef struct 
{
	int type;
	double num;
	int err;
} lval;


// parser.c
extern void setupParser();
extern void parserCleanUp();
extern void parse(char* input);

// parser.c
extern lval eval(mpc_ast_t* t);
extern lval eval_op(lval x, char* op, lval y);

// lval.c
extern lval lval_num(double x);
extern lval lval_err(int x);
extern void lval_print(lval v);
extern void lval_println(lval v);

#endif