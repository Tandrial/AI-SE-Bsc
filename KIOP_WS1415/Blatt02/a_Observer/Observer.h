#include <iostream>
#include <vector>

using namespace std;

// Baseclass 
class Subject {
	protected:
		int value;
  	public:
	    int getVal() { return value; }
	    virtual void setVal(int val);
	    virtual void notify() { }
};

// Observee MixIn
template <class Super>
class ObsMixIn : public Super {
	public :
    	vector <class cheatObs *> obs;
        void notify();
        void attach(cheatObs *o);  
		void detach(cheatObs *o);      
};

// Baseclass um Observer im Vector zuspeichern
class cheatObs {
	public:
		virtual void update() = 0;
};

// Template Class Observer
template <class T>
class Observer : public cheatObs{
    ObsMixIn<Subject> *model;
  public:
    Observer(ObsMixIn<T> *mod);
    ObsMixIn<T> *getSubject() { return model; }
    virtual void update() = 0;
};

// Observer1 f. Subject
class HexObserver: public Observer<Subject> {
  public:
    HexObserver(ObsMixIn<Subject> *mod): Observer<Subject>(mod) { }
    void update();
};

// Observer2 f. Subject
class OctObserver: public Observer<Subject> {
  public:
    OctObserver(ObsMixIn<Subject> *mod): Observer<Subject>(mod) { }
    void update();
};
