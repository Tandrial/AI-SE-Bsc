/* Test program for the Time class - Exercises 6
    Missing comments are intentional :-) */
    
#include <iostream>
#include "mytime.hpp"

int main(void) {
  MyTime t(0, 59, 121);
  std::cout << "It's " << t << std::endl;
  std::cout << "Hour " << t.getHour() << " Minute " << t.getMinute() << " Second " << t.getSecond() << std::endl;
  int s = t;
  std::cout << "In seconds: " << s << std::endl;
  std::cout << "Four seconds later: " << t + 4 << std::endl;
  const MyTime t2(1, 59, 59);
  std::cout << "Compare: " << (t > t2 ? "bigger" : "smaller") << std::endl;
  t = t + 3661;
  std::cout << "Addition of int: " << t << std::endl;
  t = t + t2;
  std::cout << "Addition of t2: " << t << std::endl;
  t = 3784 + t;
  std::cout << "Addition to int: " << t << std::endl;
  t += t2;
  std::cout << "+= t2 " << t << std::endl;
  t += 3784;
  std::cout << "+= int " << t << std::endl;
}
