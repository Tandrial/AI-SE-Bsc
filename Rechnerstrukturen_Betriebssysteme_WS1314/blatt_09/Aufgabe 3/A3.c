/********************************************************
 * RSBS WS 13/14 - Blatt 09 Aufgabe 3                   *
 *                                                      *
 * Author : Michael Krane 2233018                       *
 *                                                      *
 * Purpose: Analyse der ELF-Datenstruktur               *
 *                                                      *
 * Usage  : A3                                          *
 *                                                      *
 ********************************************************/

#include <stdio.h>
#include <stdlib.h>

int global_nicht_init;
int global_gleich_zero = 0;
int global_ungleich_zero = 1;

const int const_ungleich_zero = 2;

int main (int argc, char *argv[]) {

  // &var liefert einen Pointer der auf die Adresse im Speicher zeigt an dem var gespeichert ist
  // %p formatiert einen Pointer im hex-Format

   printf("#### Globale Variablen#####\n");

   // Hier werden die Adresse der globalen Variablen nacheinander ausgegeben
   printf("\t Globale Variable ohne Initialisierungswert:\t\t %p \n", &global_nicht_init);
   printf("\t Globale Variable mit 0 initialisiert:\t\t\t %p \n", &global_gleich_zero);
   printf("\t Globale Variable mit Initialisierungswert ungleich 0:\t %p \n", &global_ungleich_zero);
   printf("\t Globale Datenkonstante:\t\t\t\t %p \n", &const_ungleich_zero);

   printf("#### Heap Variablen ####\n");
   //Eine Heap Variable wird mittels malloc erzeugt
   
   // Pointer der auf den "reservierten" Speicher zeigt
   int *heap_int;
   // malloc reserviert genug Speicher um einen int zuspeichern
   heap_int = (int *) malloc(sizeof(int));
   // Variable wird auf den Wert 25 gesetzt
   *heap_int = 25;

   printf("\t Heap Variable = %d : \t\t\t %p \n", *heap_int, heap_int);
   // free() gibt den benutzten Speicher wieder frei
   free(heap_int);

   printf("#### Stack Variablen ####\n");
   //Variablen die nicht über malloc erzeugt werden, werden au dem Stack erzeugt
   int first_int_stack = 42;
   int second_int_stack = 43;
   int third_int_stack = 44;
	
   printf("\t Erster int auf dem Stack:\t\t %p \n", &first_int_stack);
   printf("\t Zweiter int auf dem Stack:\t\t %p \n", &second_int_stack);
   printf("\t Dritter int auf dem Stack:\t\t %p \n", &third_int_stack);

   return 0;
}
