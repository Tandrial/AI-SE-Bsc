#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <getopt.h>
#include <stdbool.h>
#include <sys/select.h>
#include <termios.h>
#include <stropts.h>
#include <time.h>
#include <math.h>
#include "experiment.h"

char* ProgName; /* error.c */
extern uint16_t period;

int main(int argc, char** argv) {
	int option = -1;
	char deviceType = 0;
	int experimentNum = -1;
	char* port = "";

	ProgName = fileName(argv[0]); /* error.c */
	if (argc == 1) {
		error("Usage: %s -p /dev/ttyUSB[Number] -t [m(aster) or s(lave)] -n Experiment_number -f start_chanPeriod\n", ProgName);
	}

	while ( (option = getopt(argc, argv, "p:t:n:f:")) != -1) {
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
			case 'f' :
			    period = atoi(optarg);
			    break;
			default:
				exit(EXIT_FAILURE);
		}
	}

	if (deviceType != 's' && deviceType != 'm') {
		error("Wrong deviceType! Must be 'm' or 's' is: %c\n", deviceType);
	}

	if (strncmp(port, "/dev/ttyUSB", 11)) {
		error("Port information wrong!");
	}

	initANT(port);
	setTransmitPower(ANT_TRANSMIT_POWER_0DBM);

	printf("Port = %s\nType = %c\nNum = %d\n", port, deviceType, experimentNum);
	switch (experimentNum) {
		case 1:
			doExperiment1(deviceType);
		break;

		case 2:
			doExperiment2(deviceType);
		break;

		case 3:
			doExperiment3(deviceType);
		break;

		case 4:
			doExperiment4(deviceType);
		break;

		case 5:
			doExperiment5(deviceType);
		break;

		case 6:
			doExperiment6(deviceType);
		break;

		default:
			error("%d is not a valid experiment! \n", experimentNum);
		break;
	}
	flushBuffer();
	return EXIT_SUCCESS;
}
