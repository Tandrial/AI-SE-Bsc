/*
 * rs_xplained.c
 *
 *  Created on: 18.12.2013
 *      Author: tan
 */

#include "rs_xplained.h"

void xplained_init(void) {
	board_init();
	mcu_wait_us(10);
	mcu_io_set(PIN_EXTERNAL, 1);
	mcu_enable_interrupt();
	mcu_wait_ms(700);

	st7036_init(ST7036_DISPLAY_3x16, PF_4, PF_3, MCU_SPI_NUM_F, SPI5Mbit);
	st7036_set_contrast(0x0f);

	//SW2-SW7 sind f�r den Interrupt1 aktiviert
	// 0xFC = 1111 1100
	PORTD.INT1MASK |= 0xFC;

	//SW2 - SW7 sind Buttons mit Pull-UP Resistoren
	PORTD.PIN3CTRL = PORT_OPC_WIREDANDPULL_gc;
	PORTD.PIN4CTRL = PORT_OPC_WIREDANDPULL_gc;
	PORTD.PIN5CTRL = PORT_OPC_WIREDANDPULL_gc;
	PORTD.PIN6CTRL = PORT_OPC_WIREDANDPULL_gc;
	PORTD.PIN7CTRL = PORT_OPC_WIREDANDPULL_gc;

	//Timer0 Timer1 haben einen High Level Overflow
	TCC0.INTCTRLA = TC_OVFINTLVL_HI_gc;
	TCC1.INTCTRLA = TC_OVFINTLVL_HI_gc;
}

void clearRow(uint8_t row) {
	st7036_goto(row, 0);
	fprintf(COMM_LCD, "                        ");
	st7036_goto(row, 0);
}
