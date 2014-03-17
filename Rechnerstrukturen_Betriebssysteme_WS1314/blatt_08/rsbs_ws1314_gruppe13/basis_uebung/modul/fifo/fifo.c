/**
 * 	@file 	fifo.c
 */

#include "fifo.h"
#include <string.h>

/**
 * @brief 		Bildet den Median für 16 Bit Buffer.
 *
 * @param bs	Pointer auf den Ringbuffer.
 * @return		Median des Ringbuffers.
 */
static uint16_t fifo_get_median16(fifo_struct *bs);


FIFO_RESULT fifo_init(fifo_struct *bs, uint8_t elementsize, void *buf, uint16_t total_elements)
{
	uint32_t tmp = elementsize * total_elements;
	if(elementsize<1)	return FIFO_ELEMENTSIZE_INVALID;
	if(tmp>=65536)		return FIFO_BUFFERSIZE_INVALID;
	bs->data = (uint8_t*)buf;
	bs->max_elements = total_elements;
	bs->element_size = elementsize;
	bs->max_len = total_elements * elementsize;
	fifo_clear(bs);
	return FIFO_OK;
}

inline void fifo_clear(fifo_struct *bs)
{
	bs->read_pos = 0;
	bs->write_pos = 0;
	bs->entries = 0;
}

inline bool fifo_put8(fifo_struct*bs, uint8_t c)
{
	return fifo_put(bs, (uint8_t*)&c);
}

inline bool fifo_put16(fifo_struct*bs, uint16_t c)
{
	return fifo_put(bs, (uint8_t*)&c);
}

inline bool fifo_put32(fifo_struct*bs, uint32_t c)
{
	return fifo_put(bs, (uint8_t*)&c);
}

bool fifo_put(fifo_struct *bs, uint8_t* c)
{
	mcu_disable_interrupt();
	if(bs->entries<bs->max_elements)
	{
		memcpy(bs->data+bs->write_pos, c, bs->element_size);
		bs->write_pos = (bs->write_pos+bs->element_size)%bs->max_len;
		(bs->entries)++;
		mcu_enable_interrupt();
		return true;
	}
	mcu_enable_interrupt();
	return false;
}

bool fifo_get(fifo_struct *bs, uint8_t* c)
{
	mcu_disable_interrupt();
	if(bs->entries>0)
	{
		memcpy(c, bs->data+bs->read_pos, bs->element_size);
		bs->read_pos = (bs->read_pos+bs->element_size)%bs->max_len;
		(bs->entries)--;
		mcu_enable_interrupt();
		return true;
	}
	mcu_enable_interrupt();
	return false;
}

inline uint8_t fifo_get8(fifo_struct* bs)
{
	uint8_t c = 0;
	fifo_get(bs, (uint8_t*)&c);
	return c;
}

inline uint16_t fifo_get16(fifo_struct* bs)
{
	uint16_t c = 0;
	fifo_get(bs, (uint8_t*)&c);
	return c;
}

inline uint32_t fifo_get32(fifo_struct* bs)
{
	uint32_t c = 0;
	fifo_get(bs, (uint8_t*)&c);
	return c;
}


inline uint16_t fifo_data_available(fifo_struct *bs)
{
	return bs->entries;
}


inline bool fifo_is_full(fifo_struct *bs)
{
	return (bs->entries>=bs->max_elements);
}

uint32_t fifo_get_average(fifo_struct *bs)
{
	uint32_t c = 0;
	uint32_t average = 0;
	uint16_t len = bs->entries;
	while(fifo_get(bs, (uint8_t*)&c))
		average+=c;
	average/=len;
	return average;
}

uint32_t fifo_get_median(fifo_struct *bs)
{
	/// @todo Median Funktion um Funktionen für 8 Bit und 32 Bit erweitern.
	switch(bs->element_size)
	{
		case 2:		return fifo_get_median16(bs);
		default: 	return 0;
	}
}

static uint16_t fifo_get_median16(fifo_struct *bs)
{
	uint16_t c = 0;
	uint16_t median = 0;
	uint16_t len = bs->entries;
	uint16_t i, j;
	uint16_t* b = (uint16_t*)bs->data;
	if(len==0)	return 0;
	if(len<=2)	return b[0];
	for(i=0; i<len-1; i++)
	{
		for(j=i+1; j<len; j++)
		{
			if(b[i]<b[j])
			{
				c = b[i];
				b[i] = b[j];
				b[j] = c;
			}
		}
	}
	median = b[len/2];
	fifo_clear(bs);
	return median;
}
