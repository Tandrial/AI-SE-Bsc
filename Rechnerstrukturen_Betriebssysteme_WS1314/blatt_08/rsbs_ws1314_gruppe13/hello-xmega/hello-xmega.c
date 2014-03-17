#include <avr/io.h>
int main() {
//Aktiviert LED0, LED2, LED4, LED6 und
// deaktiviert LED1, LED3, LED5, LED7

//Konfiguriert Block E

// 0 = Eingang , 1 = Ausgang
// Alle 8 auf Ausgang
	PORTE.DIR = 0xff;

	//Setzt Ausgänge als Block
	//0xaa =0  1  0  1  0  1  0  1
	//		L7 L6 L5 L4 L3 L2 L1 L0
	//PORTE.OUT = 0xaa

	PORTE.OUT = 1 << PIN0_bp | 1 << PIN2_bp | 1 << PIN4_bp | 1 << PIN6_bp;//1 << 6 | 1 << 4 | 1 << 2 | 1 << 0;

}
