#include <iostream>

// MAKVEVAR macro - creates a global variable with a getter/setter-Function
// set_ and get_ need to be concatinatied with NAME for the "argument" to 
// correctly be apllied.
#define MAKEVAR(NAME, TYPE)\
    TYPE NAME;\
    void set_##NAME(TYPE val) { NAME = val; }\
    TYPE get_##NAME(void) { return NAME; }

MAKEVAR(weight, int)
MAKEVAR(price, float)

int main(void) {
  set_weight(100);
  set_price(1.25);
  std::cout << "Weight: " << get_weight() << ", Price: " << get_price() << std::endl;
  return 0;
}
