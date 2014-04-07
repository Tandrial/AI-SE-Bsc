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
	
	puts("Lispy Version 0.0.0.0.7\n");
	puts("Type exit to Exit\n");

	setupParser();
	
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
	
	parserCleanUp();

	return 0;
}
