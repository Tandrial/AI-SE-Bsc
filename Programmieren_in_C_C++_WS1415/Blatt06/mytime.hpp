#include <iostream>

// make sure MyTime is only included once
#ifndef MYTIME_H
#define MYTIME_H

class MyTime {
private:
	int hour;
	int minute;
	int second;
	void fixOverflow();
public:

	MyTime(int, int, int);

	MyTime& operator+=(const int);
	MyTime& operator+=(const MyTime&);

	// function to convert from MyTime to int
	operator int() const { return second + 60 * minute + 3600 * hour; }

	// The following ops need to be declared as friend, since we directly want to 
	// use the hour, minute and second field
	
	friend std::ostream& operator<<(std::ostream& os, const MyTime& t);
	
	friend MyTime operator+(const MyTime&, const MyTime&);
	friend MyTime operator+(const int&, const MyTime&);
	friend MyTime operator+(const MyTime&, const int&);	

	// Getter
	int getHour() const;
	int getMinute() const;
	int getSecond() const;
};

#endif //MYTIME_H