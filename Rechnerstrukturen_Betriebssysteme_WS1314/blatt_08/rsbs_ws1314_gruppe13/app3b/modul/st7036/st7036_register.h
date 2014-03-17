/**
 * 	@file 	st7036_register.h
 * 	@author Tim Koczwara
 *  @date	26.08.2011
 *
 *  @brief
 *			Dieser Header enthält die Definitionen der einzelnen Register, die das st7036 LCD bietet.
 *	@section st7036_reg_head	Bezeichnungen
 *			ST7036_REG_ steht für die einzelnen Register, die mit der Funktion @link st7036_wr_register st7036_wr_register@endlink
 *			als Parameter Value übergeben werden.\n
 *			ST7036_OPT_ steht hingegen für mögliche Werte, die in den Registern gesetzt werden. Diese werden in der Funktion als Parameter
 *			option gesetzt. Mehrere Parameter können mit | kombiniert werden. Dabei ist jedoch darauf zu achten, dass einige Optionen im
 *			Gegensatz zueinander stehen und somit nicht kombiniert werden können (z.B. ST7036_OPT_ENTRY_MODE_INCREMENT_ADRESS |
 *			ST7036_OPT_ENTRY_MODE_DECREMENT_ADRESS ist nicht möglich!).\n
 *
 *			Die Namen der Optionen bestehen aus dem Kürzel ST7036_OPT_, dem Registernamen und dem Namen der Funktionalität:
@code
#define ST7036_REG_ENTRY_MODE						0x04		// Register "Entry Mode"
	#define ST7036_OPT_ENTRY_MODE_INCREMENT_ADRESS		0x02	// Option "Increment Adress" im Register "Entry Mode"
@endcode
 *
 *	@see		[1] st7036.pdf Version 1.1 (www.lcd-module.de/eng/pdf/zubehoer/st7036.pdf)
 *
 *  @version  	1.00
 *  	- Erste Version
 *
 ******************************************************************************/
#ifndef _ST7036_REGISTER_HEADER_FIRST_INCLUDE_GUARD
#define _ST7036_REGISTER_HEADER_FIRST_INCLUDE_GUARD

/************************************************
 * Register Definitionen
 ***********************************************/
// Allgemeine Register
#define ST7036_REG_CLEAR							0x01		///< Beim Aufruf dieses Registers wird die Anzeige auf dem Display gelöscht.
#define ST7036_REG_HOME								0x02		///< Der Cursor springt an Adresse 0 zurück. (Benötigt jedoch länger als manueller Sprung!)
#define ST7036_REG_ENTRY_MODE						0x04	///< Entry Mode Register
	#define ST7036_OPT_ENTRY_MODE_INCREMENT_ADRESS		0x02	///< Display zählt Adressen der Zeichen fortlaufend hoch
	#define ST7036_OPT_ENTRY_MODE_DECREMENT_ADRESS		0x00	///< Display zählt Adressen der Zeichen fortlaufend runter
	#define ST7036_OPT_ENTRY_MODE_SHIFT_LEFT			0x03	///< Nur bei Inkrement -> Display Shift Register nach Links
	#define ST7036_OPT_ENTRY_MODE_SHIFT_RIGHT			0x02	///< Nur bei Inkrement -> Display Shift Register nach Rechts
#define ST7036_REG_DISPLAY_ONOFF					0x08	///< Display On/Off Register
	#define ST7036_OPT_DISPLAY_ONOFF_DISPLAY_ON			0x04	///< Display an
	#define ST7036_OPT_DISPLAY_ONOFF_DISPLAY_OFF		0x00	///< Display aus
	#define ST7036_OPT_DISPLAY_ONOFF_CURSOR_ON			0x02	///< Cursor an
	#define ST7036_OPT_DISPLAY_ONOFF_CURSOR_OFF			0x00	///< Cursor aus
	#define ST7036_OPT_DISPLAY_ONOFF_BLINK_ON			0x01	///< Blinken an
	#define ST7036_OPT_DISPLAY_ONOFF_BLINK_OFF			0x00	///< Blinken aus
#define ST7036_REG_FUNCTION_SET						0x20	///< Function Set Register
	#define ST7036_OPT_FUNCTION_SET_8BIT_MODE			0x10	///< 8 Bit Bus Mode
	#define ST7036_OPT_FUNCTION_SET_4BIT_MODE			0x00	///< 4 Bit Bus Mode
	#define ST7036_OPT_FUNCTION_SET_3_LINE_MODE			0x08	///< 3 Zeilen Mode (Hierfür muss Pin N3 auf VDD liegen!)
	#define ST7036_OPT_FUNCTION_SET_2_LINE_MODE			0x08	///< 2 Zeilen Mode (Hierfür muss Pin N3 auf VSS liegen!)
	#define ST7036_OPT_FUNCTION_SET_1_LINE_MODE			0x00	///< 1 Zeilen Mode
	#define ST7036_OPT_FUNCTION_SET_FONT_5X16			0x04	///< Font 5x16 groß, geht nur wenn N3 auf VSS und ein Linien Mode!
	#define ST7036_OPT_FUNCTION_SET_FONT_5X8			0x00	///< Font 5x8 (Standardmäßige Buchstabengröße)
	#define ST7036_OPT_FUNCTION_SET_INST_SET_NORMAL		0x00	///< Normale Instruktionen
	#define ST7036_OPT_FUNCTION_SET_INST_SET_EXT_1		0x01	///< Erweiterte Instuktionen 1
	#define ST7036_OPT_FUNCTION_SET_INST_SET_EXT_2		0x02	///< Erweiterte Instuktionen 2
#define ST7036_REG_SET_DDRAM						0x80	///< Schreibt ein Zeichen auf das Display

// Register, die es nur im normalen Modus gibt
#define ST7036_REG_SHIFT							0x10	///< Shift Register
	#define ST7036_OPT_SHIFT_LEFT						0x00	///< Display nach Links Shiften
	#define ST7036_OPT_SHIFT_RIGHT						0x04	///< Display nach Rechts shiften
	#define ST7036_OPT_SHIFT_LEFT_FOLLOW				0x08	///< Display nach Links Shiften (Cursor folgt dem Shift)
	#define ST7036_OPT_SHIFT_RIGHT_FOLLOW				0x0C	///< Display nach Rechts shiften (Cursor folgt dem Shift)
#define ST7036_REG_SET_CGRAM						0x40	///< In das CGRAM Register können eigene Buchstaben/Zeichen geschrieben werden.

// Register im Erweiterten Modus 1
#define ST7036_REG_BIAS								0x14	///< Bias Register
	#define ST7036_OPT_BIAS_DIV_4						0x80	///< Bias ist 1/4
	#define ST7036_OPT_BIAS_DIV_5						0x00	///< Bias ist 1/5
	#define ST7036_OPT_BIAS_3_LINE						0x01	///< Muss nur im 3 Line Mode gesetzt werden!
#define ST7036_REG_SET_ICON_ADDRESS					0x40	///< Adressen von 0x00 - 0x0F
#define ST7036_REG_POWER_ICON_CONTROL				0x50	///< Power Icon Control Register (Bits [1:0] sind für den Kontrast)
	#define ST7036_OPT_POWER_ICON_DISPLAY_ON			0x08	///< ICON Display an
	#define ST7036_OPT_POWER_ICON_DISPLAY_OFF			0x00	///< ICON Display aus
	#define ST7036_OPT_POWER_ICON_BOOSTER_ON			0x04	///< Booster an	(Kann nur gesetzt werden wenn Follower OPF1 = OPF2 = 0)
	#define ST7036_OPT_POWER_ICON_BOOSTER_OFF			0x00	///< Booster aus
#define ST7036_REG_FOLLOWER_CONTROL					0x60	///< Follower Control Register (Bits [2:0] sind für die Versorgungsspannung)
	#define ST7036_REG_FOLLOWER_CONTROL_INTERNAL_ON		0x08	///< Internal Follower Circuit an (Nur wenn OPF1 = OPF2 = 0)
	#define ST7036_REG_FOLLOWER_CONTROL_INTERNAL_OFF	0x00	///< Internal Follower Circuit aus (Nur wenn OPF1 = OPF2 = 0)
#define ST7036_REG_CONTRAST_SET						0x70	///< Contrast Set Register (Bits [3:0] stellen den Kontrast ein, zusammen mit dem Power Icon Control Register)

// Register im Erweiterten Modus 2
#define ST7036_REG_DOUBLE_HEIGHT_POSITION_SELECT	0x10	///< Nur Relevant wenn im Function Set die 5x16 Font gewählt wurde. Einstellungen siehe [1] Seite 32

#endif
