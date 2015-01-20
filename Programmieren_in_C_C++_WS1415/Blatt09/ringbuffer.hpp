#include <iostream>
#include <stdexcept>

template <typename T, int size>
class RingBuffer {
private:
    // values is the array that holds all the elements
    T values[size];
    // front points to the first element in the buffer
    // back points past the last element in the buffer
    // count holds the amount of elements in the buffer
    int front, back, count;
public:
    RingBuffer() : front(0), back(0), count(0) { };
    void push_front(const T&);
    void push_back(const T&);
    T pop_front();
    T pop_back();    
    // if there are 0 elements in the buffer it is empty
    bool empty() const { return count == 0; };    
    // if there are size-elements in the buffer it is full
    bool full() const { return count == size; };
    void print() const;
};

// adds an element at the front of the buffer
template <typename T, int size>
void RingBuffer<T,size>::push_front(const T& elem) {
    // if there is no space left we silently drop the operation
    if (full())
        return;
    // since front is points directly to the first element, we first decrease
    // the pointer and then set the new element
    front = (front + size - 1) % size;
    values[front] = elem;
    // There is now one more element in the buffer
    count++;
}   

// adds an element at the back of the buffer
template <typename T, int size>
void RingBuffer<T,size>::push_back(const T& elem) {
    // if there is no space left we silently drop the operation
    if (full())
        return;
    // back points to an empty space at the end of the queue, so we first set
    // the new element and then increase the pointer
    values[back] = elem;
    back = (back + 1) % size;
    // There is now one more element in the buffer
    count++;
}

// returns and removes the first element form the queue
template <typename T, int size>
T RingBuffer<T,size>::pop_front() {
    // if the buffer is empty we throw an exception
    if (empty())
        throw std::runtime_error("pop_front called with an empty buffer!");
    // There is now one less element in the buffer
    count--;
    // save the old index
    int elemPos = front;
    // increase the pointer so that it points to the new first element
    front = (front + 1) % size;
    // return the element at the old index
    return values[elemPos];
}

// returns and removes the last element from the queue
template <typename T, int size>
T RingBuffer<T,size>::pop_back() {
    // if the buffer is empty we throw an exception
    if (empty())
        throw std::runtime_error("pop_back called with an empty buffer!");
    // There is now one less element in the buffer
    count--;
    // since the back points to the element past the last element, we first 
    // decrease the back pointer and then return the last element
    back = (back + size - 1) % size;
    return values[back];
}

// prints the whole buffer
template <typename T, int size> 
void RingBuffer<T,size>::print() const {
    // If the buffer is empty there is nothing to print
    if (empty()) {
        std::cout << "The buffer is empty!" << std::endl;
    } else {
        std::cout << "Buffer content:" << std::endl;
        int i;
        // we start from the from element and print all count element
        for(i = 0; i < count; i++) {
            std::cout << values[(front + i) % size] << std::endl;
        }
    }
}
