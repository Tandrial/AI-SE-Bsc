/**
 * 	@file 	mcu.h
 *  @author Tim Koczwara
 *  @date	06.08.2011
 *
 *  @brief
 *  		Enth�lt Defines f�r Einstellungen, Enumerationen f�r Typen und die Prototypen der externen Funktionen.
 *
 *  @par 	Prozessoren:
 *  			- Atmel:
 *  				- ATXMega 128 A1
 *  			- Renesas:
 *  				- R32C 111 (64-Pin)
 *
 *  @section	How-to-use
 *
 *	@subsection	mcu_htu_init MCU initialisieren
 *  			Das Modul bietet 3 M�glichkeiten die MCU einzustellen. Die erste M�glichkeit ist es die 3 Grundfrequenzen (extern, CPU und
 *  			Peripherie) fest vorzugeben, die das System dann nach M�glichkeit einstellt. Die n�chste M�glichkeit ist die Einstellung
 *  			der maximalen Frequenz der internen Clock, die benutzt werden kann, wenn keine externe Clock angeschlossen ist.
 *  			Als letztes gibt es noch die M�glichkeit das System mit der maximalen Frequenz zu initialisieren, indem nur die angeschlossene
 *  			externe Frequenz angegeben wird. Die letzten beiden M�glichkeiten sind mit Vorsicht zu genie�en, da manche MCUs auf der
 *  			maximalen Einstellung hei� werden k�nnen und eine entsprechende K�hlung ben�tigen.
 *  			Alle 3 Funktionen geben in einem Fehlerfall einen Error Code zur�ck, der beispielsweise
 *  			mit einem Debugger angeschaut werden kann.
 @code
 mcu_init(10000000, 60000000, 30000000);	// 10MHz externe Frequenz, 60MHz CPU und 30 MHz Peripherie
 // Alternativ
 mcu_init_max_internal();	// Gr��te Frequenz, ohne einen externen Quarz
 // Alternativ
 mcu_init_max_external(10000000);	// Gr��te Frequenz mit einem externen 10MHz Quarz
 @endcode
 *
 * 	@subsection mcu_htu_io MCU IO Ports einstellen
 * 				F�r die IO Ports gibt es 4 Einstellungen, die nach dem Start des Systems vorgenommen werden sollten.
 * 				Als erstes wird eingestellt, welche IO Ports Ein-/ und Ausg�nge sind. Hierf�r gibt es die M�glichkeit den Port
 * 				komplett oder die Pinne einzeln einzustellen.
 @code
 mcu_io_set_port_dir(PA, 0xF0);	// Port A 0 - 3 Eingang und 4-7 Ausgang
 mcu_io_set_dir(PB_4, MCU_IO_DIR_OUT);	// Port B 4 ist Ausgang
 @endcode
 *
 *  			Bei Ausg�ngen wird h�ufig ein bestimmter Pegel ben�tigt, der beim Start gesetzt werden sollte. Mit der IO Set Funktion k�nnen
 *  			sowohl einzelne Pinne, als auch komplette Ports gesetzt werden.
 @code
 mcu_io_set(PA, 0xF0);	// Port A Pin 0 bis 3 haben einen Low Pegel, Pin 4 bis 7 haben dagegen einen High Pegel
 mcu_io_set(PB_1, 1);	// Pin B 1 hat einen High Pegel
 @endcode
 *
 *  			Bei Eing�ngen hingegen kommt es vor, dass einige einen Pull-Up ben�tigen. Diese k�nnen jedoch nur einzeln eingestellt werden.
 *  			Zu beachten ist jedoch, dass es je nach Controller vorkommen kann, dass der Controller nur Pull-Ups f�r mehrere Pinne gemeinsam
 *  			aktivieren oder deaktivieren kann.
 @code
 mcu_io_set_pullup(PA_2, true);	// Port A Pin 2 Pull Up aktivieren
 @endcode
 *
 *				Zus�tzlich kann f�r Eing�nge auch noch ein Interrupt aktiviert werden. Bei der Initialisierung wird die Interrupt Nummer,
 *				sowie der zust�ndige IO Pin angegeben, da es vorkommen kann, dass sich mehrere Pins einen Interrupt teilen. Dadurch kann es
 *				auch passieren, dass bestimmte Interrupts nicht benutzt werden k�nnen, wenn diese bereits von anderen Interrupts besetzt werden.
 *				Damit die IO Interrupts benutzt werden k�nnen muss jedoch zun�chst in der mcu.h das Makro MCU_PERIPHERY_ENABLE_IO_INTERRUPT auf
 *				true gesetzt werden. Ansonsten werden die IO Interrupt Funktionen komplett ausgeblendet.
 @code
 void mcu_example_io_interrupt_trigger(void);	// Prototyp f�r die Interrupt Funktion
 ...
 mcu_io_interrupt_init(MCU_IO_INT_NUM_PB_INT0, PB_0, mcu_example_io_interrupt_trigger, MCU_INT_LVL_HI, MCU_IO_INT_EDGE_BOTH);
 // Es wird f�r den Port B Pin 0 ein Interrupt aktiviert, der beim ausl�sen die Funktion mcu_example_io_interrupt_trigger aufruft.
 // Der Interrupt wird sowohl bei einer fallenden, als auch bei einer steigenden Flanke ausgel�st.
 @endcode
 *
 *	@subsection	mcu_htu_uart_init UART
 *				Um eine UART zu benutzen, muss zun�chst eingestellt werden mit welcher Baudrate, Datenbits, Parit�t und Stopbits diese
 *				arbeitet.
 @code
 mcu_uart_init(MCU_UART_NUM_D0, 115200, 8, 'N', 1);	// Die UART D0 wird mit den g�ngigen Parametern 115200 8N1 initialisiert.
 @endcode
 *				Wenn die UART nur zum Daten senden benutzt wird ist die Initialisierung damit abgeschlossen. Um Daten zu empfangen muss
 *				jedoch entweder der UART einen Empfangspuffer zugewiesen werden, in den Daten automatisch geschrieben werden, oder aber eine
 *				alternative Empfangsfunktion eingestellt werden.
 *
 *				F�r die Nutzung des Empfangspuffers muss zun�chst Speicher allokiert und dann der UART zugewiesen werden. Danach kann auf
 *				den Empfangspuffer der UART gepollt und Bytes daraus ausgelesen werden.
 @code
 uint8_t buf_uart_d0[8000];	// Global erstellter Speicher

 mcu_uart_set_buffer(MCU_UART_NUM_D0, MCU_INT_LVL_HI, buf_uart_d0, sizeof(buf_uart_d0));	// Die UART D0 erh�lt einen 8000 Byte Buffer mit einer hohen Interrupt Priorit�t
 ...
 if(mcu_uart_available(MCU_UART_NUM_D0))	// Wenn Daten empfangen wurden,...
 {
 uint8_t b = mcu_uart_getc(MCU_UART_NUM_D0);	// ...dann lese 1 Byte aus
 // Byte bearbeiten
 }
 @endcode
 *
 *				Die eigene Empfangsfunktion hingegen wird immer dann aufgerufen, wenn ein Byte empfangen wird. In dem Fall muss nicht
 *				auf den UART Buffer gepollt werden. Diese Anwendung kann z.B. wichtig sein, wenn der Zeitpunkt, zu dem das Byte empfangen
 *				wurde bekannt sein muss.
 @code
 void my_uart_d0_receive(uint8_t b);	// Prototyp der eigenen Receive Funktion
 ...
 mcu_uart_set_alternate_receive(MCU_UART_NUM_D0, MCU_INT_LVL_HI, my_uart_d0_receive);	// Die UART D0 erh�lt eine eigene Receive Funktion mit einer hohen Interrupt Priorit�t
 @endcode
 *
 *	@subsection	mcu_htu_spi_init SPI
 *				SPI ist derzeit nur im Master Modus vorgesehen. Aus diesem Grund bietet das SPI Modul auch keine Interrupt Funktion
 *				zum empfangen, da Daten im Master Modus aktiv beim senden empfangen werden.
 *
 *				Bei der Initialisierung des SPI Moduls m�ssen nur der SPI Mode (siehe [1]) und die Clock Geschwindigkeit eingestellt werden.
 @code
 mcu_spi_init(MCU_SPI_NUM_D, MCU_SPI_MODE_3, 5000000);	// Die SPI D wird in Mode 3 mit 5 MHz gestartet.
 @endcode
 *
 *				Da SPI eine synchrone Schnittstelle ist wird das senden und empfangen �ber eine Funktion gel�st.
 @code
 mcu_spi_send(MCU_SPI_NUM_D, 0x1b, PD_2);	// 0x1B auf SPI D rausschicken mit der Chip Select Leitung PD_2
 // Die Chip Select Leitung ist jedoch optional und kann auch mit PIN_NONE �bergeben werden, falls beispielsweise ganze Bl�cke gesendet
 // werden m�ssen. Alternative:
 mcu_io_set(PD_2, 0);
 mcu_spi_send(MCU_SPI_NUM_D, 0x1b, PIN_NONE);
 mcu_io_set(PD_2, 1);
 //Empfang:
 uint8_t b = mcu_spi_send(MCU_SPI_NUM_D, 0xFF, PD_2);
 @endcode
 *
 *	@subsection	mcu_htu_ad_init AD Wandler
 *				Der AD Wandler bietet aktuell 3 unterschiedliche Betriebsmodi, die bei der Initialisierung eingestellt werden k�nnen.
 *
 *				Im normalen Modus muss die AD Wandlung immer manuell gestartet werden und sobald die Auslesung fertig ist kann der Wert
 *				abgefragt werden.
 @code
 // Normaler Modus:
 mcu_ad_init(MCU_AD_CHANNEL_0, PD_0, NULL, MCU_INT_LVL_OFF, MCU_AD_UNSIGNED, MCU_AD_RESOLUTION_8BIT, false);
 // Der AD Wandler l�uft mit Kanal 0 auf Pin D0. Es wird kein Interrupt benutzt, weshalb die Callback Funktion NULL ist und der Interrupt
 // Level auf Off steht. Der AD Wert ist vorzeichenlos mit einer 8 Bit Aufl�sung
 // Die Daten k�nnen wie folgt ausgelesen werden:
 mcu_ad_start(MCU_AD_CHANNEL_0);	// Auslesung starten
 while(!mcu_ad_ready(MCU_AD_CHANNEL_0));	// Warten bis die Auslesung fertig ist. Kann auch nicht blockierend mit if gel�st werden.
 uint8_t data = mcu_ad_read(MCU_AD_CHANNEL_0);	// Wert auslesen
 @endcode
 *					Im Interrupt Modus muss nicht darauf gewartet werden bis der AD Wert bereit ist. Der AD Wert wird, sobald er fertig
 *					gemessen wurde sofort an die eigene AD Funktion �bergeben.
 @code
 // Interrupt Modus:
 void my_ad_read(uint16_t value);	// Prototyp der eigenen AD Read Funktion
 ...
 mcu_ad_init(MCU_AD_CHANNEL_0, PD_0, my_ad_read, MCU_INT_LVL_MED, MCU_AD_UNSIGNED, MCU_AD_RESOLUTION_8BIT, false);
 // Der AD Wandler l�uft mit Kanal 0 auf Pin D0. Es wird ein mittlerer Interrupt auf die eigene AD Read Funktion gelegt.
 // Der AD Wert ist vorzeichenlos mit einer 8 Bit Aufl�sung. Da die meisten AD Wandler aber auch h�here Aufl�sungen bieten muss
 // die eigene Funktion 16 Bit bieten!
 // Die Daten k�nnen wie folgt ausgelesen werden:
 mcu_ad_start(MCU_AD_CHANNEL_0);	// Auslesung starten
 // Sobald die Ausl�sung fertig ist wird der Wert an my_ad_read �bergeben.
 @endcode
 *					Der Freerun Modus sorgt daf�r, dass der AD Wandler dauerhaft misst und jederzeit ausgelesen werden kann. Es wird dabei
 *					kein Interrupt genutzt und kein dauernder AD Start ben�tigt.
 @code
 // Freerun Modus:
 mcu_ad_init(MCU_AD_CHANNEL_0, PD_0, NULL, MCU_INT_LVL_OFF, MCU_AD_UNSIGNED, MCU_AD_RESOLUTION_8BIT, true);
 // Der AD Wandler l�uft mit Kanal 0 auf Pin D0. Es wird kein Interrupt benutzt, weshalb die Callback Funktion NULL ist und der Interrupt
 // Level auf Off steht. Der AD Wert ist vorzeichenlos mit einer 8 Bit Aufl�sung. Der Letzte Parameter startet den Freerun Modus.
 // Die Daten k�nnen wie folgt ausgelesen werden:
 mcu_ad_start(MCU_AD_CHANNEL_0);	// Auslesung einmalig starten!
 uint8_t data = mcu_ad_read(MCU_AD_CHANNEL_0);	// Der Wert kann dann jederzeit ausgelesen werden.
 @endcode
 *
 *	@version	1.01
 *				 - Globalen Interrupt Level eingef�hrt, der f�r alle MCUs gelten soll.
 *  @version	1.00
 *  			 - erste Version
 *
 *  @see	[1] http://de.wikipedia.org/wiki/Serial_Peripheral_Interface am 06.08.2011
 *
 *
 *	@todo	SPI unterst�tzt aktuell nur Master Mode.
 *	@todo	Es werden keine Watchdog Funktionen angeboten.
 *	@todo	Die AD Wandler unterst�tzen noch keine Event, oder andere m�gliche Modi.
 *	@todo	Der AD Wandler unterst�tzt auch nur eine feste Aufl�sung.
 *	@todo	Timer unterst�tzen nur den normalen Timer Modus, keinen weiteren.
 *
 ******************************************************************************/

#ifndef MCU_INIT_HEADER_FIRST_INCLUDE_GUARD
#define MCU_INIT_HEADER_FIRST_INCLUDE_GUARD

#include <stdint.h>
#include <stdbool.h>

/***********************************************************************
 *               Config
 ***********************************************************************/
// CPU Type Defines
#define MCU_ATXMEGA128A1		"atmel/mcu_atxmega128a1.h"		/**< MCU Typ: ATXMega 128 A1  */
#define MCU_R5F6411F			"renesas/mcu_r5f6411f.h"		/**< MCU Typ: Renesas R32C/111 64 Pin  */

#define MCU_TYPE				MCU_ATXMEGA128A1				/**< MCU auf ATXMEGA128A1 einstellen */

#ifndef NULL
#define NULL	0												/**< Sicherstellen, dass der NULL Typ vorhanden ist. */
#endif

// Peripherie aktivieren
#define MCU_PERIPHERY_ENABLE_WATCHDOG				false		/**< Watchdog de-/aktivieren. */
#define MCU_PERIPHERY_ENABLE_UART					true		/**< UART de-/aktivieren */
#define MCU_PERIPHERY_ENABLE_SPI					true		/**< SPI de-/aktivieren */
#define MCU_PERIPHERY_ENABLE_IO_INTERRUPT			true		/**< IO Interrupts de-/aktivieren */
#define MCU_PERIPHERY_ENABLE_TIMER					true		/**< Timer de-/aktivieren */
#define MCU_PERIPHERY_ENABLE_AD						true		/**< AD Wandler de-/aktivieren */

/***********************************************************************
 *               Allgemeine Aufz�hlungen
 ***********************************************************************/
/** @enum MCU_RESULT
 *		Wird bei verschiedenen MCU Funktionen zur�ckgegeben und gibt an ob die Einstellung OK ist, oder ob es Ungenauigkeiten / Fehler gibt.
 **/
typedef enum {
	MCU_OK = 0x00000000, /**< Kein Fehler festgestellt */
	// Frequenzen
	MCU_ERROR_FRQ_EXT_INVALID = 0x00000001, /**< Die externe Frequenz ist ung�ltig.  */
	MCU_ERROR_FRQ_MCU_INVALID = 0x00000002, /**< Die MCU/CPU Clock ist ung�ltig.  */
	MCU_ERROR_FRQ_PERIPHERAL_INVALID = 0x00000004, /**< Die Periphery Clock ist ung�ltig.  */
	// IO Interrupt
	MCU_ERROR_IO_INT_NUM_INVALID = 0x00000008, /**< IO Interrupt: Nummer ist nicht vorhanden  */
	MCU_ERROR_IO_INT_LVL_INVALID = 0x00000010, /**< IO Interrupt: Level ist ung�ltig */
	MCU_ERROR_IO_INT_EDGE_INVALID = 0x00000020, /**< IO Interrupt: Flanke ist ung�ltig (Nur steigend, fallend oder beides)  */
	// Timer
	MCU_ERROR_TMR_NUM_INVALID = 0x00000040, /**< Timer: Nummer ist nicht vorhanden  */
	MCU_ERROR_TMR_LVL_INVALID = 0x00000080, /**< Timer: Interrupt Level ist ung�ltig */
	MCU_ERROR_TMR_FRQ_INVALID = 0x00000100, /**< Timer: Frequenz kann nicht eingestellt werden.  */
	// UART
	MCU_ERROR_UART_NUM_INVALID = 0x00000200, /**< UART: Nummer ist ung�ltig. */
	MCU_ERROR_UART_DATABITS_INVALID = 0x00000400, /**< UART: Datenbits ist nicht implementiert / ung�ltig. */
	MCU_ERROR_UART_PARITY_INVALID = 0x00000800, /**< UART: Parit�t ist ung�ltig.  */
	MCU_ERROR_UART_STOPBITS_INVALID = 0x00001000, /**< UART: Stopbits ist ung�ltig.  */
	MCU_ERROR_UART_BAUDRATE_INVALID = 0x00002000, /**< UART: Baudrate ist nicht einstellbar. */
	MCU_ERROR_UART_RECEIVE_INVALID = 0x00004000, /**< UART: Alternative Empfangs Funktion oder UART Buffer ist ung�ltig. */
	// SPI
	MCU_ERROR_SPI_NUM_INVALID = 0x00008000, /**< SPI: Nummer ist ung�ltig. */
	MCU_ERROR_SPI_MODE_INVALID = 0x00010000, /**< SPI: Mode ist ung�ltig. */
	MCU_ERROR_SPI_CLOCK_INVALID = 0x00020000, /**< SPI: Clock ist ung�ltig. */
	// AD
	MCU_ERROR_AD_CHANNEL_INVALID = 0x00040000, /**< AD: Kanal ist ung�ltig. */
	MCU_ERROR_AD_IO_PIN_INVALID = 0x00080000 /**< AD: Der IO Pin l�sst sich nicht mit dem Kanal einstellen. */
} MCU_RESULT;

/**
 * @enum MCU_IO_DIRECTION
 * 		Die Aufz�hlung gibt an, ob ein IO Pin ein Ein- oder Ausgang ist. Die Werte werden in der Funktion mcu_io_set_dir ben�tigt.
 */
typedef enum {
	MCU_IO_DIR_OUT = 1,			///< Ausgang
	MCU_IO_DIR_IN = 0			///< Eingang
} MCU_IO_DIRECTION;

/**
 * @enum MCU_INT_LVL
 * 		Es gibt 4 vordefinierte Interrupt Level, die bei verschiedener Peripherie, wie UART, IO Interrupts oder AD Wandler
 * 		benutzt werden k�nnen.
 */
typedef enum {
	MCU_INT_LVL_OFF = 0,		///< Interrupt deaktivieren.
	MCU_INT_LVL_LO = 1,		///< Interrupt Level Low.
	MCU_INT_LVL_MED = 2,		///< Interrupt Level Medium.
	MCU_INT_LVL_HI = 3			///< Interrupt Level High.
} MCU_INT_LVL;

#if MCU_PERIPHERY_ENABLE_IO_INTERRUPT
/**
 * @enum MCU_IO_INT_EDGE
 * 		Die Aufz�hlung enth�lt die m�glichen Ausl�seflanken f�r einen IO Interrupt.
 */
typedef enum {
	MCU_IO_INT_EDGE_BOTH = 0,///< IO Interrupt bei steigender und fallender Flanke.
	MCU_IO_INT_EDGE_HIGH = 1,		///< IO Interrupt bei steigender Flanke.
	MCU_IO_INT_EDGE_LOW = 2		///< IO Interrupt bei fallender Flanke.
} MCU_IO_INT_EDGE;
#endif	// #if MCU_PERIPHERY_ENABLE_IO_INTERRUPT
#if MCU_PERIPHERY_ENABLE_SPI
/**
 * @enum MCU_SPI_MODE
 * 		Es gibt 4 unterschiedliche SPI Modes, die je nach angeschlossener Peripherie eingestellt werden k�nnen.
 * 		Eine genauere Beschreibung zu den Modi findet gibt es bei [1].
 */
typedef enum {
	MCU_SPI_MODE_0 = 0, /**< Clock Idle Low -> �bernahme bei erster Flanke	-> CKPOL 0 CKPH 1 */
	MCU_SPI_MODE_1 = 1, /**< Clock Idle Low -> �bernahme bei zweiter Flanke -> CKPOL 1 CKPH 0 */
	MCU_SPI_MODE_2 = 2, /**< Clock Idle High-> �bernahme bei erster Flanke	-> CKPOL 1 CKPH 1 */
	MCU_SPI_MODE_3 = 3 /**< Clock Idle High-> �bernahme bei zweiter Flanke -> CKPOL 0 CKPH 0 */
} MCU_SPI_MODE;

#endif	// MCU_PERIPHERY_ENABLE_SPI
/***********************************************************************
 *  Prozessorspezifischen Header einbinden
 ***********************************************************************/
#if defined(MCU_TYPE)
#include MCU_TYPE
#else
#error "Ung�ltiger MCU Typ!"
#endif

/***********************************************************************
 *               Makros
 ***********************************************************************/
#define MCU_IO_TOGGLE(s)     mcu_io_set(s, mcu_io_get(s)^1)		/**< Makro um einen IO Pin zu invertieren. */

/***********************************************************************
 *               Funktionen
 ***********************************************************************/
// Initialisierung
/**
 * @brief	Muss zum Start der main Methode aufgerufen werden.
 * 			Stellt die Clock Frequenzen ein.
 * @param frq_ext			Die Frequenz des externen Quarzes in Hertz
 * @param frq_cpu			Die Frequenz in Hertz mit der die CPU rechnet.
 * @param frq_peripheral	Muss der CPU Clock entsprechen!
 * @return					MCU_OK: Es ist kein Fehler aufgetreten.\n
 * 							MCU_ERROR_FRQ_EXT_INVALID: Die �bergebene externe Frequenz ist nicht als Clock Source vorgesehen.\n
 * 							MCU_ERROR_FRQ_MCU_INVALID: Die MCU Clock kann nicht eingestellt werden.\n
 * 							MCU_ERROR_FRQ_PERIPHERAL_INVALID: Die Periphery Clock kann nicht eingestellt werden.
 */
MCU_RESULT mcu_init(uint32_t frq_ext, uint32_t frq_cpu, uint32_t frq_peripheral);
/**
 * @brief	Setzt die Maximal m�gliche Frequenz mit einer internen Clock.
 * @return					MCU_OK: Es ist kein Fehler aufgetreten.\n
 * 							MCU_ERROR_FRQ_MCU_INVALID: Die MCU Clock kann nicht eingestellt werden.\n
 * 							MCU_ERROR_FRQ_PERIPHERAL_INVALID: Die Periphery Clock kann nicht eingestellt werden.
 */
MCU_RESULT mcu_init_max_internal();
/**
 * @brief	Initialisiert die CPU mit der maximalen Frequenz auf Basis des externen Quarzes
 * @param frq_ext			Die Frequenz des externen Quarzes in Hertz
 * @return					MCU_OK: Es ist kein Fehler aufgetreten.\n
 * 							MCU_ERROR_FRQ_EXT_INVALID: Die �bergebene externe Frequenz ist nicht als Clock Source vorgesehen.\n
 * 							MCU_ERROR_FRQ_MCU_INVALID: Die MCU Clock kann nicht eingestellt werden.\n
 * 							MCU_ERROR_FRQ_PERIPHERAL_INVALID: Die Periphery Clock kann nicht eingestellt werden.
 */
MCU_RESULT mcu_init_max_external(uint32_t frq_ext);

// Clock
uint32_t mcu_get_frq_external(void); /**< @return 	Frequenz des externen Quarzes in Hertz. */
uint32_t mcu_get_frq_cpu(void); /**< @return 	Frequenz der CPU Clock in Hertz. */
uint32_t mcu_get_frq_peripheral(void); /**< @return 	Frequenz der Peripherie Clock in Hertz. */

// Interrupt
/**
 * @brief	Aktiviert die Interrupts Global
 */
void mcu_enable_interrupt(void);		// Globale Aktivierung der Interrupts
/**
 * @brief	Deaktiviert die Interrupts Global
 */
void mcu_disable_interrupt(void);		// Globale Deaktivierung der Interrupts

// Reset
/**
 * @brief	F�hrt einen Software Reset aus
 */
void mcu_soft_reset(void);									// Software Reset

// IO
/**
 * @brief	Setzt die Port Direction f�r einen kompletten Port
 * @param p					IO Port
 * @param d 				Bitweise Richtung der einzelnen Ports. Z.B. 0xFF f�r alle IO Pins des Ports als Ausgang.
 */
void mcu_io_set_port_dir(MCU_IO_PIN p, uint8_t d);// Kompletten IO Port Ausg�nge setzen
/**
 * @brief	Setzt die Port Direction f�r einen einzelnen IO Pin. Wird nicht ausgef�hrt, wenn statt eines Pins ein kompletter Port �bergeben wird.
 * @param p					IO Pin
 * @param d 				Direction f�r den Pin (Ein-/Ausgang)
 */
void mcu_io_set_dir(MCU_IO_PIN p, MCU_IO_DIRECTION d);// IO Pin auf Ein/Ausgang setzen
/**
 * @brief	Schaltet den Pullup f�r einen IO Pin ein oder aus. Nur f�r einzelne IO Pins, nicht f�r ganze Ports.
 * @param p					IO Pin
 * @param pullup_active 	true: Pull Up einschalten.\n
 * 							false: Pull Up ausschalten.
 */
void mcu_io_set_pullup(MCU_IO_PIN p, bool pullup_active);// Pullup aktivieren / Deaktivieren
/**
 * @brief	Setzt IO Pin oder Port auf angegebenen Pegel (1 oder 0).
 * @param p					IO Pin oder IO Port
 * @param d 					Falls p ein Pin ist: Pegel (1 oder 0)\n
 * 							Falls p ein Port ist: Pegel bitweise f�r alle Pins des Ports.
 */
void mcu_io_set(MCU_IO_PIN p, uint8_t d);				// Ausgangspegel setzen
/**
 * @brief	Gibt den Pegel des Pins oder die Pegel aller Pins des Ports an
 * @param p					IO Pin oder IO Port
 * @return	 				Falls p ein Pin ist: Pegel (1 oder 0)\n
 * 							Falls p ein Port ist: Pegel f�r alle Pins des Ports.
 */
uint8_t mcu_io_get(MCU_IO_PIN p);					// Eingangspegel abfragen

// IO Interrupt
#if MCU_PERIPHERY_ENABLE_IO_INTERRUPT
/**
 * @brief	Aktiviert einen IO Interrupt auf entsprechendes Level und Edge Erkennung, sowie die Funktion, die beim Interrupt aufgerufen wird.
 *
 * @param num				IO Interrupt Nummer
 * @param pin				IO Pin des entsprechenden Interrupts
 * @param f					Callback Funktion, die aufgerufen wird, wenn der Interrupt ausgel�st wird
 * @param lvl				Level des Interrupts
 * @param edge				Edge Erkennung des Interrupts (Falling, Rising, Both)
 * @return					MCU_OK: Es ist kein Fehler aufgetreten.\n
 * 							MCU_ERROR_IO_INT_NUM_INVALID: Der Interrupt existiert nicht.\n
 * 							MCU_ERROR_IO_INT_EDGE_INVALID: Die eingestellte Flanke existiert nicht.\n
 * 							MCU_ERROR_IO_INT_LVL_INVALID: Das Interrupt Level existiert nicht.
 */
MCU_RESULT mcu_io_interrupt_init(MCU_IO_INT_NUM num, MCU_IO_PIN pin,
		void (*f)(void), MCU_INT_LVL lvl, MCU_IO_INT_EDGE edge);
/**
 * @brief	Deaktiviert einen IO Interrupt
 *
 * @param num				IO Interrupt Nummer
 */
void mcu_io_interrupt_disable(MCU_IO_INT_NUM num);
/**
 * @brief	Reaktiviert einen IO Interrupt.	Vorher sollte bereits die Funktion mcu_io_interrupt_init aufgerufen worden sein!
 * @param num				IO Interrupt Nummer
 * @param lvl				Level des IO Interrupts.
 */
void mcu_io_interrupt_enable(MCU_IO_INT_NUM num, MCU_INT_LVL lvl);
#endif

// Timer
#if MCU_PERIPHERY_ENABLE_TIMER
/**
 * @brief	Aktiviert einen Timer und setzt die Frequenz, den Level, sowie die Timer Interrupt Funktion
 *
 * @param num				Timer Nummer
 * @param lvl				Level des Interrupts
 * @param frq_hz			Gew�nschte Frequenz des Timers in Hz (Beispiel: 1ms = 1kHz also 1000Hz �bergeben).
 * 							Die genau eingestellte Frequenz kann hinterher mit mcu_timer_get_frq() ausgelesen werden.
 * @param f					Callback Funktion, die aufgerufen wird, wenn der Interrupt ausgel�st wird
 * @param auto_start		true: Timer sofort starten.\n
 * 							false: Timer nicht starten.
 * @return					MCU_OK: Es ist kein Fehler aufgetreten.\n
 * 							MCU_ERROR_TMR_NUM_INVALID : Der Timer existiert nicht.\n
 * 							MCU_ERROR_TMR_FRQ_INVALID : Die �bergebene Frequenz kann nicht eingestellt werden.\n
 * 							MCU_ERROR_TMR_LVL_INVALID : Das Interrupt Level existiert nicht.
 */
MCU_RESULT mcu_timer_init(MCU_TIMER_NUM num, MCU_INT_LVL lvl, uint32_t frq_hz,
		void (*f)(void), bool auto_start);
/**
 * @brief	Startet den Timer
 *
 * @param num				Timer Nummer
 */
void mcu_timer_start(MCU_TIMER_NUM num);
/**
 * @brief	Stoppt den Timer
 *
 * @param num				Timer Nummer
 */
void mcu_timer_stop(MCU_TIMER_NUM num);
/**
 * @brief	Gibt die im Timer eingestellte Frequenz zur�ck.
 *
 * @param num				Timer Nummer
 * @return					Die wirklich eingestellte Frequenz des Timers.
 */
uint16_t mcu_timer_get_frq(MCU_TIMER_NUM num);
#endif

// UART
#if MCU_PERIPHERY_ENABLE_UART
/**
 * @brief 	Initialisierung der entsprechenden UART.
 *
 *  		Wird bei ung�ltiger MCU_UART_NUM nicht ausgef�hrt, setzt jedoch einen Fehlercode.
 *
 *  @param num      		Die UART Nummer
 *  @param baud				Baudrate in Bit/s
 *  @param databits			Anzahl der Datenbits (5, 6, 7, 8, 9). Zu beachten ist, dass nicht alle MCUs auch alle Datenbits unterst�tzen!
 *  @param parity			Parit�t ('N' = None, 'O' = Odd, 'E' = Even)
 *  @param stopbits			Anzahl der Stopbits (1, 2)
 * @return					MCU_OK: Es ist kein Fehler aufgetreten.\n
 * 							MCU_ERROR_UART_NUM_INVALID : Die UART existiert nicht.\n
 * 							MCU_ERROR_UART_DATABITS_INVALID : Die Datenbits k�nnen nicht eingestellt werden.\n
 * 							MCU_ERROR_UART_PARITY_INVALID : Die Parit�t ist ung�ltig.\n
 * 							MCU_ERROR_UART_STOPBITS_INVALID : Die Stopbits k�nnen nicht eingestellt werden.\n
 * 							MCU_ERROR_UART_BAUDRATE_INVALID : Die Baudrate kann nicht eingestellt werden.\n
 */
MCU_RESULT mcu_uart_init(MCU_UART_NUM num, uint32_t baud, uint8_t databits,
		uint8_t parity, uint8_t stopbits);
/**
 * @brief 	�bergibt der UART ihren Empfangsbuffer und setzt den entsprechenden Interrupt Level, damit Daten empfangen werden k�nnen.
 * @param num				Nummer der UART
 * @param lvl				Interrupt Level
 * @param data				Pointer auf den Buffer, in dem der Speicher f�r die UART reserviert wurde.
 * @param len				L�nge des Speichers f�r die UART.
 * @return					MCU_OK: Es ist kein Fehler aufgetreten.\n
 * 							MCU_ERROR_UART_NUM_INVALID : Die UART existiert nicht.\n
 * 							MCU_ERROR_UART_RECEIVE_INVALID : Der Buffer hat keine Gr��e oder zeigt auf einen NULL Pointer.
 */
MCU_RESULT mcu_uart_set_buffer(MCU_UART_NUM num, MCU_INT_LVL lvl, uint8_t *data,
		uint16_t len);
/**
 * @brief 	Setzt eine Alternative Receive Funktion f�r den Interrupt.
 *
 *  		Wird bei ung�ltiger MCU_UART_NUM nicht ausgef�hrt, setzt jedoch einen Fehlercode.
 *
 *  @param num      		Die UART Nummer
 *  @param lvl				Interrupt Level
 *  @param f				Alternative Empfangs Funktion. Muss einen uint8 Parameter erwarten und darf keinen R�ckgabetyp haben.
 *  						Wenn f NULL ist wird der alternative Empfang deaktiviert und der normale Buffer wieder benutzt.
 * @return					MCU_OK: Es ist kein Fehler aufgetreten.\n
 * 							MCU_ERROR_UART_NUM_INVALID : Die UART existiert nicht.
 */
MCU_RESULT mcu_uart_set_alternate_receive(MCU_UART_NUM num, MCU_INT_LVL lvl,
		void (*f)(uint8_t));
/**
 * @brief 	Stellt die Baudrate der UART ein.
 *
 *  		Wird bei ung�ltiger MCU_UART_NUM nicht ausgef�hrt, setzt jedoch einen Fehlercode.
 *
 *  @param num     			Die UART Nummer
 *  @param baud				Baudrate in Bit/s
 * @return					MCU_OK: Es ist kein Fehler aufgetreten.\n
 * 							MCU_ERROR_UART_NUM_INVALID : Die UART existiert nicht.\n
 * 							MCU_ERROR_UART_BAUDRATE_INVALID : Die Baudrate kann nicht eingestellt werden.
 */
MCU_RESULT mcu_uart_set_baudrate(MCU_UART_NUM num, uint32_t baud);
/**
 * @brief	Gibt die in der UART eingestellte Baudrate zur�ck.
 * @param num				UART Nummer
 * @return					Die genaue eingestellte Baudrate.
 */
uint32_t mcu_uart_get_baud(MCU_UART_NUM num);
/**
 * @brief 	Sendet ein Byte auf der entsprechenden UART.
 *
 *  		Wird bei ung�ltiger MCU_UART_NUM nicht ausgef�hrt.
 *
 *  @param num      		Die UART Nummer
 *  @param data     		Das zu sendende Byte
 */
void mcu_uart_putc(MCU_UART_NUM num, uint8_t data);
/**
 * @brief 	Gibt an wieviele Bytes im Receive Buffer stehen.\n
 * 			Wenn ein alternativer Receive gesetzt wurde werden keine Daten in den Buffer geschrieben!\n
 * 			Wenn die UART ung�ltig ist wird immer 0 zur�ckgegeben.
 *
 *  @param num      		Die UART Nummer
 *  @return 				Anzahl der Daten im Receive Buffer.
 */
uint16_t mcu_uart_available(MCU_UART_NUM num);
/**
 * @brief 	Liest ein Byte aus dem UART Buffer und gibt es zur�ck.\n
 * 			Wenn ein alternativer Receive gesetzt wurde werden keine Daten in den Buffer geschrieben!\n
 * 			Wenn die UART ung�ltig ist wird immer 0 zur�ckgegeben.
 *
 *  @param num     		 	Die UART Nummer
 *  @return 				Empfangenes Byte
 */
uint8_t mcu_uart_getc(MCU_UART_NUM num);
/**
 * @brief 	Setzt den UART Buffer zur�ck. Bei einer ung�ltigen UART wird ein Fehler zur�ckgegeben.
 *
 *  @param num     			Die UART Nummer
 *  @return 				MCU_OK: Es ist kein Fehler aufgetreten.\n
 *  						MCU_ERROR_UART_NUM_INVALID: Die UART existiert nicht.
 */
MCU_RESULT mcu_uart_clear_rx(MCU_UART_NUM num);
#endif

// SPI
#if MCU_PERIPHERY_ENABLE_SPI
/**
 * @brief 	Initialisierung des entsprechenden SPI Ports.
 * 			Setzt den SPI Mode in die Register, aktiviert SPI und ruft die Funktion mcu_spi_set_clock auf.
 *
 *  		Wird bei ung�ltiger MCU_SPI_NUM nicht ausgef�hrt, setzt jedoch einen Fehlercode.
 *
 *  @param num      		Die SPI Nummer
 *  @param mode				SPI Mode 0-3. [1]
 *  @param frq				SPI Frequenz in Hertz
 * @return					MCU_OK: Es ist kein Fehler aufgetreten.\n
 * 							MCU_ERROR_SPI_NUM_INVALID : Die SPI Schnittstelle existiert nicht.\n
 * 							MCU_ERROR_SPI_MODE_INVALID : Der SPI Mode existiert nicht.\n
 * 							MCU_ERROR_SPI_CLOCK_INVALID : Die Clock kann nicht eingestellt werden.
 */
MCU_RESULT mcu_spi_init(MCU_SPI_NUM num, MCU_SPI_MODE mode, uint32_t frq);
/**
 * @brief 	Setzt die SPI Clock.
 * 			Die SPI Clock kann nicht frei eingestellt werden, da es nur bestimmte Divider auf die Peripherie Clock gibt.
 * 			Die Funktion versucht einen geeigneten Divider zu berechnen um nah an die gew�nschte Frequenz ranzukommen.
 *
 *  		Wird bei ung�ltiger MCU_SPI_NUM nicht ausgef�hrt, setzt jedoch einen Fehlercode.
 *
 *  @param num      		Die SPI Nummer
 *  @param frq				SPI Frequenz in Hertz
 * @return					MCU_OK: Es ist kein Fehler aufgetreten.\n
 * 							MCU_ERROR_SPI_NUM_INVALID : Die SPI Schnittstelle existiert nicht.\n
 * 							MCU_ERROR_SPI_CLOCK_INVALID : Die Clock kann nicht eingestellt werden.
 */
MCU_RESULT mcu_spi_set_clock(MCU_SPI_NUM num, uint32_t frq);
/**
 * @brief 	Schreibt ein Byte auf den SPI Bus und liest gleichzeitig ein Byte aus.
 *
 *  		Wird bei ung�ltiger MCU_SPI_NUM nicht ausgef�hrt und gibt 0 zur�ck.
 *
 *  @param num      		Die SPI Nummer
 *  @param letter			Zu sendendes Byte
 *  @param cs				Chip Select Leitung angeben, falls diese automatisch gesetzt werden soll. Ansonsten NO_PIN �bergeben.
 *  @return 				Gibt das vom SPI gelesene Byte zur�ck. Im Fehlerfall wird 0 zur�ckgegeben.
 */
uint8_t mcu_spi_send(MCU_SPI_NUM num, uint8_t letter, MCU_IO_PIN cs);
/**
 * @brief	Gibt die im SPI eingestellte Frequenz zur�ck.
 * @param num				SPI Nummer
 * @return					Die tats�chlich eingestellte Frequenz.
 */
uint32_t mcu_spi_get_frq(MCU_SPI_NUM num);
#endif

// AD Wandler
#if MCU_PERIPHERY_ENABLE_AD
/**
 * @brief 	Initialisiert den AD Wandler.
 *  		Aus der Kombination der Parameter channel und pin wird der entsprechende AD Wandler Kanal auf den IO Pin geroutet.
 * 			Die Funktion f wird aufgerufen, sobald ein Interrupt des AD Wandlers auftritt. Wenn der Interrupt beim Parameter lvl deaktiviert wurde darf
 * 			die Funktion f NULL sein.
 *
 * @param channel			AD Kanal
 * @param pin				IO Pin des entsprechenden Kanals.
 * @param f					Callback Funktion f�r den Interrupt.
 * @param lvl				Level der Interrupts.
 * @param sign				Gibt an, ob der AD Wert signed oder unsigned ist. Siehe hierzu die Aufz�hlung @link MCU_AD_SIGNEDNESS MCU_AD_SIGNEDNESS@endlink.
 * @param res				Die Aufl�sung, mit der der AD Wandler l�uft. Typisch sind hier 8-Bit oder 16-Bit. Genauere Daten zum Controller aus der @link MCU_AD_RESOLUTION MCU_AD_RESOLUTION@endlink entnehmen.
 * @param auto_read			true: AD Wandler im Freerun Modus.\n
 * 							false: AD Wandler muss manuell immer neu gestartet werden.
 * @return 					MCU_OK: Es ist kein Fehler aufgetreten.\n
 * 							MCU_ERROR_AD_CHANNEL_INVALID: Der �bergebene AD Kanal ist ung�ltig.\n
 * 							MCU_ERROR_AD_IO_PIN_INVALID: Der �bergebene IO Pin kann nicht mit dem �bergebenem Kanal benutzt werden.
 */
MCU_RESULT mcu_ad_init(MCU_AD_CHANNEL channel, MCU_IO_PIN pin,
		void (*f)(uint16_t), MCU_INT_LVL lvl, MCU_AD_SIGNEDNESS sign,
		MCU_AD_RESOLUTION res, bool auto_read);
/**
 * @brief 	Startet den AD Wandler.
 *
 * @param num				AD Wandler Kanal
 * @return					MCU_OK: Es ist kein Fehler aufgetreten.\n
 * 							MCU_ERROR_AD_CHANNEL_INVALID: Der zu startende Kanal existiert nicht.
 */
MCU_RESULT mcu_ad_start(MCU_AD_CHANNEL num);
/**
 * @brief 	Gibt an, ob der AD Wandler bereit zum auslesen ist.
 *
 * @param num				AD Wandler Kanal
 * @return 					true: Ergebnis wurde gewandelt.\n
 * 							false: Noch kein Ergebnis verf�gbar.
 */
bool mcu_ad_ready(MCU_AD_CHANNEL num);
/**
 * @brief 	Liest den AD Wert aus.
 * 			Im Normal Modus blockiert die Funktion bis der Wert empfangen wurde.
 * 			Im Freerun Modus wird direkt ausgelesen.
 *
 * @param num				AD Wandler Kanal
 * @return 					Wert des AD Wandlers oder 0, wenn ung�ltig.
 */
int32_t mcu_ad_read(MCU_AD_CHANNEL num);
#endif

// Delay
/**
 * @brief 	Wartet eine angegebene Zeit in Mikrosekunden.
 *
 *  @param delay			Zu wartende Zeit in Mikrosekunden
 */
void mcu_wait_us(uint16_t delay);
/**
 * @brief 	Wartet eine angegebene Zeit in Millisekunden.
 *
 *  @param delay			Zu wartende Zeit in Mikrosekunden
 */
void mcu_wait_ms(uint16_t delay);

#endif
