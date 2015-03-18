import java.lang.reflect.Method;

interface Visitor {
	void visit(Element element);
}

class UpVisitor implements Visitor {

	public void visit(Element element) {
		System.out.print("Class lookup... ");
		Class<? extends Element> cl = element.getClass();
		System.out.println(cl);

		try {
		Method m = this.getClass().getDeclaredMethod("visit", 				cl);
			System.out.println("Found Methode: " + m);
			m.invoke(this, element);
		} catch (Exception e) {
			System.out.println("No Method found.");
			e.printStackTrace();
		}
	}

	public void visit(This e) {
	if (e.getE() != null)
		//System.out.println("do Up on " + e.print());
		//System.out.println("hier");
		this.visit((Element)e.getE());
	}

	private void visit(That2 e) {
		System.out.println("That2");
		System.out.println("do Up on " + e.print());
	}


	private void visit(That e) {
		System.out.println("do Up on " + e.print());
	}

	private void visit(TheOther e) {
		System.out.println("do Up on " + e.print());
	}
}