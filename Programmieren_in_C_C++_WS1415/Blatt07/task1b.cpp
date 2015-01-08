#include <iostream>
#include <typeinfo>
#include "stopwatch.hpp"

StopWatch sw;
// Macro that times the function func by calling it 10 Million times
#define TIME(func)\
	sw.start();\
	for(int i = 0; i < 10000000; i++) { func }\
	std::cout << #func << "\t" << sw.stop() << std::endl;

// VirtBase with a ctor, with a virtual and a non-virtual member function and an int
class VirtBase {
private:
	int n;
public:
	VirtBase() {
		n = 0;
	}
	virtual void virtInc() {
		n++;
	}
	void inc() {
		n++;
	}
};

// VirtDer derives from VirtBase, overrides the virtual function, adds another non-virtual member funciton and another int
class VirtDer : public VirtBase {
private:
	int derN;
public:
	VirtDer() : VirtBase() {
		derN = 0;
	}
	virtual void virtInc() {
		derN++;
	}
	void derInc() {
		derN++;
	}
};

int main() {
	// same definition as in task1a
	VirtDer *pvder = new VirtDer();
	VirtBase *pvbaseder = pvder;
	// object that holds the result of the casts
	VirtDer *castvder;

	// Method 1: dynamic_cast checks whether a conversion is possible
	//           if it is it returns a pointer of the chosen type to the argument
	//           if not it returns a NULL pointer
	TIME(castvder = dynamic_cast<VirtDer*>(pvbaseder); 
		 if (castvder != NULL)
		 	castvder->inc();)

	// Method 2: typeid expects an object or class and returns the name of it
	//           a static_cast returns a pointer of the chosen type to the argument
	//           compared to dynamic_cast there are NO runtime-checks performed.
	TIME(if (typeid(*pvbaseder) == typeid(VirtDer))
			static_cast<VirtDer*>(pvbaseder)->derInc();)
	return 0;
}
