#include <iostream>

#if defined(WIN32) || defined(_WIN32) || defined(__WIN32) && !defined(__CYGWIN__)
	#include <windows.h>
	#define ISWIN
#else
	#include <time.h>
#endif

class StopWatch {
	private:
		#ifdef ISWIN
			LONGLONG g_Frequency, start_win, stop_win;
		#else
			struct timespec start_linux, stop_linux;
		#endif
 		
 		double time_diff;
  	public:
  		StopWatch();
  		void start();
  		double stop();
};

#ifdef ISWIN
	StopWatch::StopWatch() {
		QueryPerformanceFrequency((LARGE_INTEGER*) &g_Frequency);
	}

	void StopWatch::start() {
    	QueryPerformanceCounter((LARGE_INTEGER*) &start_win);
	}

	double StopWatch::stop() {				
    		QueryPerformanceCounter((LARGE_INTEGER*) &stop_win);
			time_diff =  ((double) (stop_win - start_win)) / (double) g_Frequency;
			return time_diff;		
	}
#else
	StopWatch::StopWatch() {
		start_t = NULL;
	}
	
	void StopWatch::start() {		
		clock_gettime(CLOCK_MONOTONIC, &start_linux);
	}

	double StopWatch::stop() {
		if (start_linux != NULL) {
			clock_gettime(CLOCK_MONOTONIC, &stop_linux);
			time_diff =  (stop_linux.tv_sec  - start_linux.tv_sec) + 
						((stop_linux.tv_nsec - start_linux.tv_nsec) / 1000000000.0);			
			return time_diff;
		} else {
			return -1;
		}
	}
#endif


int main(void) {
	StopWatch sw;
	sw.stop();
	sw.start();
	sw.start();
	for (int i=0; i <100000000; i++) ;
	std::cout << "Duration: " << sw.stop() << std::endl;
	return 0;
}
