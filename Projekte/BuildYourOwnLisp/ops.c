#include "defs.h"

lval* builtin_add(lenv* e, lval* a) { return builtin_op(e, a, "+"); }
lval* builtin_sub(lenv* e, lval* a) { return builtin_op(e, a, "-"); }
lval* builtin_mul(lenv* e, lval* a) { return builtin_op(e, a, "*"); }
lval* builtin_div(lenv* e, lval* a) { return builtin_op(e, a, "/"); }

lval* builtin_mod(lenv* e, lval* a) { return builtin_op(e, a, "\%"); }
lval* builtin_pow(lenv* e, lval* a) { return builtin_op(e, a, "^"); }
lval* builtin_min(lenv* e, lval* a) { return builtin_op(e, a, "min"); }
lval* builtin_max(lenv* e, lval* a) { return builtin_op(e, a, "max"); }

lval* builtin_op(lenv*e, lval* a, char* op) {
	/* Ensure all arguments are numbers */
	for (int i = 0; i < a->count; i++) {		
			LASSERT(a, (a->cell[i]->type == LVAL_NUM), 
				"Function '%s' passed incorrect type for argument %i. Got %s, Expected %s.",
				op, i, ltype_name(a->cell[i]->type), ltype_name(LVAL_NUM));
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
				x = lval_err("%s", "Division By Zero!");
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

lval* builtin_def(lenv* e, lval* a) {
	LASSERT(a, (a->cell[0]->type == LVAL_QEXPR), 
		"Function 'def' passed incorrect type for argument 0. Got %s, Expected %s.",
		ltype_name(a->cell[0]->type), ltype_name(LVAL_QEXPR));

	lval* syms = a->cell[0];

	for (int i = 0; i < syms->count; i++) {		
		LASSERT(a, (a->cell[0]->type == LVAL_SYM), 
			"Function 'def' passed incorrect type for argument %i. Got %s, Expected %s.",
			i, ltype_name(a->cell[i]->type), ltype_name(LVAL_SYM));
	}

	for (int i = 0; i < syms->count; i++) {
		lenv_put(e, syms->cell[i], a->cell[i + 1]);
	}

	lval_del(a);
	return lval_sexpr();
}

lval* builtin_head(lenv* e, lval* a) { 	
	LASSERT(a, (a->count == 1), "Function 'head' passed to many arguments. Got %i, expected %i.", a->count, 1);
	LASSERT(a, (a->cell[0]->count != 0), "Function 'head' passed {}!");
	LASSERT(a, (a->cell[0]->type == LVAL_QEXPR), 
		"Function 'head' passed incorrect type for argument 0. Got %s, Expected %s.",
		ltype_name(a->cell[0]->type), ltype_name(LVAL_QEXPR));
	
	lval* v = lval_take(a, 0);
	while (v->count > 1) {lval_del(lval_pop(v, 1)); }
	return v;
}

lval* builtin_tail(lenv* e, lval* a) { 
	LASSERT(a, (a->count == 1), "Function 'tail' passed to many arguments. Got %i, expected %i.", a->count, 1);
	LASSERT(a, (a->cell[0]->count != 0), "Function 'tail' passed {}!");	
	LASSERT(a, (a->cell[0]->type == LVAL_QEXPR), 
		"Function 'tail' passed incorrect type for argument 0. Got %s, Expected %s.",
		ltype_name(a->cell[0]->type), ltype_name(LVAL_QEXPR));
	
	lval* v = lval_take(a, 0);
	lval_del(lval_pop(v, 0));
	return v;
}

lval* builtin_list(lenv* e, lval* a) { 
	a->type = LVAL_QEXPR;
	return a;
}

lval* builtin_eval(lenv* e, lval* a) {
	LASSERT(a, (a->count == 1), "Function 'eval' passed to many arguments. Got %i, expected %i.", a->count, 1);
	LASSERT(a, (a->cell[0]->count != 0), "Function 'eval' passed {}!");	
	LASSERT(a, (a->cell[0]->type == LVAL_QEXPR), 
		"Function 'eval' passed incorrect type for argument 0. Got %s, Expected %s.",
		ltype_name(a->cell[0]->type), ltype_name(LVAL_QEXPR));

	lval* x = lval_take(a, 0);
	x->type = LVAL_SEXPR;
	return lval_eval(e, x);
}

lval* builtin_join(lenv* e, lval* a){
	for (int i = 0; i < a->count; i++) {
		LASSERT(a, (a->cell[0]->type == LVAL_QEXPR), 
		"Function 'join' passed incorrect type for argument 0. Got %s, Expected %s.",
		ltype_name(a->cell[0]->type), ltype_name(LVAL_QEXPR));
	}

	lval* x = lval_pop(a, 0);

	while(a->count) {
		x = lval_join(x, lval_pop(a, 0));
	}

	lval_del(a);
	return x;
}

lval* builtin_cons(lenv* e, lval* a) { 
	LASSERT(a, (a->count == 2), "Function 'cons' passed to many arguments. Got %i, expected %i.", a->count, 2);
	LASSERT(a, (a->cell[0]->count == 1), "Use 'join' to join 2 lists together!");
	LASSERT(a, (a->cell[0]->count != 0), "Function 'cons' passed {}!");	
	for (int i = 0; i < a->count; i++) {
		LASSERT(a, (a->cell[0]->type == LVAL_QEXPR), 
			"Function 'def' passed incorrect type for argument %i. Got %s, Expected %s.",
			i, ltype_name(a->cell[i]->type), ltype_name(LVAL_QEXPR));
	}

	lval* x = lval_join(a->cell[0], a->cell[1]);
	return x;
}

lval* builtin_len(lenv* e, lval* a) { 
 	LASSERT(a, (a->count == 1), "Function 'len' passed to many arguments. Got %i, expected %i.", a->count, 1);
 	LASSERT(a, (a->cell[0]->count != 0), "Function 'len' passed {}!");	
	LASSERT(a, (a->cell[0]->type == LVAL_QEXPR), 
		"Function 'len' passed incorrect type for argument 0. Got %s, Expected %s.",
		ltype_name(a->cell[0]->type), ltype_name(LVAL_QEXPR));
	
	lval* v = lval_take(a,0);	
	return lval_num(v->count);  
}

lval* builtin_init(lenv* e, lval* a) {
 	LASSERT(a, (a->count == 1), "Function 'init' passed to many arguments. Got %i, expected %i.", a->count, 1);
 	LASSERT(a, (a->cell[0]->count != 0), "Function 'init' passed {}!");	
	LASSERT(a, (a->cell[0]->type == LVAL_QEXPR), 
		"Function 'init' passed incorrect type for argument 0. Got %s, Expected %s.",
		ltype_name(a->cell[0]->type), ltype_name(LVAL_QEXPR));
		
	lval* v = lval_take(a, a->count - 1);
	lval_del(lval_pop(v, v->count - 1 ));
	return v; 
}

lval* builtin_last(lenv* e, lval* a) { 
	LASSERT(a, (a->count == 1), "Function 'last' passed to many arguments. Got %i, expected %i.", a->count, 1);
	LASSERT(a, (a->cell[0]->count != 0), "Function 'last' passed {}!");	
	LASSERT(a, (a->cell[0]->type == LVAL_QEXPR), 
		"Function 'last' passed incorrect type for argument 0. Got %s, Expected %s.",
		ltype_name(a->cell[0]->type), ltype_name(LVAL_QEXPR));
	
	lval* v = lval_take(a, 0);
	while (v->count > 1) {lval_del(lval_pop(v, 0)); }
	return v;
}