#ifndef _SERIAL_H
#define _SERIAL_H

#include <fcntl.h>
#include <termio.h>
#include <ctype.h>
#include "error.h"

int  setBaud(int sfd, int baud);
void setRTS(int sfd, int v);
void setDTR(int sfd, int v);
int getCTS(int sfd);
int readSerial(int fs, char *buffer, int count);
int writeSerial(int fd, char *buffer, int count);
int readSerialChar(int fd, char *buffer);
int gotSerialChar(int sfd);

#endif
