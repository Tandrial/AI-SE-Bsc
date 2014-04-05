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

	/* Print Version and Exit Information */
	puts("Lispy Version 0.0.0.0.5\n");
	puts("Type q to Exit\n");

	setupParser();

	/* In a never ending loop */
	while (1) {

		/* Output our prompt and get input */
		char* input = readline("lispy> ");

		if (strcmp(input, "q") == 0) { 
			free(input);
			break; 
		}

		/* Attempt to Parse the user Input */
		parse(input);

		/* Free retrived input */
		free(input);
	}
	/* Undefine and Delete our Parsers */
	parserCleanUp();

	return 0;
}
