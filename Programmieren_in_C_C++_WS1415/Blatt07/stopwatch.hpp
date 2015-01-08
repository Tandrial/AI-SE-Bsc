#ifndef STOPWATCH_H
#define STOPWATCH_H
#include <time.h>

class StopWatch {
private:
	clock_t start_t;
public:
	StopWatch();
	void start();
	double stop();
};

#endif  //STOPWATCH_H
