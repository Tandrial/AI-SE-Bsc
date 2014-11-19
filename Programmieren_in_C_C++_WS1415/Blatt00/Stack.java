public class Stack {
	public Node top = null;

	public boolean isEmpty() {
		return top == null;
	}

	public void push(int i) {
		top = new Node(i, top);
	}

	public int pop() {
		if (isEmpty()) 
			throw new java.util.NoSuchElementException("Der Stack ist leer!");
		int res = top.elem;
		top = top.next;
		return res;
	}

	public void print() {
		while (!isEmpty())
			System.out.println(pop());
	}

	public static void main(String[] args) {
		Stack s = new Stack();

		for (int i = 0; i < 10; i++) {
			int j = (int) (Math.random() * 1000);
			System.out.print(j + " ");
			s.push(j);
		}

		System.out.println();
		s.print();
		System.out.println(s.pop());
	}
}

class Node {
	public int elem;
	public Node next;
	
	public Node(int elem, Node next) {
		this.elem = elem;
		this.next = next;
	}
}