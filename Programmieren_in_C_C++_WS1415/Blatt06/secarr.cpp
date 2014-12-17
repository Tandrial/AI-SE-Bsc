#include <iostream>
#include "secarr.hpp"

SecArr::SecArr(int *start, int *end) {
	this->start = start;
	this->pos = this->start;
	// check to see that the first element is before the last element 
	if (end < start) {
		std::cout << "The last element is located BEFORE the first one! setting end = start!" << std::endl;
		this->end = start;
	} else {
		this->end = end;
	}
}

int& SecArr::operator*() const {
	// returns the current element as a reference
	return *pos;
}

int& SecArr::operator[](const int offset) const{
	// Check if the offset would be out of bounds
	if ((pos + offset) > end || (pos + offset) < start) {
		// if it is the last element is returned
		std::cout << "Offset is out of Bounds! Returning last element" << std::endl;
		return *end;
	} else {
		// otherwise the current + offset element is returned
		return *(pos + offset);
	}
}

SecArr& SecArr::operator++() {
	// Check if the increment would put the current element out of bounds
	if ((pos + 1) > end) {
		std::cout << "The element is out of Bounds! Increment ignored!" << std::endl;
	} else {
		pos++;
	}
	// return a reference to the object
	return *this;
}

SecArr SecArr::operator++(int) {
	// Copy the object
	SecArr old = *this;
	// Check if increment would put the current element out of bounds
	if ((pos + 1) > end) {
		std::cout << "The element is out of Bounds! Increment ignored!" << std::endl;
	} else {
		pos++;
	}
	// return the copied object with the old value
	return old;
}

// -- is basically the same as ++, 
SecArr& SecArr::operator--() {
	if ((pos - 1) < start) {
		std::cout << "The element is out of Bounds! Decrement ignored!" << std::endl;
	} else {
		pos--;
	}
	return *this;
}

SecArr SecArr::operator--(int) {
	SecArr old = *this;
	if ((pos - 1) < start) {
		std::cout << "The element is out of Bounds! Decrement ignored!" << std::endl;
	} else {
		pos--;
	}
	return old;
}