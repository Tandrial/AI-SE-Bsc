#include "defs.h"

lval lval_num(double x) {
	lval v;
	v.type = LVAL_NUM;
	v.num = x;
	return v;
}

lval lval_err(int x) {
	lval v;
	v.type = LVAL_ERR;
	v.err = x;
	return v;
}

void lval_print(lval v) {
	switch (v.type) {
		case LVAL_NUM: printf("%g\n", v.num); break;

		case LVAL_ERR:
			if(v.err == LERR_DIV_ZERO) { printf("Error: Division By Zero!\n"); }
			if(v.err == LERR_BAD_OP) { printf("Error: Invalid Operator!\n"); }
			if(v.err == LERR_BAD_NUM) { printf("Error: Invalid Number!\n"); }
			break;
		
	}
}

void lval_println(lval v) { lval_print(v); putchar('\n'); }