/**
 * 	@file 		comm.h
 *  @author 	Tim Koczwara
 *  @date		20.08.2011
 *
 *  @brief
 *  		Enth�lt die File Objekte f�r fprintf
 *
 *  @version	1.00
 *  	- Erste Version
 *
 ******************************************************************************/
#ifndef COMM_H_
#define COMM_H_

#include <stdio.h>
#include "../mcu/mcu.h"
#include "../modul/st7036/st7036.h"
#include "board_xmega_a1_xplained.h"
/**
 * @brief 		Diese Funktion wird f�r die FILE Struktur aus dem stdio ben�tigt und dient dazu ein Byte auf das Display zu schreiben.
 *
 * @param c					Das zu schreibende Byte.
 * @param stream			FILE Stream, in den geschrieben wird.
 * @return					Gibt 0 zur�ck.
 */
static int lcd_putc(char c, FILE *stream) {
	st7036_putc(c);
	return 0;
}
/**
 * @brief 		Diese Funktion wird f�r die FILE Struktur aus dem stdio ben�tigt und dient dazu ein Byte auf die Debug Schnittstelle zu schreiben.
 *
 * @param c					Das zu schreibende Byte.
 * @param stream			FILE Stream, in den geschrieben wird.
 * @return					Gibt 0 zur�ck.
 */
static int main_putc(char c, FILE *stream) {
	mcu_uart_putc(BOARD_UART_DEBUG, c);
	return 0;
}

static FILE comm_lcd = FDEV_SETUP_STREAM(lcd_putc, NULL, _FDEV_SETUP_WRITE); ///< File Objekt f�r das Display
static FILE comm_pc = FDEV_SETUP_STREAM(main_putc, NULL, _FDEV_SETUP_WRITE); ///< File Objekt f�r die Schnittstelle zum PC Terminal

#define COMM_LCD	&comm_lcd	///< Ein Makro f�r fprintf, welches den Zeiger auf das Display File Objekt enth�lt.#define COMM_PC		&comm_pc	///< Ein Makro f�r fprintf, welches den Zeiger auf das Debug File Objekt enth�lt.#endif /* COMM_H_ */
