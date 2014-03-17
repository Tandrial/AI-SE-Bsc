#include <stdio.h>
#include <stdint.h>

#include <avr/io.h>
#include <avr/delay.h>

#include "rsbs_xplained.h"

int main(void) {
	xplained_init();
	fprintf(COMM_LCD, XPLAINED_A1);

	st7036_set_contrast(0x0f);
	st7036_putc('\n');
	st7036_putc('H');
	st7036_putc('e');
	st7036_putc('l');
	st7036_putc('l');
	st7036_putc('o');
	st7036_goto(1, 6);
	st7036_putc('W');
	st7036_putc('o');
	st7036_putc('r');
	st7036_putc('l');
	st7036_putc('d');
	return 0;
}
