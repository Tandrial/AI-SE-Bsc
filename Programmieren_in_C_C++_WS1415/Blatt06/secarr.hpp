#ifndef SECARR_H
#define SECARR_H

class SecArr {

private:
	// The Adress of the first, last and current element
	int *start;
	int *end;
	int *pos;

public:
	// constructor that takes the first and last element of an array
	SecArr(int*, int*);

	// Operators for access and manipulation of the array

	// * gives access to the current element
	// returns a ref so that manipulation of the element is possible
	int& operator*() const;
	// [] gives access to the current element + and offset
	// returns a ref so that manipulation of the element is possible
	int& operator[](const int) const;

	// pre/post-Increment
	// returns a reference to the object, so that operators and be chained
	SecArr& operator++();
	// needs to return by value, since the old value is return and not the new one
	SecArr operator++(int);

	// pre/post-Decrement same as Increment
	SecArr& operator--();
	SecArr operator--(int);
};

#endif //SECARR_H