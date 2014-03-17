/********************************************************
 * RSBS WS 13/14 - Blatt 10 Aufgabe 4                   *
 *                                                      *
 * Author : Michael Krane                               *
 *                                                      *
 * Purpose: rwlock lib source file                      *
 *                                                      * 
 * Usage  : Make lock                                   *
 ********************************************************/

#include "flock.h"


// Initialisiert die Read-Write-Lock-Datenstruktur 'lock'.
int rwlock_init(struct rwlock* lock) {
  //Mutex intialisieren
	if (pthread_mutex_init(&lock->plock, NULL) < 0) {
		fprintf(stderr, "Error while trying to call mutex_init in thread %u.\n",(unsigned int) pthread_self());
		return -1;
	}
	//conditional Variable intialisieren	
	if (pthread_cond_init(&lock->pcond, NULL) < 0) {
		fprintf(stderr, "Error while trying to call cond_init in thread %u.\n",(unsigned int) pthread_self());
		return -1;
	}	
  //Variable init  
	lock->writers_waiting = 0;	
  lock->activity = 0;

  return 0;
}

// Fordert einen Lese-Lock für 'lock' an.
int rwlock_lockRead(struct rwlock* lock) {
  //START kritischer Bereich ==> Mutex setzen
  if(pthread_mutex_lock(&lock->plock) < 0) {
    fprintf(stderr, "Error while trying to lock mutex in thread %u.\n",(unsigned int) pthread_self());
	return -1;
  }

  //Wenn jemand schreibt oder einer darauf wartet zu schreiben ===> warte...
  while (lock->activity < 0 | lock->writers_waiting != 0)   
  	pthread_cond_wait(&lock->pcond, &lock->plock); 
  
  // 1 Leser mehr
  lock->activity++; 

  //ENDE kritischer Bereicht ==> Mutex weg
  if (pthread_mutex_unlock(&lock->plock) < 0) {
    fprintf(stderr, "Error while trying to unlock mutex in thread %u.\n",(unsigned int) pthread_self());
	return -1;	
  }
  return 0;
}

// Fordert einen Schreib-Lock für 'lock' an. 
int rwlock_lockWrite(struct rwlock* lock) {
  //START kritischer Bereich ==> Mutex setzen
  if(pthread_mutex_lock(&lock->plock) < 0) {
    fprintf(stderr, "Error while trying to lock mutex in thread %u.\n",(unsigned int) pthread_self());
	return -1;
  }
  //
  lock->writers_waiting++;
  
  //Wenn einer schreibt oder >0 lesen ==> warten
  while (lock->activity != 0)  
    pthread_cond_wait(&lock->pcond, &lock->plock);
  
  //Ich warte nicht mehr, sonder schreibe
  lock->writers_waiting--;    
  lock->activity = -1;

  //ENDE kritischer Bereicht ==> Mutex weg
  if (pthread_mutex_unlock(&lock->plock) < 0) {
    fprintf(stderr, "Error while trying to unlock mutex in thread %u.\n",(unsigned int) pthread_self());
	return -1;	
  }
  return 0;
}

// Gibt einen Lock auf 'lock' frei.
int rwlock_unlock(struct rwlock* lock) {
  //START kritischer Bereich ==> Mutex setzen
  if(pthread_mutex_lock(&lock->plock) < 0) {
    fprintf(stderr, "Error while trying to lock mutex in thread %u.\n",(unsigned int) pthread_self());
	return -1;
  }

  if (lock->activity == -1) {
    lock->activity = 0;
    pthread_cond_broadcast(&lock->pcond);    
  } else if (lock->activity > 0) {
    lock->activity--;
    pthread_cond_broadcast(&lock->pcond);    
  }

  //ENDE kritischer Bereicht ==> Mutex weg
  if (pthread_mutex_unlock(&lock->plock) < 0) {
    fprintf(stderr, "Error while trying to unlock mutex in thread %u.\n",(unsigned int) pthread_self());
	return -1;	
  }
  return 0;
}
