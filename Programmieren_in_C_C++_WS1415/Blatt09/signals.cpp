#include <iostream>
#include <cstdlib>
// fstream is needed to read/write from file
#include <fstream>
#include <sstream>
#include <string>
#include <vector>

enum SignalType { WLAN, BLUETOOTH };

/**
 * @brief The Signal class represents a captured signal
 */
class Signal {
private:
    SignalType type;
    std::string deviceId;
    int strength;
    unsigned channel;

public:
    Signal() : type(WLAN) {}

    SignalType getType() { return type; }
    void setType(SignalType _type) { this->type = _type; }

    std::string getDeviceId() { return deviceId; }
    void setDeviceId(std::string _deviceId) { this->deviceId = _deviceId; }

    int getStrength() { return strength; }
    void setStrength(int _strength) { this->strength = _strength; }

    unsigned getChannel() { return channel; }
    void setChannel(unsigned _channel) { this->channel = _channel; }

    friend std::ostream& operator<<(std::ostream& output, const Signal& signal);
    friend std::istream& operator>>(std::istream& input, Signal& signal);
};

std::ostream& operator<<(std::ostream& output, const Signal& signal) {
    // rebuild the lines from from the sample file
    // which look like this: "WLAN, 12FEED4CBE79, -54, 2462"
    output << ((signal.type == WLAN) ? "WLAN, " : "BLUETOOTH, ");
    output << signal.deviceId << ", ";
    output << signal.strength << ", ";
    output << signal.channel;
    return output;
}

// creates a signal object from an inputStream
std::istream& operator>>(std::istream& input, Signal& signal) {
    std::string line;
    // gets a whole line from the inputSream using a comma as a linebreak
    // since the information is seperated with ", " we need to throw away the first char
    // for all except the first info
    std::getline(input, line, ',');
    // set the type field, depending if the string is WLAN or not
    signal.setType((line.compare("WLAN") == 0) ? WLAN : BLUETOOTH);

    std::getline(input, line, ',');
    // DeviceId is a String, so no conversion is necessary
    signal.setDeviceId(line.substr(1));

    std::getline(input, line, ',');
    // stoi converts a string to an integer
    signal.setStrength(std::stoi(line.substr(1)));

    std::getline(input, line, ',');
    // creates a stringStream from the line
    std::istringstream reader(line.substr(1));
    unsigned val;
    // which is fed into and converted to an unsigned integer
    reader >> val;
    signal.setChannel(val);

    return input;
}

// reads signals from a file
std::vector<Signal> readSignals(const char* filepath) {
    Signal s;
    std::vector<Signal> result;        
    std::string line;
    // inputStream to read from the given filepath
    std::ifstream file(filepath);

    // check if the file was correctly opened
    if (file.is_open()) {
        // logging
        std::cout << "Reading from file: " << filepath << std::endl;
        // for each line in the file
        while (getline(file, line)) {
            // create a stringStream from the current line
            std::istringstream iss(line);
            // >> is used to convert from a stream to a signal
            iss >> s;
            // add the signal to the result vector
            result.push_back(s);
        }
    } else {
        // logging
        std::cout << "File not found: " << filepath << std::endl;
    }
    // close the previously opened file
    file.close();
    return result;
}

// filter a vector of signals by the given type
std::vector<Signal> filterSignals(SignalType type, std::vector<Signal>& signalReadings) {
    std::vector<Signal> result;
    std::vector<Signal>::iterator iter;
    // iterate over all Signals
    for(iter = signalReadings.begin(); iter < signalReadings.end(); iter++) {
        // add the signal to the result if the type matches
        if((*iter).getType() == type)
            result.push_back(*iter);
    }
    return result;
}

// writes a vector of signals to a file
void writeSignals(const char* outputPath, const std::vector<Signal> aggregatedSignals) {
    // outputStream 
    std::ofstream file(outputPath);
    // check if the file was correctly opened
    if (file.is_open()) {
        std::vector<Signal>::const_iterator iter;
        // iterate over all signals and write them to the file
        for(iter = aggregatedSignals.begin(); iter < aggregatedSignals.end(); iter++)
            file << *iter << std::endl;
        // logging
        std::cout << "Signal info written: " << outputPath << std::endl;
    } else {
        // logging
        std::cout  << "Coundn't open file: " << outputPath << std::endl;
    }
    // close the previously opened file
    file.close();
}

int main(void) {
    const char * filename = "signals.txt";
    std::vector<Signal> rawSignals = readSignals(filename);
    std::vector<Signal> filteredSignals = filterSignals(WLAN, rawSignals);

    std::cout << rawSignals.size() << " signals were read, of which "
              << filteredSignals.size() << " were WLAN signals!"
              << std::endl;

    writeSignals("filtered_signals.txt", filteredSignals);
    return 0;
}
