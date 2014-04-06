#include "defs.h"
// ---- Konstruktoren für die verschiedenne LVal-Typen
lval* lval_num(double x) {
	lval* v = malloc(sizeof(lval));
	v->type = LVAL_NUM;
	v->num = x;
	return v;
}

lval* lval_err(char* m, char* func) {
	lval* v = malloc(sizeof(lval));
	v->type = LVAL_ERR;
	v->err = malloc(strlen(m) + strlen(func) + 1);
	strcpy(v->err, m);
	strcat(v->err, func);
	return v;
}

lval* lval_sym(char* s) {
	lval* v = malloc(sizeof(lval));
	v->type = LVAL_SYM;
	v->sym = malloc(strlen(s) + 1);
	strcpy(v->sym, s);
	return v;
}

lval* lval_fun(lbuiltin func) {
	lval* v = malloc(sizeof(lval));
	v->type = LVAL_FUN;	
	v->fun = func;
	return v;
}

lval* lval_sexpr(void) {
	lval* v = malloc(sizeof(lval));
	v->type = LVAL_SEXPR;
	v->count = 0;
	v->cell = NULL;
	return v;
}

lval* lval_qexpr(void) {
	lval* v = malloc(sizeof(lval));
	v->type = LVAL_QEXPR;
	v->count = 0;
	v->cell = NULL;
	return v;
}

// ---- Hängt x and das Ende von v an
lval* lval_add(lval* v, lval* x) {
	v->count++;
	v->cell = realloc(v->cell, sizeof(lval*) * v->count);
	v->cell[v->count - 1] = x;
	return v;
}

// ---- Erstellt eine Deep-Copy von v
lval* lval_copy(lval* v) {
	lval* x = malloc(sizeof(lval));
	x->type = v->type;

	switch (v->type) {
		case LVAL_FUN: x->fun = v->fun; break;
		case LVAL_NUM: x->num = v->num; break;

		case LVAL_ERR: x->err = malloc(strlen(v->err) + 1); strcpy(x->err, v->err); break;
		case LVAL_SYM: x->sym = malloc(strlen(v->sym) + 1); strcpy(x->sym, v->sym); break;

		case LVAL_SEXPR:
		case LVAL_QEXPR:
			x->count = v->count;
			x->cell = malloc(sizeof(lval*) * x->count);
			for (int i = 0; i < x->count; i++) {
				x->cell[i] = lval_copy(v->cell[i]);
			}
			break;
	}
	return x;
}

// ---- Löscht v
void lval_del(lval* v) {
	switch(v->type) {
		case LVAL_NUM: break;
		case LVAL_ERR: free(v->err); break;		
		case LVAL_SYM: free(v->sym); break;
		case LVAL_FUN: break;

		case LVAL_SEXPR:
		case LVAL_QEXPR:
			for (int i = 0; i < v->count; i++) {
				lval_del(v->cell[i]);
			}
			free(v->cell);
			break;
	}
	free(v);
}

// ---- liest einen Double aus dem AST aus
lval* lval_read_num(mpc_ast_t* t) {
	double x = strtod(t->contents,&t->contents);
	return errno != ERANGE ? lval_num(x) : lval_err("invalid number", "");
}

// ---- Wandelt einen AST in eine lVAL um
lval* lval_read(mpc_ast_t* t) {
	if (strstr(t->tag, "number")) { return lval_read_num(t); }
	if (strstr(t->tag, "symbol")) { return lval_sym(t->contents); }

	lval* x = NULL;
	if (strcmp(t->tag, ">") == 0) { x = lval_sexpr(); }
	if (strstr(t->tag, "sexpr")) { x = lval_sexpr(); }
	if (strstr(t->tag, "qexpr")) { x = lval_qexpr();}

	for (int i = 0; i < t->children_num; i++) {
		if (strcmp(t->children[i]->contents, "(") == 0) { continue; }
		if (strcmp(t->children[i]->contents, ")") == 0) { continue; }
		if (strcmp(t->children[i]->contents, "{") == 0) { continue; }
		if (strcmp(t->children[i]->contents, "}") == 0) { continue; }
		if (strcmp(t->children[i]->tag, "regex") == 0) { continue; }

		x = lval_add(x, lval_read(t->children[i]));
	}

	return x;
}

// ---- Verarbeitet den Type Eval
lval* lval_eval(lenv* e, lval* v) {
	if (v->type == LVAL_SYM)   { return lenv_get(e, v); }
	if (v->type == LVAL_SEXPR) { return lval_eval_sexpr(e, v); }
  return v;
}

// ---- Verarbeitet den Type S-Expression
lval* lval_eval_sexpr(lenv* e, lval* v) {
	
	for (int i = 0; i < v->count; i++) {
		v->cell[i] = lval_eval(e, v->cell[i]);
	}

	/* Error checking */
	for (int i = 0; i <v->count; i++) {
		if (v->cell[i]->type == LVAL_ERR) { return lval_take(v, i); }
	}

	if (v->count == 0) { return v; }	
	if (v->count == 1) { return lval_take(v, 0); }

	/* Ensure first Element is Symbol */
	lval* f = lval_pop(v, 0);
	if(f->type != LVAL_FUN) {
		lval_del(f); 
		lval_del(v);
		return lval_err("First element is not a function!", "");
	}

	/* Call builtin  with op */
	lval* result = f->fun(e, v);
	lval_del(f);
	return result;
}

// ---- Liefert die i-te lVal von v und löscht v danach
lval* lval_take(lval* v, int i) {
	lval* x = lval_pop(v, i);
	lval_del(v);
	return x;
}

// ---- Liefter die i-te lVal von v
lval* lval_pop(lval* v, int i) {
	/* Get item @ i */
	lval* x = v->cell[i];

	/* Override the array @ i */
	memmove(&v->cell[i], &v->cell[i+1], sizeof(lval*) * (v->count - i - 1));

	v->count--;

	v->cell = realloc(v->cell, sizeof(lval*) * v->count);
	return x;
}

// ---- Druckt eine Komplette lVal aus
void lval_expr_print(lval* v, char open, char close) {
	putchar(open);
	for (int i = 0; i < v->count; i++) {
		lval_print(v->cell[i]);

		if (i != (v->count - 1)) {
			putchar(' ');
		}
	}
	putchar(close);
}

// ---- Druck lVals mit und ohne newline
void lval_println(lval* v) { lval_print(v); putchar('\n'); }

void lval_print(lval* v) {
	switch (v->type) {
		case LVAL_NUM: 	 printf("%g", v->num); break;
		case LVAL_ERR: 	 printf("Error: %s", v->err); break;
		case LVAL_SYM: 	 printf("%s", v->sym); break;
		case LVAL_FUN:   printf("<function>"); break;
		case LVAL_SEXPR: lval_expr_print(v, '(', ')'); break;
		case LVAL_QEXPR: lval_expr_print(v, '{', '}'); break;
	}
}
