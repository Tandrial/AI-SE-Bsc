#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <getopt.h>
#include <stdbool.h>
#include "ant.h"

#define   EXPERIMENT_COUNT    1

#define   ANT_CH              0
#define   DEVICE_NUMBER      31
#define   DEVICE_TYPE         1
#define   TRANSMISSION_TYPE   1

#define   MAX_BUFFSIZE       32

char* ProgName; /* error.c */

void OpenANT(char* ttyUSBDevice, bool isMaster) {
	if(antUARTinit(ttyUSBDevice)) 
		error("Can't initialize ANT port");
	ANT_ResetSystem();

	usleep(500);

	if (isMaster) {
		ANT_AssignChannel(ANT_CH, ANT_Bidirectional_Master, 0);

		ANT_SetChannelId(ANT_CH,
						 DEVICE_NUMBER,
						 DEVICE_TYPE,
						 TRANSMISSION_TYPE);
	} else {
		ANT_AssignChannel(ANT_CH, ANT_Bidirectional_Slave, 0);

		ANT_SetChannelId(ANT_CH,
						 DEVICE_NUMBER,
						 0,
						 0);
	}
	
	ANT_OpenChannel(ANT_CH);

	return;
}

void closeANT() {
	ANT_CloseChannel(ANT_CH);
	return;
}

void print_usage() {
	printf("Usage: %s -p /dev/ttyUSB[Number] -t [m(aster) or s(lave)] -n Experiment_number\n", ProgName);
}

int main(int argc, char** argv) {

	int option = -1;
	char deviceType = 0; 
	int experimentNum = -1;
	char* port = "";

	ProgName = fileName(argv[0]); /* error.c */
	if (argc == 1) {
		print_usage();
		exit(EXIT_FAILURE);
	}

	while ( (option = getopt(argc, argv, "p:t:n:")) != -1) {
		switch (option) {
			case 'p' :
				port = optarg;
				break;
			case 't' :
				deviceType = optarg[0];
				break;
			case 'n' :
				experimentNum = atoi(optarg);
				break;
			default:
				exit(EXIT_FAILURE);
		}
	}

	if (deviceType != 's' && deviceType != 'm') {
		error("Wrong deviceType! Must be 'm' or 's' is: %c\n", deviceType);		
	}

	if (experimentNum < 0 || experimentNum > EXPERIMENT_COUNT) {
		error("Wrong experimentNumber! Must be 0<x<=%d is: %d\n", EXPERIMENT_COUNT, experimentNum);
	}

	if (strncmp(port, "/dev/ttyUSB", 10)) {
		error("Port information wrong!");
	}

	printf("ANT IO Port = %s\nDeviceType = %c\nExperimentNumber = %d\n", port, deviceType, experimentNum);

	switch (experimentNum) {
		case 1:
			/*	Experiment 1 - Communication Distance  */
			if (deviceType == 'm') {
				OpenANT(port, true);
				uint8_t data[4] = {0x70, 0x69, 0x6E, 0x67};
				ANT_SendBroadcastData(ANT_CH, data);				
			} else if (deviceType == 's') {
				OpenANT(port, false);
				unsigned char buffer[MAX_BUFFSIZE];
				int s = 0;
				while (1) {
					/*TODO:
						wait for 10 msgs,
						if 0 error ==> print "Increase distance by 0.5m    ( distance was: x m;  y errors)"
						else           print "max communication range reached. Decrease by 0.5m  (distance was x m; y errors)"

						repeat untill 0 errors
					*/
					if ((s = BC_ANT_RecvPacket(buffer, MAX_BUFFSIZE)) != 0) {
						printf("%s\n", buffer);
					}
				}	
			}
			
			break;
		default:
			break;
	}
	return EXIT_SUCCESS;
}
