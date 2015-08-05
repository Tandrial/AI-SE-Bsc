#ifndef ERROR_H

#include "error.h"

int debugLevel = 0;

char name[MAX_NAME_SIZE];

/* returns a pointer to the last piece of the given
   string that does not contain '.' or '/' or '\'.
*/
char* fileName(char *n){
  char *begin;
  char *p,*q;

  begin = n;
  p = begin;

/* limit cases:

  1) ... , {\,/,.} , \0           -> keep only \0

  2) begin -> {\,/,.} , ... , \0  -> trash *begin

  3) begin -> {\,/,.} , \0        -> keep only \0

*/

  while( *p != '\0') p++; /* go to the end. */

  /* Here 'p' points to the end of the string. */

  do{               /* search for characters to
                       'remove'. */
    q = p - 1;
    if( *q == '.' ) break;
    if( *q == '/' ) break;
    if( *q == '\\' ) break;
    p--;
  }
  while( p > begin);

  return p;
}

void error(char *msg, ...) {
  va_list parms;
  va_start(parms, msg);

  fprintf(stderr, "\nError: ");
  vfprintf(stderr, msg, parms);
  fprintf(stderr, ".\n\n");

  va_end(parms);

  exit(-1);
}

void Dprintf(int level, char *msg, ...) {
  va_list parms;
  va_start(parms, msg);

  if(debugLevel >= level){

    fprintf(stderr, "DEBUG[%d]: ", level);
    vfprintf(stderr, msg, parms);
  }

  va_end(parms);
}

void setDebugLevel(int v){
  debugLevel = v;
}


/* antique assignment code */
extern char *ProgName;

/***************************************************************************
  er_header: rend un pointeur sur une chaine qui contient le nom du pgm
  (ProgName) suivi de ": " puis de la chaine s passée en paramètre.
  Si la taille du buffer prévu pour construire la chaine d'entete est trop
  petit, la chaine rendue est "?". La taille du buffer peut etre adaptée
  en modifiant ERROR_HEADER_LEN dans error.h.
***************************************************************************/
char *er_header(char *s){
  static char sError[ERROR_HEADER_LEN];
  if (!((strlen(ProgName) + strlen(s) + 3) > ERROR_HEADER_LEN)){
    strcpy(sError,ProgName);
    strcat(sError,": ");
    strcat(sError,s);
  }
  else
    strcpy(sError,"?");
  return sError;
}
#endif
