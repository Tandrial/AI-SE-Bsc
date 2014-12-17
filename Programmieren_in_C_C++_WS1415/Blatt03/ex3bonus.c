#include <stdio.h>

#define STACK_OFFSET 2
#define ADR_OFFSET 7


void modify(void) {
    long long i, *ptr = &i + 10;
    printf("    &i : %p \n  &ptr : %p \n",&i, &ptr);
    printf("--------------------------------------\n");
    for(i = 10; i >= -1; i--, ptr--)
        printf("i + %2d : %p : %llx\n", (int)i, ptr, *ptr);
    ptr = &i + STACK_OFFSET;
    *ptr += ADR_OFFSET;
}

int main(void) {
    int i = 42;
    modify();
    i = 0;
    printf("The answer is %d!\n", i);
    return 0;
}
