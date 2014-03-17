/********************************************************
 * RSBS WS 13/14 - Blatt 09 Aufgabe 4                   *
 *                                                      *
 * Author : Michael Krane                               *
 *                                                      *
 * Purpose: Analyse des Einflusses von Nice-Values auf  *
 *          die Laufzeit von Prozessen                  *
 *                                                      *
 * Usage  : A4 maxCount AnzahlChildren [NiceChild]      *
 *                                                      *
 ********************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/resource.h>
#include <string.h>
#include <time.h>

//Child Prozess
void DoWork(int num, long max) {
   struct timespec start, ende;
   float dauer;
   long i;

   while(1) {
      /* Start der Zeitmessung */
      clock_gettime(CLOCK_MONOTONIC, &start);
      /* Hier wird hochgez√§hlt */
      for(i = 0; i < max; i++) { }
      /* Ende Zeitmessung  */
      clock_gettime(CLOCK_MONOTONIC, &ende);
      /* Dauer wird berechnet */ 
      dauer = (ende.tv_sec - start.tv_sec) + ((ende.tv_nsec - start.tv_nsec) / 1000000000.0);

      printf("Child process %d - Time : %f seconds \n", num, dauer);
   }
}

int main (int argc, char *argv[]) {
   /* Error Checks f√ºr die Anzahl der Parameter */
   
   /* zu wenig Parameter */
   if (argc < 3) {
      fprintf(stderr, "Usage: %s count_max count_children [ nice_childn]\n", argv[0]);
      return 1;
   }

   /* stroul und atoi wandeln Strings in long bzw. int Wert um */
   int child_count = atoi(argv[2]);
   long count_max = strtoul(argv[1], NULL, 0);

   /* zuwenig nice Values f¸r alle Child-Prozesse */
   if (argc - 3 < child_count) {
      fprintf(stderr, "%d Nice-Values gefunden, es werden aber %d gebraucht. Setze Rest auf 0.\n",
         (argc - 3), atoi(argv[2]));
   }

   pid_t pids[child_count];

   int i;      
   /* Hier werden die Child-Prozesse geforkt und deren pids gespeichert */
   for (i = 0; i < child_count; i++) {
      if ((pids[i] = fork()) < 0) {
         fprintf(stderr, "Error creating child process\n");
         return 1;
      } else if (pids[i] == 0) { /* Child-Prozess */
         DoWork(i, count_max);
         return 0;
      }
   }

   /* Nachdem alle Child-Prozesse erzeugt wurden, werden die Nice-Values gesetzt */
   for(i = 0; i < child_count; i++) {
      /* F¸r den Child-Prozess i gibt es keine Nice-Value also = 0 */
      if (i >= (argc - 3)) {
         if (setpriority(PRIO_PROCESS, pids[i], 0) < 0) {
            fprintf(stderr,"Error creating child process\n");
            return 1;
         }
         else
            printf("Child %d (pid = %d) hat die Nice-Value 0 \n", i, pids[i]);
      } else {
         /* Es gibt einen Nice-Value f¸r den Child-Prozess */
         if (setpriority(PRIO_PROCESS, pids[i], atoi(argv[i + 3])) < 0) {
            fprintf(stderr,"Error creating child process\n");
            return 1;
         } else 
            printf("Child %d (pid = %d) hat die Nice-Value %d \n", i, pids[i], atoi(argv[i + 3]));
      }
   }

  /* Parent-Prozess l√§uft solange bis alle Child-Prozesse terminiert sind */
   int status;
   pid_t pid;
   while (child_count > 0) {
      /* wait wartet darauf das ein Child-Prozess terminiert und weiﬂt pid dann die pid des 
       * terminierten Prozesses zu. Status enth√§lt Informationen ¸ber den Grund. */
      pid = wait(&status);
      printf("Child mit PID %d hat mit Status 0x%x terminiert.\n", (int)pid, status);
      --child_count;
   }
   return 0;
}
