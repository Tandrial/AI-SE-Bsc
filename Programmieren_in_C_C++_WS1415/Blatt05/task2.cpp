#include <iostream>

/*
 global Objects are created BEFORE the main function is called and deleted AFTER 
 the main function finished (Chap 4, Slide 15).

 In this case that means, that the Foo constructor first prints it's message, then 
 main prints it's message and after main() finished the Foo desctructor prints it's message.
*/

// Class with print text in the ctor and dtor
class Foo {
    public:
        Foo() { std::cout << "---start---\n"; }
       ~Foo() { std::cout << "---end---\n"; }
};

// global object
Foo f;

int main(void) {
    std::cout << "Hello world! \n";
}
