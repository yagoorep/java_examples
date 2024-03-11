package yagoo.algostruct.nodelist;

class Node {
	
	private String value;
	public Node next;
	private int id;
	private static int counter;
	
	public Node() {
		id = counter++;
	}
	
	public Node(String value) {
		this();
		this.value = value;
	}
	
	public void set(String value) {
		this.value = value;
	}
	
	public String get() {
		return value;
	}
	
	public static Node reverse(Node head) {
		Node before = null;
		Node current = head;
		while(current.next != null) {
			Node forward = current.next;
			current.next = before;
			before = current;
			current = forward;
		}
		current.next = before;
		return current;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("[ ");
		result.append(id);
		result.append(" = ");
		result.append(value);
		Node fwd = next;
		while(fwd != null) {
			result.append(", ");
			result.append(fwd.id);
			result.append(" = ");
			result.append(fwd.value);
			fwd = fwd.next;
		}
		result.append(" ]");
		return result.toString();
	}
	
}

public class NodeMain {

	public static void main(String[] args) {
		Node nodes = new Node("First");
		nodes.next = new Node("Second");
		nodes.next.next = new Node("Third");
		nodes.next.next.next = new Node("Fourth");
		System.out.println(nodes);
		Node rNode = Node.reverse(nodes);
		System.out.println(rNode);
	}

}
