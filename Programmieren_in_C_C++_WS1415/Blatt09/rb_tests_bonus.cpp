#include <iostream>
#include <stdexcept>
#include "ringbuffer_bonus.hpp"

int main() {
    RingBuffer<int,4> rb;
    rb.push_front(1);
    rb.print(); std::cout << std::endl;
    rb.push_front(2);
    rb.print(); std::cout << std::endl;
    rb.push_front(3);
    rb.print(); std::cout << std::endl;
    rb.push_front(4);
    rb.print(); std::cout << std::endl;
    rb.push_front(5);
    rb.print(); std::cout << std::endl;
    std::cout << std::endl;
    try {
        std::cout << rb.pop_front() << std::endl;
        std::cout << rb.pop_front() << std::endl;
        std::cout << rb.pop_front() << std::endl;
        std::cout << rb.pop_front() << std::endl;
        std::cout << rb.pop_front() << std::endl;
    } catch (std::  exception& e) {
        std::cout << "Exception occured: " << e.what() << std::endl;
    }

    rb.push_front(1);
    rb.print(); std::cout << std::endl;
    rb.push_back(2);
    rb.print(); std::cout << std::endl;
    rb.push_front(3);
    rb.print(); std::cout << std::endl;
    rb.push_back(4);
    rb.print(); std::cout << std::endl;
    rb.push_front(5);
    rb.print(); std::cout << std::endl;
    std::cout << std::endl;
    try {
        std::cout << rb.pop_back() << std::endl;
        std::cout << rb.pop_back() << std::endl;
        std::cout << rb.pop_front() << std::endl;
        std::cout << rb.pop_back() << std::endl;
        std::cout << rb.pop_front() << std::endl;
    } catch (std::  exception& e) {
        std::cout << "Exception occured: " << e.what() << std::endl;
    }
    return 0;
}