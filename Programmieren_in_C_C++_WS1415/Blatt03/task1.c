#include <stdio.h>

// Durch eine unendliche Rekursion werden solange ints auf dem Stack
// erzeugt bis der Stack voll ist.
void smashTheStack(int i) {
    printf("Iter: %4d,   0/500 neue Int\n", i);
    // 2kb auf dem Stack erstellen ==> 500 ints
    // In zwei Schritten um die Größe besser abschätzen zukönnen
    int foo;
    printf("Iter: %4d,   1/500 Int@ %p\n", i, &foo);
    int bar[499];
    // Ausgabe von iter und Adresse von foo[0]
    printf("Iter: %4d, 500/500 Int@ %p\n", i, &bar[0]);
    // Aufruf der nächste Runde
    smashTheStack(++i);
}

int main(void) {
    // Rekursions-Start mit iter = 1
    smashTheStack(1);
    return 0;
}