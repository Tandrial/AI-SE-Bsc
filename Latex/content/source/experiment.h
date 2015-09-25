/* experiment.h */
#ifndef EXPERIMENT_H_
#define EXPERIMENT_H_

#include <stdbool.h>
#include "ant.h"

#define   DEVICE_NUMBER       33
#define   DEVICE_TYPE          1
#define   TRANSMISSION_TYPE    1

#define   ID_CHAN1             0
#define   FREQ_CHAN1          66
#define   ID_CHAN2             1
#define   FREQ_CHAN2          77

#define   START_FREQ      0xFFFF /* 0.5 Hz */
#define   STD_FREQ        0x2000 /*   4 Hz */
#define   END_FREQ        0x00A5 /* 200 Hz */
#define   STOP_SINGLE     0x00FF /* 255 Hz */
#define   STOP_DOUBLE     0x01FF /* 511 Hz */

#define   RUN_COUNT           10
#define   SLICE_COUNT         10
#define   TRANSFER_TIME_S     10 /* = 10 * 10 = 100 s */

typedef enum {
  DS_INCRESING = 0,
  DS_DECREASING,
  DS_DONE
} distanceState_t;

typedef enum {
  DM_SENDING_PACKET = 0,
  DM_WAITING_FOR_ACK   
} DelayMeasureState_t;

typedef struct {
  uint16_t sucess;
  uint16_t fail;
  double duration;
} expriment_t;

typedef expriment_t (*exprHandler)(uint8_t);

void initANT(char* ttyUSBDevice);
void resetANT();
void openChannel(uint8_t channel, uint8_t freq, uint16_t period,
                 bool isMaster);
void closeANT(uint8_t channel);

void setTransmitPower(uint8_t pSetting);
uint16_t halfPeriod(uint16_t period);
void printBuffer(uint8_t *buffer);
int kbhit(void);

void doExperiment(uint8_t msgType, uint8_t channel, exprHandler fn);
expriment_t receiveMessageAndCount(uint8_t msgType);
expriment_t receiveMessageAck(uint8_t msgType);
void doExperiment1(char deviceType);
void doExperiment2(char deviceType);
void doExperiment3(char deviceType);
void doExperiment4(char deviceType);
void doExperiment5(char deviceType);
void doExperiment6(char deviceType);

#endif /* EXPERIMENT_H_ */