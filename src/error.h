#ifndef ERROR_H
#define ERROR_H

#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
#include <string.h>

#define MAX_NAME_SIZE 100

char* fileName(char*);
void error(char *msg, ...);
void Dprintf(int level, char *msg, ...);
void setDebugLevel(int v);


/* ancient code from "1ere licence" assignments */
#define ERROR_HEADER_LEN 100
char *er_header(char*);

#endif
