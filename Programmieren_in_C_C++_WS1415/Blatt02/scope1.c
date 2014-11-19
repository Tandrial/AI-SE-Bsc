#include <stdio.h>

int globalvar = 1;

extern void modtest(void);

int main() {
  modtest();
  return 0;
}
