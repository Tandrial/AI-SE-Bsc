/*
 * rs_xplained.h
 *
 *  Created on: 18.12.2013
 *      Author: tan
 */

#ifndef RS_XPLAINED_H_
#define RS_XPLAINED_H_

#include <stdlib.h>
#include <math.h>
#include "mcu/mcu.h"
#include "board/comm.h"
#include "board/board_xmega_a1_xplained.h"
#include "modul/st7036/st7036.h"

#define LCD_ROW0				0x00
#define LCD_ROW1				0x01
#define LCD_ROW2				0x02
#define SPI5Mbit				5000000UL

typedef unsigned char uint8_t;

/**
 * @brief 	Initialisierungroutine: Displays, Interrupts (freischalten), dcf77 sensors, clock.
 */
void xplained_init(void);
void clearRow(uint8_t row);

#endif /* RS_XPLAINED_H_ */
