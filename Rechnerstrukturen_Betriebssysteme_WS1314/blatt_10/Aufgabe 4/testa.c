/********************************************************
 * RSBS WS 13/14 - Blatt 10 Aufgabe 4                   *
 *                                                      *
 * Author : Michael Krane                               *
 *                                                      *
 * Purpose: Testfall Leselock > 1 möglich               *
 *                                                      * 
 * Usage  : Make testa                                  *
 ********************************************************/

#include "flock.h"
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/types.h>
#include <unistd.h>

struct rwlock varlock;
int count;

static void *writer(void *arg) {      
  int c;
  // 2 Runden
  for(c = 0;c <2; c++){
    // Writelock aktiv
    rwlock_lockWrite(&varlock);
    printf("Writer incrementing...\n");  
    //hochzählen
    int i;
    for(i = 0; i < 100; i++) {
      count++;          
    }
    printf("Writer waiting...\n");
    //Writelock weg
    rwlock_unlock(&varlock);
    sleep(1);
  }
  printf("Writer done.\n");
   return NULL;
}

static void *reader(void *arg) {      
  // 2 Runden
  int c;
  for(c = 0;c <2; c++){
    // Lese lock anfordern
    rwlock_lockRead(&varlock);
    printf("Reader printing...%u.\n",(unsigned int) pthread_self());
    int i;
    // Runde Nr., i und die Threadid ausgeben und zwischendurch warten
    for(i = 1; i <= 20 ; i++) {
      printf("c = %d i = %d count = %d  \t %u.\n", c, i, count,(unsigned int) pthread_self());
      if (i % 50) 
        sleep(rand() % 2);
    }    
    //Readlock weg
    rwlock_unlock(&varlock);
    printf("Reader waiting...%u\n",(unsigned int) pthread_self());
    sleep(1);
  }
  printf("Reader done..%u\n",(unsigned int) pthread_self());
   return NULL;
}

int main(void) {
  rwlock_init(&varlock);
  count = 0;
  // 1 Writer erhöht eine globale Variable von 0 auf 199 in 2 Schritten
  // 3 Reader lesen die selbe globale Variable nach 100 Schritten aus
  pthread_t w1, r1, r2, r3;
  printf("Writer 1 started.\n");
  pthread_create(&w1, NULL, writer, NULL);    
  printf("Reader 1 started.\n");
  pthread_create(&r1, NULL, reader, NULL);   
  printf("Reader 2 started.\n");
  pthread_create(&r2, NULL, reader, NULL);   
  printf("Reader 3 started.\n");
  pthread_create(&r3, NULL, reader, NULL);   
  pthread_join(w1, NULL);
  pthread_join(r1, NULL);
  pthread_join(r2, NULL);
  pthread_join(r3, NULL);
  return 0;
}
