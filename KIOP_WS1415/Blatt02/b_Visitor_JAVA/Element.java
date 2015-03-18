public interface Element {
	 //public void accept(Visitor v);
	public String print();
	public Element getChild();
}

class This implements Element {
	public This e = null;
	
	public This getE() { return e;}

	/* public void accept(Visitor v) {
	 v.visit(this);
	 }*/

	public	Element getChild() {
		return e;
	}
	public String print() {
		return "This";
	}
}

class TheOther implements Element {
/*	 public void accept(Visitor v) {
	 v.visit(this);
	 }
*/
	public Element getChild() { return null; }

	public String print() {
		return "TheOther";
	}
}

class That2 extends That {

public Element getChild() { return null; }
	public String print() {
		return "That2";
	}
}


class That implements Element {
/*	 public void accept(Visitor v) {
	 v.visit(this);
	 }
*/
public Element getChild() { return null; }
	public String print() {
		return "That";
	}
}