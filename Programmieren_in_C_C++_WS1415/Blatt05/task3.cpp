#include <iostream>
#include "stack.hpp"
#include "stopwatch.hpp"

// Helper Methode to return a "rValue Stack"
Stack createStack() {
    Stack s(10);
    return s;
}

int main () {
    int i, iter = 10000000;
    StopWatch sw;

    //Stack to copy in Test1
    Stack copyStack = createStack();

    //Stack used for testing
    Stack stack(10); 

    // Test 1: copy assignment operator
    sw.start();
    for (i = 0; i < iter;i++)
        stack = copyStack;
    std::cout << "Copying 10,000,000 stacks took " << sw.stop() << "s\n";

    // Since we want to only time the move and not the create in the methode
    // we calculate the time is takes to create the stacks and subtract later
    sw.start();
    for (i = 0; i < iter;i++) {
        createStack();
    }       
    double create_time = sw.stop();

    // Test 2: move assignment operator 
    sw.start();
    for (i = 0; i < iter;i++) {
        stack = createStack();
    }
    // time for the move operator is (measured time) - (the time it takes to create the stacks)
    std::cout << "Moving  10,000,000 stacks took " << (sw.stop() - create_time) << "s\n";
    return 0;
}
