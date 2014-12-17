#include <cstring>
#include <iostream>

// make sure Stack is only included once
#ifndef STACK_H
#define STACK_H

// to get the output for task1 compile with 
//   g++ -Wall -std=c++11 -D DEBUG_FLAG -o task1 stack.cpp stacktest.cpp 
// or uncomment the following line:

// #define DEBUG_FLAG

// debug stuff for task 3:
// if the debug flag is set the debug message is printed otherwise it's discarded
#ifdef DEBUG_FLAG
#define DEBUG(msg) std::cout << #msg << std::endl;
#else
#define DEBUG(msg) 
#endif

class Stack {
    private:
        // pos points to where the next element is going to be written to
        int pos;
        // size save the maximum size of the stack
        int size;
        // data holds the items currently on the stack
        int *data;
    public:
        // std. constructor 
        // explicit stops the compiler from trying to implicity convert
        // from different types to Stack
        explicit Stack(int);        
        // Alternative: explicitly delete the =-operator for ints
        //Stack& operator=(const int) = delete;
        // destructor
        ~Stack();
        // copy constructor
        Stack(const Stack&);
        // copy assignment operator
        Stack& operator=(const Stack&);        
        // move constructor
        Stack(Stack&&);
        // move assignment operator
        Stack& operator=(Stack&&);

        bool isfull();
        bool isempty();
        void push(int);
        int pop();
};

#endif  //STACK_H
