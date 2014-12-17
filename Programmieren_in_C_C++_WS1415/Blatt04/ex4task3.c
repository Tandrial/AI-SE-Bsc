#include <stdio.h>

int main(void) {
  struct stest {
    int top;
    int arr[5];
    int bottom;
  } s;
  int i;

  s.top = 1;
  s.bottom = 2;

  for (i = 0; i <= 5; i++) {
    s.arr[i] = 42;
  }

  printf("top = %d, bottom = %d\n", s.top, s.bottom);
  return 0;
}
