#include <stdio.h>
#include <stdlib.h>

/*
 matrix ist ein Pointer auf einen Pointer vom Typ int. 
 Um damit eine 2D-Matrix darstellen zukönnen, wird für den äußeren Pointer
 genug Speicher allokiert um für jede Zeile ein Pointer auf ein int-Array
 speichern zu könnne. Dem innere Pointer wird genügend Speicher für col-viele
 ints zugewiesen. Zusätzlich muss noch die Größe der Matrix gespeichert werden, für
 die Anzahl der Zeilen wird matrix[0][0] genutzt. Damit die Adressierung konsistent
 bleibt (das linke obere Element ist matrix[1][1] und NICHT matrix[1][0]),
 wird die Anzahl der Spalten in jedem Zeilen Array an der 1. Stelle gespeichert.

                    ------------
                --->| SIZE_ROW |
                |   ------------
                | 
           ----------------------------------
 matrix--->| *int[] | *int[] | *int[] | ... |
           ----------------------------------
                        |
                        |    ------------------------------
                        ---->| SIZE_COL | int | int | ... |
                             ------------------------------

 Der äußere Pointer zeigt also auf eine ganze Zeile und der innere Pointer auf einen int in
 der gegebenen Zeile.
*/

// Loop-Variablen
int i, j;

// Forward-Declaration, damit die Funktion in createMatrix genutzt werden kann
void deleteMatrix(int **);

int ** createMatrix(int row, int col) {
    // m soll ein 2D-Array vom Typ int sein
    int **m;
    // Speicher wird in 3 Schritten zugewiesen
    // 1. Speicher für das Array mit den int-Array-Pointer
    if ((m = (int**) malloc((row + 1) * sizeof(int*))) == NULL)
        return NULL;
    // 2. Speicher für das Arry, dass die Anzahl der Zeilen enthält
    if ((m[0] = (int*) malloc(sizeof(int))) == NULL) {  
        // Falls die Allokierung fehlschlägt wird der zugewiesener Speicher für m
        // freigegeben
        free(m);
        return NULL;
    }
    // Anzahl Zeile speichern
    m[0][0] = row;

    // 3. Speicher für die einzelnde int-Arrays
    for(i = 1; i < row + 1; i++) {
        if ((m[i] = (int*) malloc((col + 1) * sizeof(int))) == NULL) {
            // Falls die Allokierung fehlschlägt wird aller zugewiesender Speicher
            // wieder freigegeben
            deleteMatrix(m);
            return NULL;
        }
        // Anzahl Spalten speichern
        m[i][0] = col;
    }
    // Matrix ist fertig und wird zurück gegeben
    return m;
}

void deleteMatrix(int **matrix) {
    // Check of die Matrix existiert
    if (matrix == NULL)
        return;
    // Anzahl der Zeilen 
    int row_count = matrix[0][0];
    // Zuerst die "inneren" int-Arrays löschen
    for (i = 0; i <= row_count; i++)
        // Check ob Speicher allokiert wurde
        if (matrix[i] != 0)
            free(matrix[i]);
    // Dann das Array mit den int-Array-Pointer
    free(matrix);
    // Pointer invalidieren
    matrix = NULL;
}

void fillMatrix(int **matrix) {
    // Check of die Matrix existiert
    if (matrix == NULL)
        return;    
    // Das obere linke Element der Matrix ist in matrix[1][1] gespeichert.
    // In matrix[0][0] ist die Anzahl der Zeilen und in matrix[i | i > 0][0]
    // die Anzahl der Spalten gespeichert.  
    for(i = 1; i <= matrix[0][0]; i++)
        for(j = 1; j <= matrix[i][0]; j++)
            // Der Wert des Elements ist die Summe von Zeilen- und Spaltenindex
            // Das heißt der Wert von matrix[1][1] ist 0
            matrix[i][j] = (i - 1) + (j - 1);
}

// Druckt die ganze Matrix so formatiert aus, dass jede Zelle
// mindestens 3 Plätze belegt.
void printMatrix(int **matrix) {
    // Check of die Matrix existiert
    if (matrix == NULL)
        return; 
    // Adressierung genau wie in fillMatrix()
    for(i = 1; i <= matrix[0][0]; i++) {
        for(j = 1; j <= matrix[i][0]; j++) {
            printf("%3d ",matrix[i][j]);
        }
        printf("\n");
    }
}

int main(int argc, char** argv) {
    // Abbruch wenn nicht passende Parameter übergeben werden
    if (argc != 3) {
        printf("Unbekannte Eingabe! Erwartet: %s <row> <col>\n", argv[0]);
        exit(1);
    }

    // Einlesen der Argumente
    int r = atoi(argv[1]);
    int c = atoi(argv[2]);

    // Check ob die Parameter gültig (größer als 0) sind
    if (r <= 0 || c <= 0 ) {
        printf("Die Anzahl der Spalten und Zeilen muss größer als 0 sein.\n");
        exit(1);
    }

    int **m = createMatrix(r,c);
    fillMatrix(m);
    printMatrix(m);
    deleteMatrix(m);
    m = NULL;        
    return 0;
}
