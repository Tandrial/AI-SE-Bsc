#include <iostream>
#include "secarr.hpp"

using namespace std;

int main(void) {
  int a[] = {1, 2, 3, 4};
  SecArr sarr(a, a + (sizeof(a) / sizeof(int) ) - 1);
  cout << "*sarr = " <<  *sarr << endl;
  *sarr = 0;
  cout << "sarr[0] = " <<  sarr[0] << endl;
  ++sarr;
  cout << "After increment: *sarr = " << *sarr << ", sarr[0] = " <<  sarr[0] << endl;
  cout << "sarr[2] = " <<  sarr[2] << endl;
  cout << "sarr[-1] = " <<  sarr[-1] << endl;
  cout << "sarr[-2] = " <<  sarr[-2] << endl;
  sarr[2] = 6;
  cout << "sarr[3] = " <<  sarr[3] << endl;
  cout << "*(sarr++) = " << *(sarr++) << endl;
  cout << "*sarr = " <<  *sarr << endl;
  cout << "*(++sarr) = " << *(++sarr) << endl;
  cout << "*sarr = " <<  *sarr << endl;
  cout << "Should not increment 2x: " << endl;
  cout << "*(++sarr) = " << *(++sarr) << endl;
  sarr++;
  cout << "*sarr = " <<  *sarr << endl;
  cout << "*(sarr--) = " << *(sarr--) << endl;
  cout << "*sarr = " <<  *sarr << endl;
  cout << "*(--sarr) = " << *(--sarr) << endl;
  cout << "*sarr = " <<  *sarr << endl;
  --sarr;
  cout << "Should not decrement 2x: " << endl;
  cout << "*(--sarr) = " << *(--sarr) << endl;
  sarr--;
  cout << "*sarr = " <<  *sarr << endl;
}