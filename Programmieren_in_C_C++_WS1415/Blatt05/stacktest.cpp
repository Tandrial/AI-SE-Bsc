#include <iostream>
#include "stack.hpp"

Stack createStack(int size) {
  // it needs to be this complicated to avoid copy elision, at least on my gcc 4.7.3
  Stack s1(size);
  Stack s2(size);
  for (int i = 1; i <= size; i++) {
    s1.push(2*i);
    s2.push(2*i-1);
  }
  std::cout << "Returning new stack" << std::endl;
  if (size % 2)
    return s1;
  else
    return s2;
}

Stack createEven(int size) {
  Stack s(size);
  for (int i = 1; i <= size; i++)
    s.push(2*i);
  std::cout << "Returning new stack" << std::endl;
  return s;
}

int main() {
  std::cout << "-----std ctor-----" << std::endl;
  Stack mystack(4);
  std::cout << "Empty: " << mystack.isempty() << ", Full " << mystack.isfull() << std::endl;
  mystack.push(1);
  std::cout << "Empty: " << mystack.isempty() << ", Full " << mystack.isfull() << std::endl;
  mystack.push(2);
  mystack.push(3);
  std::cout << "Empty: " << mystack.isempty() << ", Full " << mystack.isfull() << std::endl;
  mystack.push(4);
  std::cout << "Empty: " << mystack.isempty() << ", Full " << mystack.isfull() << std::endl;
  mystack.push(5);
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
  
  std::cout << "-----Copy ctor-----" << std::endl;
  mystack.push(24);
  mystack.push(12);
  Stack second = mystack;
  std::cout << "Pop2 " << second.pop() << std::endl;
  std::cout << "Pop2 " << second.pop() << std::endl;
  std::cout << "Empty: " << second.isempty() << ", Full " << second.isfull() << std::endl;
  second.push(99);
  std::cout << "Pop1 " << mystack.pop() << std::endl;
  std::cout << "Pop1 " << mystack.pop() << std::endl;
  std::cout << "Empty: " << mystack.isempty() << ", Full " << mystack.isfull() << std::endl;
  
  std::cout << "----Copy op------" << std::endl;
  mystack.push(1);
  mystack.push(4);
  mystack.push(9);
  mystack.push(25);
  second = mystack;
  std::cout << "Pop2 " << second.pop() << std::endl;
  std::cout << "Pop2 " << second.pop() << std::endl;
  second.push(99);
  std::cout << "Pop1 " << mystack.pop() << std::endl;
  std::cout << "Pop1 " << mystack.pop() << std::endl;

  std::cout << "-----Move ctor-----" << std::endl;
  Stack third = createStack(3);
  std::cout << "Empty: " << third.isempty() << ", Full " << third.isfull() << std::endl;
  std::cout << "Pop " << third.pop() << std::endl;
  std::cout << "Pop " << third.pop() << std::endl;
  std::cout << "Pop " << third.pop() << std::endl;
  std::cout << "Empty: " << third.isempty() << ", Full " << third.isfull() << std::endl;

  std::cout << "----Move op------" << std::endl;
  mystack = createEven(4);
  std::cout << "Empty: " << mystack.isempty() << ", Full " << mystack.isfull() << std::endl;
  std::cout << "Pop " << mystack.pop() << std::endl;
  std::cout << "Pop " << mystack.pop() << std::endl;
  std::cout << "Pop " << mystack.pop() << std::endl;
  std::cout << "Pop " << mystack.pop() << std::endl;
  std::cout << "Empty: " << mystack.isempty() << ", Full " << mystack.isfull() << std::endl;
  
  // remove the // in the following line and check that this does not compile:
  // mystack = 10;
}
