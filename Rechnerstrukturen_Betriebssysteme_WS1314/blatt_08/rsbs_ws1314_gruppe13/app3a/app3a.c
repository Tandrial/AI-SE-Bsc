#include <stdio.h>
#include <stdint.h>

#include <avr/io.h>
#include <avr/interrupt.h>

#include "rs_xplained.h"

#define G_NAME "GRUPPE 13 - A3a"

volatile uint8_t countSW3 = 0;
volatile uint8_t countSW4 = 0;

int main(void) {
	xplained_init();

	while (1) {
		st7036_goto(LCD_ROW0, 0);
		fprintf(COMM_LCD, "Count SW3: %i", countSW3);

		st7036_goto(LCD_ROW1, 0);
		fprintf(COMM_LCD, "Count SW4: %i", countSW4);

		clearRow(LCD_ROW2);
		st7036_goto(LCD_ROW2, 0);
		fprintf(COMM_LCD, "Diff     : %i", countSW3 - countSW4);
	}
	return 0;
}

ISR (PORTD_INT1_vect) {
	if ((PORTD.IN & PIN3_bm) == 0) {
		countSW3++;
	}
	if ((PORTD.IN & PIN4_bm) == 0) {
		countSW4++;
	}
}
