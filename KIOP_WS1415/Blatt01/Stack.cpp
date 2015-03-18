#include <iostream>
#include <vector>

class Stack {
  public:
    void push (int x) {
	data.push_back(x);
    }

    int pop() {
       int elem = data.back();
       data.pop_back();
       return elem;
    }
  private:
    std::vector<int> data;
};

int main () {
  Stack s;
  s.push(4);
  s.push(8);
  s.push(22);	
  std::cout<<s.pop()<<std::endl;
  std::cout<<s.pop()<<std::endl;
  std::cout<<s.pop()<<std::endl;
  return 0;
}