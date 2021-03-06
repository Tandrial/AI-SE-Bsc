/*
 * app3c.c
 *
 *  Created on: 19.12.2013
 *      Author: tan
 */

#include <stdio.h>
#include <stdint.h>

#include <avr/io.h>
#include <avr/interrupt.h>

#include "rs_xplained.h"

#define G_NAME "GRUPPE 13 - A3c"

volatile uint8_t ms = 0;
volatile uint8_t s = 0;
volatile uint8_t min = 0;
volatile uint8_t hour = 0;
volatile uint8_t day = 1;
volatile uint8_t month = 1;
volatile uint16_t year = 1900;
volatile bool update = true;

uint8_t tageImMonat[12] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

bool isSchalt(uint16_t y) {
	if (y % 400 == 0)
		return true;
	if (y % 100 == 0)
		return false;
	if (y % 4 == 0)
		return true;
	return false;
}

void setDate(uint16_t pYear, uint8_t pMonth, uint8_t pDay, uint8_t pHour,
		uint8_t pMin, uint8_t pS) {
	if (pYear < 0 || pYear > 32767)
		year = 1900;
	else
		year = pYear;

	if (pMonth <= 0 || pMonth > 12)
		month = 1;
	else
		month = pMonth;

	if (pDay <= 0)
		day = 1;
	else
		day = pDay;

	if (isSchalt(year) & (month == 2)) {
		if (day > 29) {
			day = 1;
		}
	} else {
		if (day > tageImMonat[month - 1]) {
			day = 1;
		}
	}

	if (pHour < 0 || pHour > 23)
		hour = 0;
	else
		hour = pHour;

	if (pMin < 0 || pMin > 59)
		min = 0;
	else
		min = pMin;

	if (pS < 0 || pS > 59)
		s = 0;
	else
		s = pS;
	ms = 0;
}

int main(void) {
	xplained_init();
	fprintf(COMM_LCD, G_NAME);
	setDate(2013, 1, 15, 11, 32, 50);
// 250 ms Timer siehe app3b.c ~ 0,09% Fehler

// Top Wert
	TCC0.PER = 32031;
// Prescaler
	TCC0.CTRLA = TC_CLKSEL_DIV256_gc;

	mcu_enable_interrupt();

	while (1) {
		if (update) {
			clearRow(LCD_ROW1);
			fprintf(COMM_LCD, "%.2i:%.2i:%.2i", hour, min, s);

			//Ganz komischer Fehler mit clearRow(LCD_ROW2), irgendwie wird LCD_ROW0 zum teil überschrieben, deshalb so:
			st7036_goto(LCD_ROW2, 0);
			fprintf(COMM_LCD, "             ");
			st7036_goto(LCD_ROW2, 0);
			fprintf(COMM_LCD, "%i.%i.%i", day, month, year);
			update = false;
		}
	}
	return 0;
}

// Timer0 OVERFLOW INT
ISR(TCC0_OVF_vect) {
	ms += 25;
	if (ms >= 100) {
		ms = 0;
		s++;
		update = true;
	}

	if (s >= 60) {
		s = 0;
		min++;
	}

	if (min >= 60) {
		min = 0;
		hour++;
	}

	if (hour >= 24) {
		hour = 0;
		day++;
	}

	if (isSchalt(year) & (month == 2)) {
		if (day > 29) {
			day = 1;
			month++;
		}
	} else {
		if ((day > tageImMonat[month - 1])) {
			day = 1;
			month++;
		}
	}
	if (month >= 13) {
		month = 1;
		year++;
	}
}
