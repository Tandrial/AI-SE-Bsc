import java.util.ArrayList;

public class StackMVererbung extends ArrayList<Object> {

	private static final long serialVersionUID = -2684659077913700703L;

	public boolean isEmpty() {
		return size() == 0;
	}

	public Object pop() {
		return remove(0);
	}

	public void push(Object elem) {
		add(0, elem);
	}

	public static void main(String[] args) {

		StackMVererbung t = new StackMVererbung();

		t.push(4);
		t.push(7);
		t.push(8);
		t.push(49);
		t.push(50);
		System.out.println(t.pop());
		System.out.println(t.pop());
		System.out.println(t.pop());
		System.out.println(t.pop());
		System.out.println(t.pop());
	}
}
