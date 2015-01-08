#include <iostream>
#include "stopwatch.hpp"

StopWatch sw;
// Macro that times the function func by calling it 10 Million times
#define TIME(func)\
	sw.start();\
	for(int i = 0; i < 10000000; i++) { func }\
	std::cout << #func << "\t" << sw.stop() << std::endl;

// StaticBase with a static member function and a static int
class StaticBase {
private:
	static int n;
public:
	static void inc() {
		n++;
	}
};

// init for the static int
int StaticBase::n = 0;

// PureBase with a ctor, a member functiona and an int
class PureBase {
private:
	int n;
public:
	PureBase() {
		n = 0;
	}
	void inc() {
		n++;
	}
};

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
class VirtDer: public VirtBase {
private:
	int derN;
public:
	VirtDer() :	VirtBase() {
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
	PureBase pure;
	VirtBase vbase;
	VirtDer vder;
	PureBase *ppure = new PureBase();
	VirtBase *pvbase = new VirtBase();
	VirtDer *pvder = new VirtDer();
	VirtBase *pvbaseder = pvder;
	for (int i = 1; i <= 5; i++) {
		std::cout << "Round " << i << std::endl;
		// PureBase non-virtual member function call: directly and with a pointer
		TIME(pure.inc();)
		TIME(ppure->inc();)
		std::cout << std::endl;

		// VirtBase non-virtual member function call: directly and with a pointer
		TIME(vbase.inc();)
		TIME(pvbase->inc();)
		std::cout << std::endl;

		// VirtBase virtual member function call: directly and with a pointer
		TIME(vbase.virtInc();)
		TIME(pvbase->virtInc();)
		std::cout << std::endl;

		// VirtDer virtual member function call: directly and with a pointer
		TIME(vder.virtInc();)
		TIME(pvder->virtInc();)
		std::cout << std::endl;

		// VirtDer non-virtual member function call inside the base class: directly and with a pointer
		TIME(vder.inc();)
		TIME(pvder->inc();)
		std::cout << std::endl;

		// VirtDer non-virtual member function call: directly and with a pointer
		TIME(vder.derInc();)
		TIME(pvder->derInc();)
		std::cout << std::endl;

		// VirtBase pointer to a VirtDer object
		// calls VirtBase::inc()
		TIME(pvbaseder->inc();)
		// calls VirtDer::virtInc()
		TIME(pvbaseder->virtInc();)
		std::cout << std::endl;

		// StaticBase static function call
		TIME(StaticBase::inc();)
	}
	return 0;
}
