#include <iostream>
#include <time.h>

class StopWatch {
	private:
		// holds the startpoint
		clock_t start_t;
	public:
		StopWatch() {
			// explicitly set to -1 so we can check if StopWatch is running
			start_t = -1;
		}

		void start() {
			if (start_t != -1) {
				std::cout << "start() was already called!\n";
			} else {
				// get current clock tick amount
				start_t = clock();	
			}				
		}
		
		double stop() {
			// check if start() war run at some time
			if (start_t == -1) {
				// else return error
				std::cout << "start() needs to be called before stop()!\n";
				return -1;
			} else {
				// (current clock ticks) - (start clock ticks) / clock ticks per second
				// 	==> result is the time in seconds.
				double time_diff = (double)(clock() - start_t) / CLOCKS_PER_SEC;
				start_t = -1;
				return time_diff;
			}
		}
};

int main(void) {
	StopWatch sw;
	sw.stop();	
	sw.start();
	sw.start();
	for (int i=0; i <100000000; i++) ;
	std::cout << "Duration: " << sw.stop() << std::endl;
	return 0;
}
