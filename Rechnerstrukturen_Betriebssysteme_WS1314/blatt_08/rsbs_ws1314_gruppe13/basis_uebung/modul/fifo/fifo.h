/**
 * 	@file 	fifo.h
 * 	@author Tim Koczwara
 *  @date	26.08.2011
 *
 *  @brief
 *			Das FIFO Modul stellt eine Struktur für unterschiedliche Anwendungen zur Verfügung.
 *			In der Initialisierungsfunktion wird angegeben, welche Größe ein einzelnes Element im Puffer hat. Dadurch ist es möglich
 *			sowohl primitive Typen wie char oder int zu benutzen, als auch ganze Paketströme, bei denen ein Paket eine bestimmte Länge hat.
 *
 *	@section ff_init Initialisierung
 *			Im folgenden Beispiel werden 3 FIFOs mit entsprechendem Speicherplatz erstellt. Der erste enthält jeweils 8-Bit, der zweite
 *			16-Bit und der dritte enthält 144 Byte Elemente.
@code
	uint8_t 	buff_8[500];		// 500 Elemente mit einer Elementgröße von 1 Byte
	uint16_t 	buff_16[500];		// 500 Elemente mit einer Elementgröße von 2 Byte
	uint8_t 	buff_prot[10*144];	// 10 Elemente, mit einer Größe von jeweils 144 Byte
	...
	fifo_struct b_8;		// Ringbuffer mit 1 Byte Elementen
	fifo_struct b_16; 		// Ringbuffer mit 2 Byte Elementen
	fifo_struct b_prot;		// Ringbuffer mit 144 Byte Elementen
	...
	fifo_init(&b_8, 		1, 	buff_8, 	500);
	fifo_init(&b_16, 		2, 	buff_16, 	500);
	fifo_init(&b_prot,	144,	buff_prot, 	10);
@endcode
 *
 *			Zu beachten ist hierbei, dass die Init Funktion im letzten Parameter die Anzahl der Elemente erhalten muss. Aus diesem Grund darf
 *			die Funktion sizeof() nicht benutzt werden, da diese die gesamte Größe des Puffers angibt: Elementgröße * Anzahl der Elemente\n
 *			Dies kann man im obigen Beispiel an dem 16-Bit Puffer erkennen. Durch die Eigenschaft uint16_t besitzt er bereits pro Element 2 Byte
 *			und davon 500 Elemente. Die Funktion sizeof() würde hierbei jedoch 1000 zurückgeben.
 *
 *	@section ff_put Daten Einfügen
 *			Um Daten in die FIFO einzufügen gibt es unterschiedliche Möglichkeiten, die das folgende Beispiel zeigt. Es wird angenommen,
 *			dass die Struktur aus dem ersten Beispiel besteht.
@code
	// ...
	uint8_t prot_data[144];
	uint8_t data_8 = 0x74;
	uint16_t data_16 = 0x7364;
	// ...
	fifo_put8(&b_8, 0x23);		// 1 Byte einfügen
	fifo_put(&b_8, &data_8);		// 1 Byte per Pointer einfügen

	fifo_put16(&b_16, 0x2039);	// 2 Byte einfügen
	fifo_put(&b_16, &data_16);	// 2 Byte per Pointer einfügen

	fifo_put(&b_prot, prot_data);	// Protokoll Buffer einfügen
	// ...
@endcode
 *			Die @link fifo_put fifo_put@endlink Funktion kann auch dazu benutzt werden 8-Bit, 16-Bit oder 32-Bit Werte einzufügen.
 *
 *	@section ff_get Daten Auslesen
 *			Für das Auslesen der Daten muss gewartet werden, bis die Funktion @link fifo_data_available fifo_data_available@endlink
 *			zurückgibt, dass mindestens 1 Element im Puffer steht. Anschließend können die Daten mit den get Funktionen ausgelesen werden.
 *			Die Ausnahme hierbei ist die Funktion @link fifo_get fifo_get@endlink, die ohne @link fifo_data_available fifo_data_available@endlink
 *			abgefragt werden kann und true zurückgibt, sobald etwas im Puffer steht. Hier noch ein Beispiel, das auf den oberen beiden basiert.
@code
	// ...
	while(//...//)
	{
		//...
		if(fifo_data_available(&b_8))
			data_8 = fifo_get8(&b_8);
		// Funktioniert auch mit get!
		if(fifo_get(&b_8, &data_8)){
			// Daten behandeln
		}
		//...
		if(fifo_data_available(&b_16))
			data_16 = fifo_get16(&b_16);
		//...
		if(fifo_get(&b_prot, prot_data)){
			// Daten behandeln
		}
	}
@endcode
 *
 *	@version 	1.01
 *		- Typen aus mytypedef.h entfernt und dafür stdlib.h und stdbool.h Typen eingebunden.
 *		- Das Modul von Ringbuffer in FIFO umbenannt.
 *  @version  	1.00
 *  	- Erste Version
 *
 ******************************************************************************/

#ifndef FIFO_HEADER_FIRST_INCLUDE_GUARD
#define FIFO_HEADER_FIRST_INCLUDE_GUARD

#include "../../mcu/mcu.h"

/**
 * @struct fifo_struct
 * 		Struktur für die FIFO Objekte, die an die Funktionen übergeben werden.
 */
typedef struct{
	uint8_t *data;				/**< Zeiger auf den Puffer, der bei der Initialisierung festgelegt wird. */
	uint16_t element_size;		/**< Größe eines einzelnen Elements im Puffer. */
	uint16_t max_elements;		/**< Anzahl der Elemente, die maximal in den Puffer passen. */
	uint16_t max_len;			/**< Maximale Gesamtgröße des Puffers: element_size * max_elements */
	uint16_t read_pos;			/**< Aktuelle Position von der ausgelesen wird. */
	uint16_t write_pos;			/**< Aktuelle Position an die das nächste Element eingetragen wird. */
	uint16_t entries;			/**< Anzahl der aktuell im Puffer vorhandenen Elemente. */
}fifo_struct;

/**
 * @enum FIFO_RESULT
 * 		Fehlercodes, die bei der Initialisierung des FIFO Moduls zurückgegeben werden.
 */
typedef enum{
	FIFO_OK = 0,						/**< Es ist kein Fehler aufgetreten. */
	FIFO_ELEMENTSIZE_INVALID = 1,		/**< Es wurde eine ungültige Elementgröße übergeben (z.B. Elementgröße 0). */
	FIFO_BUFFERSIZE_INVALID = 2			/**< Die Gesamtgröße des Puffers überschreitet 65536 Byte. */
}FIFO_RESULT;

/**
 * @brief 		Initialisiert die FIFO Struktur und fügt den Puffer ein.
 * 				Ein Beispiel für den Aufruf der Funktion findet sich im Abschnitt Initialisierung.
 *
 * @param bs				Zeiger auf die zu initialisierende FIFO.
 * @param elementsize		Anzahl der Bytes eines Elements.
 * @param buf				Puffer mit einer Gesamtgröße von elementsize * total_elements.
 * @param total_elements	Anzahl der Elements im Puffer.
 * @return					FIFO_OK:					Der Ringbuffer wurde initialisiert.\n
 * 							FIFO_ELEMENTSIZE_INVALID:	Der Puffer hat 0 Elemente haben.\n
 * 							FIFO_BUFFERSIZE_INVALID:	Die Gesamtgröße des Puffers (elementsize * total_elements) ist größer als 65536.
 */
FIFO_RESULT fifo_init(fifo_struct* bs, uint8_t elementsize, void* buf, uint16_t total_elements);
/**
 * @brief 		Diese Funktion leert die FIFO.
 * 				Hinweis: Es werden nur die Zeiger zurückgesetzt und kein Speicher freigegeben!
 *
 * @param bs				Zeiger auf die FIFO, die geleert werden soll.
 */
void fifo_clear(fifo_struct* bs);

/**
 * @brief 		Fügt ein Byte anhand eines Zeigers ein. Hierbei ist zu beachten, dass das Element, auf das der Zeiger zeigt, dieselbe
 * 				Größe hat, die auch in der @link fifo_init Init Funktion@endlink angegeben wurde.
 *
 * @param bs				Zeiger auf die FIFO.
 * @param c					Zeiger auf das einzufügende Element.
 * @return					true:	Das Element wurde hinzugefügt.\n
 * 							false:	Das Element wurde nicht hinzugefügt, da der Puffer voll ist.
 */
bool fifo_put(fifo_struct* bs, uint8_t* c);

/**
 * @brief 		Fügt ein Byte in die FIFO ein.
 *
 * @param bs				Zeiger auf die FIFO.
 * @param c					Das einzufügende 8-Bit Element für den Puffer.
 * @return					true:	Das Element wurde hinzugefügt.\n
 * 							false:	Das Element wurde nicht hinzugefügt, da der Puffer voll ist.
 */
bool fifo_put8(fifo_struct* bs, uint8_t c);

/**
 * @brief 		Fügt 2 Byte in die FIFO ein.
 *
 * @param bs				Zeiger auf die FIFO.
 * @param c					Das einzufügende 16-Bit Element für den Puffer.
 * @return					true:	Das Element wurde hinzugefügt.\n
 * 							false:	Das Element wurde nicht hinzugefügt, da der Puffer voll ist.
 */
bool fifo_put16(fifo_struct* bs, uint16_t c);

/**
 * @brief 		Fügt 4 Byte in die FIFO ein.
 *
 * @param bs				Zeiger auf die FIFO.
 * @param c					Das einzufügende 32-Bit Element für den Puffer.
 * @return					true:	Das Element wurde hinzugefügt.\n
 * 							false:	Das Element wurde nicht hinzugefügt, da der Puffer voll ist.
 */
bool fifo_put32(fifo_struct* bs, uint32_t c);

/**
 * @brief 		Liest ein Element entsprechend der vordefinierten Elementgröße aus und kopiert es an die Stelle des übergebenen Zeigers.
 *
 * @param bs				Zeiger auf die FIFO.
 * @param c					Zeiger auf das einzutragende Element.
 * @return					true:	Element ausgelesen.\n
 * 							false:	Element nicht ausgelesen, da der Puffer leer ist.
 */
bool fifo_get(fifo_struct* bs, uint8_t* c);

/**
 * @brief 		Liest ein einzelnes Byte aus dem Puffer aus und gibt dieses zurück.
 *
 * @param bs				Zeiger auf die FIFO.
 * @return					Das ausgelesene 8-Bit Element.
 */
uint8_t fifo_get8(fifo_struct* bs);

/**
 * @brief 		Liest einen 16 Bit Wert aus dem Puffer aus und gibt dieses zurück.
 *
 * @param bs				Zeiger auf die FIFO.
 * @return					Das ausgelesene 16-Bit Element.
 */
uint16_t fifo_get16(fifo_struct* bs);

/**
 * @brief 		Liest einen 32 Bit Wert aus dem Puffer aus und gibt dieses zurück.
 *
 * @param bs				Zeiger auf die FIFO.
 * @return					Das ausgelesene 32-Bit Element.
 */
uint32_t fifo_get32(fifo_struct* bs);

/**
 * @brief 		Gibt an, wieviele Elemente im Puffer stehen.
 *
 * @param bs				Zeiger auf die FIFO.
 * @return					Anzahl der Elemente im Puffer.
 */
uint16_t fifo_data_available(fifo_struct* bs);

/**
 * @brief 		Gibt an, ob der Puffer voll ist.
 *
 * @param bs				Zeiger auf die FIFO.
 * @return					true: 	Der Puffer ist voll.\n
 * 							false:	Es passen noch Elemente in den Puffer.
 */
bool fifo_is_full(fifo_struct* bs);

/**
 * @brief 		Bildet den Durchschnittswert der Elemente des Puffers. Diese Funktion ist nur für 8-, 16- und 32-Bit Elemente gedacht.
 *
 * @param bs				Zeiger auf die FIFO.
 * @return					Durchschnittswert der FIFO Elemente.
 */
uint32_t fifo_get_average(fifo_struct *bs);

/**
 * @brief 		Gibt den Median der Elemente des Puffers zurück.
 *
 * @param bs				Zeiger auf die FIFO.
 * @return					Median des FIFO Elemente.
 */
uint32_t fifo_get_median(fifo_struct *bs);

#endif
