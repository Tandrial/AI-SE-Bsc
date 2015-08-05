/*
 * ant.h
 *
 *  Created on: 2012/10/12
 *      Author: ryuchi,
 *
 *  Modified by Tim Meurer, Hugues Smeets
 */

#ifndef ANT_H_
#define ANT_H_

#define ANT_PC           
/* 
#define ANT_PIC 
*/

#define ANT_OK 1

typedef  unsigned int uint32_t;
typedef  unsigned short int uint16_t;
typedef  unsigned char uint8_t;

#ifdef ANT_PIC
  #include "p24FJ64GB002.h"
  #include "uart1.h"
  #include "Delay.h"
#endif

#ifdef ANT_PC
  #include <unistd.h>                   /* usleep() */
  #include "serial.h"
  #include "error.h"
  #define UINT8 unsigned char
  #define IO_SERIAL_DATA_RATE 19200
#endif

typedef struct{

  char sync;
  char id;
  UINT8 *data;
  int length;
  char checksum;
  int padbytes;

} ANT_MESSAGE;

#define ANT_IO_PORT "/dev/ttyUSB0"

#define BC_ANT_BUFFSIZE  72


/* ANT serial port abstraction (portability PC/PIC) */


int antUARTinit( char* );

int  antUARTrts( void );
void antUARTputChar( char );
int  antUARTgetChar( char* );
int  antUARTreceivedChar( void );

/* ANT time abstraction */

void ANT_delayMs( int );

/* high-level ANT support functions (Tim, Hugues) */

void  antCreateMessage(ANT_MESSAGE* message, UINT8 id, UINT8* data, UINT8 length);
UINT8 antSendMessage(ANT_MESSAGE* message, UINT8 maxWaitingTime);
void  antInit(UINT8 master, UINT8 channel, void (*antIdle)(void) );


#define ANT_CHANNEL_BIDI_MASTER 0x10

#define ANT_SYNCBYTE  0xA4

#define  ANT_UNASSIGN_CHANNEL           0x41
#define  ANT_ASSIGN_CHANNEL             0x42
#define  ANT_SET_CHANNEL_ID             0x51
#define  ANT_CHANNEL_MESSAGING_PREIOD   0x43
#define  ANT_CHANNEL_SEARCH_TIMEOUT     0x44
#define  ANT_CHANNEL_RF_FREQUENCY       0x45
#define  ANT_SET_NETWORK_KEY            0x46
#define  ANT_TRANSMIT_POWER             0x47
#define  ANT_ADD_CHANNEL_ID             0x59
#define ANT_CONFIG_LIST_ID              0x5A
#define  ANT_SET_CHANNEL_TX_POWER       0x60
#define ANT_CHANNEL_LOW_PRIORITY_SERARCH_TIMEOUT 0x63
#define ANT_SERIAL_NUMBER_CHANNEL_ID    0x65
#define  ANT_ENABLE_EXTENDED_MESSAGGES  0x66
#define ANT_ENABLE_LED                  0x68
#define ANT_ENABLE_CRYSTAL              0x6D
#define  ANT_FREQUENCY_AGILITY          0x70
#define  ANT_PROXIMITY_SEARCH           0x71
#define  ANT_SET_USB_DESCRIPTOR_STRING  0xC7
#define ANT_STARTUP_MESSAGE             0x6F
#define  ANT_RESET_SYSTEM               0x4A
#define ANT_OPEN_CHANNEL                0x4B
#define  ANT_CLOSE_CHANNEL              0x4C
#define  ANT_REQUEST_MESSAGE            0x4D
#define ANT_OPEN_RX_SCAN_MODE           0x5B
#define  ANT_SLEEP_MESSAGE              0xC5
#define  ANT_BROADCAST_DATA             0x4E
#define ANT_ACKNOWLEDGED_DATA           0x4F
#define  ANT_BURST_DATA                 0x50
#define  ANT_CHANNEL_RESPONSE           0x40
#define  ANT_CHANNEL_EVENT              0x40
#define  ANT_CHANNEL_STATUS             0x52
#define  ANT_CHANNEL_ID                 0x51
#define ANT_VERSION                     0x31
#define  ANT_CAPABILITIES               0x54
#define  ANT_DEVICE_SERIAL_NUMBER       0x61
#define  ANT_INIT_CW_TEST_MODE          0x53
#define  ANT_CW_TEST_MODE               0x48
#define  ANT_EXTENDED_BROADCAST_DATA    0x5D
#define ANT_EXTENDED_ACKNOWLEDGED_DATA  0x5E
#define  ANT_EXTENDED_BURST_DATA        0x5F

/* Assign Channel: parameters (Channel Type) */
#define ANT_Bidirectional_Slave         0x00
#define ANT_Bidirectional_Master        0x10
#define ANT_Shared_Bidirectional_Slave  0x20
#define  ANT_Slave_Receive_Only         0x40
#define ANT_Master_Transmission_Only    0x50

/* Assign Channel: parameters (Extened Assignment) */
#define  ANT_BACKGRAND_SCANNING_CHANNEL_ENABLE  0x01
#define  ANT_FREQUENCY_AGILITY_ENABLE           0x04

/* Transmit Power: parameters (Transmission Power) */
#define  ANT_TRANSMIT_POWER_MINUS_20DBM         0x00
#define ANT_TRANSMIT_POWER_MINUS_10DBM          0x01
#define  ANT_TRANSMIT_POWER_MINUS_5DBM          0x02
#define  ANT_TRANSMIT_POWER_0DBM                0x03

/* Set Channle Tx Power: parameters (Transmission Power) */
#define  ANT_SET_CHANNEL_TRANSMIT_POWER_MINUS_20DBM 0x00
#define ANT_SET_CHANNEL_TRANSMIT_POWER_MINUS_10DBM  0x01
#define  ANT_SET_CHANNEL_TRANSMIT_POWER_MINUS_5DBM  0x02
#define  ANT_SET_CHANNEL_TRANSMIT_POWER_0DBM        0x03

/* Enable Extended Message: parameters */
#define ANT_EXTENDED_MESSAGE_ENABLE         1
#define ANT_EXTENDED_MESSAGE_DISABLE        0

/* Enable LED Message: parameters */
#define ANT_LED_ENABLE                 1
#define ANT_LED_DISABLE                0

#define  ANT_CRYSTAL_ENABLE            0

/*  Set USB Descriptor: */
#define  ANT_USB_DESCRIPTOR_PID_VID          0
#define  ANT_USB_DESCRIPTOR_MANUFACTURE      1
#define  ANT_USB_DESCRIPTOR_DEVICE           2
#define  ANT_USB_DESCRIPTOR_SERIAL_NUMBER    3

/*  Startup Message */
#define  ANT_STARTUP_MESSAGE                     0x6F
#define  ANT_STARTUP_MESSAGE_POWER_ON_RESET      0x00
#define  ANT_STARTUP_MESSAGE_HARDWARE_RESET_LINE 0x01
#define  ANT_STARTUP_MESSAGE_WATCH_DOG_RESET     0x02
#define  ANT_STARTUP_MESSAGE_COMMAND_RESET       0x20
#define  ANT_STARTUP_MESSAGE_SYNCHRONOUS_RESET   0x40
#define  ANT_STARTUP_MESSAGE_SUSPEND_RESET       0x80

#define  ANT_DATA_MESSAGE_EXTENDED_FLAG 0x80

#define  ANT_MESSAGE_RESPONSE_NO_ERROR        0
#define  ANT_MESSAGE_EVENT_RX_SEARCH_TIMEOUT  1
#define  ANT_MESSAGE_EVENT_RX_FAIL            2
#define  ANT_MESSAGE_EVENT_TX                 3
#define  ANT_MESSAGE_EVENT_TRANSFER_RX_FAILED 4
#define  ANT_MESSAGE_EVENT_TRANSFER_TX_COMPLETED 5
#define  ANT_MESSAGE_EVENT_TRANSFER_TX_FAILED    6
#define  ANT_MESSAGE_CHANNEL_CLOSED           7
#define  ANT_MESSAGE_EVENT_RX_FAIL_GO_TO_SEARCH  8
#define  ANT_MESSAGE_EVNET_CHANNEL_COLLISION     9
#define  ANT_MESSAGE_EVENT_TRANSFER_TX_START     10
#define  ANT_MESSAGE_CHANNEL_IN_WRONG_STATE      21
#define  ANT_MESSAGE_CHANNEL_NOT_OPEND         22
#define  ANT_MESSAGE_CHANNEL_ID_NOT_SET        24
#define  ANT_MESSAGE_CLOSE_ALL_CHANNELS        25
#define  ANT_MESSAGE_TRANSFER_IN_PROGRESS      31
#define  ANT_MESSAGE_TRANSFER_SEQUENCE_NUMBER_ERROR  32
#define  ANT_MESSAGE_TRANSFER_IN_ERROR        33
#define  ANT_MESSAGE_INVALID_MESSAGE          40
#define  ANT_MESSAGE_INVALID_NETWORK_NUMBER   41
#define  ANT_MESSAGE_LIST_ID                  48
#define  ANT_MESSAGE_INVALID_SCAN_TX_CHANNEL  49
#define  ANT_MESSAGE_INVALID_PARAMETER_PROVIDED 51
#define  ANT_MESSAGE_EVENT_QUE_OVERFLOW         53
#define  ANT_MESSAGE_NVM_FULL_ERROR             64
#define  ANT_MESSAGE_WRITE_ERROR                65

#define  ANT_CHANNEL_STATUS_UN_ASSIGNED        0
#define  ANT_CHANNEL_STATUS_ASSIGNED           1
#define  ANT_CHANNEL_STATUS_SEARCHING          2
#define  ANT_CHANNEL_STATUS_TRACKING           3

#define  ANT_CAPABILITIES_NO_RECEIVE_CHANNELS  0x01
#define  ANT_CAPABILITIES_NO_TRANSMIT_CHANNELS 0x02
#define  ANT_CAPABILITIES_NO_RECEIVE_MESSAGE   0x04
#define  ANT_CAPABILITIES_NO_TRANSMIT_MESSAGE  0x08
#define  ANT_CAPABILITIES_NO_ACKD_MESSAGE      0x10
#define  ANT_CAPABILITIES_NO_BURST_MESSAGE     0x20
#define  ANT_CAPABILITIES_NETWORK_ENABLED      0x01
#define  ANT_CAPABILITIES_SERIAL_NUMBER_ENABLED 0x04
#define  ANT_CAPABILITIES_PER_CHANNEL_TX_POWER_ENABLED 0x10
#define  ANT_CAPABILITIES_LOW_PRIORITY_SEARCH_ENABLED  0x20
#define  ANT_CAPABILITIES_SCRIPT_ENABLED         0x40
#define  ANT_CAPABILITIES_SEARCH_LIST_ENABLED    0x80
#define  ANT_CAPABILITIES_LED_ENABLED            0x01
#define  ANT_CAPABILITIES_EXT_MESSAGE_ENABLED    0x02
#define  ANT_CAPABILITIES_SCAN_MODE_ENABLED      0x04
#define  ANT_CAPABILITIES_PROX_SRARCH_ENABLED    0x10
#define  ANT_CAPABILITIES_EXT_ASSIGN_ENABLED     0x20

#define  ANT_DEVICE_SERIAL_NUMBER 0x61
#define  ANT_PADDING_BYTE 0

uint32_t ANT_UnAssignChannel( uint8_t Channel );
uint32_t ANT_AssignChannel( uint8_t Channel, uint8_t ChannelType, uint8_t NetworkNumber) ;
uint32_t ANT_AssignChannelExt( uint8_t Channel, uint8_t ChannelType, uint8_t NetworkNumber, uint8_t Extend );
uint32_t ANT_SetChannelId( uint8_t Channel, uint16_t DeviceNum, uint8_t DeviceType, uint8_t TransmissionType );
uint32_t ANT_SetChannelPeriod( uint8_t Channel, uint16_t MessaggePeriod );
uint32_t ANT_SetChannelPeriod_Hz( uint8_t Channel, uint16_t Period );
uint32_t ANT_SetChannelSearchTimeout( uint8_t ChannelNum, uint8_t SearcchTimeout );
uint32_t ANT_SetChannelRFFreq( uint8_t Channel, uint8_t RFFreq );
uint32_t ANT_SetNetworkKey( uint8_t NetworkNumber,  uint8_t *pucKey);
uint32_t ANT_SetTransmitPower( uint8_t TransmitPower );
uint32_t ANT_AddChannelID( uint8_t Channel, uint16_t DeviceNum, uint8_t DeviceType, uint8_t TransmissionType, uint8_t ListIndex );
uint32_t ANT_ConfigList( uint8_t Channel, uint8_t ListSize, uint8_t Exclude );
uint32_t ANT_SetChannelTxPower( uint8_t Channel, uint8_t TxPower );
uint32_t ANT_SetLowPriorityChannelSearchTimeout( uint8_t ChannelNum, uint8_t SearchTimeout );
uint32_t ANT_SetSerialNumChannelId( uint8_t Channel, uint8_t DeviceType, uint8_t TransmissionType );
uint32_t ANT_RxExtMesgsEnable( uint8_t Enable );
uint32_t ANT_EnableLED( uint8_t Enable );
uint32_t ANT_CrystalEnable( uint8_t Enable );
uint32_t ANT_ConfigFrequencyAgility( uint8_t Channel, uint8_t Frequency1, uint8_t Frequency2, uint8_t Frequency3 );
uint32_t ANT_SetProximitySearch( uint8_t Channel, uint8_t SearchThreshold );
uint32_t ANT_SetUSBDescriptorString( uint8_t StringNum, uint8_t *pucDescString, uint8_t StringSize );
uint32_t ANT_ResetSystem ( void );
uint32_t ANT_OpenChannel(uint32_t Channel );
uint32_t ANT_CloseChannel(uint32_t Channel );
uint32_t ANT_RequestMessage(uint32_t Channel, uint8_t MessageID );
uint32_t ANT_OpenRxScanMode( void );
uint32_t ANT_SleepMessage( void );
uint32_t ANT_SendBroadcastData(uint8_t Channel, uint8_t * BroadcastData );
uint32_t ANT_SendBroadcastDataExt(uint8_t Channel, uint8_t *BroadcastData, uint16_t DeviceNumber, uint8_t DeviceType, uint8_t TransmissionType );
uint32_t ANT_SendAcknowledgedData(uint8_t Channel, uint8_t *BroadcastData );
uint32_t ANT_SendAcknowledgedDataExt( uint8_t Channel, uint8_t *BroadcastData, uint16_t DeviceNumber, uint8_t DeviceType, uint8_t TransmissionType );
uint32_t ANT_SendBurstTransferPacket( uint8_t ChannelSeq, uint8_t *BurstData );
uint32_t ANT_InitCWTestMode( void );
uint32_t ANT_SetCWTestMode( uint8_t TransmitPower, uint8_t RFChannel );
uint32_t ANT_SendExtBroadcastData( uint8_t Channel, uint16_t DeviceNum, uint8_t DeviceType, uint8_t TransmissionType, uint8_t *Data );
uint32_t ANT_SendExtAcknowledgedData( uint8_t Channel, uint16_t DeviceNum, uint8_t DeviceType, uint8_t TransmissionType, uint8_t *Data );
uint32_t ANT_SendExtBurstData( uint8_t Channel, uint16_t DeviceNum, uint8_t DeviceType, uint8_t TransmissionType, uint8_t *Data );

uint32_t BC_ANT_build_packet( uint8_t *souce, uint32_t size, uint8_t *dist );

uint32_t BC_ANT_RecvPacket(uint8_t *buffer, unsigned int size);

uint32_t BC_ANT_ResetSystem( void );
uint32_t BC_ANT_OpenChannel( uint8_t Channel );
uint32_t BC_ANT_CloseChannel( uint8_t Channel );
uint32_t BC_ANT_RequestMessage( uint8_t Channel, uint8_t MessageID );



#endif /* ANT_H_ */
