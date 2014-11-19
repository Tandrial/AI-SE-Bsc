#include <stdio.h>
#include <string.h>

#define BUFFER_SIZE 256

int main(void) {
	// Buffer für den Userinput
	char userInput[BUFFER_SIZE];		
	// Buffer für das ErrorChecking
	char error[BUFFER_SIZE];
	int running = 1;
	int num1, num2;
	char op;
	printf("Der Taschenrechner erwartet eine Eingaben der Form <int> <+-*/> <int>:\nEnde mit '0'\n");

	while(running) {
		printf("> ");
		// Speichert den UserInput im Buffer
		if (fgets(userInput, BUFFER_SIZE, stdin) != NULL) {
			// Abbruch bei der Eingabe "0"
			if (strncmp(userInput, "0\n", 2) == 0)
				running = 0;
			else {
				// Durchsucht den String nach dem Schema  "<int> <char> <int>"
				// Nur wenn danach nichts mehr kommt sind nur 3 Argumente gesetzt
				if (sscanf(userInput, "%d %c %d %s", &num1, &op, &num2, error) == 3){
					// Falls genau das Schema gefunden wurde berechne das Ergebnis
					switch(op) {
						case '+':
							printf(" %d\n", num1 + num2);
							break;
						case '-':
							printf(" %d\n", num1 - num2);
							break;
						case '*':
							printf(" %lld\n", (long long int) num1 * num2);
							break;
						case '/':
							// Divion durch 0 ist nicht erlaubt
							if (num2 != 0)
								// Float division!!!!
								printf(" %g\n", num1 / (float) num2);
							else 
								printf("Division durch 0\n");
							break;
						default:
							printf("Unbekannter Operator: %c\n", op);
							break;
					}
				} else 
					// Ansonsten Error-Nachricht
					printf("Unerwartete Eingabe: %s", userInput);
			}
		}
	}
	return 0;
}
