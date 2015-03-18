public class VisitorDemo {
	public static void main(String[] args) {
		Element[] list = 
{ new This(), new That(), new TheOther() };
	This pos = (This)list[0];
	for (int j = 0; j < 5000; j++) {
	pos.e = new This();
	pos = pos.e;
	}
	long start = System.currentTimeMillis();
		Visitor v = new UpVisitor();
		v.visit(list[0]);
	long stop = System.currentTimeMillis();
	System.out.println(stop - start);
			 //list[i].accept(v);
		
	}
}
