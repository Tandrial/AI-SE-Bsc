/**
 * 	@file 	st7036.c
 **/

#include "st7036.h"
#include "st7036_register.h"
#include <string.h>

/************************************************
 * Interne Variablen
 ***********************************************/
static const uint8_t st7036_2x40_ddr_addr[2] = { 0x00, 0x40 }; ///< Enthält die Startadressen der Zeilen für ein 2x40 Display.
static const uint8_t st7036_3x16_ddr_addr[3] = { 0x00, 0x10, 0x20 }; ///< Enthält die Startadressen der Zeilen für ein 3x16 Display.

static MCU_IO_PIN st7036_chip_select, ///< Zwischenspeicher für den IO Pin der Chip Select Leitung.
		st7036_register_select;	///< Zwischenspeicher für den IO Pin der Register Select Leitung.
static MCU_SPI_NUM st7036_spi_num;///< Zwischenspeicher für den SPI Port des Display.
static uint8_t st7036_row_num, ///< Enthält die aktuell eingestellte Maximale Zeilenanzahl.
		st7036_col_num;	///< Enthält die aktuell eingestellte Maximale Spaltenzahl.
static ST7036_DISPLAY_TYPE st7036_type;	///< Zwischspeicher des eingestellten Displaytyps.

static uint8_t st7036_cur_pos_row = 0;			///< Aktuelle Zeile des Cursors.
static uint8_t st7036_cur_pos_col = 0;		///< Aktuelle Spalte des Cursors.
static uint8_t st7036_cur_shown[ST7036_MAX_ROW][ST7036_MAX_COL];///< Speichert, was aktuell auf dem Display steht, damit kein Zeichen doppelt auf das Display geschrieben wird um flimmern zu verhindern.

static bool st7036_cursor_active = false;///< Gibt an, ob der Cursor aktiv ist.
static bool st7036_blink_active = false;///< Gibt an, ob das aktuelle Zeichen blinkt.

/************************************************
 * Prototypen interner Funktionen
 ***********************************************/

/**
 * @brief 		Schreibt einen Wert in ein Register hinein.
 *
 * @param value				Die Register Addresse.
 * @param option			Die Option Bytes des Register. Siehe hierfür die @link st7036_register.h st7036_register.h@endlink.
 */
static void st7036_wr_register(uint8_t value, uint8_t option);

/**
 * @brief 		Schreibt ein Zeichen an die Stelle des Cursors.
 *
 * @param value				Das anzuzeigende Zeichen.
 */
static void st7036_wr_letter(char value);

/**
 * @brief		Springt an den Anfang der nächsten Zeile.
 */
static void st7036_next_line();

/**
 * @brief		Bewegt den Cursor um ein Zeichen nach Rechts.
 */
static void st7036_shift_right();

/************************************************
 * Externe Funktionen
 ***********************************************/
void st7036_init(ST7036_DISPLAY_TYPE type, MCU_IO_PIN cs, MCU_IO_PIN rs,
		MCU_SPI_NUM num, uint32_t spi_speed) {
	uint8_t var_reg_function_set = ST7036_OPT_FUNCTION_SET_8BIT_MODE;
	uint8_t var_reg_bias = ST7036_OPT_BIAS_DIV_5;

	st7036_type = type;
	st7036_chip_select = cs;
	st7036_register_select = rs;
	st7036_spi_num = num;

	// Spalten und Zeilen laden
	st7036_row_num = (type >> 8) & 0xFF;
	st7036_col_num = type & 0xFF;
	memset(st7036_cur_shown, 0x20, sizeof(st7036_cur_shown));// Leerzeichen ins Array schreiben, da diese beim Start gezeigt werden

	// IO Pins initialisieren
	mcu_io_set_dir(st7036_chip_select, MCU_IO_DIR_OUT);
	mcu_io_set(st7036_chip_select, 1);
	mcu_io_set_dir(st7036_register_select, MCU_IO_DIR_OUT);
	mcu_io_set(st7036_register_select, 0);

	// SPI initialisieren
	mcu_spi_init(st7036_spi_num, MCU_SPI_MODE_3, spi_speed);

	// Auf Display Typ prüfen
	switch (st7036_row_num) {
	case 3:
		var_reg_bias |= ST7036_OPT_BIAS_3_LINE;
	case 2:	// Das fehlende break ist absicht
		var_reg_function_set |= ST7036_OPT_FUNCTION_SET_2_LINE_MODE; // Für 2 und 3 Linien!
		break;
	}

	// ST7036 Initialisierungsroutine	
	st7036_wr_register(ST7036_REG_FUNCTION_SET,
			var_reg_function_set | ST7036_OPT_FUNCTION_SET_INST_SET_EXT_1);	// Displayabhängige Funktionen, die vorher gesetzt wurden
	st7036_wr_register(ST7036_REG_BIAS, var_reg_bias);// Hat Display Typ abhängige Funktionen, die gesetzt wurden
	st7036_wr_register(ST7036_REG_POWER_ICON_CONTROL,
	ST7036_OPT_POWER_ICON_BOOSTER_ON);
	//st7036_wr_register(ST7036_REG_FOLLOWER_CONTROL, ST7036_REG_FOLLOWER_CONTROL_INTERNAL_ON | 0x05);	// 0x05 für die Stromversorgung
	st7036_wr_register(ST7036_REG_FOLLOWER_CONTROL,
	ST7036_REG_FOLLOWER_CONTROL_INTERNAL_ON | 0x06);// 0x06 für die Stromversorgung
	st7036_wr_register(ST7036_REG_CONTRAST_SET, 0x0C);	// 0x0C für den Kontrast
	st7036_wr_register(ST7036_REG_FUNCTION_SET, var_reg_function_set);
	st7036_wr_register(ST7036_REG_DISPLAY_ONOFF,
	ST7036_OPT_DISPLAY_ONOFF_DISPLAY_ON);
	st7036_wr_register(ST7036_REG_CLEAR, 0);
	st7036_wr_register(ST7036_REG_ENTRY_MODE,
	ST7036_OPT_ENTRY_MODE_INCREMENT_ADRESS);
	st7036_clear();
	temp_var_reg_function_set = var_reg_function_set;
	contrast_value = 0x0C;
}

void st7036_set_contrast(uint8_t contrast_v) {
	if ((contrast_v <= 0x0F) && (contrast_v >= 0x00)) {
		st7036_wr_register(ST7036_REG_FUNCTION_SET,
				temp_var_reg_function_set
						| ST7036_OPT_FUNCTION_SET_INST_SET_EXT_1);
		st7036_wr_register(ST7036_REG_CONTRAST_SET, contrast_v);
		contrast_value = contrast_v;
	}
}

uint8_t st7036_get_contrast(void) {
	return contrast_value;
}

void st7036_putc(char letter) {
	switch (letter) {
	case '\r':
		st7036_goto(st7036_cur_pos_row, 0);
		return;
	case '\n':
		st7036_next_line();
		return;
	}
	if (st7036_cur_shown[st7036_cur_pos_row][st7036_cur_pos_col] != letter)	// Zeichen steht nicht auf dem Display
			{
		st7036_cur_shown[st7036_cur_pos_row][st7036_cur_pos_col] = letter;// neues Zeichen in Array schreiben
		st7036_goto(st7036_cur_pos_row, st7036_cur_pos_col);// Zur aktuellen Koordinate springen
		st7036_wr_letter(letter);					// Daten auf LCD ausgeben
		st7036_cur_pos_col++;	// Buchstabe weiterzaehlen
	} else // Zeichen steht bereits auf dem Display
	{
		if (st7036_cursor_active || st7036_blink_active) // Wenn Cursor aktiv ist oder Blinken Aktiv ist muss visuell das nächste Zeichen angezeigt werden
			st7036_shift_right();						// Also shiften
		else
			st7036_cur_pos_col++;	// Buchstabe weiterzaehlen
	}
	if (st7036_cur_pos_col == st7036_col_num)// Überprüfen ob Zeilenende erreicht wurde
		st7036_next_line();
}

void st7036_home(void) {
	st7036_goto(0, 0);	// Schneller als der "Return Home" Befehl...
}

void st7036_clear(void) {
	st7036_wr_register(ST7036_REG_CLEAR, 0);
	memset(st7036_cur_shown, 0x20, sizeof(st7036_cur_shown));// Leerzeichen ins Array schreiben, da jetzt nix mehr angezeigt wird
	mcu_wait_ms(1);
	st7036_goto(0, 0);	// Schneller als der "Return Home" Befehl...
}

void st7036_goto(uint8_t row, uint8_t col) {
	uint8_t jump_addr = 0x00;
	if (row >= st7036_row_num)
		return;	// Verhindern, dass Zeichen in Zeile angesprungen werden die es nicht gibt
	if (col >= st7036_col_num)
		return;	// Verhindern, dass zuviele Zeichen in der Zeile eingetragen werden.
	st7036_cur_pos_col = col;
	st7036_cur_pos_row = row;
	jump_addr = st7036_cur_pos_col;
	switch (st7036_type) {
	case ST7036_DISPLAY_1x8:
		break;
	case ST7036_DISPLAY_1x20:
		break;
	case ST7036_DISPLAY_2x40:
		jump_addr += st7036_2x40_ddr_addr[st7036_cur_pos_row];
		break;
	case ST7036_DISPLAY_3x16:
		jump_addr += st7036_3x16_ddr_addr[st7036_cur_pos_row];
		break;
	}
	st7036_wr_register(ST7036_REG_SET_DDRAM, (jump_addr & 0x7F));
}

/************************************************
 * Interne Funktionen
 ***********************************************/
void st7036_shift_right() {
	if (st7036_cur_pos_col >= (st7036_col_num - 1))	// Zeile Ende
		st7036_next_line();	// Nächste Zeile
	else
		st7036_goto(st7036_cur_pos_row, st7036_cur_pos_col + 1);// Nächstes Zeichen in Zeile
}

void st7036_next_line() {
	st7036_goto(
			(st7036_cur_pos_row >= (st7036_row_num - 1)) ?
					0 : (st7036_cur_pos_row + 1), 0); // Wenn Display in letzter Zeile war wieder zur ersten springen, sonst zur nächsten
}

void st7036_wr_register(uint8_t value, uint8_t option) {
	mcu_io_set(st7036_register_select, 0);
	mcu_spi_send(st7036_spi_num, value | option, st7036_chip_select);
	mcu_disable_interrupt();
	mcu_wait_us(ST7036_INSTRUCTION_WAIT_US);
	mcu_enable_interrupt();
}

void st7036_wr_letter(char value) {
	mcu_io_set(st7036_register_select, 1);
	mcu_spi_send(st7036_spi_num, value, st7036_chip_select);
	mcu_disable_interrupt();
	mcu_wait_us(ST7036_INSTRUCTION_WAIT_US);
	mcu_enable_interrupt();
}
