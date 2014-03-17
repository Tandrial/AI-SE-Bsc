#include <stdio.h>
#include <stdint.h>

#include <avr/io.h>
#include <avr/interrupt.h>

#include "rs_xplained.h"

#define G_NAME "GRUPPE 13 - A1b"
#define COUNTER_MAX 255

volatile uint8_t count = 0;
volatile bool update = false;

/*
 * EINGÄNGE
 * 		SW3 : Befindet sich auf PIN3 von PORTD
 *
 * AUSGÄNGE
 *		LED0 - LED7 : Befinden sich auf PORTE, wobei LED0 das LSB und LED7 das MSB ist.
 *
 *		LCD : Angestuert über st7036_goto und fprintf
 */

int main(void) {
	xplained_init();
	fprintf(COMM_LCD, G_NAME);

	// 200 ms Timer
	// 80% vom Timer aus Aufgabe 3
	// + 30 min ==> 0s Abweichung ==> Fehler  < 0,05 %
	// +135 min ==>~1s Abweichung ==> Fehler  ~ 0,01 %

	// Top Wert
	TCC0.PER = 25625;
	// Prescaler
	TCC0.CTRLA = TC_CLKSEL_DIV256_gc;

	while (1) {
		if (update) {
			st7036_goto(LCD_ROW1, 0);
			fprintf(COMM_LCD, "Count: %i", count);
			update = false;
		}
	}
	return 0;
}

// Timer0 OVERFLOW INT
ISR (TCC0_OVF_vect) {
	if (count > COUNTER_MAX) {
		count = 0;
	}
	//LEDs neusetzen
	PORTE.OUT = ~count++; // 0001 1111 1110 0000
	clearRow(LCD_ROW1);
	update = true;
}

// SWx INT1
ISR (PORTD_INT1_vect) {
//SW3
	if ((PORTD.IN & PIN3_bm) == 0) {
		//Reset counter und Ausgabe
		count = 0;
		PORTE.OUT = ~count;
		clearRow(LCD_ROW1);
		update = true;
	}
}
