#include <stdio.h>
#include <stdlib.h>
#include <math.h>

typedef struct {
	int value;
	float baseSci;
	int expoSci;
} IEEE;

void calc_SciNotation(IEEE *val);
void calc_fraction(IEEE *val);
void print_IEEE(IEEE *val);
void prettyprint_IEEE(IEEE *val);

IEEE *convertToIEEE(float num) {
	IEEE *val =(IEEE*) malloc(sizeof(IEEE));
	printf("Converting %f\n", num);
	val->value = 0;
	val->baseSci = num;
	//printf("Setting SignBit\n");
	if (num < 0) {
		val->value |= (1 << 31);
		val->baseSci *= -1;
	}
	calc_SciNotation(val);
	//printf("base = %g   expo = %d\n", val->baseSci, val->expoSci);
	calc_fraction(val);
	return val;
}

void calc_SciNotation(IEEE *val) {
	short expo = 0;
	//float value = 1;
	float neu = val->baseSci / pow(2, expo);
	//printf("neu = %g   expo = %i\n", neu, expo);
	while(neu < 1 || neu >= 2) {
		expo++;
		neu = val->baseSci / pow(2, expo);		
		//printf("neu = %g   expo = %i\n", neu, expo);
	}
	//printf("neu = %g   expo = %i\n", neu, expo);
	val->baseSci = neu - 1;
	val->expoSci = expo + 127;
	//printf("Writing exponent:\n");
	val->value |= (val->expoSci << 23);	
}

void calc_fraction(IEEE *val) {
	int pos = 22;
	while (val->baseSci > 0 && pos >= 0) {
		float num = pow(2, pos - 23);
		if (val->baseSci - num >= 0) {
			//printf("num = %g  pos -22 = %d\n", num, pos - 23);
			val->value |= (1 << pos);
			val->baseSci -= num;
		}
		pos--;
	}
}

void print_IEEE(IEEE *val) {
	int i;
	char buf[33];
	for (i = 31; i >= 0; i--)
		buf[31-i] = '0' + ((val->value >> i) & 0x1);
	buf[32] = 0;
	printf(">%s\n", buf);
	return;
}

void prettyprint_IEEE(IEEE *val) {
	int e = ((val->value >> 23) & 0xFF);
	float m = 0;
	int s = 0;
	if((val->value >> 31) & 0x1)
		s = 1;
	int i;
	float manAdd = 1;
	for (i = 22; i >= 0; i--) {
		manAdd /= 2;
		m += ((val->value >> i) & 0x1) * manAdd;		
	}
		

	printf("s = %d e = %d m = %g \n", s, e, m);
	float value = pow(-1, s) * (1 + m) * pow (2, e - 127);
	printf("%g = %g * %g * 2^%d\n\n", value, pow(-1, s), (1 + m), e - 127);
}

int main(void) {
	float num;
	while(1) {
		if (scanf("%f",&num) == 1) {
			IEEE *t = convertToIEEE(num);
			print_IEEE(t);
			prettyprint_IEEE(t);
			free(t);
		}
	}
}
