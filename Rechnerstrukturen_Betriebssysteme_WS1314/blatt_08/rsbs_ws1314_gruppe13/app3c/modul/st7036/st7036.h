/**
 * 	@file 	st7036.h
 * 	@author Tim Koczwara, Omar Bousbiba
 *  @date	21.06.2011, 15.12.2011
 *
 *  @brief
 *			Mit diesem Modul kann ein ST7036 Display angesteuert werden.
 *			Das ST7036 bietet verschiedene Möglichkeiten, zur Ansteuerung, von denen aktuell jedoch nur SPI benutzt wird.
 *			Um eine andere Möglichkeiten zu benutzen müssen die Funktionen @link st7036_wr_register st7036_wr_register@endlink
 *			und @link st7036_wr_letter st7036_wr_letter@endlink in der @link st7036.c st7036.c@endlink überschrieben werden, da diese
 *			Funktionen die direkte Schnittstelle zum Display sind. Entsprechend muss auch die Init Funktion angepasst werden, da bei dieser
 *			die IO Pins für SPI übergeben werden. Hier kann eine Struktur erstellt werden, die beispielsweise die IO Pins für SPI oder einen
 *			anderen Anschluss enthält und diese übergeben.
 *
 *	@see		[1] st7036.pdf Version 1.1 (www.lcd-module.de/eng/pdf/zubehoer/st7036.pdf)
 *
 *  @version  	1.00
 *  	- Erste Version
 *
 *  @todo 	Es können noch Funktionen erstellt werden um den Cursors zu aktivieren/deaktivieren und ebenso für das blinken, des aktuellen Zeichens.
 *
 ******************************************************************************/
 
#ifndef _ST7036_HEADER_FIRST__INCL__
#define _ST7036_HEADER_FIRST__INCL__

#include "../../mcu/mcu.h"

 // Konfigurationen
#define ST7036_MAX_COL					40 						///< Wird für die Speicherung der Display Daten als Maximale Größe verwendet
#define ST7036_MAX_ROW					3						///< Wird für die Speicherung der Display Daten als Maximale Größe verwendet
#define ST7036_INSTRUCTION_WAIT_US		27						///< Dauer der Wartezeit zwischen Befehlen in Mikrosekunden

// Variablen
/**
 * @enum ST7036_DISPLAY_TYPE
 * 	Enthält die Möglichen einstellbaren Displaygrößen für das Modul. Beim hinzufügen neuer Typen könnten weitere Funktionen, wie die Init
 * 	Funktion angepasst werden müssen. Das Format der Elemente ist ein 16 Bit Wert, der aus der Zeilenanzahl im höheren Byte und der
 * 	Spaltenzahl im niedrigeren Byte besteht.
 */
typedef enum {	
	ST7036_DISPLAY_1x8  = 0x0108,								///< Das Display hat eine Zeile mit 8 Zeichen
	ST7036_DISPLAY_1x20 = 0x0114,								///< Das Display hat eine Zeile mit 20 Zeichen
	ST7036_DISPLAY_2x40 = 0x0228,								///< Das Display hat zwei Zeilen mit je 40 Zeichen
	ST7036_DISPLAY_3x16 = 0x0310								///< Das Display hat drei Zeilen
} ST7036_DISPLAY_TYPE;		


volatile uint8_t temp_var_reg_function_set;

/**
 * @brief 		gespeicherter Kontrastwert
 */
volatile uint8_t contrast_value;
// Funktionen

/**
 * @brief 		Die Funktion initialisiert das ST7036 Display und die internen Variablen gemäß den übergebenen Parametern.
 * 				Es sollte darauf geachtet werden, dass die Definitionen ST7036_MAX_COL und ST7036_MAX_ROW für die übergebene Displaygröße
 * 				genügend groß sind.
 *
 * @param type				Der Typ gibt an, welche größe das Display hat. Mit Hilfe des Typs werden die Adressen der einzelnen Zeichen auf
 * 							dem Display berechnet.
 * @param cs				IO Pin für die Chip Select Leitung (SPI).
 * @param rs				IO Pin für den Register Select Pin.
 * @param num				Der vorgesehene SPI Port des Displays.
 * @param spi_speed			Die Geschwindigkeit, mit der das SPI laufen soll.
 */
void st7036_init(ST7036_DISPLAY_TYPE type, MCU_IO_PIN cs, MCU_IO_PIN rs, MCU_SPI_NUM num, uint32_t spi_speed);

/**
 * @brief 		Die Funktion schreibt ein einzelnes Zeichen auf das Display, wenn dieses noch nicht an der entsprechenden Stelle steht.
 * 				Die Prüfung, ob das Zeichen bereits auf dem Display steht verhindert lästiges flimmern, bei häufiger Aktualisierung der
 * 				Anzeige.
 *
 * @param letter			Das anzuzeigende Zeichen.
 */
void st7036_putc(char letter);

/**
 * @brief 		Springt mit dem Cursor an eine bestimmte Stelle auf dem Display.
 *
 * @param row				Die Zeile, an die gesprungen wird (Beginnt bei 0).
 * @param col				Die Spalte, an die gesprungen wird (Beginnt bei 0).
 */
void st7036_goto(uint8_t row, uint8_t col);

/**
 * @brief 		Springt mit dem Cursor an die Stelle 0/0 auf dem Display. Die Funktion entspricht st7036_goto(0,0).
 */
void st7036_home(void);

/**
 * @brief 		Füllt das komplette Display mit Leerzeichen und springt mit dem Cursor an Adresse 0.
 */
void st7036_clear(void);


/**
 * @brief 		Ändert den Kontrastwert des LC-Display. Der Kontrastwert liegt zwischen 0 und 0x0F.
 *				Übergaben von höherer/niedrigen Werte werden ignoriert.
 * @param value				Der übergebene Kontrastwert.
 */
void st7036_set_contrast(uint8_t value);
/**
 * @return uint8_t				Die Methode liefert den aktuellen Kontrastwert des LCDs.
 */
uint8_t st7036_get_contrast(void);
#endif
