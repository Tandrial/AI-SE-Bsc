var PrintVisitor = function() {
	this.visit = function(elem) {
		this.doIt = elem.doIt;
		this.doIt(elem);
	}
  return this;
};

var Visitable = function() {
	this.doIt = function() {
		console.log("Base-Type Visitable");
	}
}

var Thiss = function(value) {	
	this.val = value;
	this.doIt = function(elem) {
		console.log("Thiss Value : " + elem.val);
	}
}

var That = function() {	
	this.doIt = function(elem) {
		console.log("That");
	}
}

var TheOther = function() {	
	this.doIt = function(elem) {
		console.log("TheOther");
	}
}

Thiss.prototype = new Visitable();
That.prototype = new Visitable();
TheOther.prototype = new Visitable();


var arr =  [ new Thiss(10), new That(), new TheOther(), new Visitable()];

var v = new PrintVisitor();


for(var i = arr.length; i--;) {
	v.visit(arr[i]);
}

