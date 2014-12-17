#include "mytime.hpp"
#include <iostream>

//std. constructor
MyTime::MyTime(int h = 0, int m = 0, int s = 0) {
	this->hour = h;	
	this->minute = m;
	this->second = s;	
	fixOverflow();
}

// Makes sure that minutes and seconds are in the correct interval
void MyTime::fixOverflow() {	
	// if second is bigger then 60 the result of the int division is added to minutes
	// second itself become second mod 60
	// e. g. second = 75
	// minute += (75 / 60) ==> 1
	// second = (75 mod 60) ==> 15
	if (second >= 60) {		
		minute += second / 60;
		second = second % 60;
	}	
	// same as above
	if (minute >= 60) {
		hour += minute / 60;
		minute = minute % 60;
	}
}

// Getter for hour, minute and second
int MyTime::getHour() const {
	return this->hour;
}

int MyTime::getMinute() const {
	return this->minute;
}

int MyTime::getSecond() const {
	return this->second;
}

// MyTime += int ==> the int is added as seconds to MyTime
MyTime& MyTime::operator+=(const int rhs) {
	second += rhs;
	// check if seconds is now >= 60
	fixOverflow();
	return *this;
}

// MyTime += MyTime ==> hours are added to hours, minutes to minutes and seconds to seconds
MyTime& MyTime::operator+=(const MyTime& rhs) {
	second += rhs.second;
	minute += rhs.minute;
	hour += rhs.hour;
	fixOverflow();
	return *this;
}

// int + MyTime ==> the int is added as seconds to MyTime
// and the result is return as a new object
MyTime operator+(const int& lhs, const MyTime& rhs) {
	return MyTime(rhs.hour, rhs.minute, rhs.second + lhs);	
}
// MyTime + int ==> the int is added as seconds to MyTime
// and the result is return as a new object
MyTime operator+(const MyTime& lhs, const int& rhs) {
	return MyTime(lhs.hour, lhs.minute, lhs.second + rhs);	
}

// MyTime + MyTime ==> hours are added to hours, minutes to minutes and seconds to seconds
MyTime operator+(const MyTime& lhs, const MyTime& rhs) {
	return MyTime(lhs.hour + rhs.hour,
				  lhs.minute + rhs.minute,
	 			  lhs.second + rhs.second);	
}

// Overloaded << operator so MyTime can be used in outputStreams
std::ostream& operator<<(std::ostream& lhs, const MyTime& rhs) {
	lhs <<  rhs.hour << "h:" << rhs.minute << "m:" << rhs.second << "s";
	return lhs;
}

