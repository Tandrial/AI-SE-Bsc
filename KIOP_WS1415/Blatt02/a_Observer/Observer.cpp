#include <iostream>
#include <vector>
#include <algorithm>
#include "Observer.h"

void Subject::setVal(int val) {
  value = val;
	notify();
}

template <class Super> 
void ObsMixIn<Super>::attach(cheatObs *o) {
	obs.push_back(o);
}

template <class Super>
void ObsMixIn<Super>::detach(cheatObs *o) {
    obs.erase(remove(obs.begin(), obs.end(), o), obs.end());
}

template <class Super>
void ObsMixIn<Super>::notify() {
  for (int i = 0; i < (signed) obs.size(); i++)
    obs[i]->update();
}

template <class T>
Observer<T>::Observer(ObsMixIn<T> *mod) {
    model = mod;
    model->attach(this);
}

void HexObserver::update() {
    int v = getSubject()->getVal();
    cout << v << " als Hex ist " << std::hex << v << std::dec << '\n';
}

void OctObserver::update() {
    int v = getSubject()->getVal();
    cout << v << " als Octal ist " << std::oct << v << std::dec << '\n';
}

int main() {
  ObsMixIn<Subject> subj;
  HexObserver hexObs(&subj);
  OctObserver octObs(&subj);
  subj.setVal(14);
  subj.detach(&hexObs);
  subj.setVal(64);
  subj.setVal(100);
}
