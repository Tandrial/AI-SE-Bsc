#include <stdlib.h>
#include <stdio.h>

// In der Union werden 
typedef union {
	// Die Zahl als "raw byte" Darstellung
	int raw;
	// Die Zahl als float
	float value;
	// Stukture die die Zahl in Vorzeichen, Exponent und Mantisse aufteilt	
	struct {
		// Little Endian ==> Reihenfolge umdrehen (siehe report)
		unsigned mantisse : 23;
		unsigned exponent : 8;
		unsigned sign : 1;
	} IEEE;	
} IEEE754;

// Wir die Zahl über einen float eingelesen wird value gesetzt
IEEE754 *readFromFloat(float value) {
	IEEE754 *res = (IEEE754*) malloc(sizeof(IEEE754));
	res->value = value;
	return res;
}

// Wir die Zahl über einen int eingelesen wird raw gesetzt
IEEE754 *readFromInt(int raw) {
	IEEE754 *res = (IEEE754*) malloc(sizeof(IEEE754));
	res->raw = raw;
	return res;
}

// Druckt alle gespeicherten Informationen aus
void print_IEEE(IEEE754 *val) {	
	printf("value = %f\n", val->value);	
	printf("raw = %#x\n", val->raw);
	printf("sign = %u\n", val->IEEE.sign);
	printf("Exponent = %u\n", val->IEEE.exponent);
	printf("Mantisse = %#x\n", val->IEEE.mantisse);
}

//BONUS Aufgabe: Druckt die Zahl im Format "m * 2^e"
void prettyprint_IEEE(IEEE754 *val) {
	// CHEAT: Um einfach an den Wert den Dezimalwert der Mantisse zukommen,
	// wird eine neuen IEEE754 mit der selben Mantisse, aber einem e von 127 erstellt.
	// Wegen dem Bias von 127 wird diese neue Zahl zu m * 2^(127-127) = m ausgewertet.
	IEEE754 *helper = (IEEE754*) malloc(sizeof(IEEE754));
	helper->raw = val->raw;
	helper->IEEE.sign = 0;
	helper->IEEE.exponent = 127;
	
	/*
	//(ALT) Alles bis auf Mantisse raus (siehe Report)  
	helper->raw &= 0x007FFFFF;	
	//(ALT) Exponent wird auf 127 gesetzt (siehe Report)
	helper->raw |= 0x3F800000;
	*/
	
	char s = val->IEEE.sign ? '-' : ' ';
	float m = helper->value;
	int e = val->IEEE.exponent;
		
	printf("  %c(1 + %g) * 2 ^ (%d - 127)\n", s, m - 1, e);
	printf("=>%c%g * 2 ^ %d\n", s, m, e - 127);
	free(helper);
}


// Test Methoden

void testFloat(float value) {
	IEEE754 *f = readFromFloat(value);
	print_IEEE(f);
	prettyprint_IEEE(f);
	free(f);
	printf("\n");
}

void testRaw(int raw) {
	IEEE754 *f = readFromInt(raw);
	print_IEEE(f);
	prettyprint_IEEE(f);
	free(f);
	printf("\n");
}

int main(void) {
	testRaw(0xBFC00000);
	testRaw(0x3FC00000);
	testFloat(5.255);
	testFloat(-5.255);
	testFloat(4.5);
	testFloat(-4.5);	
	testFloat(5);	
	testFloat(-5);
	testFloat(-11);
	testFloat(11);	
	testFloat(0.003);
	testFloat(1000000);
	
	return 0;
}
