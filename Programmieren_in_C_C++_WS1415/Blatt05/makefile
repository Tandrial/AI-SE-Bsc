C++ = g++
COPS = -Wall -std=c++11

make: task1 task2 task3

clean:
	rm task1 task2 task3

cleanwin:
	del *.exe *.o

task1:
	$(C++) $(COPS) -D DEBUG_FLAG -o task1 stack.cpp stacktest.cpp 

task2:
	$(C++) $(COPS) -o task2 task2.cpp

task3: 
	$(C++) $(COPS) -o task3 stack.cpp stopwatch.cpp task3.cpp