/********************************************************
 * RSBS WS 13/14 - Blatt 10 Aufgabe 4                   *
 *                                                      *
 * Author : Michael Krane                               *
 *                                                      *
 * Purpose: Testfall Writer Priorität                   *
 *                                                      * 
 * Usage  : Make testb                                  *
 ********************************************************/

#include "flock.h"
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/types.h>
#include <unistd.h>

struct rwlock filelock;
FILE *fp;

// 3 mal dasselbe für writer1..3
static void *writer1(void *arg) {      
  //Writelock anfordern
  rwlock_lockWrite(&filelock);
  printf("Writer 1 working...\n");
  //Datei öffnen ...
  fp = fopen("testb.txt","a");
  if (fp == NULL) {
    printf("Error trying to open testb.txt\n");
    return NULL;
  }
  //Zahlen schreiben
  int i;
  for(i = 0; i < 100; i++) {
    fprintf(fp,"%d w1\n",i);
    if (i % 20 == 0)
      sleep(1);
  }
  fclose(fp);
  printf("Writer 1 done.\n");
  //Writelock weg
  rwlock_unlock(&filelock);

   return NULL;
}

static void *writer2(void *arg) {    
  rwlock_lockWrite(&filelock);
  printf("Writer 2 working...\n");
  fp = fopen("testb.txt","a");
  if (fp == NULL) {
    printf("Error trying to open testb.txt\n");
    return NULL;
  }

  int i;
  for(i = 100; i < 200; i++) {
    fprintf(fp,"%d w2\n",i);
    if (i % 20 == 0)
      sleep(1);
  }
  fclose(fp);
  printf("Writer 2 done.\n");
  rwlock_unlock(&filelock);
  return NULL;
}

static void *writer3(void *arg) {    
  rwlock_lockWrite(&filelock);
  printf("Writer 3 working...\n");
  fp = fopen("testb.txt","a");
  if (fp == NULL) {
    printf("Error trying to open testb.txt\n");
    return NULL;
  }

  int i;
  for(i = 200; i < 300; i++) {
    fprintf(fp,"%d w3\n",i);
    if (i % 20 == 0)
      sleep(1);
  }
  fclose(fp);
  printf("Writer 3 done.\n");
  rwlock_unlock(&filelock);
  return NULL;
}

static void *reader1(void *arg) {  
  //Leselock anfordern
  rwlock_lockRead(&filelock);
  printf("Reader 1 working...\n");
  //Datei öffnen
  fp = fopen("testb.txt","rt");
  if (fp == NULL) {
    printf("Error trying to open testb.txt\n");
    return NULL;
  }  
  //Datei komplett auslesen
  int c;  
  while ((c = getc(fp)) != EOF) {
    putchar(c);        
  }    
  fclose(fp);
  printf("Reader 1 done.\n");
  rwlock_unlock(&filelock);
  return NULL;
}

int main(void) {
  // 3 Writer schreiben nach einander über eine globale Variable in die selbe Datei
  // 1 Reader liest den Inhalt der Datei aus, erst nachdem alle 3 Writer fertig sind
  rwlock_init(&filelock); 
  remove("testb.txt");
  pthread_t w1, w2, w3, r1;
  printf("Writer 1 started.\n");
  pthread_create(&w1, NULL, writer1, NULL);  
  sleep(1);
  printf("Reader 1 started.\n");
  pthread_create(&r1, NULL, reader1, NULL); 
  sleep(1); 
  printf("Writer 2 started.\n");
  pthread_create(&w2, NULL, writer2, NULL);   
  printf("Writer 3 started.\n");
  pthread_create(&w3, NULL, writer3, NULL);   

  pthread_join(w1, NULL);
  pthread_join(w2, NULL);
  pthread_join(r1, NULL);

  return 0;
}
