/**
 * 	@file 	board_xmega_a1_xplained.c
 **/

#include "board_xmega_a1_xplained.h"

void board_init(void) {
	uint8_t i;
	mcu_init_max_internal();
	mcu_io_set_port_dir(PE, 0xFF);	// Port E sind die LEDs
	mcu_io_set(PE, 0xFF);
	mcu_io_set_port_dir(PD, 0x00);	// Port D sind die Taster
	for (i = PD_0; i <= PD_7; i++)
		mcu_io_set_pullup(i, true);	// Pull Ups für alle Schalter aktivieren
	mcu_uart_init(BOARD_UART_DEBUG, 115200, 8, 'N', 1);
	board_reset_sw_init();
}

void board_led_set(uint8_t num, uint8_t state) {
	if (num >= BOARD_LED_TOTAL)
		return;
	mcu_io_set(PE_0 + num, state);
}

void board_led_toggle(uint8_t num) {
	if (num >= BOARD_LED_TOTAL)
		return;
	MCU_IO_TOGGLE(PE_0 + num);
}

uint8_t board_button_get(uint8_t num) {
	if (num >= BOARD_BUTTONS_TOTAL)
		return 0;
	return mcu_io_get(PD_0 + num);
}

void board_reset_sw_init() {
	PORTD.DIRCLR = 0xff; //Input
	PORTD.INTCTRL = PORT_INT0LVL_HI_gc | PORT_INT1LVL_MED_gc; // Highest level
	PMIC.CTRL |= PMIC_HILVLEN_bm | PMIC_MEDLVLEN_bm | PMIC_LOLVLEN_bm;
	PORTD.INTFLAGS = 1; //clear interrupt flag
	PORTD.INT0MASK |= 0x03;

	PORTD.PIN0CTRL = PORT_OPC_WIREDANDPULL_gc;
	PORTD.PIN1CTRL = PORT_OPC_WIREDANDPULL_gc;
}

void board_sw_reset() {
	CCP = CCP_IOREG_gc;
	RST.CTRL = 1;
}

ISR (PORTD_INT0_vect) {
	if ((PORTD.IN & 0x1) == 0) {
		board_sw_reset();
	}

	if ((PORTD.IN & 0x2) == 0) {
		board_sw_reset();
	}

	board_led_toggle(4);
}
