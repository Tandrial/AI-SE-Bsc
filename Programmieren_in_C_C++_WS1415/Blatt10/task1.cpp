#include <iostream>
#include <vector>
#include <deque>
#include <list>
#include <algorithm>

// Comment the next line to disable the invalide iterator bug-fix
#define POSFIX

//fills the given container with all even numbers from 2 to 64
template <class T>
void fill(T &t) {	
	for(int i = 2; i <= 64; i+=2) {
		t.push_back(i);
	}
}

// prints the contants of the container
template <class T>
void print(T const &t) {	
	// T::value_type is of the type which is stored inside the container
	for (typename T::value_type x : t) {
		std::cout << x << " ";
	}
	std::cout << std::endl;
}

// finds a position inside the container where newElem can be inserted
// so that the container is still sorted
// the new element would be inserted between before and pos
template <class T>
void findposition(T &t, long newElem, typename T::iterator &before, typename T::iterator &pos) {
	for(before = pos = t.begin(); pos != t.end(); pos++) {
		if (*pos > newElem)
			break;
		before = pos;
	}
}

// prints the next 3 elements starting from iter
template <class T>
void printnext3(T &t, typename T::iterator iter) {	
	// we need to check if there is an element left to print
	for (int cnt = 3; iter != t.end() && cnt > 0; iter++, cnt--) {
		std::cout << *iter << " ";	
	}
	std::cout << std::endl;
}

// Predicate that decides if the passed number is odd
template<class T>
struct isOdd {
	bool operator()(const T& i) {
		return ((i % 2) == 1);
	}
};

// removes all odd elements from the container
template <class T> 
void deleteodd(T &t) {
	// remove_if works by moving all elements for which the predicate 
	// returns true to the front, while still keeping the relative order
	// e. g.  if 20 was before 30 BEFORE the call, 20 will be before 30 AFTER the call
	typename T::iterator iter = remove_if(t.begin(), t.end(),
		// functor: instance of the struct isOdd
	//  isOdd<typename T::value_type>());
		//lambda function: with one Parameter and a bool return type ==> Predicate function
		[](typename T::value_type& i) {return (i % 2) == 1;});

	// iter now points to the 1. element for which the predicate returned false
	// so we erase from that point to the last element
	t.erase(iter, t.end());
}

int main(void) {
	std::vector<long> vec;
	std::deque<long> deq;
	std::list<long> list;
	std::cout << "Container being filled with even numbers 2 <= x <= 64:" << std::endl;
	fill(vec); fill(deq); fill(list); 
	print(vec); print(deq);	print(list);

	std::vector<long>::iterator vecIter = vec.begin();
	std::deque<long>::iterator deqIter = deq.begin();
	std::list<long>::iterator listIter = list.begin();

	std::cout << "Iter moved to the third element:" << std::endl;
	vecIter += 2;
	deqIter += 2;
	//list doesn't have a random access modifier, so we need to move the iter one by one
	++++listIter;

	std::cout << "vector[2] = " << *vecIter << std::endl;
	std::cout << "deque[2]  = " << *deqIter << std::endl;
	std::cout << "list[2]   = " << *listIter << std::endl;

	std::vector<long>::iterator vecBefore, vecPos;
	std::deque<long>::iterator deqBefore, deqPos;
	std::list<long>::iterator listBefore, listPos;
	std::cout << "Inserting 31 into the container:" << std::endl;
	findposition(vec, 31, vecBefore, vecPos);
	findposition(deq, 31, deqBefore, deqPos);
	findposition(list, 31, listBefore, listPos);

	// fix as described in the report
	#ifndef POSFIX
		vec.insert(vecPos, 31);
		deq.insert(deqPos, 31);
	#else
		// the resulting iterator from insert is valid because it's "from AFTER the insert"
		vecBefore = vec.insert(vecPos, 31);
		deqBefore = deq.insert(deqPos, 31);
		// the result of insert points to the inserted element
		// before we can safely move it to the element before we need to check if it isn't
		// already the first
		if (vecBefore != vec.begin())
			--vecBefore;

		if (deqBefore != deq.begin())
			--deqBefore;
	#endif

	// list is unaffacted
	list.insert(listPos, 31); 	

	printnext3(vec, vecBefore);
	printnext3(deq, deqBefore);
	printnext3(list, listBefore);

	std::cout << "Containers with 31 inserted:" << std::endl;
	print(vec); print(deq);	print(list);
	deleteodd(vec); deleteodd(deq); deleteodd(list);
	std::cout << "Containers with all odd Numbers removed:" << std::endl;
	print(vec); print(deq);	print(list);

	return 0;
}
