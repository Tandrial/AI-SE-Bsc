import java.util.ArrayList;

public class StackOvererbung {

	private ArrayList<Object> stack = new ArrayList<Object>();

	public boolean isEmpty() {
		return stack.size() == 0;
	}

	public Object pop() {
		return stack.remove(0);
	}

	public void push(Object elem) {
		stack.add(0, elem);
	}

	public static void main(String[] args) {
		StackOvererbung t = new StackOvererbung();
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
