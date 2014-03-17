/**
 * 	@file 	board_xmega_a1_xplained.h
 * 	@author Tim Koczwara
 *  @date	07.08.2011
 *
 *  @brief
 *			Enthält Definitionen und Funktionen der verfügbaren LEDs, sowie der verfügbaren Schalter für das Atmel XMega Xplained A1 Board.
 *			Mit der Initilaisierungsfunktion für das Board werden die MCU, sowie angeschlossene Schalter, LEDs und Peripherie initialisiert.
 *			Das hat den Vorteil, dass Besonderheiten, wie z.B. Pullups intern gesetzt werden.
 *
 *  @version  	1.00
 *  	- Erste Version
 *
 ******************************************************************************/
#ifndef BOARD_HEADER_FIRST_INCLUDE_GUARD
#define BOARD_HEADER_FIRST_INCLUDE_GUARD

#include "../mcu/mcu.h"
#include <avr/io.h>
#include <avr/interrupt.h>
#include <util/delay.h>

#define BOARD_LED_TOTAL			8	/**< Es gibt insgesamt 8 Board LEDs */
#define BOARD_BUTTONS_TOTAL		8	/**< Es gibt insgesamt 8 Board Taster */
#define BOARD_UART_DEBUG		MCU_UART_NUM_C0	/**< UART, die für Debug Ausgaben benutzt wird. */

#define BOARD_LED_ON			0	/**< Board LED einschalten */
#define BOARD_LED_OFF			1	/**< Board LED ausschalten */

/**
 * @brief 		Initialisiert die MCU, sowie die IO Pins für die Schalter und LEDs.
 */
void board_init(void);

/**
 * @brief 		Schaltet eine der verfügbaren Board LEDs. Wenn die LED Nummer nicht vorhanden ist bricht die Funktion einfach ab.
 *
 * @param num				Zu schaltende Board LED 0-7.
 * @param state				0 = LED ein\n
 * 							1 = LED aus
 */
void board_led_set(uint8_t num, uint8_t state);

/**
 * @brief 		Invertiert den Zustand einer Board LED.
 *
 * @param num				Board LED 0-7.
 */
void board_led_toggle(uint8_t num);

/**
 * @brief 		Fragt den aktuellen Zustand einer Taste ab.
 *
 * @param num				Board Taste 0-7.
 * @return					1 = Taste gedrückt.\n
 * 							0 = Taste nicht gedrückt.
 */
uint8_t board_button_get(uint8_t num);

/**
 * Prints a debug message via printf(). The parameters are equal
 * to those of the printf() function.
 */
#define DEBUG(_fmt, ...) \
	printf("DEBUG(%s:%d): "  _fmt,__FILE__,__LINE__,__VA_ARGS__);

/*
 * XPlained LED usage:
 * LED 0 .. LED 3: debug-code steps (hang check)
 * LED 4 .. LED 7: Application specific debug codes
 */
#define DEBUG_LED_STEP(_gen) \
		PORTE.OUT = ((PORTE.OUT&0xf0)|((~_gen)&0x0f))

#define DEBUG_LED_APP(_app) PORTE.OUT = (PORTE.OUT & 0x0f) | ((~_app) << 4)

#define DEBUG_LED(_gen,_app) PORTE.OUT = ((~_gen) & 0x0f) | ((~_app) << 4)

#define DEBUG_LED_INIT() {PORTE.DIR = 0xff;PORTE.OUT = ~0x00;}

void board_reset_sw_init(void);
void board_sw_reset(void);

#endif
