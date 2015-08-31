#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <getopt.h>
#include <stdbool.h>
#include <time.h>
#include <math.h>
#include "ant.h"

#define   DEVICE_NUMBER		33
#define   DEVICE_TYPE		 1
#define   TRANSMISSION_TYPE	 1

#define   ID_CHAN1			 0
#define   FREQ_CHAN1		11
#define   ID_CHAN2			 1
#define   FREQ_CHAN2		22
#define   ID_CHAN3			 2
#define   FREQ_CHAN3		33
#define   ID_CHAN4			 3
#define   FREQ_CHAN4		44

#define   MAX_BUFFSIZE		72

/* CONSTANTS EXPERIMENT 1 */
#define   PING_COUNT		20

typedef enum {
  DS_INCRESING = 0,
  DS_DECREASING,
  DS_DONE
} distanceState_t;

/* CONSTANTS EXPERIMENT 2 */
#define   TRANSFER_TIME_S	10  /* = 1 min */
#define   RUN_COUNT			10
double results[RUN_COUNT];

typedef enum {
  SS_NONE = 0,
  SS_BROADCAST,
  SS_BURST
} sendState_t;

/* CONSTANTS EXPERIMENT 3 */

typedef enum {
  PS_WAITING_FOR_PACKET = 0,
  PS_SENDING_PACKET
} pingState_t;

void clockToMsg(uint8_t *buffer) {
	uint8_t pos = 0;
	clock_t now_t = clock();
	while (now_t > 0 && pos <= 7) {
		buffer[pos] = (now_t >> pos) & 0xFF;
		pos++;
	}
}
char* ProgName; /* error.c */

uint8_t buffer[MAX_BUFFSIZE];
uint32_t s = 0; /* holds the result for received messages */

void initANT(char* ttyUSBDevice) {
	if(antUARTinit(ttyUSBDevice))
		error("Can't initialize ANT port");
	ANT_ResetSystem();
	flushBuffer();
	ANT_delayMs(500);
}

void  setTransmitPower(uint8_t pSetting) {	
	switch (pSetting) {
		case ANT_TRANSMIT_POWER_MINUS_20DBM:
			printf("Power setting is now : -20 dBm\n");
			break;
		case ANT_TRANSMIT_POWER_MINUS_10DBM:
			printf("Power setting is now : -10 dBm\n");
			break;
		case ANT_TRANSMIT_POWER_MINUS_5DBM:
			printf("Power setting is now : -5 dBm\n");
			break;
		case ANT_TRANSMIT_POWER_0DBM:
			printf("Power setting is now : 0 dBm\n");
			break;
		default:
		error("Unkown power setting : %02X!\n", pSetting);
	}
	ANT_SetTransmitPower(pSetting);
}

void openChannel(uint8_t channel, uint8_t freq, bool isMaster) {

	ANT_AssignChannel(channel, isMaster ? ANT_Bidirectional_Master : ANT_Bidirectional_Slave, 0);
	ANT_SetChannelId(channel,
					 DEVICE_NUMBER,
					 isMaster? DEVICE_TYPE : 0,
					 isMaster? TRANSMISSION_TYPE : 0);

	/*ANT_SetChannelRFFreq(channel, freq);*/
	ANT_OpenChannel(channel);
}

void closeANT(uint8_t channel) {
	ANT_CloseChannel(channel);
}

void print_usage() {
	printf("Usage: %s -p /dev/ttyUSB[Number] -t [m(aster) or s(lave)] -n Experiment_number\n", ProgName);
}

void printMsg(uint8_t *buffer) {
	uint8_t i;
	for (i = 0;i < buffer[1] + 4; ++i) {
		printf("[%2X]", buffer[i]);
	}
	printf("\n");
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

	while ( (option = getopt(argc, argv, "cp:t:n:")) != -1) {
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

	if (strncmp(port, "/dev/ttyUSB", 11)) {
		error("Port information wrong!");
	}
	initANT(port);
	setTransmitPower(ANT_TRANSMIT_POWER_MINUS_20DBM);

	printf("Port = %s\nType = %c\nNum = %d\n", port, deviceType, experimentNum);
	switch (experimentNum) {
		case 1:	{							/*	Experiment 1 - Delay Measurement  */
			/* two nodes, in two networks, each one is master in one and slave in the other
			pass msg (8 bytes) back and forth via ANT_SendAcknowledgedData(channel, data)
			msg contains clock_t  used to calculate delay
			*/
			pingState_t pState = PS_SENDING_PACKET;
			if (deviceType == 'm') {
				openChannel(ID_CHAN1, FREQ_CHAN1, true);
				openChannel(ID_CHAN2, FREQ_CHAN2, false);
				 pState = PS_SENDING_PACKET;
			} else {
				openChannel(ID_CHAN1, FREQ_CHAN1, false);
				openChannel(ID_CHAN2, FREQ_CHAN2, true);
				pState = PS_WAITING_FOR_PACKET;
			}
			
			while (1) {
				if (pState == PS_SENDING_PACKET) {
					uint8_t data[8];
					clockToMsg(data);
					ANT_SendAcknowledgedData(deviceType == 'm' ? ID_CHAN1 : ID_CHAN2, data);
				}
				s = ANT_RecvPacket_Blockfree(buffer, MAX_BUFFSIZE);
				if (s == RS_PACKET_COMPLETE) {
					printMsg(buffer);
					if (buffer[5] == ANT_MESSAGE_EVENT_TRANSFER_TX_COMPLETED) {
						printf("ack packet arrived!\n");
						pState = PS_WAITING_FOR_PACKET;
					}
					if (buffer[2] == ANT_ACKNOWLEDGED_DATA) {
						printf("ping packet arrvied!\n");						
						pState = PS_SENDING_PACKET;
					}
				}
			}
				closeANT(ID_CHAN1);
				closeANT(ID_CHAN2);
			break;
			}
		case 3:								/*	Experiment 3 - Communication Distance  */
			if (deviceType == 'm') {
				openChannel(ID_CHAN1, FREQ_CHAN1, true);
				uint8_t data[4] = {0x70, 0x69, 0x6E, 0x67};
				
				ANT_SendBroadcastData(ID_CHAN1, data);
			
				printf("Started broadcast! Press return to exit!\n");
				getchar();
				closeANT(ID_CHAN1);
			} else if (deviceType == 's') {
				int pSetting = ANT_TRANSMIT_POWER_MINUS_20DBM;
				for(; pSetting <= ANT_TRANSMIT_POWER_0DBM; ++pSetting) {
					openChannel(ID_CHAN1, FREQ_CHAN1, false);
					distanceState_t state = DS_INCRESING;
					float distance = 0.0f;
					while (true) {
						uint8_t count = 0, fail = 0, correct = 0;
						while(count < PING_COUNT) {
							s = ANT_RecvPacket_Blockfree(buffer, MAX_BUFFSIZE);
							if (s == RS_PACKET_COMPLETE) {
								printf("Received # %3d: ", count);
								printMsg(buffer);
								switch (buffer[2]) {
									case ANT_CHANNEL_EVENT:
										if (buffer[5] == ANT_MESSAGE_EVENT_RX_FAIL || buffer[5] == ANT_MESSAGE_EVENT_RX_FAIL_GO_TO_SEARCH) {
											fail++;
											count++;
										}
									break;
									case ANT_BROADCAST_DATA:
										count++;
										correct++;
									break;
								}
							}
						}
						if (correct > fail) {
							if (state == DS_INCRESING) {
								printf("Still in range (current %fm)! (%d failed, %d correct)\nIncrease distance by 0.5m and press return!", distance, fail, correct);
								distance += .5f;
							} else {
								printf("Found connection again (current range %fm)!\n", distance);
								state = DS_DONE;
							}
						} else {
							if (state == DS_INCRESING) {
								state = DS_DECREASING;
								printf("Lost connection (current range %fm)! Decrease distance by 0.5m and press return!", distance);
							} else {
								printf("Still no connection (current range %fm! Decrease distance by 0.5m and press return!", distance);
							}
							distance -= .5f;
						}
						if (state == DS_DONE) {
							break;
						} else {
							getchar();
							flushBuffer();
							printf("%s flushed\n", port);
						}
					}
					closeANT(ID_CHAN1);
				}
			}
			break;
		case 4:  /* Experiment 1a: Broadcast Data Transfer between two nodes */
			if (deviceType == 'm') {
				uint16_t period = 65535;
				while (period > 0) {
					printf("period = %d ==> Freq = %f\n", period, 32768.0 / period );
					ANT_SetChannelPeriod(ID_CHAN1, period);
					openChannel(ID_CHAN1, FREQ_CHAN1, true);
					uint8_t data[4] = {0x70, 0x69, 0x6E, 0x67};
					ANT_SendBroadcastData(ID_CHAN1, data);				
					printf("Started broadcast! Press return to exit!\n");
					getchar();
					period = period >> 1;
					closeANT(ID_CHAN1);
				}
			} else {
				uint16_t period = 65535;
				while (period > 0) {
					printf("period = %d ==> Freq = %f\n", period, 32768.0 / period );
					ANT_SetChannelPeriod(ID_CHAN1, period);
					openChannel(ID_CHAN1, FREQ_CHAN1, false);
					uint8_t i;
					double speed_avr = 0.0;
					double total_t = 0.0;
					for (i = 0; i < RUN_COUNT; ++i) {
						uint8_t count = 0;
						struct timespec tstart={0,0}, tend={0,0};
						clock_gettime(CLOCK_MONOTONIC, &tstart);
						while (true) {
							s = ANT_RecvPacket_Blockfree(buffer, MAX_BUFFSIZE);
							if (s == RS_PACKET_COMPLETE) {
								
								if (buffer[2] == ANT_BURST_DATA || buffer[2] == ANT_BROADCAST_DATA) {
									count++;
								}
							}
							clock_gettime(CLOCK_MONOTONIC, &tend);
							total_t = (tend.tv_sec - tstart.tv_sec);
							total_t += (tend.tv_nsec - tstart.tv_nsec) / 1000000000.0;
							if (total_t >= TRANSFER_TIME_S) {
								break;
							}
						}
						results[i] = count * 8 / total_t;
						speed_avr += results[i];
						printf("Run %d: %d bytes received in %f s => %f Bps\n", i, count * 8, total_t, results[i]);
						count = 0;
					}
					speed_avr /= RUN_COUNT;
					double stdDev = 0.0;
					for (i = 0; i < RUN_COUNT; ++i) {
						double cur = results[i] - speed_avr;
						stdDev += cur * cur;
					}
					stdDev /= RUN_COUNT;
					printf("Average datarate : %f \tStd dev: %f\n", speed_avr, sqrt(stdDev));
					getchar();
					period = period >> 1;
					closeANT(ID_CHAN1);
				}
			break;
		case 6:  /* 1x Burst */
			if (deviceType == 'm') {
				openChannel(ID_CHAN1, FREQ_CHAN1, true);
				uint8_t data[4] = {0x70, 0x69, 0x6E, 0x67};
				printf("Master node is Bursting!\n");
				getchar();
				while(true) {
					ANT_SendBurstTransferPacket(ID_CHAN1, data);
				}
				closeANT(ID_CHAN1);
			} else {
				openChannel(ID_CHAN1, FREQ_CHAN1, false);
				uint8_t i;
				double speed_avr = 0.0;
				double total_t = 0.0;
				for (i = 0; i < RUN_COUNT; ++i) {
					uint8_t count = 0;
					struct timespec tstart={0,0}, tend={0,0};
					clock_gettime(CLOCK_MONOTONIC, &tstart);
					while (true) {
						s = ANT_RecvPacket_Blockfree(buffer, MAX_BUFFSIZE);
						if (s == RS_PACKET_COMPLETE) {
							if (buffer[2] == ANT_BURST_DATA || buffer[2] == ANT_BROADCAST_DATA) {
								count++;
							}
						}
						/*now_t = clock(); total_t = ((double) (now_t - start_t)) / CLOCKS_PER_SEC;	*/
						clock_gettime(CLOCK_MONOTONIC, &tend);
						total_t = (tend.tv_sec - tstart.tv_sec);
						total_t += (tend.tv_nsec - tstart.tv_nsec) / 1000000000.0;
						if (total_t >= TRANSFER_TIME_S) {
							break;
						}
					}
					results[i] = count * 8 / total_t;
					speed_avr += results[i];
					printf("Run %d: %d bytes received in %f s => %f Bps\n", i, count * 8, total_t, results[i]);
					count = 0;
				}
				speed_avr /= RUN_COUNT;
				double stdDev = 0.0;
				for (i = 0; i < RUN_COUNT; ++i) {
					double cur = results[i] - speed_avr;
					stdDev += cur * cur;
				}
				stdDev /= RUN_COUNT;
				printf("Average datarate : %f \tStd dev: %f\n", speed_avr, sqrt(stdDev));
				closeANT(ID_CHAN1);
				}
			break;
		case 5:  /* 2x Broadcast */			
		case 7:  /* 1x Burst 1x Broadcast */
		case 8: {/* 2x Burst */
				sendState_t sState = SS_NONE;
			uint8_t data[4] = {0x70, 0x69, 0x6E, 0x67};
			if (deviceType == 'm') {
				openChannel(ID_CHAN1, FREQ_CHAN1, true);
				openChannel(ID_CHAN2, FREQ_CHAN2, false);
				if (experimentNum == 4 || experimentNum == 5) {
					sState = SS_BROADCAST;
					printf("Master node is Broadcasting!\n");
					ANT_SendBroadcastData(ID_CHAN1, data);
				} else {
					sState = SS_BURST;
					printf("Master node is Bursting!\n");
				}
			} else {
				openChannel(ID_CHAN1, FREQ_CHAN1, false);
				openChannel(ID_CHAN2, FREQ_CHAN2, true);
				if (experimentNum == 5 || experimentNum == 7) {
					sState = SS_BROADCAST;					
					printf("Slave node is Broadcasting!\n");
					ANT_SendBroadcastData(ID_CHAN2, data);	
				} else if (experimentNum == 8) {
					sState = SS_BURST;					
					printf("Slave node is Bursting!\n");
				}
			}
			getchar();
			printf("waiting 10s\n");
			ANT_delayMs(10000);			
			printf("started...\n");
			uint8_t i;
			double speed_avr = 0.0;
			double total_t = 0.0;
			for (i = 0; i < RUN_COUNT; ++i) {
				uint8_t count = 0;
				flushBuffer();
				struct timespec tstart={0,0}, tend={0,0};
				clock_gettime(CLOCK_MONOTONIC, &tstart);
				while (true) {
					if (sState == SS_BURST) {
						ANT_SendBurstTransferPacket(deviceType == 'm' ? ID_CHAN1 : ID_CHAN2, data);
					}
					s = ANT_RecvPacket_Blockfree(buffer, MAX_BUFFSIZE);
					if (s == RS_PACKET_COMPLETE) {
						
						if (buffer[2] == ANT_BURST_DATA || buffer[2] == ANT_BROADCAST_DATA) {
							count++;
						}
					}
					/*now_t = clock();
					total_t = ((double) (now_t - start_t)) / CLOCKS_PER_SEC;
					*/
					clock_gettime(CLOCK_MONOTONIC, &tend);
					total_t = (tend.tv_sec - tstart.tv_sec);
					total_t += (tend.tv_nsec - tstart.tv_nsec) / 1000000000.0;
					if (total_t >= TRANSFER_TIME_S) {
						break;
					}
				}
				results[i] = count * 8 / total_t;
				speed_avr += results[i];
				printf("Run %d: %d bytes received in %f s => %f Bps\n", i, count * 8, total_t, results[i]);
				count = 0;
			}
			speed_avr /= RUN_COUNT;
			double stdDev = 0.0;
			for (i = 0; i < RUN_COUNT; ++i) {
				double cur = results[i] - speed_avr;
				stdDev += cur * cur;
			}
			stdDev /= RUN_COUNT;
			printf("Average datarate : %f \tStd dev: %f\n", speed_avr, sqrt(stdDev));
			ANT_delayMs(5000);
			closeANT(ID_CHAN1);
			closeANT(ID_CHAN2);
			}
			break;
		default:
			error("%d is not a valid experiment! \n", experimentNum);
			break;
	}
	return EXIT_SUCCESS;
}
