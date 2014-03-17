/**
 * 	@file 		mcu_atxmega128a1.c
 **/

#include "../mcu.h"
#include "../../modul/fifo/fifo.h"

#include <avr/io.h>
#include <avr/interrupt.h>
#include <util/delay.h>

/**********************
 * Interne Enumerations
 *********************/
/**
 * @enum MCU_FRQ_SYS_SOURCE
 *		Aufzählung der möglichen Quellen der System Clock.
 **/
typedef enum {
	MCU_FRQ_SYS_SRC_EXTERNAL_OSCILLATOR = 0, /**< Externer Quarz */
	MCU_FRQ_SYS_SRC_INTERNAL_2MHZ, /**< 2.000.000 Hz */
	MCU_FRQ_SYS_SRC_INTERNAL_32MHZ, /**< 32.000.000 Hz - Interne 32MHz durch 1 geteilt */
	MCU_FRQ_SYS_SRC_PLL /**< PLL */
} MCU_FRQ_SYS_SOURCE;

/**
 * @enum MCU_FRQ_PLL_SOURCE
 *		Aufzählung der möglichen Quellen der PLL Clock.
 **/
typedef enum {
	MCU_FRQ_PLL_SRC_EXTERNAL_OSCILLATOR = 0, /**< Externer Quarz */
	MCU_FRQ_PLL_SRC_INTERNAL_2MHZ, /**< 2.000.000 Hz */
	MCU_FRQ_PLL_SRC_INTERNAL_32MHZ_DIV_4, /**< 8.000.000 Hz - Interne 32MHz durch 4 geteilt */
	MCU_FRQ_PLL_SRC_NONE /**< Nur auszuwählen wenn MCU_FRQ_SYS_SOURCE nicht auf MCU_FRQ_SYS_SRC_PLL steht! */
} MCU_FRQ_PLL_SOURCE;

/**********************
 * Interne Variablen
 *********************/
static uint32_t mcu_frq_ext_hz = 0; /**< Enthält die externe Frequenz in Hertz. Für interne und externe Berechnungen.*/
static uint32_t mcu_frq_pll_hz = 0; /**< Enthält die PLL Frequenz in Hertz. Für interne Berechnungen. */
static uint32_t mcu_frq_cpu_hz = 0; /**< Enthält die CPU Frequenz in Hertz. Für interne und externe Berechnungen.*/
static uint32_t mcu_frq_sys_hz = 0; /**< Enthält die System Frequenz in Hertz. Für interne Berechnungen.*/
static uint32_t mcu_frq_peripheral_hz = 0; /**< Enthält die Peripherie Frequenz in Hertz. Für interne und externe Berechnungen*/

static uint8_t mcu_external_pin = 0; /**< Eine Dummy Variable die einen IO Pin simuliert. Kann mit PIN_EXTERNAL gesetzt und gelesen werden. */

/**********************
 * Interne Prototypen
 *********************/
/**
 * @brief	Initialisiert die MCU mit den übergebenen Clock Sourcen. Im Fehlerfall wird abgebrochen und eine Fehlermeldung zurückgegeben.
 * @param frq_ext			Externe Frequenz, oder beliebiger Wert, wenn keine externe Frequenz anliegt. Controller unterstützt 4kHz bis 16 MHz.
 * @param sys_src			Quelle der Systemclock anhand der Aufzählung @link MCU_FRQ_SYS_SOURCE MCU_FRQ_SYS_SOURCE@endlink.
 * @param pll_src			Quelle der PLL Clock anhand der Aufzählung @link MCU_FRQ_PLL_SOURCE MCU_FRQ_PLL_SOURCE@endlink. Wenn keine PLL
 * 								benutzt wird ist der Wert beliebig.
 * @param pll_multiplicator	PLL Multiplikator, mit dem die PLL Frequenz erhöht wird. Möglicher Multiplikator ist 1-31 oder beliebig,
 * 								wenn keine PLL benutzt wird.
 * @return					Gibt MCU_OK oder entsprechenden MCU_RESULT Error Code zurück. Siehe hierzu die Fehlercodes von mcu_init in der mcu.h.
 */
static MCU_RESULT mcu_atxega128a1_init(uint32_t frq_ext,
		MCU_FRQ_SYS_SOURCE sys_src, MCU_FRQ_PLL_SOURCE pll_src,
		uint8_t pll_multiplicator);

/**
 * @brief	Interne Funktion, die für die io_set und io_get Funktionen benutzt wird.
 * @param p					IO Pin
 * @return					Gibt ein Byte mit einem gesetzten Bit, entsprechend des Bits des Ports zurück. Beispiel: PC_3 gibt 0x08 zurück.
 */
static uint8_t mcu_io_get_pin_bin(MCU_IO_PIN p);

/**
 * @brief	Interne Funktion, die das Port Register des übergebenen IO Pins zurückgibt
 * @param p					IO Pin
 * @return					Port Register oder NULL
 */
static PORT_t* mcu_get_port_reg(MCU_IO_PIN p);

/**
 * @brief	Interne Funktion, die das Port PinNctrl Register des Angegebenen IO Pins setzt.
 * @param p					IO Pin
 * @param is_set			true: Wert setzen.\n
 * 							false: Wert löschen.
 * @param value				Wert, der in das Register geschrieben wird.
 */
static void mcu_set_port_pinnctrl(MCU_IO_PIN p, bool is_set, uint8_t value);

#if MCU_PERIPHERY_ENABLE_IO_INTERRUPT
/**
 * @brief	Interne Funktion, die das IntCtrl Register setzt
 * @param num				IO Interrupt Nummer
 * @param is_set			true: Wert setzen.\n
 * 							false: Wert löschen.
 * @param val				Wert, der in das Register geschrieben wird.
 */
static void mcu_io_int_set_intctrl(MCU_IO_INT_NUM num, bool is_set, uint8_t val);
#endif

#if MCU_PERIPHERY_ENABLE_TIMER
/**
 * @brief		Setzt das Timer Register auf entsprechenden Wert
 * @param num				Timer Nummer
 * @param is_on				true: Timer starten.\n
 * 							false: Timer stoppen.
 */
static void mcu_timer_set_start(MCU_TIMER_NUM num, bool is_on);
#endif

#if MCU_PERIPHERY_ENABLE_UART
/** @brief
 *  Interne Funktion: Liest Byte aus entsprechender UART und schreibt dieses in den Buffer oder übergibt es an die alternative Funktion.
 *
 *  @param num     			Die UART Nummer
 */
static void mcu_uart_rcv_interrupt(MCU_UART_NUM num);
#endif

#if MCU_PERIPHERY_ENABLE_AD
/**
 * @brief	Interne Funktion: Prüft nach ob die Complete Flag gesetzt wurde und löscht diese.
 * @param num				AD Kanal
 * @return					true: 	AD Wandlung fertig.\n
 * 							false: 	AD Wandlung nicht fertig.
 */
static bool mcu_ad_check_complete_flag(MCU_AD_CHANNEL num);

/**
 * @brief	Interne Funktion: Gibt den Wert aus dem AD Register zurück.
 * @param num				AD Kanal
 * @return					AD Ergebnis oder 0 bei Fehler.
 */
static uint16_t mcu_ad_get_res(MCU_AD_CHANNEL num);
#endif

/**********************
 * Funktionen
 *********************/
uint32_t mcu_get_frq_external(void) {
	return mcu_frq_ext_hz;
}
uint32_t mcu_get_frq_cpu(void) {
	return mcu_frq_cpu_hz;
}
uint32_t mcu_get_frq_peripheral(void) {
	return mcu_frq_peripheral_hz;
}

MCU_RESULT mcu_init(uint32_t frq_ext, uint32_t frq_cpu, uint32_t frq_peripheral) {
	if (frq_cpu != frq_peripheral)
		return MCU_ERROR_FRQ_PERIPHERAL_INVALID; /** @todo Peripherie Clock /2 bzw /4 berücksichtigen */

	switch (frq_cpu) /** @todo Andere Frequenzen als 200, 32 , 8 und 2 MHz berechnen! */
	{
	case 2000000:	// 2 MHz
		return mcu_atxega128a1_init(frq_ext, MCU_FRQ_SYS_SRC_INTERNAL_2MHZ,
				MCU_FRQ_PLL_SRC_NONE, 0);

	case 8000000:	// 8 MHz
		return mcu_atxega128a1_init(frq_ext, MCU_FRQ_SYS_SRC_PLL,
				MCU_FRQ_PLL_SRC_INTERNAL_32MHZ_DIV_4, 1);

	case 32000000:	// 32 MHz
		return mcu_atxega128a1_init(frq_ext, MCU_FRQ_SYS_SRC_INTERNAL_32MHZ,
				MCU_FRQ_PLL_SRC_NONE, 1);

	case 200000000:	// 200 MHz
		return mcu_atxega128a1_init(frq_ext, MCU_FRQ_SYS_SRC_PLL,
				MCU_FRQ_PLL_SRC_INTERNAL_32MHZ_DIV_4, 25);// Vorsicht: Prozessor wird heiß!
	}
	return MCU_ERROR_FRQ_MCU_INVALID;
}

MCU_RESULT mcu_init_max_internal() {
	/// @todo	Maximal möglich wären 200 MHz, jedoch wird dann der Prozessor heiß. Deswegen ist es aktuell auf 32 MHz eingestellt.
	return mcu_atxega128a1_init(0, MCU_FRQ_SYS_SRC_INTERNAL_32MHZ,
			MCU_FRQ_PLL_SRC_NONE, 1);
}

MCU_RESULT mcu_init_max_external(uint32_t frq_ext) {
	/// @todo	Maximal wäre 200 MHz möglich, jedoch wire bei mcu_init_max_internal erwähnt auf 32 MHz reduziert.
	if (frq_ext == 0)
		return MCU_ERROR_FRQ_EXT_INVALID;	// Division durch 0 verhindern!
	return mcu_atxega128a1_init(frq_ext, MCU_FRQ_SYS_SRC_PLL,
			MCU_FRQ_PLL_SRC_EXTERNAL_OSCILLATOR, (320000000 / frq_ext));// Maximale Frequenz 200 MHz
}

static MCU_RESULT mcu_atxega128a1_init(uint32_t frq_ext,
		MCU_FRQ_SYS_SOURCE sys_src, MCU_FRQ_PLL_SOURCE pll_src,
		uint8_t pll_multiplicator) {
	uint8_t pll_src_reg = 0;
	uint32_t pll_src_frq = 0;
	uint8_t clk_src = 0;
	switch (sys_src) {
	case MCU_FRQ_SYS_SRC_EXTERNAL_OSCILLATOR:
		if (frq_ext > 16000000)
			return MCU_ERROR_FRQ_EXT_INVALID;// Der ATXMega 128 A1 verträgt maximal 16 MHz!
		if (frq_ext < 400000)
			return MCU_ERROR_FRQ_EXT_INVALID;// Es werden mindestens 400 kHz benötigt!

		if (frq_ext >= 12000000)
			OSC.XOSCCTRL = 0xC3;	// Range 12-16 und CTAL_256CLK
		else if (frq_ext >= 9000000)
			OSC.XOSCCTRL = 0x83;	// Range 9-12 und CTAL_256CLK
		else if (frq_ext >= 2000000)
			OSC.XOSCCTRL = 0x43;	// Range 2-9 und CTAL_256CLK
		else
			OSC.XOSCCTRL = 0x03;	// Range 0.4-2 und CTAL_256CLK

		OSC.CTRL |= 0x08;								// Enable Bit setzen
		while (!(OSC.STATUS & 0x08))
			;						// Warten bis Clock Stabil ist
		mcu_frq_sys_hz = frq_ext;
		clk_src = CLK_SCLKSEL_XOSC_gc;
		break;

	case MCU_FRQ_SYS_SRC_INTERNAL_2MHZ:
		OSC.CTRL |= 0x01;								// Enable Bit setzen
		while (!(OSC.STATUS & 0x01))
			;						// Warten bis Clock Stabil ist
		mcu_frq_sys_hz = 2000000;
		clk_src = CLK_SCLKSEL_RC2M_gc;
		break;

	case MCU_FRQ_SYS_SRC_INTERNAL_32MHZ:
		OSC.CTRL |= 0x02;								// Enable Bit setzen
		while (!(OSC.STATUS & 0x02))
			;						// Warten bis Clock Stabil ist
		mcu_frq_sys_hz = 32000000;
		clk_src = CLK_SCLKSEL_RC32M_gc;
		break;

	case MCU_FRQ_SYS_SRC_PLL:
		if (pll_multiplicator == 0 || pll_multiplicator > 31)
			return MCU_ERROR_FRQ_MCU_INVALID;	// Multiplikator muss 1-31 sein
		OSC.CTRL &= ~0x01;	// PLL deaktivieren!
		switch (pll_src) {
		case MCU_FRQ_PLL_SRC_EXTERNAL_OSCILLATOR:
			if (frq_ext < 400000 || frq_ext > 16000000)
				return MCU_ERROR_FRQ_EXT_INVALID;// Externer Quarz außerhalb der Spezifikation!
			// TODO: Bei Anwendung möglicherweise dieselben Register setzen, wie oben bei der externen Clock?
			pll_src_reg = 0xC0;
			pll_src_frq = frq_ext;
			break;

		case MCU_FRQ_PLL_SRC_INTERNAL_2MHZ:
			OSC.CTRL |= 0x01;								// Enable Bit setzen
			while (!(OSC.STATUS & 0x01))
				;						// Warten bis Clock Stabil ist
			pll_src_reg = 0x00;
			pll_src_frq = 2000000;
			break;

		case MCU_FRQ_PLL_SRC_INTERNAL_32MHZ_DIV_4:
			OSC.CTRL |= 0x02;								// Enable Bit setzen
			while (!(OSC.STATUS & 0x02))
				;						// Warten bis Clock Stabil ist
			pll_src_reg = 0x80;
			pll_src_frq = 8000000;
			break;

		case MCU_FRQ_PLL_SRC_NONE:
			return MCU_ERROR_FRQ_MCU_INVALID;// Wenn die Quelle der System Clock die PLL ist, muss diese auch eine Quelle haben

		default:
			return MCU_ERROR_FRQ_MCU_INVALID;	// S.o. PLL muss Quelle haben
		}
		mcu_frq_pll_hz = pll_src_frq * pll_multiplicator;
		if (mcu_frq_pll_hz < 10000000)
			return MCU_ERROR_FRQ_MCU_INVALID;// PLL Clock muss mindestens 10 MHz sein!
		if (mcu_frq_pll_hz > 200000000)
			return MCU_ERROR_FRQ_MCU_INVALID;// PLL Clock darf maximal 200 MHz sein!
		// PLL setzen nach [3] Seite 6
		OSC.PLLCTRL = pll_src_reg | pll_multiplicator;// PLL Source und Multiplikator setzen
		OSC.CTRL |= 0x10;								// PLL Enable Bit setzen
		while (!(OSC.STATUS & 0x10))
			;
		mcu_frq_sys_hz = mcu_frq_pll_hz;
		clk_src = CLK_SCLKSEL_PLL_gc;
		break;
	default:
		return MCU_ERROR_FRQ_MCU_INVALID;	// Ungültige System Clock Source
	}

	CCP = CCP_IOREG_gc;	// Protection entfernen um Clock zu setzen
	CLK.CTRL = clk_src;
	CLK.PSCTRL = 0x00;// No Division -> In Zukunft vielleicht auch einstellbar, wenn nötig

	mcu_frq_cpu_hz = mcu_frq_sys_hz;
	mcu_frq_peripheral_hz = mcu_frq_sys_hz;

	PMIC.CTRL |= 0x07;  //High-, Medium und Low-Level Interrupts freigeben

	return MCU_OK;
}

void mcu_enable_interrupt(void) {
	sei();
}

void mcu_disable_interrupt(void) {
	cli();
}

void mcu_soft_reset(void) {
	CCP = CCP_IOREG_gc;
	RST.CTRL = 1;
}

void mcu_io_set_port_dir(MCU_IO_PIN p, uint8_t d) {
	PORT_t * p_reg = mcu_get_port_reg(p);
	if (p_reg != NULL)
		p_reg->DIR = d;
}

void mcu_io_set_dir(MCU_IO_PIN p, MCU_IO_DIRECTION d) {
	PORT_t * p_reg = mcu_get_port_reg(p);
	if (p_reg != NULL) {
		if (d == MCU_IO_DIR_OUT)
			p_reg->DIRSET = mcu_io_get_pin_bin(p);
		else
			p_reg->DIRCLR = mcu_io_get_pin_bin(p);
	}
}

void mcu_io_set_pullup(MCU_IO_PIN p, bool pullup_active) {
	mcu_set_port_pinnctrl(p, pullup_active, 0x38);
}

void mcu_io_set(MCU_IO_PIN p, uint8_t d) {
	PORT_t * p_reg = mcu_get_port_reg(p);
	uint8_t pin_bin = mcu_io_get_pin_bin(p);
	if (p_reg != NULL) {
		if (!pin_bin)
			p_reg->OUT = d;
		else {
			if (d)
				p_reg->OUTSET = pin_bin;
			else
				p_reg->OUTCLR = pin_bin;
		}
	} else {
		if (p == PIN_EXTERNAL)
			mcu_external_pin = d;
	}
}

uint8_t mcu_io_get(MCU_IO_PIN p) {
	uint8_t p_num = p & 0x0F;
	PORT_t * p_reg = mcu_get_port_reg(p);
	if (p_num > 7 && p_num < 0x0F)// Nur gültige Port Pins, oder gesamten Port zulassen
		return 0;
	if (p_reg != NULL) {
		if (!mcu_io_get_pin_bin(p))
			return p_reg->IN;
		else
			return (p_reg->IN >> p_num) & 1;
	} else {
		if (p == PIN_EXTERNAL)
			return mcu_external_pin;
		else
			return 0;
	}
}

static uint8_t mcu_io_get_pin_bin(MCU_IO_PIN p) {
	switch (p & 0x0F) {
	case 0:
		return 0x01;
	case 1:
		return 0x02;
	case 2:
		return 0x04;
	case 3:
		return 0x08;
	case 4:
		return 0x10;
	case 5:
		return 0x20;
	case 6:
		return 0x40;
	case 7:
		return 0x80;
	default:
		return 0x00;
	}
}

static PORT_t* mcu_get_port_reg(MCU_IO_PIN p) {
	switch (p & 0xF0) {
	case PA_0:
		return &PORTA;
	case PB_0:
		return &PORTB;
	case PC_0:
		return &PORTC;
	case PD_0:
		return &PORTD;
	case PE_0:
		return &PORTE;
	case PF_0:
		return &PORTF;
	case PH_0:
		return &PORTH;
	case PJ_0:
		return &PORTJ;
	case PK_0:
		return &PORTK;
	case PQ_0:
		return &PORTQ;
	default:
		return NULL;
	}
}

static void mcu_set_port_pinnctrl(MCU_IO_PIN p, bool is_set, uint8_t value) {
	PORT_t * p_reg = mcu_get_port_reg(p);
	if (p_reg != NULL) {
		switch (p & 0x0F) {
		case 0:
			if (is_set)
				p_reg->PIN0CTRL |= value;
			else
				p_reg->PIN0CTRL &= (~value);
			break;
		case 1:
			if (is_set)
				p_reg->PIN1CTRL |= value;
			else
				p_reg->PIN1CTRL &= (~value);
			break;
		case 2:
			if (is_set)
				p_reg->PIN2CTRL |= value;
			else
				p_reg->PIN2CTRL &= (~value);
			break;
		case 3:
			if (is_set)
				p_reg->PIN3CTRL |= value;
			else
				p_reg->PIN3CTRL &= (~value);
			break;
		case 4:
			if (is_set)
				p_reg->PIN4CTRL |= value;
			else
				p_reg->PIN4CTRL &= (~value);
			break;
		case 5:
			if (is_set)
				p_reg->PIN5CTRL |= value;
			else
				p_reg->PIN5CTRL &= (~value);
			break;
		case 6:
			if (is_set)
				p_reg->PIN6CTRL |= value;
			else
				p_reg->PIN6CTRL &= (~value);
			break;
		case 7:
			if (is_set)
				p_reg->PIN7CTRL |= value;
			else
				p_reg->PIN7CTRL &= (~value);
			break;
		}
	}
}

#if MCU_PERIPHERY_ENABLE_IO_INTERRUPT

static void *mcu_int_io_callback[MCU_IO_INT_NUM_MAX]; /**< Enthält die Callback Funktionen der Interrupts */

ISR (PORTA_INT0_vect) {
	PORTA_INTFLAGS |= 0x01;
	if (mcu_int_io_callback[0])
		((void (*)(void)) mcu_int_io_callback[0])();
} /**< IO Interrupt A0 ausführen und Callback aufrufen */
ISR (PORTA_INT1_vect) {
	PORTA_INTFLAGS |= 0x02;
	if (mcu_int_io_callback[1])
		((void (*)(void)) mcu_int_io_callback[1])();
} /**< IO Interrupt A1 ausführen und Callback aufrufen */
ISR (PORTB_INT0_vect) {
	PORTB_INTFLAGS |= 0x01;
	if (mcu_int_io_callback[2])
		((void (*)(void)) mcu_int_io_callback[2])();
} /**< IO Interrupt B0 ausführen und Callback aufrufen */
ISR (PORTB_INT1_vect) {
	PORTB_INTFLAGS |= 0x02;
	if (mcu_int_io_callback[3])
		((void (*)(void)) mcu_int_io_callback[3])();
} /**< IO Interrupt B1 ausführen und Callback aufrufen */
ISR (PORTC_INT0_vect) {
	PORTC_INTFLAGS |= 0x01;
	if (mcu_int_io_callback[4])
		((void (*)(void)) mcu_int_io_callback[4])();
} /**< IO Interrupt C0 ausführen und Callback aufrufen */
ISR (PORTC_INT1_vect) {
	PORTC_INTFLAGS |= 0x02;
	if (mcu_int_io_callback[5])
		((void (*)(void)) mcu_int_io_callback[5])();
} /**< IO Interrupt C1 ausführen und Callback aufrufen */
//war auskommentiert
//ISR (PORTD_INT0_vect)  {	PORTD_INTFLAGS |= 0x01;		if(mcu_int_io_callback[6])	((void(*)(void))mcu_int_io_callback[6])();		}	/**< IO Interrupt D0 ausführen und Callback aufrufen */
//ISR (PORTD_INT1_vect)  {	PORTD_INTFLAGS |= 0x02;		if(mcu_int_io_callback[7])	((void(*)(void))mcu_int_io_callback[7])();		}	/**< IO Interrupt D1 ausführen und Callback aufrufen */
ISR (PORTE_INT0_vect) {
	PORTE_INTFLAGS |= 0x01;
	if (mcu_int_io_callback[8])
		((void (*)(void)) mcu_int_io_callback[8])();
} /**< IO Interrupt E0 ausführen und Callback aufrufen */
ISR (PORTE_INT1_vect) {
	PORTE_INTFLAGS |= 0x02;
	if (mcu_int_io_callback[9])
		((void (*)(void)) mcu_int_io_callback[9])();
} /**< IO Interrupt E1 ausführen und Callback aufrufen */
ISR (PORTF_INT0_vect) {
	PORTF_INTFLAGS |= 0x01;
	if (mcu_int_io_callback[10])
		((void (*)(void)) mcu_int_io_callback[10])();
} /**< IO Interrupt F0 ausführen und Callback aufrufen */
ISR (PORTF_INT1_vect) {
	PORTF_INTFLAGS |= 0x02;
	if (mcu_int_io_callback[11])
		((void (*)(void)) mcu_int_io_callback[11])();
} /**< IO Interrupt F1 ausführen und Callback aufrufen */
ISR (PORTH_INT0_vect) {
	PORTH_INTFLAGS |= 0x01;
	if (mcu_int_io_callback[12])
		((void (*)(void)) mcu_int_io_callback[12])();
} /**< IO Interrupt H0 ausführen und Callback aufrufen */
ISR (PORTH_INT1_vect) {
	PORTH_INTFLAGS |= 0x02;
	if (mcu_int_io_callback[13])
		((void (*)(void)) mcu_int_io_callback[13])();
} /**< IO Interrupt H1 ausführen und Callback aufrufen */
ISR (PORTJ_INT0_vect) {
	PORTJ_INTFLAGS |= 0x01;
	if (mcu_int_io_callback[14])
		((void (*)(void)) mcu_int_io_callback[14])();
} /**< IO Interrupt J0 ausführen und Callback aufrufen */
ISR (PORTJ_INT1_vect) {
	PORTJ_INTFLAGS |= 0x02;
	if (mcu_int_io_callback[15])
		((void (*)(void)) mcu_int_io_callback[15])();
} /**< IO Interrupt J1 ausführen und Callback aufrufen */
ISR (PORTK_INT0_vect) {
	PORTK_INTFLAGS |= 0x01;
	if (mcu_int_io_callback[16])
		((void (*)(void)) mcu_int_io_callback[16])();
} /**< IO Interrupt K0 ausführen und Callback aufrufen */
ISR (PORTK_INT1_vect) {
	PORTK_INTFLAGS |= 0x02;
	if (mcu_int_io_callback[17])
		((void (*)(void)) mcu_int_io_callback[17])();
} /**< IO Interrupt K1 ausführen und Callback aufrufen */
ISR (PORTQ_INT0_vect) {
	PORTQ_INTFLAGS |= 0x01;
	if (mcu_int_io_callback[18])
		((void (*)(void)) mcu_int_io_callback[18])();
} /**< IO Interrupt Q0 ausführen und Callback aufrufen */
ISR (PORTQ_INT1_vect) {
	PORTQ_INTFLAGS |= 0x02;
	if (mcu_int_io_callback[19])
		((void (*)(void)) mcu_int_io_callback[19])();
} /**< IO Interrupt Q1 ausführen und Callback aufrufen */
ISR (PORTR_INT0_vect) {
	PORTR_INTFLAGS |= 0x01;
	if (mcu_int_io_callback[20])
		((void (*)(void)) mcu_int_io_callback[20])();
} /**< IO Interrupt R0 ausführen und Callback aufrufen */
ISR (PORTR_INT1_vect) {
	PORTR_INTFLAGS |= 0x02;
	if (mcu_int_io_callback[21])
		((void (*)(void)) mcu_int_io_callback[21])();
} /**< IO Interrupt R1 ausführen und Callback aufrufen */

MCU_RESULT mcu_io_interrupt_init(MCU_IO_INT_NUM num, MCU_IO_PIN pin,
		void (*f)(void), MCU_INT_LVL lvl, MCU_IO_INT_EDGE edge) {
	uint8_t pin_val;
	if (num == MCU_IO_INT_NUM_NONE)
		return MCU_OK;	// Es ist gewollt, dass der Interrupt nicht benutzt wird
	if (pin == PIN_NONE)
		return MCU_OK;// Es wird kein Pin benutzt, also soll der Interrupt nicht benutzt werden.
	if (lvl > 3)
		return MCU_ERROR_IO_INT_LVL_INVALID;	// Level nur 0-3
	if (edge > 2)
		return MCU_ERROR_IO_INT_EDGE_INVALID;	// Edges nur 0-2
	if (num >= MCU_IO_INT_NUM_MAX)
		return MCU_ERROR_IO_INT_NUM_INVALID;	// Der Interrupt ist falsch

	mcu_int_io_callback[num] = (void*) f;
	mcu_io_set_dir(pin, MCU_IO_DIR_IN);		// Interrupt Pin auf Eingang setzen
	pin_val = mcu_io_get_pin_bin(pin);			// Die Binäre Pin Nummer laden
	mcu_set_port_pinnctrl(pin, false, 0x07);// ISC Bits löschen [1] Seite 143
	mcu_set_port_pinnctrl(pin, true, edge);		// Edge in die ISC Bits einfügen
	switch (num) {
	case MCU_IO_INT_NUM_PA_INT0:
		PORTA_INT0MASK = pin_val;
		break;
	case MCU_IO_INT_NUM_PA_INT1:
		PORTA_INT1MASK = pin_val;
		break;

	case MCU_IO_INT_NUM_PB_INT0:
		PORTB_INT0MASK = pin_val;
		break;
	case MCU_IO_INT_NUM_PB_INT1:
		PORTB_INT1MASK = pin_val;
		break;

	case MCU_IO_INT_NUM_PC_INT0:
		PORTC_INT0MASK = pin_val;
		break;
	case MCU_IO_INT_NUM_PC_INT1:
		PORTC_INT1MASK = pin_val;
		break;

	case MCU_IO_INT_NUM_PD_INT0:
		PORTD_INT0MASK = pin_val;
		break;
	case MCU_IO_INT_NUM_PD_INT1:
		PORTD_INT1MASK = pin_val;
		break;

	case MCU_IO_INT_NUM_PE_INT0:
		PORTE_INT0MASK = pin_val;
		break;
	case MCU_IO_INT_NUM_PE_INT1:
		PORTE_INT1MASK = pin_val;
		break;

	case MCU_IO_INT_NUM_PF_INT0:
		PORTF_INT0MASK = pin_val;
		break;
	case MCU_IO_INT_NUM_PF_INT1:
		PORTF_INT1MASK = pin_val;
		break;

	case MCU_IO_INT_NUM_PH_INT0:
		PORTH_INT0MASK = pin_val;
		break;
	case MCU_IO_INT_NUM_PH_INT1:
		PORTH_INT1MASK = pin_val;
		break;

	case MCU_IO_INT_NUM_PJ_INT0:
		PORTJ_INT0MASK = pin_val;
		break;
	case MCU_IO_INT_NUM_PJ_INT1:
		PORTJ_INT1MASK = pin_val;
		break;

	case MCU_IO_INT_NUM_PK_INT0:
		PORTK_INT0MASK = pin_val;
		break;
	case MCU_IO_INT_NUM_PK_INT1:
		PORTK_INT1MASK = pin_val;
		break;

	case MCU_IO_INT_NUM_PQ_INT0:
		PORTQ_INT0MASK = pin_val;
		break;
	case MCU_IO_INT_NUM_PQ_INT1:
		PORTQ_INT1MASK = pin_val;
		break;

	default:
		return MCU_ERROR_IO_INT_NUM_INVALID;// Theoretisch oben durch die ifs bereits abgedeckt
	}
	mcu_io_int_set_intctrl(num, false, 0x03);	// Interrupt Level Bits löschen
	mcu_io_int_set_intctrl(num, true, lvl);		// Interrupt Level Bits setzen

	return MCU_OK;
}

void mcu_io_interrupt_disable(MCU_IO_INT_NUM num) {
	mcu_io_int_set_intctrl(num, false, 0x03);
}

void mcu_io_interrupt_enable(MCU_IO_INT_NUM num, MCU_INT_LVL lvl) {
	mcu_io_int_set_intctrl(num, false, 0x03);
	mcu_io_int_set_intctrl(num, true, lvl);
}

static void mcu_io_int_set_intctrl(MCU_IO_INT_NUM num, bool is_set, uint8_t val) {
	if ((num & 0x01) == 1)
		val <<= 2;	// Wenn Num Gerade, dann INT0, sonst INT1
	switch (num) {
	case MCU_IO_INT_NUM_PA_INT0:
	case MCU_IO_INT_NUM_PA_INT1:
		if (is_set)
			PORTA_INTCTRL |= val;
		else
			PORTA_INTCTRL &= ~val;
		break;

	case MCU_IO_INT_NUM_PB_INT0:
	case MCU_IO_INT_NUM_PB_INT1:
		if (is_set)
			PORTB_INTCTRL |= val;
		else
			PORTB_INTCTRL &= ~val;
		break;

	case MCU_IO_INT_NUM_PC_INT0:
	case MCU_IO_INT_NUM_PC_INT1:
		if (is_set)
			PORTC_INTCTRL |= val;
		else
			PORTC_INTCTRL &= ~val;
		break;

	case MCU_IO_INT_NUM_PD_INT0:
	case MCU_IO_INT_NUM_PD_INT1:
		if (is_set)
			PORTD_INTCTRL |= val;
		else
			PORTD_INTCTRL &= ~val;
		break;

	case MCU_IO_INT_NUM_PE_INT0:
	case MCU_IO_INT_NUM_PE_INT1:
		if (is_set)
			PORTE_INTCTRL |= val;
		else
			PORTE_INTCTRL &= ~val;
		break;

	case MCU_IO_INT_NUM_PF_INT0:
	case MCU_IO_INT_NUM_PF_INT1:
		if (is_set)
			PORTF_INTCTRL |= val;
		else
			PORTF_INTCTRL &= ~val;
		break;

	case MCU_IO_INT_NUM_PH_INT0:
	case MCU_IO_INT_NUM_PH_INT1:
		if (is_set)
			PORTH_INTCTRL |= val;
		else
			PORTH_INTCTRL &= ~val;
		break;

	case MCU_IO_INT_NUM_PJ_INT0:
	case MCU_IO_INT_NUM_PJ_INT1:
		if (is_set)
			PORTJ_INTCTRL |= val;
		else
			PORTJ_INTCTRL &= ~val;
		break;

	case MCU_IO_INT_NUM_PK_INT0:
	case MCU_IO_INT_NUM_PK_INT1:
		if (is_set)
			PORTK_INTCTRL |= val;
		else
			PORTK_INTCTRL &= ~val;
		break;

	case MCU_IO_INT_NUM_PQ_INT0:
	case MCU_IO_INT_NUM_PQ_INT1:
		if (is_set)
			PORTQ_INTCTRL |= val;
		else
			PORTQ_INTCTRL &= ~val;
		break;

	default:
		break;
	}
}

#endif	// MCU_PERIPHERY_ENABLE_IO_INTERRUPT
#if MCU_PERIPHERY_ENABLE_TIMER

static void *mcu_timer_interrupt_callback[MCU_TIMER_NUM_MAX]; /**< Enthält die Callback Funktionen der Timer Interrupts */
static uint8_t mcu_timer_interrupt_lvl[MCU_TIMER_NUM_MAX]; /**< Enthält die Level der Timer Interrupts */
static uint16_t mcu_timer_frq[MCU_TIMER_NUM_MAX]; /**< Enthält die wirklich eingestellte Frequenz des Timers. */

//ISR (TCC0_OVF_vect)  {	if(mcu_timer_interrupt_callback[0])	((void(*)(void))mcu_timer_interrupt_callback[0])();		}	/**< Timer Interrupt C0 ausführen und Callback aufrufen */
//ISR (TCC1_OVF_vect)  {	if(mcu_timer_interrupt_callback[1])	((void(*)(void))mcu_timer_interrupt_callback[1])();		}	/**< Timer Interrupt C1 ausführen und Callback aufrufen */
ISR (TCD0_OVF_vect) {
	if (mcu_timer_interrupt_callback[2])
		((void (*)(void)) mcu_timer_interrupt_callback[2])();
} /**< Timer Interrupt D0 ausführen und Callback aufrufen */
ISR (TCD1_OVF_vect) {
	if (mcu_timer_interrupt_callback[3])
		((void (*)(void)) mcu_timer_interrupt_callback[3])();
} /**< Timer Interrupt D1 ausführen und Callback aufrufen */
ISR (TCE0_OVF_vect) {
	if (mcu_timer_interrupt_callback[4])
		((void (*)(void)) mcu_timer_interrupt_callback[4])();
} /**< Timer Interrupt E0 ausführen und Callback aufrufen */
ISR (TCE1_OVF_vect) {
	if (mcu_timer_interrupt_callback[5])
		((void (*)(void)) mcu_timer_interrupt_callback[5])();
} /**< Timer Interrupt E1 ausführen und Callback aufrufen */
ISR (TCF0_OVF_vect) {
	if (mcu_timer_interrupt_callback[6])
		((void (*)(void)) mcu_timer_interrupt_callback[6])();
} /**< Timer Interrupt F0 ausführen und Callback aufrufen */
ISR (TCF1_OVF_vect) {
	if (mcu_timer_interrupt_callback[7])
		((void (*)(void)) mcu_timer_interrupt_callback[7])();
} /**< Timer Interrupt F1 ausführen und Callback aufrufen */

MCU_RESULT mcu_timer_init(MCU_TIMER_NUM num, MCU_INT_LVL lvl, uint32_t frq_hz,
		void (*f)(void), bool auto_start) {
	uint32_t timer_value = (mcu_frq_peripheral_hz / frq_hz) - 1;// Berechnung des Zählers für den Timer
	TC0_t *tcN0 = NULL;
	TC1_t *tcN1 = NULL;
	uint8_t timer_div = 0x01;
	const uint16_t timer_divider[8] = { 0, 1, 2, 4, 8, 64, 256, 1024 };
	if (num == MCU_TIMER_NUM_NONE)
		return MCU_OK;	// Ist ok, da offiziell kein Timer gewollt ist.
	if (lvl > 3)
		return MCU_ERROR_TMR_LVL_INVALID;
	if (num >= MCU_TIMER_NUM_MAX)
		return MCU_ERROR_TMR_NUM_INVALID;

	for (timer_div = 1; timer_div < sizeof(timer_divider); timer_div++) {
		if ((timer_value / timer_divider[timer_div]) > 0xFFFF)
			continue;
		else {
			timer_value /= timer_divider[timer_div];
			break;
		}
	}
	if (timer_value > 0xFFFF || timer_div == sizeof(timer_divider))	// Übergebene Frequenz kann nicht gesetzt werden
		return MCU_ERROR_TMR_FRQ_INVALID;

	mcu_timer_interrupt_callback[num] = f;
	mcu_timer_interrupt_lvl[num] = lvl;

	mcu_timer_frq[num] = mcu_frq_peripheral_hz
			/ ((timer_value * timer_divider[timer_div]) + 1);

	switch (num)		// Register in Pointer zwischenspeichern
	{
	case MCU_TIMER_NUM_C0:
		tcN0 = &TCC0;
		break;
	case MCU_TIMER_NUM_C1:
		tcN1 = &TCC1;
		break;
	case MCU_TIMER_NUM_D0:
		tcN0 = &TCD0;
		break;
	case MCU_TIMER_NUM_D1:
		tcN1 = &TCD1;
		break;
	case MCU_TIMER_NUM_E0:
		tcN0 = &TCE0;
		break;
	case MCU_TIMER_NUM_E1:
		tcN1 = &TCE1;
		break;
	case MCU_TIMER_NUM_F0:
		tcN0 = &TCF0;
		break;
	case MCU_TIMER_NUM_F1:
		tcN1 = &TCF1;
		break;
	default:
		return MCU_ERROR_TMR_NUM_INVALID;
	}

	switch (num & 0x01)	// Register einstellen
	{
	case 0:
		tcN0->CNT = 0x00;
		tcN0->PER = timer_value;
		tcN0->CTRLA = timer_div;
		tcN0->INTCTRLA = auto_start ? lvl : 0;
		break;
	case 1:
		tcN1->CNT = 0x00;
		tcN1->PER = timer_value;
		tcN1->CTRLA = timer_div;
		tcN1->INTCTRLA = auto_start ? lvl : 0;
		break;
	}

	return MCU_OK;
}

void mcu_timer_start(MCU_TIMER_NUM num) {
	mcu_timer_set_start(num, true);
}

void mcu_timer_stop(MCU_TIMER_NUM num) {
	mcu_timer_set_start(num, false);
}

static void mcu_timer_set_start(MCU_TIMER_NUM num, bool is_on) {
	if (num >= MCU_TIMER_NUM_MAX)
		return;

	switch (num)			// Timer zurücksetzen und Level setzen oder löschen
	{
	case MCU_TIMER_NUM_C0:
		TCC0.CNT = 0;
		TCC0.INTCTRLA = (is_on ? mcu_timer_interrupt_lvl[num] : 0x00);
		break;
	case MCU_TIMER_NUM_C1:
		TCC1.CNT = 0;
		TCC1.INTCTRLA = (is_on ? mcu_timer_interrupt_lvl[num] : 0x00);
		break;
	case MCU_TIMER_NUM_D0:
		TCD0.CNT = 0;
		TCD0.INTCTRLA = (is_on ? mcu_timer_interrupt_lvl[num] : 0x00);
		break;
	case MCU_TIMER_NUM_D1:
		TCD1.CNT = 0;
		TCD1.INTCTRLA = (is_on ? mcu_timer_interrupt_lvl[num] : 0x00);
		break;
	case MCU_TIMER_NUM_E0:
		TCE0.CNT = 0;
		TCE0.INTCTRLA = (is_on ? mcu_timer_interrupt_lvl[num] : 0x00);
		break;
	case MCU_TIMER_NUM_E1:
		TCE1.CNT = 0;
		TCE1.INTCTRLA = (is_on ? mcu_timer_interrupt_lvl[num] : 0x00);
		break;
	case MCU_TIMER_NUM_F0:
		TCF0.CNT = 0;
		TCF0.INTCTRLA = (is_on ? mcu_timer_interrupt_lvl[num] : 0x00);
		break;
	case MCU_TIMER_NUM_F1:
		TCF1.CNT = 0;
		TCF1.INTCTRLA = (is_on ? mcu_timer_interrupt_lvl[num] : 0x00);
		break;
	case MCU_TIMER_NUM_NONE:
		break;
	}
}

uint16_t mcu_timer_get_frq(MCU_TIMER_NUM num) {
	if (num >= MCU_TIMER_NUM_MAX)
		return 0;
	return mcu_timer_frq[num];
}

#endif	// MCU_PERIPHERY_ENABLE_TIMER
#if MCU_PERIPHERY_ENABLE_UART
/** Array mit den USARTXN Registeradressen, der jeweiligen UARTs (X = Buchstabe, N = 0 oder 1) */
static USART_t *mcu_uart_usartxn[MCU_UART_TOTAL_NUMBER] = { &USARTC0, &USARTC1,
		&USARTD0, &USARTD1, &USARTE0, &USARTE1, &USARTF0, &USARTF1 };

/** Array, in das die tatsächlich eingestellten Baudraten gespeichert werden. */
static uint32_t mcu_uart_baud[MCU_UART_TOTAL_NUMBER];

/** Array mit den TXD Pinnen der UART */
static const MCU_IO_PIN mcu_uart_tx_pin[MCU_UART_TOTAL_NUMBER] = { PC_3, PC_0,
		PD_3, PD_0, PE_3, PE_0, PF_3, PF_0 }; /**< @todo Es wurde nicht ausprobiert ob alle IO Pins richtig sind, da dies im Datenblatt [1] nicht richtig angegeben war. */

/** Array mit alternativen Empfangs Funktionen für den Interrupt */
static void *mcu_uart_alternative_receive[MCU_UART_TOTAL_NUMBER];

/** Ringbuffer Strukturen für jede UART */
static fifo_struct mcu_uart_buf[MCU_UART_TOTAL_NUMBER];

MCU_RESULT mcu_uart_init(MCU_UART_NUM num, uint32_t baud, uint8_t databits,
		uint8_t parity, uint8_t stopbits) {
	uint8_t usart_ctrlc = USART_CMODE_ASYNCHRONOUS_gc;
	MCU_RESULT baud_set = MCU_OK;
	if (num == MCU_UART_NUM_NONE)
		return MCU_OK;
	if (num >= MCU_UART_TOTAL_NUMBER)
		return MCU_ERROR_UART_NUM_INVALID;
	switch (databits) {
	case 5:
		usart_ctrlc |= USART_CHSIZE_5BIT_gc;
		break;
	case 6:
		usart_ctrlc |= USART_CHSIZE_6BIT_gc;
		break;
	case 7:
		usart_ctrlc |= USART_CHSIZE_7BIT_gc;
		break;
	case 8:
		usart_ctrlc |= USART_CHSIZE_8BIT_gc;
		break;
	default:
		return MCU_ERROR_UART_DATABITS_INVALID;
	}
	switch (parity) {
	case 'N':
		usart_ctrlc |= USART_PMODE_DISABLED_gc;
		break;	// Parität None
	case 'O':
		usart_ctrlc |= USART_PMODE_ODD_gc;
		break;	// Parität Odd
	case 'E':
		usart_ctrlc |= USART_PMODE_EVEN_gc;
		break;	// Parität Even
	default:
		return MCU_ERROR_UART_PARITY_INVALID;
	}
	switch (stopbits) {
	case 1:
		usart_ctrlc |= 0x00;
		break;	// 1 Stopbit
	case 2:
		usart_ctrlc |= 0x08;
		break;	// 2 Stopbit
	default:
		return MCU_ERROR_UART_STOPBITS_INVALID;
	}

	mcu_uart_usartxn[num]->CTRLA = 0x00; 			// Interrupts deaktivieren
	mcu_io_set(mcu_uart_tx_pin[num], 1);				// TXD Pin auf 1 setzen
	mcu_io_set_dir(mcu_uart_tx_pin[num], MCU_IO_DIR_OUT);// TXD Pin als Ausgang setzen
	baud_set = mcu_uart_set_baudrate(num, baud);			// Baudrate setzen
	mcu_uart_usartxn[num]->CTRLC = usart_ctrlc;
	mcu_uart_usartxn[num]->CTRLB = 0x18;	// Receive und Transmit aktivieren

	fifo_init(&mcu_uart_buf[num], 1, NULL, 0);

	return baud_set;
}

MCU_RESULT mcu_uart_set_buffer(MCU_UART_NUM num, MCU_INT_LVL lvl, uint8_t *data,
		uint16_t len) {
	if (num == MCU_UART_NUM_NONE)
		return MCU_OK;
	if (num >= MCU_UART_TOTAL_NUMBER)
		return MCU_ERROR_UART_NUM_INVALID;
	if (data == NULL)
		return MCU_ERROR_UART_RECEIVE_INVALID;
	fifo_init(&mcu_uart_buf[num], 1, data, len);
	mcu_uart_usartxn[num]->CTRLA = lvl << 4;
	return MCU_OK;
}

MCU_RESULT mcu_uart_set_alternate_receive(MCU_UART_NUM num, MCU_INT_LVL lvl,
		void (*f)(uint8_t)) {
	if (num == MCU_UART_NUM_NONE)
		return MCU_OK;
	if (num >= MCU_UART_TOTAL_NUMBER)
		return MCU_ERROR_UART_NUM_INVALID;
	mcu_uart_alternative_receive[num] = (void*) f;
	mcu_uart_usartxn[num]->CTRLA = lvl << 4;
	return MCU_OK;
}

MCU_RESULT mcu_uart_set_baudrate(MCU_UART_NUM num, uint32_t baud) {
	if (num == MCU_UART_NUM_NONE)
		return MCU_OK;
	if (num >= MCU_UART_TOTAL_NUMBER)
		return MCU_ERROR_UART_NUM_INVALID;
	uint32_t bsel = (uint32_t) (mcu_frq_peripheral_hz / (16 * baud)) - 1;

	if (bsel > 0x0FFF)
		return MCU_ERROR_UART_BAUDRATE_INVALID;
	mcu_disable_interrupt();
	mcu_uart_usartxn[num]->BAUDCTRLA = bsel & 0xFF;
	mcu_uart_usartxn[num]->BAUDCTRLB = (bsel >> 8) & 0x0F;
	mcu_enable_interrupt();

	mcu_uart_baud[num] = mcu_frq_peripheral_hz / ((bsel + 1) * 16);

	return MCU_OK;
}

uint32_t mcu_uart_get_baud(MCU_UART_NUM num) {
	if (num >= MCU_UART_TOTAL_NUMBER)
		return 0;
	return mcu_uart_baud[num];
}

void mcu_uart_putc(MCU_UART_NUM num, uint8_t data) {
	if (num >= MCU_UART_TOTAL_NUMBER)
		return;		// Abbrechen wenn UART ungültig -> Sonst läuft Array über
	while (!(mcu_uart_usartxn[num]->STATUS & USART_DREIF_bm))
		;	// Warte bis das Register leer ist
	mcu_uart_usartxn[num]->DATA = data;	// Schreibe Daten in das Senderegister
}

uint16_t mcu_uart_available(MCU_UART_NUM num) {
	if (num >= MCU_UART_TOTAL_NUMBER)
		return 0;	// Abbrechen wenn UART ungültig -> Sonst läuft Array über
	return fifo_data_available(&mcu_uart_buf[num]);
}

uint8_t mcu_uart_getc(MCU_UART_NUM num) {
	if (num >= MCU_UART_TOTAL_NUMBER)
		return 0;	// Abbrechen wenn UART ungültig -> Sonst läuft Array über
	return fifo_get8(&mcu_uart_buf[num]);
}

MCU_RESULT mcu_uart_clear_rx(MCU_UART_NUM num) {
	if (num == MCU_UART_NUM_NONE)
		return MCU_OK;
	if (num >= MCU_UART_TOTAL_NUMBER)
		return MCU_ERROR_UART_NUM_INVALID;
	fifo_clear(&mcu_uart_buf[num]);
	return MCU_OK;
}

static void mcu_uart_rcv_interrupt(MCU_UART_NUM num) {
	uint8_t rcv;
	if (num >= MCU_UART_TOTAL_NUMBER)
		return;						// Abbrechen wenn UART ungültig
	while (!(mcu_uart_usartxn[num]->STATUS & USART_RXCIF_bm))
		;	// Warten bis das Byte im Empfangsregister steht.
	rcv = mcu_uart_usartxn[num]->DATA;
	if (mcu_uart_alternative_receive[num])
		((void (*)(uint8_t)) mcu_uart_alternative_receive[num])(rcv);// Wenn Alternativer Empfang gesetzt ist, diesem das Byte übergeben
	else
		fifo_put8(&mcu_uart_buf[num], rcv);	// Sonst in Empfangsbuffer kopieren
}

ISR(USARTC0_RXC_vect) {
	mcu_uart_rcv_interrupt(MCU_UART_NUM_C0);
} /**< Receive Interrupt von UART C0 */
ISR(USARTC1_RXC_vect) {
	mcu_uart_rcv_interrupt(MCU_UART_NUM_C1);
} /**< Receive Interrupt von UART C1 */
ISR(USARTD0_RXC_vect) {
	mcu_uart_rcv_interrupt(MCU_UART_NUM_D0);
} /**< Receive Interrupt von UART D0 */
ISR(USARTD1_RXC_vect) {
	mcu_uart_rcv_interrupt(MCU_UART_NUM_D1);
} /**< Receive Interrupt von UART D1 */
ISR(USARTE0_RXC_vect) {
	mcu_uart_rcv_interrupt(MCU_UART_NUM_E0);
} /**< Receive Interrupt von UART E0 */
ISR(USARTE1_RXC_vect) {
	mcu_uart_rcv_interrupt(MCU_UART_NUM_E1);
} /**< Receive Interrupt von UART E1 */
ISR(USARTF0_RXC_vect) {
	mcu_uart_rcv_interrupt(MCU_UART_NUM_F0);
} /**< Receive Interrupt von UART F0 */
ISR(USARTF1_RXC_vect) {
	mcu_uart_rcv_interrupt(MCU_UART_NUM_F1);
} /**< Receive Interrupt von UART F1 */

#endif // MCU_PERIPHERY_ENABLE_UART
#if MCU_PERIPHERY_ENABLE_SPI

static SPI_t *spixn[MCU_SPI_TOTAL_NUMBER] = { &SPIC, &SPID, &SPIE, &SPIF }; /**< Pointer auf die SPI Register. */

static uint32_t mcu_spi_frq[MCU_SPI_TOTAL_NUMBER]; /**< Enthält die tatsächlich eingestellte SPI Geschwindigkeit. */

MCU_RESULT mcu_spi_init(MCU_SPI_NUM num, MCU_SPI_MODE mode, uint32_t frq) {
	switch (num)	// Clock und MOSI Port auf Ausgang setzen.
	{				// MOSI									CLK
	case MCU_SPI_NUM_C:
		mcu_io_set_dir(PC_5, MCU_IO_DIR_OUT);
		mcu_io_set_dir(PC_7, MCU_IO_DIR_OUT);
		break;
	case MCU_SPI_NUM_D:
		mcu_io_set_dir(PD_5, MCU_IO_DIR_OUT);
		mcu_io_set_dir(PD_7, MCU_IO_DIR_OUT);
		break; /**< @todo Es muss getestet werden, ob die Pinne für SPI D stimmen! */
	case MCU_SPI_NUM_E:
		mcu_io_set_dir(PE_5, MCU_IO_DIR_OUT);
		mcu_io_set_dir(PE_7, MCU_IO_DIR_OUT);
		break; /**< @todo Es muss getestet werden, ob die Pinne für SPI E stimmen! */
	case MCU_SPI_NUM_F:
		mcu_io_set_dir(PF_5, MCU_IO_DIR_OUT);
		mcu_io_set_dir(PF_7, MCU_IO_DIR_OUT);
		break;
	default:
		return MCU_ERROR_SPI_NUM_INVALID;
	}
	if (mode > 3)
		return MCU_ERROR_SPI_MODE_INVALID;
	spixn[num]->CTRL = 0x40 |	// [6] 		SPI Enable
			0x10 |	// [4]		SPI Master
			mode << 2;	// [3:2]	SPI Mode
	return mcu_spi_set_clock(num, frq);
}

MCU_RESULT mcu_spi_set_clock(MCU_SPI_NUM num, uint32_t frq) {
	uint8_t prescaler = 0;
	uint8_t clk_dbl = 0;
	uint32_t tmp = mcu_get_frq_peripheral() / frq;
	if (num >= MCU_SPI_TOTAL_NUMBER)
		return MCU_ERROR_SPI_NUM_INVALID;
	if (tmp > 128)
		return MCU_ERROR_SPI_CLOCK_INVALID;

	mcu_spi_frq[num] = mcu_get_frq_peripheral();

	if (tmp > 96) {
		prescaler = 0x03;
		mcu_spi_frq[num] /= 128;
	}	// Clock / 128
	else if (tmp > 48) {
		prescaler = 0x02;
		mcu_spi_frq[num] /= 64;
	}	// Clock / 64
	else if (tmp > 24) {
		clk_dbl = 1;
		prescaler = 0x02;
		mcu_spi_frq[num] /= 32;
	}	// Clock / 32
	else if (tmp > 12) {
		prescaler = 0x01;
		mcu_spi_frq[num] /= 16;
	}	// Clock / 16
	else if (tmp > 6) {
		clk_dbl = 1;
		prescaler = 0x01;
		mcu_spi_frq[num] /= 8;
	}	// Clock / 8
	else if (tmp > 3) {
		prescaler = 0x00;
		mcu_spi_frq[num] /= 4;
	}	// Clock / 4
	else {
		clk_dbl = 1;
		prescaler = 0x00;
		mcu_spi_frq[num] /= 2;
	}	// Clock / 2

	spixn[num]->CTRL &= 0x7C;	// [7] CLK2X und [1:0] PRESCALER zurücksetzen
	spixn[num]->CTRL |= clk_dbl << 6 | prescaler;

	return MCU_OK;
}

uint8_t mcu_spi_send(MCU_SPI_NUM num, uint8_t letter, MCU_IO_PIN cs) {
	/// @todo Empfangen testen
	uint8_t spi_read = 0;
	if (num >= MCU_SPI_TOTAL_NUMBER)
		return 0;
	mcu_io_set(cs, 0);	// Chip Select Leitung ggf auf 0 runterziehen
	spixn[num]->DATA = letter;
	while ((spixn[num]->STATUS & 0x80) == 0x00)
		;
	spi_read = spixn[num]->DATA;
	mcu_io_set(cs, 1);	// Chip Select Leitung ggf wieder auf 1 zurücksetzen
	return spi_read;
}

uint32_t mcu_spi_get_frq(MCU_SPI_NUM num) {
	if (num >= MCU_SPI_TOTAL_NUMBER)
		return 0;
	return mcu_spi_frq[num];
}

#endif	// MCU_PERIPHERY_ENABLE_SPI
#if MCU_PERIPHERY_ENABLE_AD

static bool mcu_ad_flag_ready[MCU_AD_TOTAL_NUMBER]; /**< Gibt an ob der AD Wandler bereit ist. */
static bool mcu_ad_auto_read[MCU_AD_TOTAL_NUMBER]; /**< Gibt an, ob der AD Wandler im Freerun Modus ist. */
static void *mcu_ad_callback[MCU_AD_TOTAL_NUMBER]; /**< Callback Funktion für den Interrupt. */

ISR(ADCA_CH0_vect) {
	ADCA.CH0.INTFLAGS |= 0x01;
	if (mcu_ad_callback[0] != NULL)
		((void (*)(uint16_t)) mcu_ad_callback[0])(ADCA.CH0.RES);
} /**< Interrupt Channel 0 */

MCU_RESULT mcu_ad_init(MCU_AD_CHANNEL channel, MCU_IO_PIN pin,
		void (*f)(uint16_t), MCU_INT_LVL lvl, MCU_AD_SIGNEDNESS sign,
		MCU_AD_RESOLUTION res, bool auto_read) {
	/**
	 * @todo Es wird aktuell nur ein Channel benutzt.
	 * @todo Es wird nur der Singleshot Mode unterstützt.
	 * @todo Es wird nur ein Prescaler unterstützt.
	 */
	if (channel > MCU_AD_TOTAL_NUMBER)
		return MCU_ERROR_AD_CHANNEL_INVALID;
	if ((pin & 0xF0) != PA_0 || (pin & 0x0F) > 7)
		return MCU_ERROR_AD_IO_PIN_INVALID;	// Nur Port A Pins annehmen

	mcu_io_set_dir(pin, MCU_IO_DIR_IN);

	ADCA.CTRLA = 0x01; 								// [7:6] (00) DMA Off
													// [5:2] Channel AD Conversion Start Bit
													// [1]	 Flush
													// [0]	 ADC Enable

	ADCA.CTRLB = sign | res;	// [4]	 Conversion Mode (0) Unsigned (1) Signed
								// [3]	 Freerun
								// [2:1] Resolution (00) 12 Bit Right (10) 8 Bit (11) 12 Bit Left

	ADCA.REFCTRL = 0x02;// [5:4] Reference Selection (00) Internal 1V (01) Internal VCC/1.6 (10) AREFA (11) AREFB
						// [1]	 Bandgap
						// [0]	 Temperature Reference Mode

	ADCA.PRESCALER = ADC_PRESCALER_DIV16_gc; // [2:0] Prescaler = 2^(x+1) mit x = Register Wert

	switch (channel) {
	case MCU_AD_CHANNEL_0:
		ADCA.CH0.CTRL = 0x01;								// [7]	 Start Bit
															// [4:2] Gain Factor = 2^x mit x = Register Wert
															// [1:0] Input Mode
															//		-> Unsigned: (00) Internal (01) Single Ended
															//		-> Signed: (00) Internal (01) Single Ended (10) Diff (11) Diff with Gain

		ADCA.CH0.MUXCTRL = (pin & 0x0F) << 3; // [6:3] Mux Pos: IO Pin für Positiven Wert
											  // [1:0] Mux Neg: IO Pin für Negativen Wert
		break;
	}

	if (auto_read) {
		ADCA.CTRLB |= 0x08;	// Freerun Modus aktivieren
	}

	mcu_ad_callback[channel] = (void*) f;

	if (mcu_ad_callback[channel] != NULL) {
		mcu_ad_flag_ready[channel] = false;
		switch (channel) {
		case MCU_AD_CHANNEL_0:
			ADCA.CH0.INTCTRL = lvl;
			break;		// INT Level
		}
	}

	mcu_ad_flag_ready[channel] = false;

	for (int i = 0; i < 24 * 512; i++)	// Wait at least 24 ADC clock cycles
		asm volatile ("nop");
	// according to AVR1300, sec. 4.1.

	return MCU_OK;
}

MCU_RESULT mcu_ad_start(MCU_AD_CHANNEL num) {
	if (num > MCU_AD_TOTAL_NUMBER)
		return MCU_ERROR_AD_CHANNEL_INVALID;
	mcu_ad_flag_ready[num] = false;
	switch (num) {
	case MCU_AD_CHANNEL_0:
		ADCA.CH0.CTRL |= ADC_CH_START_bm;
		break;		// Start single conversion
	default:
		return MCU_ERROR_AD_CHANNEL_INVALID;
	}
	return MCU_OK;
}

bool mcu_ad_ready(MCU_AD_CHANNEL num) {
	if (num > MCU_AD_TOTAL_NUMBER)
		return false;
	if (mcu_ad_check_complete_flag(num))
		mcu_ad_flag_ready[num] = true;
	return mcu_ad_flag_ready[num];
}

int32_t mcu_ad_read(MCU_AD_CHANNEL num) {
	if (num > MCU_AD_TOTAL_NUMBER)
		return 0;
	if (mcu_ad_auto_read[num])
		return mcu_ad_get_res(num);
	else {
		while (!mcu_ad_ready(num))
			;
		return mcu_ad_get_res(num);
	}
}

static bool mcu_ad_check_complete_flag(MCU_AD_CHANNEL num) {
	switch (num) {
	case MCU_AD_CHANNEL_0:
		if (ADCA.CH0.INTFLAGS & 0x01) {
			ADCA.CH0.INTFLAGS |= 0x01;
			return true;
		}
	}
	return false;
}

static uint16_t mcu_ad_get_res(MCU_AD_CHANNEL num) {
	switch (num) {
	case MCU_AD_CHANNEL_0:
		return ADCA.CH0.RES;
	default:
		return 0;
	}
}

#endif	// MCU_PERIPHERY_ENABLE_AD
void mcu_wait_us(uint16_t delay) {
	//_delay_us(delay);		// Funktioniert manchmal nicht richtig?
	while (delay--) {
		uint16_t x = 16;	// Geschätzt! Muss genau geprüft werden
		while (x--)
			asm("nop");
	}
}

void mcu_wait_ms(uint16_t delay) {
	while (delay--)
		mcu_wait_us(1000);
	//_delay_ms(delay);		// Funktioniert manchmal nicht richtig?
}
