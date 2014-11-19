#include <stdio.h>

int main(void) {
	printf("size of char         = %2u bytes\n", (unsigned) sizeof(char));	
	printf("size of short        = %2u bytes\n", (unsigned) sizeof(short));
	printf("size of int          = %2u bytes\n", (unsigned) sizeof(int));
	printf("size of long         = %2u bytes\n", (unsigned) sizeof(long));
	printf("size of long long    = %2u bytes\n", (unsigned) sizeof(long long));
	printf("size of float        = %2u bytes\n", (unsigned) sizeof(float));
	printf("size of double       = %2u bytes\n", (unsigned) sizeof(double));
	printf("size of long double  = %2u bytes\n", (unsigned) sizeof(long double));
	printf("size of pointer      = %2u bytes\n", (unsigned) sizeof(void*));
	return 0;
}
