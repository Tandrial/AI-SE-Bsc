#include <iostream>
#include <string>
#include <cctype>

// class according to the diagram
class StringPrinter {
public:
	void print(const char*);
	// pure virtual ==> StringPrinter is an abstract class
	virtual void doPrint(std::string&) = 0;
	// valgrind complained about memory that wasn't freed
	// virtual dtor to be able to clean up the stringComponent ref in StringDecorator
	virtual ~StringPrinter() { };
};

// the argument is converted into a string and passed to doPrint()
void StringPrinter::print(const char* s) {
	std::string str = s;
	doPrint(str);
}



// class according to the diagram
class StreamPrinter : public StringPrinter {
private:
	std::ostream& output;
public:
	StreamPrinter(std::ostream& os) : output(os) { };
	virtual void doPrint(std::string&);
};

// the passed string is framed by pipes
void StreamPrinter::doPrint(std::string& s) {
	output << "|" << s << "|" << std::endl;
}



// class according to the diagram
class StringDecorator : public StringPrinter {
private:	
	StringPrinter* stringComponent;
public:
	StringDecorator(StringPrinter* component) : stringComponent(component) { };
	// deletes the object behind the reference to a stringComponent
	virtual ~StringDecorator() { delete stringComponent; }
	virtual void doPrint(std::string&);
};

void StringDecorator::doPrint(std::string& s) {
	stringComponent->doPrint(s);
}



// class according to the diagram
class StringTrim : public StringDecorator {
public:
	StringTrim(StringPrinter* component) : StringDecorator(component) { };
	virtual void doPrint(std::string&);
};

void StringTrim::doPrint(std::string& s) {
	std::string::iterator iter;
	// from the start of the string we search for the first char that isn't a space
	for(iter = s.begin(); iter < s.end() && isspace(*iter); iter++);
	// erase everthing up to the first non-space char
	s.erase(s.begin(), iter);
	// from the back of the string we search backwards for the first char that isn't a space
	// s.end() points after the string, so we must subtract 1 to get the last char
	for(iter = s.end() - 1; iter > s.begin() && isspace(*iter); iter--);
	// since erase includes the first char, we need to move the iter by one
	++iter;
	//erase everything after the last non-space char
	s.erase(iter, s.end());

	// method call in the super class, to pass the modified string on
	StringDecorator::doPrint(s);
}



// class according to the diagram
class StringCompress : public StringDecorator {
public:
	StringCompress(StringPrinter* component) : StringDecorator(component) { };
	virtual void doPrint(std::string&);
};

void StringCompress::doPrint(std::string& s) {
	size_t pos;
	// string::find() returns the index of the first occurance of the argument
	// if it doesn't find any string::npos (= -1) is returned
	while ((pos = s.find("  ")) !=  std::string::npos) {
		// the first of the two whitespaces is deleted
		s.erase(pos, 1);
	}
	// method call in the super class, to pass the modified string on
	StringDecorator::doPrint(s);
}



// class according to the diagram
class StringAlphaNum : public StringDecorator {
public:
	StringAlphaNum(StringPrinter* component) : StringDecorator(component) { };
	virtual void doPrint(std::string&);
};

void StringAlphaNum::doPrint(std::string& s) {
	std::string::iterator iter;
	// we iterate over the whole string
	for(iter = s.begin(); iter < s.end(); iter++) {
		// if the current char isn't a whitespace or alphanumeric-char		
		if (!isspace(*iter) && !isalnum(*iter)) {
			// it is replaced with a whitespace
			*iter = ' ';
		}
	}
	// method call in the super class, to pass the modified string on
	StringDecorator::doPrint(s);
}

int main(void) {	
  StringPrinter *normal = new StreamPrinter(std::cout);  
  StringPrinter *trimmer = new StringTrim(new StreamPrinter (std::cout));
  StringPrinter *alphanum = new StringAlphaNum(new StringCompress (new StreamPrinter(std::cout)));
  StringPrinter *all = new StringAlphaNum(new StringTrim(new StringCompress (new StreamPrinter(std::cout))));
  
  normal->print("  Hello, world!  ");
  trimmer->print("  Hello, world!  ");
  alphanum->print("  Hello, world!  ");
  all->print("  Hello, world!  ");
  alphanum->print("C++ is great!");
  all->print("C++ is great!");

  delete normal;
  delete trimmer;
  delete alphanum;
  delete all;
  return 0;
}
