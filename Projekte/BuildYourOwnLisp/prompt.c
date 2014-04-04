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
	puts("Lispy Version 0.0.0.0.2\n");
	puts("Press Ctrl+c to Exit\n");

	setupParser();

	/* In a never ending loop */
	while (1) {

		/* Output our prompt and get input */
		char* input = readline("lispy> ");

		/* Attempt to Parse the user Input */
		parse(input);

		/* Free retrived input */
		free(input);
	}
	/* Undefine and Delete our Parsers */
	parserCleanUp();

	return 0;
}
