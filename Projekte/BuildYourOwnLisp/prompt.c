#include "defs.h"

static char buffer[2048];


char* readline(char* prompt) {
	fputs("lispy> ", stdout);
	fgets(buffer, 2048, stdin);
	char* cpy = malloc(strlen(buffer) + 1);
	strcpy(cpy, buffer);
	cpy[strlen(cpy) - 1] = '\0';
	return cpy;
}

int main(int argc, char** argv) {
	puts("Lispy Version 0.0.0.1.1\n");
	setupParser();

	if (argc == 1) {
//	puts("Loading stdlib ...");
//	lval* args = lval_add(lval_sexpr(), lval_str("s.lspy"));

//	lval* x = builtin_load(env, args);
//	if (x->type == LVAL_ERR) { lval_println(x); }
//	lval_del(args); lval_del(x);

		puts("Type exit to Exit\n");

		while (1) {
			char* input = readline("lispy> ");

			if (strcmp(input, "exit") == 0) {
				puts("Exiting...");
				free(input);
				break;
			}

			parse(input);
			free(input);
		}
	} else if (argc >= 2) {
		for (int i = 1; i < argc; i++) {			
			lval* args = lval_add(lval_sexpr(), lval_str(argv[i]));
			lval* x = builtin_load(env, args);
			if (x->type == LVAL_ERR) { lval_println(x); }
			lval_del(args); lval_del(x);
		}
	}

	parserCleanUp();

	return 0;
}
