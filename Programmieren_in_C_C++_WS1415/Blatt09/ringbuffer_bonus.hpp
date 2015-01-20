#include <iostream>
#include <stdexcept>

template <typename T, int size>
class RingBuffer {
private:
    // values is the array that holds all the elements
    // the size is increased by 1 for the bonus
    // The extra space is never filled, but it is used to distinguish
    // between an empty and a full buffer.
    T values[size + 1];
    // front points to the first element in the buffer
    // back points past the last element in the buffer
    int front, back;
public:
    RingBuffer() : front(0), back(0) { };
    void push_front(const T&);
    void push_back(const T&);
    T pop_front();
    T pop_back();
    // The buffer is empty if front and back point to the same element
    bool empty() const { return front == back; };
    // The buffer is full if after the next push_{front,back} the back and front
    // would point to the same element
    bool full() const { return (back + 1) % (size + 1) == front; };
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
    // here size + 1 is used because internally the buffer is one bigger
    front = (front + size) % (size + 1);
    values[front] = elem;
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
    // here size + 1 is used because internally the buffer is one bigger
    back = (back + 1) % (size + 1);
}

// returns and removes the first element form the queue
template <typename T, int size>
T RingBuffer<T,size>::pop_front() {
    // if the buffer is empty we throw an exception
    if (empty())
        throw std::runtime_error("pop_front called with an empty buffer!");
    // save the old index
    int elemPos = front;
    // increase the pointer so that it points to the new first element
    // here size + 1 is used because internally the buffer is one bigger
    front = (front + 1) % (size + 1);
    // return the element at the old index
    return values[elemPos];
}

// returns and removes the last element from the queue
template <typename T, int size>
T RingBuffer<T,size>::pop_back() {
    // if the buffer is empty we throw an exception
    if (empty())
        throw std::runtime_error("pop_back called with an empty buffer!");
    // since the back points to the element past the last element, we first 
    // decrease the back pointer and then return the last element
    // here size + 1 is used because internally the buffer is one bigger
    back = (back + size) % (size + 1);
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
        // we start from the first element and print as long as we 
        // haven't reached the back pointer
        // here size + 1 is used because internally the buffer is one bigger
        for(i = front; i != back; i = (i + 1) % (size + 1)) {
            std::cout << values[i] << std::endl;
        }
    }
}
