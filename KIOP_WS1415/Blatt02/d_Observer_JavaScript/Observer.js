var Observable = function() {
  obs = [];

  this.attach = function(Observer) {
    obs.push(Observer);
  };

  this.detach = function(Observer) {
    for(var i = obs.length; i--;) {
      if(obs[i] === Observer) {
        obs.splice(i, 1);
      }
    }
  };

  this.notify = function() {
    for(var i = obs.length; i--;) {
      obs[i].update();
    }
  };

  return this;
};

var Subject = function(value) {
  this.value = value;

  this.setVal = function (val) {
    this.value = val;
    this.notify();
  };

  return this;
};

Observable.call(Subject.prototype);

var Observer = function(subject) {
  this.sub = subject;
  this.sub.attach(this);
  this.update = function() { };
  return this;
};

var HexObserver = function(subject) {
  var instance = new Observer(subject);

instance.update = function() {
    var v = this.sub.value;
    console.log(v + " als Hex ist "+ v.toString(16));
  };

  return instance;
};

var OctObserver = function(subject) {
  var instance = new Observer(subject);

instance.update = function() {
    var v = this.sub.value;
    console.log(v + " als Oct ist "+ v.toString(8));
  };

  return instance;
};

var sub = new Subject(10);
var obs1 = new HexObserver(sub);
var obs2 = new OctObserver(sub);
var obs3 = new HexObserver(sub);
var obs4 = new OctObserver(sub);

sub.setVal(15);
sub.detach(obs1);
sub.detach(obs3);
sub.setVal(10);
sub.setVal(5);