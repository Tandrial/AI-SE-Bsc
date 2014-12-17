#include <iostream>
#include <cstring>

std::ostream& operator<<(std::ostream& lhs, const char* rhs){
	const char* before = "---start---\n";
	const char* after  = "---end---\n";

	lhs.write(before, strlen(before));
	lhs.write(rhs, strlen(rhs));	
	lhs.write(after, strlen(after));

    return lhs;
}

int main() {
 std::cout << "Hello world!\n";
}
