#ifndef DEFS_H
#define DEFS_H

#include <stdio.h>
#include <stdlib.h>

#include "mpc.h"


mpc_parser_t* Number;
mpc_parser_t* Operator;
mpc_parser_t* Expr;
mpc_parser_t* Lispy;

mpc_result_t r;

// parser.c

extern void setupParser();
extern void parserCleanUp();
extern void parse(char* input);

#endif