#include <iostream>
#include <vector>
#include <map>

// Typedefs for the value and the map itself
typedef std::vector<int> IntVector;
typedef std::map<std::string, IntVector> LineMap;

int main(void) {
	LineMap map;
	std::string line;
	int lineCount = 1;
	// read a whole line 
	std::getline(std::cin, line);
	// loop until we encounter a "."
	while(line != ".") {		
		// [] allows access to an element in the map and returns a reference
		// push_back adds the lineCount at the end of the vector for the key "line"
		map[line].push_back(lineCount);
		// increases the lineCount
		lineCount++;		
		// read the next line
		std::getline(std::cin, line);
	}

	// read a whole line 
	std::getline(std::cin, line);
	// loop until we encouter a "."
	while(line != ".") {
		// find returns an iterator that points to the element with the key "line"
		LineMap::iterator iter = map.find(line);	
		// if the element doens't exists it points to map.end()
		if (iter != map.end()) {				
			std::cout << "\twas previously entered on the following lines: ";
			// loop through the Value and print all the elements
			for(IntVector::value_type v : iter->second) {
				std::cout << v << " ";
			}	
			std::cout << std::endl;	
		} else {			
			// Message if there is no element with the key "line" in the map
			std::cout << "\twas not previously entered!" << std::endl;
		}
		// Read the next line 
		std::getline(std::cin, line);	
	}
	return 0;
}
