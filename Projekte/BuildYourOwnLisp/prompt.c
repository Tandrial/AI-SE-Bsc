#include <stdio.h>

/* Declare a static buffer for user input of maximum size 2048 */
static char input[2048];

int main(int argc, char** argv) {

	/* Print Version and Exit Information */
	puts("Lispy Version 0.0.0.0.1\n");
	puts("Press Ctrl+c to Exit\n");

	/* In a never ending loop */
	while (1) {

		/* Output our prompt */
		fputs("lispy> ", stdout);

		/* Read a line of user input of maximum size 2048 */
		fgets(input, 2048, stdin);

		/* Echo input back to user */
		printf("No you're a %s\n", input);
	}

	return 0;
}
