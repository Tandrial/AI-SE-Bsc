#include <stdio.h>
#include <stdlib.h>
#include <sys/select.h>
#include <termios.h>
#include <time.h>
#include <math.h>
#include "experiment.h"

uint8_t buffer[BC_ANT_BUFFSIZE];
double results[RUN_COUNT];
uint16_t period = START_FREQ;
uint32_t s = 0; /* holds the result for received messages */

uint8_t data[4] = {0x70, 0x69, 0x6E, 0x67};

void initANT(char* ttyUSBDevice) {
	if(antUARTinit(ttyUSBDevice))
		error("Can't initialize ANT port");
	ANT_ResetSystem();
	flushBuffer();
	ANT_delayMs(500);
}

void openChannel(uint8_t channel, uint8_t freq, uint16_t period, bool isMaster) {
	ANT_delayMs(100);
	ANT_AssignChannel(channel, isMaster ? ANT_Bidirectional_Master : ANT_Bidirectional_Slave, 0);
	ANT_delayMs(100);
	ANT_SetChannelId(channel,
					 DEVICE_NUMBER,
					 isMaster? DEVICE_TYPE : 0,
					 isMaster? TRANSMISSION_TYPE : 0);
	ANT_delayMs(100);
	ANT_SetChannelRFFreq(channel, freq);
	ANT_delayMs(100);
	if (period != STD_FREQ)
		ANT_SetChannelPeriod(channel, period);
	ANT_delayMs(100);
	ANT_OpenChannel(channel);
}

void closeANT(uint8_t channel) {
	ANT_CloseChannel(channel);
}

void setTransmitPower(uint8_t pSetting) {
	switch (pSetting) {
		case ANT_TRANSMIT_POWER_MINUS_20DBM:
			printf("Power setting is now: -20dBm\n");
			break;
		case ANT_TRANSMIT_POWER_MINUS_10DBM:
			printf("Power setting is now: -10dBm\n");
			break;
		case ANT_TRANSMIT_POWER_MINUS_5DBM:
			printf("Power setting is now: -5dBm\n");
			break;
		case ANT_TRANSMIT_POWER_0DBM:
			printf("Power setting is now: 0dBm\n");
			break;
		default:
		error("Unkown power setting : %02X!\n", pSetting);
	}
	ANT_SetTransmitPower(pSetting);
}

uint16_t decreasePeriod(uint16_t period, uint16_t stopShiftingAt) {
	uint16_t result = END_FREQ;
	if (period >= stopShiftingAt) {
		result =  period >> 1;
	} else if ((period - 10) > END_FREQ) {
		result = period - 10;
	}
	printf("Message Period = %d (%f Hz)\n", result, 32768.0 / result );
	return result;
}

void printBuffer(uint8_t *buffer) {
	uint8_t i;
	for (i = 0;i < buffer[1] + 4; ++i) {
		printf("[%2X]", buffer[i]);
	}
	printf("\n");
}

void printAndWait(uint8_t msgType, uint8_t channel) {
	printf("Press any key to start the Experiment!\n");
	bool ready = false;
	while (!kbhit()) {
		s = ANT_RecvPacket_Blockfree(buffer, BC_ANT_BUFFSIZE);
		if (s == RS_PACKET_COMPLETE) {
			if (!ready) {				
				switch(msgType) {
					case ANT_BROADCAST_DATA:
						if (buffer[2] == msgType && buffer[3] == channel) {
							printf("pkg (%2X) on Chan %d found! Can start!\n", buffer[2], buffer[3]);
							printBuffer(buffer);
							ready = true;
						}
					break;
					case ANT_ACKNOWLEDGED_DATA:
						if (buffer[5] == ANT_MESSAGE_EVENT_TRANSFER_TX_COMPLETED && buffer[3] == channel) {
							printf("pkg (%2X) on Chan %d found! Can start!\n", buffer[2], buffer[3]);
							printBuffer(buffer);
							ready = true;
						}
					break;
					default:
						printBuffer(buffer);
					break;
				}
			}
		}
	}
}


int kbhit(void) {
  struct termios oldt, newt;
  int ch;
  int oldf;

  tcgetattr(STDIN_FILENO, &oldt);
  newt = oldt;
  newt.c_lflag &= ~(ICANON | ECHO);
  tcsetattr(STDIN_FILENO, TCSANOW, &newt);
  oldf = fcntl(STDIN_FILENO, F_GETFL, 0);
  fcntl(STDIN_FILENO, F_SETFL, oldf | O_NONBLOCK);

  ch = getchar();

  tcsetattr(STDIN_FILENO, TCSANOW, &oldt);
  fcntl(STDIN_FILENO, F_SETFL, oldf);

  if(ch != EOF) {
    ungetc(ch, stdin);
    int c;
    while ((c = getchar()) != '\n' && c != EOF);
    return 1;
  }
  return 0;
}

void doExperiment(uint8_t msgType, uint8_t channel, exprHandler fn) {
	double speed_avr = 0.0;
	uint8_t i;
	printAndWait(msgType, channel);
	for (i = 0; i < RUN_COUNT; ++i) {
		flushBuffer();
		expriment_t result = fn(msgType);
		results[i] = result.sucess * 8 / result.duration;
		speed_avr += results[i];
		printf("Run %d: failed %d sucess %d: %d bytes received in %f s => %f\n", i, result.fail, result.sucess, result.sucess * 8, result.duration, results[i]);
	}
	speed_avr /= RUN_COUNT;
	double stdDev = 0.0;
	for (i = 0; i < RUN_COUNT; ++i) {
		double cur = results[i] - speed_avr;
		stdDev += cur * cur;
	}
	stdDev /= RUN_COUNT;
	printf("Average datarate : %f \tStd dev: %f\n", speed_avr, sqrt(stdDev));
}

expriment_t receiveMessageAndCount(uint8_t msgType) {
	uint16_t count = 0, fail = 0;
	double total_t = 0.0;
	struct timespec tstart = {0,0}, tend = {0,0};
	clock_gettime(CLOCK_MONOTONIC, &tstart);
	while (total_t < TRANSFER_TIME_S) {
		s = ANT_RecvPacket_Blockfree(buffer, BC_ANT_BUFFSIZE);
		if (s == RS_PACKET_COMPLETE) {
			if (buffer[2] == msgType) {
				count++;
			} else if (buffer[5] == ANT_MESSAGE_EVENT_RX_FAIL) {
				fail++;
			}
		}
		clock_gettime(CLOCK_MONOTONIC, &tend);
		total_t = (tend.tv_sec - tstart.tv_sec);
		total_t += (tend.tv_nsec - tstart.tv_nsec) / 1000000000.0;
	}
	expriment_t result =  {count, fail, total_t};
	return result;
}

expriment_t receiveMessageAck(uint8_t msgType) {
	uint16_t count = 0, fail = 0;
	DelayMeasureState_t state = DM_SENDING_PACKET;	
	ANT_SendAcknowledgedData(ID_CHAN1, data);
	double total_t = 0.0;
	struct timespec tstart = {0,0}, tend = {0,0};
	clock_gettime(CLOCK_MONOTONIC, &tstart);	
	while (total_t < TRANSFER_TIME_S) {
		if (state == DM_SENDING_PACKET) {
			ANT_SendAcknowledgedData(ID_CHAN1, data);
			state = DM_WAITING_FOR_ACK;
		}
		s = ANT_RecvPacket_Blockfree(buffer, BC_ANT_BUFFSIZE);
		if (s == RS_PACKET_COMPLETE) {
			if (buffer[5] == ANT_MESSAGE_EVENT_TRANSFER_TX_COMPLETED) {
				count++;
				state = DM_SENDING_PACKET;
			} else if (buffer[5] == ANT_MESSAGE_EVENT_TRANSFER_TX_FAILED) {
				fail++;
				state = DM_SENDING_PACKET;
			}
		}
		clock_gettime(CLOCK_MONOTONIC, &tend);
		total_t = (tend.tv_sec - tstart.tv_sec);
		total_t += (tend.tv_nsec - tstart.tv_nsec) / 1000000000.0;
	}
	expriment_t result =  {count, fail, total_t};
	return result;
}

expriment_t measureMessageAck() {
	uint16_t count = 1, fail = 0;
	DelayMeasureState_t state = DM_SENDING_PACKET;
	double total_t = 0.0, res = 0.0;
	struct timespec tstart = {0,0}, tend = {0,0};
	while(true) {
		if (state == DM_SENDING_PACKET) {			
			ANT_SendAcknowledgedData(ID_CHAN1, data);
			clock_gettime(CLOCK_MONOTONIC, &tstart);
			state = DM_WAITING_FOR_ACK;
		}
		s = ANT_RecvPacket_Blockfree(buffer, BC_ANT_BUFFSIZE);
		if (s == RS_PACKET_COMPLETE) {
			if (buffer[5] ==ANT_MESSAGE_EVENT_TRANSFER_TX_COMPLETED) {
				clock_gettime(CLOCK_MONOTONIC, &tend);
				total_t = (tend.tv_sec - tstart.tv_sec);
				total_t += (tend.tv_nsec - tstart.tv_nsec) / 1000000000.0;
				expriment_t result = {count, fail, total_t};
				return result;
			} else if (buffer[5] == ANT_MESSAGE_EVENT_TRANSFER_TX_FAILED) {
				fail++;
				state = DM_SENDING_PACKET;
			}
		}
	}
	expriment_t result = {count, fail, res};
	return result;
}

/* Experiment 1: Broadcast Data Transfer between two nodes */
void doExperiment1(char deviceType) {
	if (deviceType == 'm') {
		while (period >= END_FREQ) {
			openChannel(ID_CHAN1, FREQ_CHAN1, period, true);					
			ANT_SendBroadcastData(ID_CHAN1, data);
			printf("Started broadcast! Press return to exit!\n");
			getchar();
			closeANT(ID_CHAN1);
			ANT_delayMs(2000);
			period = decreasePeriod(period, STOP_SINGLE);
		}
	} else {
		while (period >= END_FREQ) {
			openChannel(ID_CHAN1, FREQ_CHAN1, period, false);
			doExperiment(ANT_BROADCAST_DATA, ID_CHAN1, &receiveMessageAndCount);
			closeANT(ID_CHAN1);
			getchar();
			period = decreasePeriod(period, STOP_SINGLE);
		}
	}
}

/* Experiment 2: Broadcast Data Transfer between multiple nodes */
void doExperiment2(char deviceType) {	
	if (deviceType == 'm') {
		while (period >= END_FREQ) {
			openChannel(ID_CHAN1, FREQ_CHAN1, period, true);					
			ANT_SendBroadcastData(ID_CHAN1, data);
			printAndWait(ANT_BROADCAST_DATA, ID_CHAN1);
			openChannel(ID_CHAN2, FREQ_CHAN2, period, false);
			
			doExperiment(ANT_BROADCAST_DATA, ID_CHAN2, &receiveMessageAndCount);
			closeANT(ID_CHAN1);
			closeANT(ID_CHAN2);
			ANT_delayMs(2000);
			period = decreasePeriod(period, STOP_DOUBLE);
		}
	} else {
		while (period >= END_FREQ) {
			
			openChannel(ID_CHAN1, FREQ_CHAN1, period, false);					
			printAndWait(ANT_BROADCAST_DATA, ID_CHAN1);
			openChannel(ID_CHAN2, FREQ_CHAN2, period, true);
			ANT_SendBroadcastData(ID_CHAN1, data);

			doExperiment(ANT_BROADCAST_DATA, ID_CHAN1, &receiveMessageAndCount);
			closeANT(ID_CHAN1);
			closeANT(ID_CHAN2);
			ANT_delayMs(2000);
			period = decreasePeriod(period, STOP_DOUBLE);
		}
	}	
}

/* Experiment 3: Ackowledge Data Transfer between two nodes */
void doExperiment3(char deviceType) {
	if (deviceType == 'm') {
		uint8_t data[4] = {0x70, 0x69, 0x6E, 0x67};
		while (period >= END_FREQ) {
			openChannel(ID_CHAN1, FREQ_CHAN1, period, true);
			ANT_SendBroadcastData(ID_CHAN1, data);
			doExperiment(ANT_ACKNOWLEDGED_DATA, ID_CHAN1, &receiveMessageAck);
			closeANT(ID_CHAN1);
			ANT_delayMs(2000);
			period = decreasePeriod(period, STOP_SINGLE);
		}
	} else {
		while (period >= END_FREQ) {
			openChannel(ID_CHAN1, FREQ_CHAN1, period, false);
			printf("Slave node is listning!\n");
			printAndWait(0x00, ID_CHAN1);
			closeANT(ID_CHAN1);
			ANT_delayMs(2000);
			period = decreasePeriod(period, STOP_SINGLE);
		}
	}
}

/* Experiment 4: Ackowledge Data Delay */
void doExperiment4(char deviceType) {
	if (deviceType == 'm') {
		while (period >= END_FREQ) {
			openChannel(ID_CHAN1, FREQ_CHAN1, period, true);
			ANT_SendBroadcastData(ID_CHAN1, data);
			printAndWait(ANT_BROADCAST_DATA, ID_CHAN1);
			double speed_avr = 0;
			uint8_t i;
			for (i = 0; i < RUN_COUNT; ++i) {
				flushBuffer();
				expriment_t result = measureMessageAck();
				results[i] = result.duration;
				speed_avr += results[i];
				printf("Run %d: Delay %f\n", i, result.duration);
				
			}
			speed_avr /= RUN_COUNT;
			double stdDev = 0.0;
			for (i = 0; i < RUN_COUNT; ++i) {
				double cur = results[i] - speed_avr;
				stdDev += cur * cur;
			}
			stdDev /= RUN_COUNT;
			printf("Average delay : %f \tStd dev: %f\n", speed_avr, sqrt(stdDev));
			closeANT(ID_CHAN1);
			ANT_delayMs(2000);
			period = decreasePeriod(period, STOP_SINGLE);
			}
	} else {
		while (period >= END_FREQ) {
			openChannel(ID_CHAN1, FREQ_CHAN1, period, false);
			printf("Slave node is listning!\n");
			printAndWait(0x00, ID_CHAN1);
			closeANT(ID_CHAN1);
			ANT_delayMs(2000);
			period = decreasePeriod(period, STOP_SINGLE);
		}
	}
}

/*	Experiment 5 - Communication Distance  */
void doExperiment5(char deviceType) {
	if (deviceType == 'm') {
		openChannel(ID_CHAN1, FREQ_CHAN1, STD_FREQ, true);		
		ANT_SendBroadcastData(ID_CHAN1, data);
		printf("Started broadcast! Press return to exit!\n");
		getchar();
		closeANT(ID_CHAN1);
	} else if (deviceType == 's') {
		int pSetting = ANT_TRANSMIT_POWER_MINUS_20DBM;
		for(; pSetting <= ANT_TRANSMIT_POWER_0DBM; ++pSetting) {
			openChannel(ID_CHAN1, FREQ_CHAN1, STD_FREQ, false);
			distanceState_t state = DS_INCRESING;
			float distance = 0.0f;
			while (true) {
				uint8_t count = 0, fail = 0, correct = 0;
				while(count < RUN_COUNT) {
					s = ANT_RecvPacket_Blockfree(buffer, BC_ANT_BUFFSIZE);
					if (s == RS_PACKET_COMPLETE) {
						printf("Received # %3d: ", count);
						printBuffer(buffer);
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
				}
			}
			closeANT(ID_CHAN1);
		}
	}
}

/* Experiment 6: Burst Data Transfer between two nodes */
void doExperiment6(char deviceType) {
	if (deviceType == 'm') {
		printf("Burst mode not working correctly! Please use ANTWareII to simulate!\n");
	} else {
		struct timespec tstart = {0,0}, tend = {0,0};
		uint32_t count = 0;
		openChannel(ID_CHAN1, FREQ_CHAN1, STD_FREQ, false);
		printAndWait(ANT_BROADCAST_DATA, ID_CHAN1);
		while(true) {
			s = ANT_RecvPacket_Blockfree(buffer, BC_ANT_BUFFSIZE);
			if (s == RS_PACKET_COMPLETE) {
				if (buffer[2] == ANT_BURST_DATA) {
					count++;
					if (buffer[3] == 0x00) {
						clock_gettime(CLOCK_MONOTONIC, &tstart);
						count = 1;
					} else if ((buffer[3] & 0x80) > 0) {
						clock_gettime(CLOCK_MONOTONIC, &tend);
						double total_t = (tend.tv_sec - tstart.tv_sec);
						total_t += (tend.tv_nsec - tstart.tv_nsec) / 1000000000.0;
						printf("Received %d burst packets in %f s ==> %f\n", count, total_t, count * 8 / total_t);
					}
				}
			}
		}
		closeANT(ID_CHAN1);
	}
}
