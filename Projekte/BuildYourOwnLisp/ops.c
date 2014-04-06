#include "defs.h"

lval* builtin_add(lenv* e, lval* a) { return builtin_op(e, a, "+"); }
lval* builtin_sub(lenv* e, lval* a) { return builtin_op(e, a, "-"); }
lval* builtin_mul(lenv* e, lval* a) { return builtin_op(e, a, "*"); }
lval* builtin_div(lenv* e, lval* a) { return builtin_op(e, a, "/"); }

lval* builtin_mod(lenv* e, lval* a) { return builtin_op(e, a, "\%"); }
lval* builtin_pow(lenv* e, lval* a) { return builtin_op(e, a, "^"); }
lval* builtin_min(lenv* e, lval* a) { return builtin_op(e, a, "min"); }
lval* builtin_max(lenv* e, lval* a) { return builtin_op(e, a, "max"); }

/*lval* builtin(lval* a, char* func) {
	if (strcmp("list", func) == 0) { return builtin_list(a); }
	if (strcmp("head", func) == 0) { return builtin_head(a); }
	if (strcmp("tail", func) == 0) { return builtin_tail(a); }
	if (strcmp("last", func) == 0) { return builtin_last(a); }
	if (strcmp("init", func) == 0) { return builtin_init(a); }

	if (strcmp("join", func) == 0) { return builtin_join(a); }
	if (strcmp("eval", func) == 0) { return builtin_eval(a); }
	if (strcmp("cons", func) == 0) { return builtin_cons(a); }
	if (strcmp("len" , func) == 0) { return builtin_len(a);  }
	

	if (strstr("+-/*^\%minmax", func)) { return builtin_op(a, func); }

	lval_del(a);
	return lval_err("Unknown Function! ", func);
}*/

lval* builtin_op(lval* a, char* op) {
	/* Ensure all arguments are numbers */
	for (int i = 0; i < a->count; i++) {
		if (a->cell[i]->type != LVAL_NUM) {
			lval_del(a);
			return lval_err("Cannot operate on non number! - ", "");
		}
	}
	/* pop first elem */
	lval* x = lval_pop(a, 0);

	/* - as a singleOp */
	if (strcmp(op, "-") == 0 && a->count == 0) { x->num = -x->num; }

	while (a->count > 0) {

		lval* y = lval_pop(a, 0);

		if (strcmp(op, "+") == 0) { x->num += y->num; }
		if (strcmp(op, "-") == 0) { x->num -= y->num; }
		if (strcmp(op, "*") == 0) { x->num *= y->num; }
		if (strcmp(op, "/") == 0) { 
			if (y->num == 0) {
				lval_del(x); lval_del(y); lval_del(a);
				x = lval_err("Division By Zero!", "");
				break;
			} else {
				x->num /= y->num;
			}
		}

		if (strcmp(op, "\%") == 0) { x->num = (int)x->num % (int)y->num; }
		if (strcmp(op, "^") == 0) { x->num = pow(x->num, y->num); }
		if (strcmp(op, "min") == 0) { x->num = x->num < y->num ? x->num : y->num;}
		if (strcmp(op, "max") == 0) { x->num = x->num > y->num ? x->num : y->num;}

		lval_del(y);
	}

	lval_del(a);
	return x;
}

lval* builtin_head(lenv* e, lval* a) { 
	LASSERT(a, (a->count == 1					), "Function 'head' passed to many arguments!");
	LASSERT(a, (a->cell[0]->type == LVAL_QEXPR	), "Function 'head' passed incorrect tpye!");
	LASSERT(a, (a->cell[0]->count != 0			), "Function 'head' passed {}!");

	lval* v = lval_take(a, 0);
	while (v->count > 1) {lval_del(lval_pop(v, 1)); }
	return v;
}

lval* builtin_tail(lenv* e, lval* a) { 
	LASSERT(a, (a->count == 1					), "Function 'tail' passed to many arguments!");
	LASSERT(a, (a->cell[0]->type == LVAL_QEXPR	), "Function 'tail' passed incorrect tpye!");
	LASSERT(a, (a->cell[0]->count != 0			), "Function 'tail' passed {}!");

	lval* v = lval_take(a, 0);
	lval_del(lval_pop(v, 0));
	return v;
}

lval* builtin_list(lenv* e, lval* a) { 
	a->type = LVAL_QEXPR;
	return a;
}

lval* builtin_eval(lenv* e, lval* a) {
	LASSERT(a, (a->count == 1					), "Function 'eval' passed to many arguments!");
	LASSERT(a, (a->cell[0]->type == LVAL_QEXPR	), "Function 'eval' passed incorrect tpye!");

	lval* x = lval_take(a, 0);
	x->type = LVAL_SEXPR;
	return lval_eval(x);
}

lval* builtin_join(lenv* e, lval* a){
	for (int i = 0; i < a->count; i++) {
		LASSERT(a, (a->cell[i]->type == LVAL_QEXPR	), "Function 'join' passed incorrect tpye!");
	}

	lval* x = lval_pop(a, 0);

	while(a->count) {
		x = lval_join(x, lval_pop(a, 0));
	}

	lval_del(a);
	return x;
}

lval* lval_join(lval* x, lval* y) {
	while (y->count) {
		x = lval_add(x, lval_pop(y, 0));
	}

	lval_del(y);
	return x;
}

lval* builtin_cons(lenv* e, lval* a) { 
	LASSERT(a, (a->count == 2					), "Function 'len' passed to many arguments!");
	LASSERT(a, (a->cell[0]->count == 1			), "Use 'join' to join 2 lists together!");
	for (int i = 0; i < a->count; i++) {
		LASSERT(a, (a->cell[i]->type == LVAL_QEXPR	), "Function 'join' passed incorrect tpye!");
	}

	lval* x = lval_join(a->cell[0], a->cell[1]);
	return x;
}

lval* builtin_len(lenv* e, lval* a) { 
 	LASSERT(a, (a->count == 1					), "Function 'len' passed to many arguments!");
	LASSERT(a, (a->cell[0]->type == LVAL_QEXPR	), "Function 'len' passed incorrect tpye!");
	LASSERT(a, (a->cell[0]->count != 0			), "Function 'len' passed {}!");

	lval* v = lval_take(a,0);	
	return lval_num(v->count);  
}

lval* builtin_init(lenv* e, lval* a) {
 	LASSERT(a, (a->count == 1					), "Function 'init' passed to many arguments!");
	LASSERT(a, (a->cell[0]->type == LVAL_QEXPR	), "Function 'init' passed incorrect tpye!");
	LASSERT(a, (a->cell[0]->count != 0			), "Function 'init' passed {}!");

	lval* v = lval_take(a, a->count - 1);
	lval_del(lval_pop(v, v->count - 1 ));
	return v; 
}

lval* builtin_last(lenv* e, lval* a) { 
	LASSERT(a, (a->count == 1					), "Function 'last' passed to many arguments!");
	LASSERT(a, (a->cell[0]->type == LVAL_QEXPR	), "Function 'last' passed incorrect tpye!");
	LASSERT(a, (a->cell[0]->count != 0			), "Function 'last' passed {}!");

	lval* v = lval_take(a, 0);
	while (v->count > 1) {lval_del(lval_pop(v, 0)); }
	return v;
}