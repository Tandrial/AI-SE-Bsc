#include <stdio.h>
#include <stdint.h>
#include <avr/io.h>
#include <avr/interrupt.h>

#include "rs_xplained.h"

#define G_NAME "GRUPPE 13 - A2"

#define ROT  		PIN2_bm
#define GELB 		PIN3_bm
#define GRUEN_OPEN  PIN4_bm
#define GRUEN_CLOSE PIN5_bm

#define TASTE  		PIN2_bm
#define T0			PIN4_bm
#define T1			PIN5_bm

volatile uint8_t state = 0;
volatile uint8_t ms = 0;
volatile uint8_t s = 0;
volatile bool blink = false;

int main(void) {
	xplained_init();
	fprintf(COMM_LCD, G_NAME);

	// 250 ms Timer siehe app3b.c ~ 0,09% Fehler
	// Top Wert
	TCC0.PER = 32031;
	// Prescaler
	TCC0.CTRLA = TC_CLKSEL_DIV256_gc;

	while (1) {
	}
	return 0;
}

ISR (PORTD_INT1_vect) {
	//Taste
	if ((PORTD.IN & TASTE) == 0) {
		if (state == 0) {
			state = 1;
		}
	}

	//T0
	if ((PORTD.IN & T0) == 0) {
		if (state == 3)
			state = 0;
	}

	//T1
	if ((PORTD.IN & T1) == 0) {
		if (state == 1)
			state = 2;
	}
}

ISR (TCC0_OVF_vect) {

	switch (state) {
	//Start
	case 0:
		PORTE.OUT = 0xFF;
		break;
		//opening
	case 1:
		//GRUEN blinkt
		if (blink)
			PORTE.OUT = ~(ROT | GRUEN_OPEN);
		else
			PORTE.OUT = ~(ROT);
		blink = !blink;
		break;

		//wait 10 s
	case 2:
		ms += 25;
		if (ms >= 100) {
			ms = 0;
			s++;
		}
		if (s == 10) {
			state = 3;
			s = 0;
		}
		if (blink)
			PORTE.OUT = ~(ROT | GELB);
		else
			PORTE.OUT = ~(ROT);
		blink = !blink;
		break;

		//closing
	case 3:
		//GRUEN blinkt
		if (blink)
			PORTE.OUT = ~(ROT | GRUEN_CLOSE);
		else
			PORTE.OUT = ~(ROT);
		blink = !blink;
		break;
	default:
		break;
	}
}
