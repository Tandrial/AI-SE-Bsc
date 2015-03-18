#include <iostream>
#include <vector>

class StackMV : private std::vector<int> {
  public:
    void push (int x) {
	push_back(x);
    }

     int pop() {
       int elem = back();
       pop_back();
       return elem;
     }
};

int main () {
  StackMV s;
  s.push(4);
  s.push(8);
  s.push(22);	
  std::cout<<s.pop()<<std::endl;
  std::cout<<s.pop()<<std::endl;
  std::cout<<s.pop()<<std::endl;
  return 0;
}