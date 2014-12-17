#include <iostream>
// MAXM-Macro: replaces MAXM with a simple ternary-Operation
// e. g.  MAXM(3, 5) becomes (3 > 5 ? 3 : 5)
#define MAXM(X, Y) (X > Y ? X : Y)

// the overloaded maxf functions, one for int and another one for double
int    maxf(int    x, int    y) { return (x > y) ? x : y;}
double maxf(double x, double y) { return (x > y) ? x : y;}

int main(void) {
  int a = 10, b = 20;
  std::cout << "MAXM1 = " << MAXM(a, b) << std::endl;
  std::cout << "MAXM2 = " << MAXM(a, b + 0.2) << std::endl;
  std::cout << "MAXM3 = " << MAXM(a, b++) << std::endl;
  std::cout << "maxf1 = " << maxf(a, b) << std::endl;
  // what's the problem with the following line?
  //std::cout << "maxf = " << maxf(a, b + 0.2) << std::endl;
  std::cout << "maxf2 = " << maxf(a + 0.1, b + 0.2) << std::endl;
  std::cout << "maxf3 = " << maxf(a, b++) << std::endl;
  std::cout << "a = " << a << ", b = " << b << std::endl;
  return 0;
}
