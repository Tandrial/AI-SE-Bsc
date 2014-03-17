/********************************************************
 * RSBS WS 13/14 - Blatt 10 Aufgabe 4                   *
 *                                                      *
 * Author : Michael Krane                               *
 *                                                      *
 * Purpose: rwlock lib header file                      *
 *                                                      * 
 * Usage  : Make lock                                   *
 ********************************************************/


#ifndef _FLOCK_
#define _FLOCK_

#include <pthread.h>
#include <stdio.h>


struct rwlock {		
	// -1 Writer active
	//  0 no activity
	//  x Reader activ
	int activity;
	int writers_waiting;
	pthread_mutex_t plock;	
	pthread_cond_t pcond;
};

// Initialisiert die Read-Write-Lock-Datenstruktur 'lock'.
int rwlock_init(struct rwlock* lock);


// Fordert einen Lese-Lock für 'lock' an.
int rwlock_lockRead(struct rwlock* lock);


// Fordert einen Schreib-Lock für 'lock' an. 
int rwlock_lockWrite(struct rwlock* lock);


// Gibt einen Lock auf 'lock' frei.
int rwlock_unlock(struct rwlock* lock);

#endif /* _FLOCK_ */
