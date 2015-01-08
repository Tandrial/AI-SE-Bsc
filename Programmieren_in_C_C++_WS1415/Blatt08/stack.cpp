#include <iostream>
#include <exception>

class StackException : public std::exception {
private:
	const char* errMsg;
public:
	StackException(const char* s) : errMsg(s) { }
	const char* what() const throw()  { return errMsg; }
};

template <typename T, int size>
class Stack {
	public:
	Stack();
	bool isfull();
	bool isempty();
	void push(T);
	T pop();
	private:
	T stack[size];
	int count;
};

template <typename T, int size>
Stack<T, size>::Stack() {
	count = 0;
}
template <typename T, int size>
bool Stack<T, size>::isfull() {
	return count == size;
}

template <typename T, int size>
bool Stack<T, size>::isempty() {
	return count == 0;
}

template <typename T, int size>
void Stack<T, size>::push(T v) {
	if (!isfull())
	stack[count++] = v;
	else
	throw StackException("Stack is full");
}

template <typename T, int size>
T Stack<T, size>::pop() {
	if (!isempty())
	return stack[--count];
	throw StackException("Stack is empty");
}

int main(void) {
	Stack<int,4> mystack;
	std::cout << "Empty: " << mystack.isempty() << ", Full " << mystack.isfull() << std::endl;
	try {
		mystack.push(1);
		std::cout << "Empty: " << mystack.isempty() << ", Full " << mystack.isfull() << std::endl;
		mystack.push(2);
		mystack.push(3);
		std::cout << "Empty: " << mystack.isempty() << ", Full " << mystack.isfull() << std::endl;
		mystack.push(4);
		std::cout << "Empty: " << mystack.isempty() << ", Full " << mystack.isfull() << std::endl;
		mystack.push(5);  
	} catch (std::  exception& e) {
		std::cout << "Exception occured: " << e.what() << std::endl;
	}
	try {
		std::cout << "Empty: " << mystack.isempty() << ", Full " << mystack.isfull() << std::endl;
		std::cout << "Pop " << mystack.pop() << std::endl;
		std::cout << "Empty: " << mystack.isempty() << ", Full " << mystack.isfull() << std::endl;
		std::cout << "Pop " << mystack.pop() << std::endl;
		std::cout << "Pop " << mystack.pop() << std::endl;
		std::cout << "Empty: " << mystack.isempty() << ", Full " << mystack.isfull() << std::endl;
		std::cout << "Pop " << mystack.pop() << std::endl;
		std::cout << "Empty: " << mystack.isempty() << ", Full " << mystack.isfull() << std::endl;
		std::cout << "Pop " << mystack.pop() << std::endl;
		std::cout << "Empty: " << mystack.isempty() << ", Full " << mystack.isfull() << std::endl;
	} catch (std::  exception& e) {
		std::cout << "Exception occured: " << e.what() << std::endl;
	}
}
