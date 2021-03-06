#include <stdio.h>
#include <stdint.h>

#include <avr/io.h>
#include <avr/interrupt.h>

#include "rs_xplained.h"

#define G_NAME "GRUPPE 13 - A3b"

volatile uint8_t ms = 0;
volatile uint8_t s = 0;
volatile uint8_t min = 0;
volatile bool run = false;
volatile bool update = true;

int main(void) {
	xplained_init();
	fprintf(COMM_LCD, G_NAME);

// 250 ms Timer
// 32 MHz CPU-Takt
// http://eleccelerator.com/avr-timer-calculator/
// 256 Prescaler
// 31250 Timer Top Wert. ~15 s Differenz �ber 10 min ==> ~2,5% Fehler
// 32031 manuell < 0,5 s Differenz �ber 10 min ==>  ~ 0,09% Fehler

// Top Wert
	TCC0.PER = 32031;
// Prescaler
	TCC0.CTRLA = TC_CLKSEL_DIV256_gc;

	while (1) {
		if (update) {
			clearRow(LCD_ROW1);
			fprintf(COMM_LCD, "%i:%i:%i", min, s, ms);
			update = false;
		}
	}
	return 0;
}
// Timer0 OVERFLOW INT
ISR (TCC0_OVF_vect) {
	if (run) {
		ms += 25;
		update = true;
		if (ms >= 100) {
			ms = 0;
			s++;
		}

		if (s >= 60) {
			s = 0;
			min++;
		}

		if (min >= 60)
			min = 0;
	}
}

// SWx INT1
ISR (PORTD_INT1_vect) {
	//SW4
	if ((PORTD.IN & PIN4_bm) == 0) {
		ms = 0;
		s = 0;
		min = 0;
		update = true;
	}

	//SW5
	if ((PORTD.IN & PIN5_bm) == 0)
		run = !run;
}
