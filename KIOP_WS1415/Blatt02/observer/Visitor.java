import java.lang.reflect.Method;

interface Visitor {
	void visit(Element element);
}

class UpVisitor implements Visitor {
	static Method m = null;
	static {
		try {
			m = UpVisitor.class.getDeclaredMethod("visit", This.class);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void visit(Element element) {
		if (element == null)
			return;
		// System.out.print("Class lookup... ");
		Class<? extends Element> cl = element.getClass();
		if (cl == null) {
			System.out.println("????????");
			System.exit(-1);
		}
		// System.out.println(cl);

		try {
			// Method m = this.getClass().getDeclaredMethod("visit",This.class);
			if (m == null) {
				System.out.println("????????!!!!!!!");
				System.exit(-1);
			}
			// System.out.println("Found Methode: " + m);
			m.invoke(this, element);
		} catch (Exception e) {
			System.out.println();
			System.out.println("No Method found.");
			System.out.println(this);
			System.out.println(e.getCause());
			System.out.println(e.getMessage());
			System.out.println(element);
			System.exit(-1);
		}
	}

	public void visit(This e) {
		if (e.e != null) {
			// System.out.println("do Up on " + e.print());
			System.out.print("*");
			this.visit((Element) e.e);
		}
	}

	public void visit(That2 e) {
		System.out.println("That2");
		System.out.println("do Up on " + e.print());
	}

	public void visit(That e) {
		System.out.println("do Up on " + e.print());
	}

	public void visit(TheOther e) {
		System.out.println("do Up on " + e.print());
	}
}