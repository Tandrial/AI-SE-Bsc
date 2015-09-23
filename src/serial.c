#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <termio.h>
#include <linux/serial.h>
#include <unistd.h>
#include "serial.h"

char verbose = 0;

int setBaud(int sfd, int baud) {
	struct termios tp;

	if (sfd == -1) 
		return 0;
	if (tcgetattr(sfd, &tp) < 0) 
		return 1;
	
	tp.c_cflag = B0 | CS8 | CLOCAL | CREAD | CSTOPB;
	tp.c_oflag = 0;

	tp.c_iflag = 0;
	tp.c_lflag = 0;
	cfsetspeed (&tp, baud);
	if (tcsetattr(sfd, TCSANOW, &tp) < 0) 
		return 1;	
	return 0;
}

void setRTS(int sfd, int v) {
	int arg;

	ioctl(sfd, TIOCMGET, &arg);

	if (v) {
		arg |= TIOCM_RTS;
	} else {
		arg &= ~(TIOCM_RTS);
	}
	ioctl(sfd, TIOCMSET, &arg);
}

void setDTR(int sfd, int v) {
	int arg;

	ioctl(sfd, TIOCMGET, &arg);

	if (v) {
		arg |= TIOCM_DTR;
	} else {
		arg &= ~(TIOCM_DTR);
	}
	ioctl(sfd, TIOCMSET, &arg);
}

int readSerialChar(int fd, char *buffer) {
	while(readSerial( fd, buffer, 1) != 1);
	return 1;
}

int readSerial(int fd, char *buffer, int count) {
	int bytes=0;
	int total=0;
	fd_set rfds;
	struct timeval t;

	if (fd == -1)
		return 0;
	if (buffer == NULL) 
		return -1;

	do {
		FD_ZERO(&rfds);
		FD_SET(fd, &rfds);

		t.tv_usec = 0;
		t.tv_sec = 0;

		select(fd + 1, &rfds, NULL, NULL, &t);

		if(FD_ISSET(fd, &rfds)) {
			bytes = read(fd, buffer + total, count - total);
			total += bytes;
		}
	 } while(total != count);
	return(total);
}

int writeSerial(int fd, char *buffer, int count) {
	int bytes;
	bytes = write(fd, buffer, count);

	if (bytes != count) 
		return -1;
	return count;
}

int  getCTS(int sfd) {
	int status;

	if (ioctl(sfd, TIOCMGET, &status) == -1)
		error("TIOCMGET failed");

	return (!(status & TIOCM_CTS));
}

int  gotSerialChar(int sfd) {
	int bytes_available;
	ioctl(sfd, FIONREAD, &bytes_available);
	return bytes_available;
}
