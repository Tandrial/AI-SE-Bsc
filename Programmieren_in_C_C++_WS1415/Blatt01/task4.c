#include <stdio.h>
#include <string.h>

char *test1 = "Hello Welt!";
char *test2 = "";

// Strlen 
int strlenA(char *s) {
	int cnt = 0;
	//Den String mittels Index bis zum Ende durchlaufen
	while(s[cnt] != '\0') {
		cnt++;
	}
	return cnt;
}

int strlenB(char *s) {
	//Ein zweiter Pointer wird auf das Ende des Strings verschoben
	char *pos = s;
	while(*pos != '\0') {
		pos++;
	}
	//Die Adresse des letzen chars - Die Adresse des erstens chars
	//ist die Laenge vom String(1 char ==> 1 byte)
	return (pos - s);
}

int main (void) {
	//Test-Cases standart strlen vs strelenA vs strlenB
	printf("strlen(\"%s\")  = %u\n", test1, (unsigned) strlen(test1));
	printf("strlenA(\"%s\") = %d\n", test1, strlenA(test1));
	printf("strlenB(\"%s\") = %d\n", test1, strlenB(test1));
	printf("strlen(\"%s\")  = %u\n", test2, (unsigned) strlen(test2));
	printf("strlenA(\"%s\") = %d\n", test2, strlenA(test2));
	printf("strlenB(\"%s\") = %d\n", test2, strlenB(test2));

	return 0;
}
