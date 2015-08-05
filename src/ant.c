/*
 *  Created on: 2012/10/12
 *      Author: ryuchi

Added high-level support for PIC and PC: 2015/07/07 (Hugues Smeets)

 */

#include "ant.h"

uint8_t BC_ANT_buffer[BC_ANT_BUFFSIZE];
uint8_t BC_ANT_raw_buffer[BC_ANT_BUFFSIZE];

int antFD = 0;

/******************************
  ANT serial port abstraction 
*******************************/

#ifdef ANT_PIC
int antUARTinit( char *port  ){
  TRISBbits.TRISB0 = 1;   /* Set RB0 as input. */
  return UART1Init();     /* check if  UART1Init() returns something... */
}

int  antUARTrts( void ){
}
void antUARTputChar( char ){
}
int  antUARTgetChar( char *p ){uart1_recv_char( p );}
}
int  antUARTreceivedChar( void ){return antUARTreceivedChar();}
#endif

#ifdef ANT_PC

/*****************************************************************************
Input: on PC: port: device name
       on PIC: port is not used.
Output: 1 if error, 0 if OK.
Rem: -
*****************************************************************************/
int antUARTinit( char *port  ){

  if( antFD != 0 ) return 1; /* assume was opened before */
  antFD =  open( port, O_RDWR );
  if(antFD == -1) error("Unable to open serial port %s", port);
  if(setBaud(antFD, IO_SERIAL_DATA_RATE)) error("Set baud failed");
  setDTR(antFD,1);
  setRTS(antFD,1);
  return 0;
}

int  antUARTrts( void  ){
  return getCTS(antFD);
}


void antUARTputChar( char c ){
printf("[ %02X ]",(unsigned char)c);
  writeSerial( antFD, &c, 1);
}
int  antUARTgetChar( char *p ){
  readSerialChar( antFD, p);
  return 1;
}
int  antUARTreceivedChar( void ){
  error("antUARTreceivedChar");
  return 0;
}

#endif

/********************
   Timing abstraction
********************/

#ifdef ANT_PIC
  void antDelayMs( int i ){ delayMs( i ); }
#endif

#ifdef ANT_PC
  void antDelayMs( int i ){ usleep( i * 1000 );}
#endif

/**********************
  high level functions
**********************/


/*****************************************************************************
Input:
Output:
Rem: -
*****************************************************************************/
void antCreateMessage(ANT_MESSAGE* message, UINT8 id, UINT8* data, UINT8 length)
{
    UINT8 i;
    message->sync = 0xA4;
    message->id = id;
    message->data = data;
    message->length = length;

    message->checksum = message->sync ^ message->id ^ message->length;

    for(i = 0; i < length; i++)
    {
        message->checksum ^= message->data[i];
    }

    message->padbytes = 2;
}

/*****************************************************************************
Input:
  ANT_MESSAGE* message
  UINT8 maxWaitingTime: maximum time to wait for an answer from the ANT mote
                        in 10ms units, 0xFF for no timeout.
Output:
  ANT_OK: the ANT node accepted the message.
  ANT_TIMEOUT: the ANT did not accept the message within the given time.
Rem: -
*****************************************************************************/
UINT8 antSendMessage(ANT_MESSAGE* message, UINT8 maxWaitingTime)
{
    UINT8 i;

#ifdef ANT_PIC
    UINT8 waitingTime = 0;
    while(PORTBbits.RB0) 
    {
        if((maxWaitingTime != 0xff) && (waitingTime >= maxWaitingTime))
        {
            return ANT_TIMEOUT;
        }
        DelayMs(10);
        waitingTime++;
    }
#endif

#ifdef ANT_PC    
    while( antUARTrts() );
#endif


    antUARTputChar(message->sync);
    antUARTputChar(message->length);
    antUARTputChar(message->id);

    for(i = 0; i < message->length;i++)
    {
        antUARTputChar(message->data[i]);
    }

    antUARTputChar(message->checksum);

    for(i = 0; i < message->padbytes; i++)
    {
        antUARTputChar(0x00);
    }

    return ANT_OK;
}

/*****************************************************************************
Input:
  UINT8 master: the master ID
  UINT8 channel: the channel ID
  void (*antIdle)(void): a pointer to a function that will be called
                         whenever a function of this module has to wait.
Output: -
Rem: -
*****************************************************************************/
void antInit(UINT8 master, UINT8 channel, void (*antIdle)(void) )
{
#ifdef ANT_PC
  if( antUARTinit( ANT_IO_PORT ) ) error("Can't initialize ANT port");
#endif

#ifdef ANT_PIC
  antUARTinit( ANT_IO_PORT );  /* maybe we make an LED blink if failed? */
#endif
    if(master > 0)
    {
		ANT_MESSAGE assignChannelMsg;
        /*Assign Channel (Bidi Master): Channel 1
        
                                      +-> channel number      +-> channel type
                                      |                       |
                                      |                       |     +-> network number
                                      |                       |     | */
        UINT8 assignChannelData[] = {0x01,ANT_CHANNEL_BIDI_MASTER,0x00};
        antCreateMessage(&assignChannelMsg,0x42,assignChannelData,3);
        antSendMessage(&assignChannelMsg,0xFF);

        ANT_MESSAGE setChannelIDMsg;
        /*Set Channel ID
  
                                      +-> channel number
                                      |
                                      |   device number
                                      |    LSB  MSB
                                      |     |    |   +-> device type ID
                                      |     |    |   |
                                      |     |    |   |     +-> tranmission type
                                      |     |    |   |     | */
        UINT8 sendChannelIDData[] = {0x01,0x01,0x00,0x07,0x01};
        antCreateMessage(&setChannelIDMsg,0x51,sendChannelIDData,5);
        antSendMessage(&setChannelIDMsg,0xFF);

        ANT_MESSAGE setFrequencyMsg;
        UINT8 setFrequencyData[] = {0x01,0x05};
        antCreateMessage(&setFrequencyMsg,0x45,setFrequencyData,2);
        antSendMessage(&setFrequencyMsg,0xFF);

        ANT_SetChannelPeriod_Hz( 1, 4 );

        ANT_MESSAGE openChannelMsg;
/*                                   +-> channel number
                                     | */
        UINT8 openChannelData[] = {0x01};
        antCreateMessage(&openChannelMsg,0x4B,openChannelData,1);
        antSendMessage(&openChannelMsg,0xFF);


        while(1)
        {
            ANT_MESSAGE testMsg;
            UINT8 testData[] = {0x01,0x02,0x03,0x04};
            antCreateMessage(&testMsg,0x4E,testData,0x04);
            antSendMessage(&testMsg,0xFF);
            antDelayMs(10000);
			printf(";");fflush(stdout);
        }
    }
    else
    {

    }
exit(-1);
}


/*********************
 low level functions
**********************/

uint32_t BC_ANT_RecvPacket(uint8_t *buffer, unsigned int size) {

	unsigned char c;
	uint8_t checksum;
	unsigned int length, mesg_len;

	if (!antUARTreceivedChar())
        {
            return 0;
        }


        /*Wait for syncbyte */
	antUARTgetChar( (char*) &c );

	while ( c != ANT_SYNCBYTE ) {
                *buffer++ = c;
		if (!antUARTgetChar( (char*) &c )) {
                        *buffer++ = c;
			return 0;
		}
	}


	*buffer++ = c;
        checksum = ANT_SYNCBYTE;
	length = 1;

        /*Get Message Length */
	mesg_len = antUARTreceivedChar();
	*buffer++ = mesg_len;
	checksum ^= mesg_len;
	length++;

        /*Get message ID */
	*buffer  = antUARTreceivedChar();
	checksum ^= *buffer++;
	length++;

	while( mesg_len-- ) {
		*buffer = antUARTreceivedChar();
		checksum ^= *buffer++;
		length++;
		if ( size < length) {
			return 0;
		}
	}

	*buffer = antUARTreceivedChar();
	if ( *buffer == checksum ) {
		return length + 1;
	 }

	return 0;

}

unsigned int BC_ANT_SendPacket( uint8_t *packet, unsigned int size ) {

  uint32_t i = 0;
  while( i < size ){
    while ( antUARTrts() );
      antUARTputChar( *( packet + i ) );
      i++;
    }
    return 0;
}

unsigned int BC_ANT_build_packet( uint8_t *source, unsigned int size, uint8_t *dist ) {

	uint8_t checksum;
	uint8_t *p = dist;
	uint32_t i;

	*p++ =     ANT_SYNCBYTE;
	checksum = ANT_SYNCBYTE;

	size &= 63;
	*p++ = size;
	checksum ^= size;
	i = size + 1;

	while( i-- ) {
		checksum = checksum ^ *source;
		*p++ = *source++;
	}

	*p++ = checksum;
	*p++ = 0x00;
	*p++ = 0x00;

	return size + 6;

}

unsigned int ANT_UnAssignChannel( uint8_t Channel ) {

	uint8_t *p = BC_ANT_buffer;
	uint32_t psize;

	*p++ = ANT_UNASSIGN_CHANNEL;
	*p = Channel;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 1, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}

unsigned int ANT_AssignChannel(
		uint8_t Channel,
		uint8_t ChannelType,
		uint8_t NetworkNumber ) {

	uint8_t *p = BC_ANT_buffer;
	unsigned int psize;

	*p++ = ANT_ASSIGN_CHANNEL;
	*p++ = Channel;
	*p++ = ChannelType;
	*p++ = NetworkNumber;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 3, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}

unsigned int ANT_AssignChannelExt(
		uint8_t Channel,
		uint8_t ChannelType,
		uint8_t NetworkNumber,
		uint8_t ExtendAssignment ) {

	uint8_t *p = BC_ANT_buffer;
	unsigned int psize;

	*p++ = ANT_ASSIGN_CHANNEL;
	*p++ = Channel;
	*p++ = ChannelType;
	*p++ = NetworkNumber;
	*p++ = ExtendAssignment;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 4, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}

uint32_t ANT_SetChannelId(
		uint8_t Channel,
		uint16_t DeviceNum,
		uint8_t DeviceType,
		uint8_t TransmissionType ) {

	uint8_t *p = BC_ANT_buffer;
	uint32_t psize;

	*p++ = ANT_SET_CHANNEL_ID;
	*p++ = Channel;
	*p++ = DeviceNum & 0xff;
	*p++ = DeviceNum >> 8;
	*p++ = DeviceType;
	*p++ = TransmissionType;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 5, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}

uint32_t ANT_SetChannelPeriod(
		uint8_t Channel,
		uint16_t MessagePeriod ) {

	uint8_t *p = BC_ANT_buffer;
	uint32_t psize;

	*p++ = ANT_CHANNEL_MESSAGING_PREIOD;
	*p++ = Channel;
	*p++ = MessagePeriod & 0xff;
	*p++ = MessagePeriod >> 8;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 3, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;


}

uint32_t ANT_SetChannelPeriod_Hz(
		uint8_t Channel,
		uint16_t Period ) {

	uint8_t *p = BC_ANT_buffer;
	uint32_t psize;
	uint16_t MessagePeriod;

	MessagePeriod = 32768 / Period;

	*p++ = ANT_CHANNEL_MESSAGING_PREIOD;
	*p++ = Channel;
	*p++ = MessagePeriod & 0xff;
	*p++ = MessagePeriod >> 8;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 3, BC_ANT_raw_buffer );
printf("stuck...");fflush(stdout);
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);
printf("unstuck...");fflush(stdout);

	return 0;


}

uint32_t ANT_SetChannelSearchTimeout(
		uint8_t ChannelNum,
		uint8_t SearchTimeout ) {

	uint8_t *p = BC_ANT_buffer;
	uint32_t psize;

	*p++ = ANT_CHANNEL_SEARCH_TIMEOUT;
	*p++ = ChannelNum;
	*p++ = SearchTimeout;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 2, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;


}
uint32_t ANT_SetChannelRFFreq(
		uint8_t Channel,
		uint8_t RFFreq ) {

	uint8_t *p = BC_ANT_buffer;
	uint32_t psize;

	*p++ = ANT_CHANNEL_RF_FREQUENCY;
	*p++ = Channel;
	*p++ = RFFreq;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 2, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}
uint32_t ANT_SetNetworkKey(
		uint8_t NetworkNumber,
		uint8_t *pubKey) {

	uint8_t *p = BC_ANT_buffer, *key = pubKey;
	uint32_t i, psize;

	*p++ = ANT_SET_NETWORK_KEY;
	*p++ = NetworkNumber;
	for ( i = 0; i < 7; i++ ) {
		*p++ = *key++;
	}

	psize = BC_ANT_build_packet( BC_ANT_buffer, 9, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}
uint32_t ANT_SetTransmitPower(
		uint8_t TransmitPower ) {

	uint8_t *p = BC_ANT_buffer;
	uint32_t psize;

	*p++ = ANT_TRANSMIT_POWER;
	*p++ = 0;
	*p++ = TransmitPower & 0x03;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 2, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}
uint32_t ANT_AddChannelID(
		uint8_t Channel,
		uint16_t DeviceNum,
		uint8_t DeviceTypeId,
		uint8_t TransmissionType,
		uint8_t ListIndex ) {

	uint8_t *p = BC_ANT_buffer;
	uint32_t psize;

	*p++ = ANT_ADD_CHANNEL_ID;
	*p++ = Channel;
	*p++ = DeviceNum & 0xff;
	*p++ = DeviceNum >> 8;
	*p++ = DeviceTypeId;
	*p++ = TransmissionType;
	*p++ = ListIndex & 0x03;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 5, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}
uint32_t ANT_ConfigList(
		uint8_t Channel,
		uint8_t ListSize,
		uint8_t Exclude ) {

	uint8_t *p = BC_ANT_buffer;
	uint32_t psize;

	*p++ = ANT_CONFIG_LIST_ID;
	*p++ = Channel;
	*p++ = ListSize & 0x07;
	*p++ = Exclude & 0x01;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 3, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}
uint32_t ANT_SetChannelTxPower(
		uint8_t Channel,
		uint8_t TxPower ) {

	uint8_t *p = BC_ANT_buffer;
	uint32_t psize;

	*p++ = ANT_SET_CHANNEL_TX_POWER;
	*p++ = Channel;
	*p++ = TxPower & 0x03;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 2, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}
uint32_t ANT_SetLowPriorityChannelSearchTimeout(
		uint8_t ChannelNum,
		uint8_t SearchTimeout ) {

	uint8_t *p = BC_ANT_buffer;
	uint32_t psize;

	*p++ = ANT_CHANNEL_LOW_PRIORITY_SERARCH_TIMEOUT;
	*p++ = ChannelNum;
	*p++ = SearchTimeout;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 2, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;


}
uint32_t ANT_SetSerialNumChannelId(
		uint8_t Channel,
		uint8_t DeviceType,
		uint8_t TransmissionType ) {

	uint8_t *p = BC_ANT_buffer;
	uint32_t psize;

	*p++ = ANT_SERIAL_NUMBER_CHANNEL_ID;
	*p++ = Channel;
	*p++ = DeviceType;
	*p++ = TransmissionType;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 3, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}
uint32_t ANT_RxExtMesgsEnable( uint8_t Enable ) {

	uint8_t *p = BC_ANT_buffer;
	uint32_t psize;

	*p++ = ANT_ENABLE_EXTENDED_MESSAGGES;
	*p++ = 0;
	*p++ = Enable;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 2, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}
uint32_t ANT_EnableLED( uint8_t Enable ) {

	uint8_t *p = BC_ANT_buffer;
	uint32_t psize;

	*p++ = ANT_ENABLE_LED;
	*p++ = 0;
	*p++ = Enable;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 2, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}
uint32_t ANT_CrystalEnable( uint8_t Enable ) {

	uint8_t *p = BC_ANT_buffer;
	uint32_t psize;

	*p++ = ANT_ENABLE_CRYSTAL;
	*p++ = 0;
	*p++ = Enable;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 2, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;


}
uint32_t ANT_ConfigFrequencyAgility(
		uint8_t Channel,
		uint8_t Frequency1,
		uint8_t Frequency2,
		uint8_t Frequency3 ) {

	uint8_t *p = BC_ANT_buffer;
	uint32_t psize;

	*p++ = ANT_FREQUENCY_AGILITY;
	*p++ = Channel;
	*p++ = Frequency1;
	*p++ = Frequency2;
	*p++ = Frequency3;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 4, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}
uint32_t ANT_SetProximitySearch(
		uint8_t Channel,
		uint8_t SearchThreshold ) {

	uint8_t *p = BC_ANT_buffer;
	uint32_t psize;

	*p++ = ANT_PROXIMITY_SEARCH;
	*p++ = Channel;
	*p++ = SearchThreshold;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 2, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}

uint32_t ANT_SetUSBDescriptorString(
		uint8_t StringNum,
		uint8_t *pubDescString,
		uint8_t StringSize ) {

	uint8_t *p = BC_ANT_buffer, *d = pubDescString;
	uint32_t psize, i;

	*p++ = ANT_SET_USB_DESCRIPTOR_STRING;
	*p++ = StringNum;

	i = StringSize;
	if ( i > 32)
		i = 32;

	while( i-- ) {
		*p++ = *d++;
	}
	*p++ = StringSize;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 2, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}

uint32_t ANT_ResetSystem ( void ) {

	uint8_t *p = BC_ANT_buffer;
	uint32_t psize;

	*p++ = ANT_RESET_SYSTEM;
	*p++ = 0;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 1, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;
}

uint32_t ANT_OpenChannel(uint32_t Channel ) {

	uint8_t *p = BC_ANT_buffer;
	uint32_t psize;

	*p++ = ANT_OPEN_CHANNEL;
	*p++ = Channel;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 1, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}

uint32_t ANT_CloseChannel(uint32_t Channel ) {

	uint8_t *p = BC_ANT_buffer;
	uint32_t psize;

	*p++ = ANT_CLOSE_CHANNEL;
	*p++ = Channel;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 1, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;
}

uint32_t ANT_RequestMessage(uint32_t Channel, uint8_t MessageID ) {

	uint8_t *p = BC_ANT_buffer;
	uint32_t psize;

	*p++ = ANT_REQUEST_MESSAGE;
	*p++ = Channel;
	*p++ = MessageID;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 2, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}

uint32_t ANT_OpenRxScanMode( void ) {

	uint8_t *p = BC_ANT_buffer;
	uint32_t psize;

	*p++ = ANT_OPEN_RX_SCAN_MODE;
	*p++ = 0;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 1, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}

uint32_t ANT_SleepMessage( void ) {

	uint8_t *p = BC_ANT_buffer;
	uint32_t psize;

	*p++ = ANT_SLEEP_MESSAGE;
	*p++ = 0;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 1, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}

uint32_t ANT_SendBroadcastData(
		uint8_t Channel,
		uint8_t * BroadcastData ) {

	uint8_t *p = BC_ANT_buffer, *d = BroadcastData;
	uint32_t psize, i;

	*p++ = ANT_BROADCAST_DATA;
	*p++ = Channel;

	for( i = 0; i < 8; i++ ) {
		*p++ = *d++;
	}
	psize = BC_ANT_build_packet( BC_ANT_buffer, 9, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}

uint32_t ANT_SendBroadcastDataExt(
		uint8_t Channel,
		uint8_t *BroadcastData,
		uint16_t DeviceNumber,
		uint8_t DeviceType,
		uint8_t TransmissionType ) {

	uint8_t *p = BC_ANT_buffer, *d = BroadcastData;
	uint32_t psize, i;

	*p++ = ANT_BROADCAST_DATA;
	*p++ = Channel;

	for( i = 0; i < 8; i++ ) {
		*p++ = *d++;
	}
	*p++ = 0x80;
	*p++ = DeviceNumber & 0xff;
	*p++ = DeviceNumber >> 8;
	*p++ = DeviceType;
	*p++ = TransmissionType;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 14, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}

uint32_t ANT_SendAcknowledgedData(
		uint8_t Channel,
		uint8_t *BroadcastData ) {

	uint8_t *p = BC_ANT_buffer, *d = BroadcastData;
	uint32_t psize, i;

	*p++ = ANT_ACKNOWLEDGED_DATA;
	*p++ = Channel;

	for( i = 0; i < 8; i++ ) {
		*p++ = *d++;
	}
	psize = BC_ANT_build_packet( BC_ANT_buffer, 9, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}

uint32_t ANT_SendAcknowledgedDataExt(
		uint8_t Channel,
		uint8_t *BroadcastData,
		uint16_t DeviceNumber,
		uint8_t DeviceType,
		uint8_t TransmissionType ) {

	uint8_t *p = BC_ANT_buffer, *d = BroadcastData;
	uint32_t psize, i;

	*p++ = ANT_ACKNOWLEDGED_DATA;
	*p++ = Channel;

	for( i = 0; i < 8; i++ ) {
		*p++ = *d++;
	}
	*p++ = 0x80;
	*p++ = DeviceNumber & 0xff;
	*p++ = DeviceNumber >> 8;
	*p++ = DeviceType;
	*p++ = TransmissionType;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 14, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}

uint32_t ANT_SendBurstTransferPacket(
		uint8_t ChannelSeq,
		uint8_t *BurstData ) {

	uint8_t *p = BC_ANT_buffer, *d = BurstData;
	uint32_t psize, i;

	*p++ = ANT_BURST_DATA;
	*p++ = ChannelSeq;

	for( i = 0; i < 8; i++ ) {
		*p++ = *d++;
	}
	psize = BC_ANT_build_packet( BC_ANT_buffer, 9, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;
}

uint32_t ANT_InitCWTestMode( void ) {

	uint8_t *p = BC_ANT_buffer;
	uint32_t psize;

	*p++ = ANT_INIT_CW_TEST_MODE;
	*p++ = 0;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 1, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}

uint32_t ANT_SetCWTestMode(
		uint8_t TransmitPower,
		uint8_t RFChannel ) {

	uint8_t *p = BC_ANT_buffer;
	uint32_t psize;

	*p++ = ANT_CW_TEST_MODE;
	*p++ = 0;
	*p++ = TransmitPower;
	*p++ = RFChannel;

	psize = BC_ANT_build_packet( BC_ANT_buffer, 3, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}

uint32_t ANT_SendExtBroadcastData(
		uint8_t Channel,
		uint16_t DeviceNum,
		uint8_t DeviceType,
		uint8_t TransmissionType,
		uint8_t *Data ) {

	uint8_t *p = BC_ANT_buffer, *d = Data;
	uint32_t psize, i;

	*p++ = ANT_EXTENDED_BROADCAST_DATA;
	*p++ = Channel;
	*p++ = DeviceNum & 0xff;
	*p++ = DeviceNum >> 8;
	*p++ = DeviceType;
	*p++ = TransmissionType;

	for( i = 0; i < 8; i++ ) {
		*p++ = *d++;
	}
	psize = BC_ANT_build_packet( BC_ANT_buffer, 13, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}

uint32_t ANT_SendExtAcknowledgedData(
		uint8_t Channel,
		uint16_t DeviceNum,
		uint8_t DeviceType,
		uint8_t TransmissionType,
		uint8_t *Data ) {

	uint8_t *p = BC_ANT_buffer, *d = Data;
	uint32_t psize, i;

	*p++ = ANT_EXTENDED_ACKNOWLEDGED_DATA;
	*p++ = Channel;
	*p++ = DeviceNum & 0xff;
	*p++ = DeviceNum >> 8;
	*p++ = DeviceType;
	*p++ = TransmissionType;

	for( i = 0; i < 8; i++ ) {
		*p++ = *d++;
	}
	psize = BC_ANT_build_packet( BC_ANT_buffer, 13, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;

}

uint32_t ANT_SendExtBurstData(
		uint8_t ChannelSeq,
		uint16_t DeviceNum,
		uint8_t DeviceType,
		uint8_t TransmissionType,
		uint8_t *Data ) {

	uint8_t *p = BC_ANT_buffer, *d = Data;
	uint32_t psize, i;

	*p++ = ANT_EXTENDED_BURST_DATA;
	*p++ = ChannelSeq;
	*p++ = DeviceNum & 0xff;
	*p++ = DeviceNum >> 8;
	*p++ = DeviceType;
	*p++ = TransmissionType;

	for( i = 0; i < 8; i++ ) {
		*p++ = *d++;
	}
	psize = BC_ANT_build_packet( BC_ANT_buffer, 13, BC_ANT_raw_buffer );
	BC_ANT_SendPacket( BC_ANT_raw_buffer, psize);

	return 0;
}

