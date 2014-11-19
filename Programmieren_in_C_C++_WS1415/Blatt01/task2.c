#include <stdio.h>

int main (void){
    int *p, *q;
    
    int single = 8;
    int numbers[10] = {5, 8, 9, 6, 4, 7, 3, 5, 0, 2};
    //                 0  1  2  3  4  5  6  7  8  9
    // p erhält die Adresse von single
    // q erhält die Adresse von numbers[0]
    p = &single;
    q = numbers;

    // A = p dereferenziert = single ==> 8
    // B = q gibt numbers[0] aus ==> 5
    printf("\t A=%d \t B=%d \n\n", *p, *q);

    // C = *q+1 (erst dereferenziert dann +1 ) ==> 6
    // D = *(q+1) (erst +1 dann dereferenziert) ==> numbers[1] ==>8
    printf("\t C=%d \t D=%d \n\n", *q + 1, *(q + 1));

    // p zeigt auf numbers[0]
    p=q;

    // E = p[5] ==> numbers[5] ==> 7
    // F = *(q + 8) + 2 ==> numbers[8] + 2 ==> 0 + 2 ==> 2
    printf("\t E=%d \t F=%d \n\n", p[5], *(q + 8) + 2);

    // G = numbers[1 + numbers[2] - 4] ==> 3 ODER G = numbers[0 + numbers[2] - 4] ==> 7
    // H = numbers[0] ==> 5  (q zeigt jetzt auf numbers[1])
    printf("\t G=%d \t H=%d \n\n", q[*(p + 2) - 4], *q++);
    // Compiler Warnung [mit gcc -Wall -o task2 task2.c]:
    // task2.c:32:55: warning: operation on 'q' may be undefined [-Wsequence-point]
    // 2 Möglichkeiten für G, abhängig von der Auswertungsreihenfolge der Arugmente von printf
    // siehe http://www.open-std.org/jtc1/sc22/WG14/www/docs/n1256.pdf §6.5.2.2p10

    // I = q dereferenziert ==> numbers[1] ==> 8
    // J = p dereferenziert ==> numbers[0] ==> 5
    printf("\t I=%d \t J=%d \n\n", *q, *p);

    // K = numbers[9] ==> 2
    // L = numbers[10] ==> ArrayOutOfBounds undefined behavior 
    // siehe http://www.open-std.org/jtc1/sc22/WG14/www/docs/n1256.pdf §J.2ub49
    printf("\t K=%d \t L=%d \n\n", numbers[9], numbers[10]);

    return 0;
}
