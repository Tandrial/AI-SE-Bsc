C++ = g++
COPS = -Wall -std=c++11

make: task1 task2 

clean:
	rm task1 task2 task2_bonus

cleanwin:
	del *.exe *.o

task1:
	$(C++) $(COPS) -o task1 signals.cpp

task2:
	$(C++) $(COPS) -o task2 rb_tests.cpp
	$(C++) $(COPS) -o task2_bonus rb_tests_bonus.cpp
