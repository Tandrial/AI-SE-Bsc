// The Header needs to be included
#include "stack.hpp"

// std. constructor
// size defaults to 10 if no argument is passed
Stack::Stack(int size = 10) {
    DEBUG(Std. constructor called!)
    //set the initial values and create an int-array
    pos = 0;
    this->size = size;
    data = new int[size]; 
}

// copy constructor
Stack::Stack(const Stack& that) {
    DEBUG(Stack copy constructor called!)
    // Copy the size/pos information of the stack
    size = that.size;
    pos = that.pos;
    // the old data is automatically delete inside a copy constructor
    // so we only need to create a new int-array
    data = new int[size];
    // copy the elements of the array
    memcpy(data, that.data, sizeof(int) * size);
}

// copy assignment operator
Stack& Stack::operator=(const Stack& that) {
    DEBUG(Stack copy assign op called!)
    // If the stack is assigned to itself, we're done
    // e. g.   stack1 = stack1;
    if (this != &that) {
        // otherwise copy the size/pos information of the stack
        size = that.size;
        pos = that.pos;
        // here the old data array has to be explicity deleted
        if (data != NULL)
            delete[] data;
        // create new array and copy the elements
        data = new int[size];
        memcpy(data, that.data, sizeof(int) * size);
    }
    return *this;
}

// move constructor
Stack::Stack(Stack&& that) {
    DEBUG(Stack move constructor called!)
    // copy the int values
    size = that.size;
    pos = that.pos;
    // the old data is automatically delete inside a move constructor
    // copy the ref to the new data array
    data = that.data;

    // invalidate the pointer to the int-array of the other stack
    // so the descturor doesn't delete the array
    that.data = NULL;
}

// move assignment operator
Stack& Stack::operator=(Stack&& that) {
    DEBUG(Stack move assign op called!)
    // If the stack is somehow assigned to itself, we're done
    if (this != &that) {
        // otherwise copy the int values
        size = that.size;
        pos = that.pos;
        //as with the other assignment op data needs to be explicitly deleted
        if (data != NULL)
            delete[] data;
        // copy the ref to the new data array
        data = that.data;

        // invalidate the pointer to the int-array of the other stack
        // so the descturor doesn't delete the array
        that.data = NULL;
    }
    return *this;
}

Stack::~Stack() {
    DEBUG(Stack destructor called!)
    // if the data array was set we need to delete it
    if (data != NULL) {
        DEBUG(\t...deleted array!\n)
        delete[] data;
    }
}

// The stack is full if the next element would be written out of bounds 
bool Stack::isfull(){
    return pos == size;
}

// The stack is empty if the next element would be written to pos 0
bool Stack::isempty() {
    return pos == 0;
}

// If the Stack is not full, whe save the pushed value and increment pos

void Stack::push(int value) { 
    if (!isfull()) {
        data[pos++] = value;
    } else {
        // otherwwise print an error-message
        std::cout << "Stack is full, push failed!" << std::endl;
    }
}

// If the Stack is not empty, we decrement pos and return the top element 
int Stack::pop() {
    if (!isempty()) {
        return data[--pos];
    } else {
        // otherwwise print an error-message
        std::cout << "Stack is empty, pop failed!" << std::endl;
        return 0;
    }
}
