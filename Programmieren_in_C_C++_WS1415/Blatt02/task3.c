#include <stdio.h>
#include <string.h>
#include <stdlib.h>

// Typ für die Funktions-Pointer
typedef void (*fn)(int,int);

// Typ für die Kombination (OpCode/Funktion)
typedef struct {
	char *op;
	fn function;
} opCode;

// Die verschiedenen Funktionen
void add(int num1, int num2) {
	printf(" %d\n", num1 + num2);
}

void sub(int num1, int num2) {
	printf(" %d\n", num1 - num2);
}

void mult(int num1, int num2) {
	printf(" %lld\n", (long long int) num1 * num2);
}

void divide(int num1, int num2) {
	if (num2 != 0)
		printf(" %g\n", num1 / (float) num2);
	else
		printf("Division durch 0\n");
}

// Array mit OpCodes OpCode : Funktion
opCode opCodes[] = {{"+", &add},  {"-", &sub},
					{"*", &mult}, {"/", &divide}};

int main(int argc, char** argv) {
	int size, num1, num2, i;
	char *op;

	// Abbruch wenn nicht passende Parameter übergeben werden
	if (argc != 4) {
		printf("Unbekannte Eingabe! Erwartet: %s <int> <op> <int>\n", argv[0]);
		exit(1);	
	}

	// Einlesen der Argumente
	num1 = atoi(argv[1]);
	op   = argv[2];
	num2 = atoi(argv[3]);
	size = sizeof(opCodes) / sizeof(opCode);	

	// Schleife durch alle bekannten OpCodes
	for (i = 0; i < size; i++) {
		// Falls gefunden Funktion ausführen
		if (strcmp(opCodes[i].op, op) == 0) {
			(*opCodes[i].function)(num1, num2);
			exit(0);
		}
	}
	
	printf("Unbekannter OpCode : %s\n", argv[2]);
	exit(1);
}
