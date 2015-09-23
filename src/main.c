#include <unistd.h>
#include <stdio.h>
#include <getopt.h>
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
		error("Usage: %s -p /dev/ttyUSB[Number] -t [m(aster) or s(lave)] -n experiment [-f start_period]\n", ProgName);
	}

	while ((option = getopt(argc, argv, "p:t:n:f:")) != -1) {
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
		
	switch (experimentNum) {
		case 1:
			printf("Experiment 1: Broadcast Data Transfer between two nodes\n");
			doExperiment1(deviceType);
		break;

		case 2:
			printf("Experiment 2: Broadcast Data Transfer between multiple nodes\n");
			doExperiment2(deviceType);
		break;

		case 3:
			printf("Experiment 3: Ackowledge Data Transfer between two nodes\n");
			doExperiment3(deviceType);
		break;

		case 4:
			printf("Experiment 4: Ackowledge Data Delay\n");
			doExperiment4(deviceType);
		break;

		case 5:
			printf("Experiment 5: Communication Distance\n");
			doExperiment5(deviceType);
		break;

		case 6:
			printf("Experiment 6: Burst Data Transfer between two nodes\n");
			doExperiment6(deviceType);
		break;

		default:
			printf("Available Experiments:\n");
			printf("Experiment 1: Broadcast Data Transfer between two nodes\n");
			printf("Experiment 2: Broadcast Data Transfer between multiple nodes\n");
			printf("Experiment 3: Ackowledge Data Transfer between two nodes\n");
			printf("Experiment 4: Ackowledge Data Delay\n");
			printf("Experiment 5: Communication Distance\n");
			printf("Experiment 6: Burst Data Transfer between two nodes\n");
			error("%d is not a valid experiment! \n", experimentNum);

		break;
	}
	flushBuffer();
	return EXIT_SUCCESS;
}
