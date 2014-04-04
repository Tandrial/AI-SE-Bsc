#ifndef DEFS_H
#define DEFS_H

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#include "mpc.h"

// parser.c
extern void setupParser();
extern void parserCleanUp();
extern void parse(char* input);

// parser.c
extern long eval(mpc_ast_t* t);
extern long eval_op(long x, char* op, long y);

#endif