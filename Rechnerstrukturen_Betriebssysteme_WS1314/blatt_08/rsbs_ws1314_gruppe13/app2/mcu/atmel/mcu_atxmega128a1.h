/**
 * 	@file 		mcu_atxmega128a1.h
 *  @author 	Tim Koczwara
 *  @date		14.08.2011
 *
 *  @brief
 *  		Das MCU Modul für den ATXMega 128 A1.
 *
 *  		Clock Einstellungen:
 *  			Der ATXMega besitzt 6 mögliche Clock Sourcen:
 *  				- Intern 32kHz Oszillator
 *  				- Interne 2 MHz
 *  				- Interne 32 MHz
 *  				- Externe 32 kHz
 *  				- Externer Quarz
 *  				- Externe Clock
 *
 *  			Aus diesen Clock Sourcen wird die System Clock hergeleitet. Hierfür können neben den genannten Sourcen auch die PLL
 *  			Clock genutzt werden. Die PLL nutzt folgende Sourcen:
 *  				- Interne 2 MHz
 *  				- Interne 32 MHz durch 4 geteilt
 *  				- Externe Clock / Quarz
 *
 *  			Die maximal mögliche Frequenz der PLL beträgt 200 MHz, die minimale 10 MHz.
 *
 *	@attention	Die Funktionen für die maximale Interne Clock nimmt die 32 MHz Source, die maximale externe Clock verwendet hingegen die
 *  			PLL um auch 32 MHz zu erzeugen. Bei 200 MHz Betrieb wird die MCU sehr heiß, weshalb genau geschaut werden muss, wann diese
 *  			Einstellung benutzt werden kann.
 *
 *  			Enthaltene Peripherie:
 *  				- 22 IO Interrupts auf 11 Ports verteilt, bei denen jeweils 2 Register für den Interrupt genutzt werden können.
 *  				- 8 Timer
 *  				- 8 UARTs auf 4 Ports, bei denen jeweils 2 auf die Ports C, D, E und F verteilt sind.
 *  				- 4 SPI auf 4 Ports
 *
 *	@version	1.02
 *				 - Die Interrupt Level der einzelnen Bereiche durch einen globalen Interrupt Level in der mcu.h ersetzt.
 *	@version	1.01
 *				 - MCU_AD_MODE in MCU_AD_SIGNEDNESS umbenannt.
 *  @version	1.00
 *  			 - Erste Version
 *
 *  @see		[1] doc8077.pdf - XMega A Manual (Preliminary, 8077H- AVR-12/09)
 *  @see		[2] AVR 1307 - Sourcecode zur Benutzung der UART (Atmel.com vom 23.06.2011)
 *  @see		[3] AVR 1003 - Using the XMEGA Clock System (doc8072.pdf - Rev. 8072E-AVR-11/09)
 *  @see		[4] AVR 1300 - Using the Atmel AVR XMEGA ADC (Atmel.com vom 05.08.2011)
 *
 ******************************************************************************/

#ifndef MCU_ATXMEGA128A1_HEADER_FIRST_INCLUDE_GUARD
#define MCU_ATXMEGA128A1_HEADER_FIRST_INCLUDE_GUARD

/// @enum MCU_IO_PIN
/// Aufzählung aller IO Pins, die der Controller unterstützt.
/// Diese werden benutzt, um unabhängig von den Controller Typen einen gemeinsamen Aufzählungstyp zu haben.
typedef enum {	
	PA_0 = 0x00,	///< Port A Bit 0
	PA_1 = 0x01,	///< Port A Bit 1
	PA_2 = 0x02,	///< Port A Bit 2
	PA_3 = 0x03,	///< Port A Bit 3
	PA_4 = 0x04,	///< Port A Bit 4
	PA_5 = 0x05,	///< Port A Bit 5
	PA_6 = 0x06,	///< Port A Bit 6
	PA_7 = 0x07,	///< Port A Bit 7
	PA	 = 0x0F,	///< Port A Gesamt

	PB_0 = 0x10,	///< Port B Bit 0
	PB_1 = 0x11,	///< Port B Bit 1
	PB_2 = 0x12,	///< Port B Bit 2
	PB_3 = 0x13,	///< Port B Bit 3
	PB_4 = 0x14,	///< Port B Bit 4
	PB_5 = 0x15,	///< Port B Bit 5
	PB_6 = 0x16,	///< Port B Bit 6
	PB_7 = 0x17,	///< Port B Bit 7
	PB	 = 0x1F,	///< Port B Gesamt

	PC_0 = 0x20,	///< Port C Bit 0
	PC_1 = 0x21,	///< Port C Bit 1
	PC_2 = 0x22,	///< Port C Bit 2
	PC_3 = 0x23,	///< Port C Bit 3
	PC_4 = 0x24,	///< Port C Bit 4
	PC_5 = 0x25,	///< Port C Bit 5
	PC_6 = 0x26,	///< Port C Bit 6
	PC_7 = 0x27,	///< Port C Bit 7
	PC	 = 0x2F,	///< Port C Gesamt

	PD_0 = 0x30,	///< Port D Bit 0
	PD_1 = 0x31,	///< Port D Bit 1
	PD_2 = 0x32,	///< Port D Bit 2
	PD_3 = 0x33,	///< Port D Bit 3
	PD_4 = 0x34,	///< Port D Bit 4
	PD_5 = 0x35,	///< Port D Bit 5
	PD_6 = 0x36,	///< Port D Bit 6
	PD_7 = 0x37,	///< Port D Bit 7
	PD	 = 0x3F,	///< Port D Gesamt

	PE_0 = 0x40,	///< Port E Bit 0
	PE_1 = 0x41,	///< Port E Bit 1
	PE_2 = 0x42,	///< Port E Bit 2
	PE_3 = 0x43,	///< Port E Bit 3
	PE_4 = 0x44,	///< Port E Bit 4
	PE_5 = 0x45,	///< Port E Bit 5
	PE_6 = 0x46,	///< Port E Bit 6
	PE_7 = 0x47,	///< Port E Bit 7
	PE	 = 0x4F,	///< Port E Gesamt

	PF_0 = 0x50,	///< Port F Bit 0
	PF_1 = 0x51,	///< Port F Bit 1
	PF_2 = 0x52,	///< Port F Bit 2
	PF_3 = 0x53,	///< Port F Bit 3
	PF_4 = 0x54,	///< Port F Bit 4
	PF_5 = 0x55,	///< Port F Bit 5
	PF_6 = 0x56,	///< Port F Bit 6
	PF_7 = 0x57,	///< Port F Bit 7
	PF	 = 0x5F,	///< Port F Gesamt

	PH_0 = 0x60,	///< Port H Bit 0
	PH_1 = 0x61,	///< Port H Bit 1
	PH_2 = 0x62,	///< Port H Bit 2
	PH_3 = 0x63,	///< Port H Bit 3
	PH_4 = 0x64,	///< Port H Bit 4
	PH_5 = 0x65,	///< Port H Bit 5
	PH_6 = 0x66,	///< Port H Bit 6
	PH_7 = 0x67,	///< Port H Bit 7
	PH	 = 0x6F,	///< Port H Gesamt

	PJ_0 = 0x70,	///< Port J Bit 0
	PJ_1 = 0x71,	///< Port J Bit 1
	PJ_2 = 0x72,	///< Port J Bit 2
	PJ_3 = 0x73,	///< Port J Bit 3
	PJ_4 = 0x74,	///< Port J Bit 4
	PJ_5 = 0x75,	///< Port J Bit 5
	PJ_6 = 0x76,	///< Port J Bit 6
	PJ_7 = 0x77,	///< Port J Bit 7
	PJ	 = 0x7F,	///< Port J Gesamt

	PK_0 = 0x80,	///< Port K Bit 0
	PK_1 = 0x81,	///< Port K Bit 1
	PK_2 = 0x82,	///< Port K Bit 2
	PK_3 = 0x83,	///< Port K Bit 3
	PK_4 = 0x84,	///< Port K Bit 4
	PK_5 = 0x85,	///< Port K Bit 5
	PK_6 = 0x86,	///< Port K Bit 6
	PK_7 = 0x87,	///< Port K Bit 7
	PK	 = 0x8F,	///< Port K Gesamt

	PQ_0 = 0x90,	///< Port Q Bit 0
	PQ_1 = 0x91,	///< Port Q Bit 1
	PQ_2 = 0x92,	///< Port Q Bit 2
	PQ_3 = 0x93,	///< Port Q Bit 3
	PQ_4 = 0x94,	///< Port Q Bit 4
	PQ_5 = 0x95,	///< Port Q Bit 5
	PQ_6 = 0x96,	///< Port Q Bit 6
	PQ_7 = 0x97,	///< Port Q Bit 7
	PQ	 = 0x9F,	///< Port Q Gesamt
	
	PIN_EXTERNAL = 0xE0,		/**< 	Ein Pseudo Pin, der bei mcu_io_set den Wert in eine Variable schreibt, anstatt den Pegel
										auf einen Pin zu legen. Mit mcu_io_get wird dieser Pin entsprechend ausgelesen.
										Andere IO Funktionen haben auf diesen Pin keine Auswirkung. Wird dieser Pin für die Initialisierung
										anderer Module, wie Interrupt oder AD Wandler verwendet werden diese Module nicht funktionieren. */

	PIN_NONE = 0xFF				/**< 	Dieser Pin dient als Pseudonym für IO Pins die nicht verwendet werden. Wenn ein Modul beispielsweise
										ein Signal auf einem Pin herausgeben kann, kann dies mit setzen dieses Pins verhindert werden. */
} MCU_IO_PIN;


#if MCU_PERIPHERY_ENABLE_IO_INTERRUPT

#define MCU_IO_INT_NUM_MAX 		22	///< Anzahl der möglichen IO Interrupts.

/// @enum MCU_IO_INT_NUM
/// Aufzählung aller IO Interrupts, sowie eines Wertes für "Kein IO Interrupt".
typedef enum {	
	MCU_IO_INT_NUM_PA_INT0 = 0,		///< Port A INT 0
	MCU_IO_INT_NUM_PA_INT1 = 1,		///< Port A INT 1
	MCU_IO_INT_NUM_PB_INT0 = 2,		///< Port B INT 0
	MCU_IO_INT_NUM_PB_INT1 = 3,		///< Port B INT 1
	MCU_IO_INT_NUM_PC_INT0 = 4,		///< Port C INT 0
	MCU_IO_INT_NUM_PC_INT1 = 5,		///< Port C INT 1
	MCU_IO_INT_NUM_PD_INT0 = 6,		///< Port D INT 0
	MCU_IO_INT_NUM_PD_INT1 = 7,		///< Port D INT 1
	MCU_IO_INT_NUM_PE_INT0 = 8,		///< Port E INT 0
	MCU_IO_INT_NUM_PE_INT1 = 9,		///< Port E INT 1
	MCU_IO_INT_NUM_PF_INT0 =10,		///< Port F INT 0
	MCU_IO_INT_NUM_PF_INT1 =11,		///< Port F INT 1
	MCU_IO_INT_NUM_PH_INT0 =12,		///< Port H INT 0
	MCU_IO_INT_NUM_PH_INT1 =13,		///< Port H INT 1
	MCU_IO_INT_NUM_PJ_INT0 =14,		///< Port J INT 0
	MCU_IO_INT_NUM_PJ_INT1 =15,		///< Port J INT 1
	MCU_IO_INT_NUM_PK_INT0 =16,		///< Port K INT 0
	MCU_IO_INT_NUM_PK_INT1 =17,		///< Port K INT 1
	MCU_IO_INT_NUM_PQ_INT0 =18,		///< Port Q INT 0
	MCU_IO_INT_NUM_PQ_INT1 =19,		///< Port Q INT 1
	MCU_IO_INT_NUM_PR_INT0 =20,		///< Port R INT 0
	MCU_IO_INT_NUM_PR_INT1 =21,		///< Port R INT 1

	MCU_IO_INT_NUM_NONE = 255		/**< 	Ein Wert, bei dem alle Interrupt Funktionen direkt abbrechen, ohne eine Fehlermeldung zurückzugeben.
											Dies kann benötigt werden, wenn der Interrupt für ein Modul von außen aufgerufen wird und das Modul selber
											diesen nicht mehr initialisieren muss. */
} MCU_IO_INT_NUM;	

#endif	// #if MCU_PERIPHERY_ENABLE_IO_INTERRUPT

#if MCU_PERIPHERY_ENABLE_TIMER

#define MCU_TIMER_NUM_MAX 	8		///< 8 Timer verfügbar.

/// @enum MCU_TIMER_NUM
/// Aufzählung aller Timer.
typedef enum {	
	MCU_TIMER_NUM_C0 	= 0,		///< Port C Timer 0
	MCU_TIMER_NUM_C1 	= 1,		///< Port C Timer 1
	MCU_TIMER_NUM_D0 	= 2,		///< Port D Timer 0
	MCU_TIMER_NUM_D1 	= 3,		///< Port D Timer 1
	MCU_TIMER_NUM_E0 	= 4,		///< Port E Timer 0
	MCU_TIMER_NUM_E1 	= 5,		///< Port E Timer 1
	MCU_TIMER_NUM_F0 	= 6,		///< Port F Timer 0
	MCU_TIMER_NUM_F1 	= 7,		///< Port F Timer 1
	MCU_TIMER_NUM_NONE  = 255		/**< 	Wert, bei dem alle Timer Funktionen direkt abbrechen ohne einen Fehler zurückzugeben.
											Wird benötigt, wenn ein Timer für mehrere Modultimer zuständig ist, ohne, dass jedes Modul einen
											eigenen besitzt. */
} MCU_TIMER_NUM;

#endif	// MCU_PERIPHERY_ENABLE_TIMER

#if MCU_PERIPHERY_ENABLE_UART

#define MCU_UART_TOTAL_NUMBER 	8	///< 8 UARTs verfügbar.

/// @enum MCU_UART_NUM
/// Aufzählung aller gültigen UARTs.
typedef enum {
	MCU_UART_NUM_C0 	= 0,		///< Port C UART 0
	MCU_UART_NUM_C1 	= 1,		///< Port C UART 1
	MCU_UART_NUM_D0 	= 2,		///< Port D UART 0
	MCU_UART_NUM_D1 	= 3,		///< Port D UART 1
	MCU_UART_NUM_E0 	= 4,		///< Port E UART 0
	MCU_UART_NUM_E1 	= 5,		///< Port E UART 1
	MCU_UART_NUM_F0 	= 6,		///< Port F UART 0
	MCU_UART_NUM_F1 	= 7,		///< Port F UART 1
	MCU_UART_NUM_NONE 	= 255		///< Wert, bei dem alle UART Funktionen direkt abbrechen ohne einen Fehler zurückzugeben.
} MCU_UART_NUM;

#endif	// MCU_PERIPHERY_ENABLE_UART

#if MCU_PERIPHERY_ENABLE_SPI

#define MCU_SPI_TOTAL_NUMBER	4	///< Controller besitzt 4 SPI Schnittstellen.

/// @enum MCU_SPI_NUM
/// Aufzählung aller gültigen SPI Schnittstellen.
/// @todo Der Controller besitzt die Möglichkeit einige der UARTs auch als SPI zu nutzen. Hierfür müsste auch auf Kollisionen bei UARTs im SPI und USART Modus geprüft werden.
typedef enum {
	MCU_SPI_NUM_C = 0,			///< Port C SPI
	MCU_SPI_NUM_D = 1,			///< Port D SPI
	MCU_SPI_NUM_E = 2,			///< Port E SPI
	MCU_SPI_NUM_F = 3,			///< Port F SPI
	MCU_SPI_NUM_NONE = 255		///< Wert, bei dem alle SPI Funktionen direkt abbrechen ohne einen Fehler zurückzugeben.
} MCU_SPI_NUM;

#endif	// MCU_PERIPHERY_ENABLE_SPI

#if MCU_PERIPHERY_ENABLE_AD

#define MCU_AD_TOTAL_NUMBER	1	///< 4 AD Channels vorhanden
								///< @todo Wenn mehr als ein AD Channel implementiert werden, muss die MCU_AD_TOTAL_NUMBER angepasst werden!

/// @enum MCU_AD_CHANNEL
/// Aufzählung aller gültigen AD Wandler Kanäle.
/// @todo Wenn mehr als ein AD Channel implementiert werden, müssen diese zu MCU_AD_CHANNEL hinzugefügt werden!
typedef enum {	
	MCU_AD_CHANNEL_0 = 0	///< AD Kanal 0
} MCU_AD_CHANNEL;	

/// @enum MCU_AD_SIGNEDNESS
/// Der ATXMega A 128 unterstützt sowohl AD Messungen mit, als auch ohne Vorzeichen.
typedef enum {
	MCU_AD_SIGNED 		= 0x10,	///< Vorzeichenbehaftet
	MCU_AD_UNSIGNED 	= 0x00	///< Ohne Vorzeichen
} MCU_AD_SIGNEDNESS;

/// @enum MCU_AD_RESOLUTION
/// Der ATXMega A 128 A1 besitzt 3 verschiedene Auflösungen.
typedef enum {
	MCU_AD_RESOLUTION_12BIT_RIGHT 		= 0x00,	///< 12 Bit von Rechts entspricht einer normalen 12 Bit Auflösung [11:0].
	MCU_AD_RESOLUTION_8BIT			 	= 0x04,	///< 8 Bit Auflösung.
	MCU_AD_RESOLUTION_12BIT_LEFT	 	= 0x06	///< 12 Bit von Links entspricht einer 16 Bit Auflösung, bei der die unteren 4 Bit wegfallen [15:4].
} MCU_AD_RESOLUTION;

#endif	// MCU_PERIPHERY_ENABLE_AD

#endif	// MCU_ATXMEGA128A1_HEADER_FIRST_INCLUDE_GUARD
