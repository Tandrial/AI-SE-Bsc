#include "stopwatch.hpp"
#include <iostream>

StopWatch::StopWatch() {
	// explicitly set to -1 so we can check if StopWatch is running
	start_t = -1;
}

void StopWatch::start() {
	if (start_t != -1) {
		std::cout << "start() was already called!\n";
	} else {
		// get current clock tick amount
		start_t = clock();
	}
}

double StopWatch::stop() {
	// check if start() war run at some time
	if (start_t == -1) {
		// else return error
		std::cout << "start() needs to be called before stop()!\n";
		return -1;
	} else {
		// (current clock ticks) - (start clock ticks) / clock ticks per second
		//  ==> result is the time in seconds.
		double time_diff = (double) (clock() - start_t) / CLOCKS_PER_SEC;
		start_t = -1;
		return time_diff;
	}
}
