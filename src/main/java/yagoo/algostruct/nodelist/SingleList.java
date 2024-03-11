package yagoo.algostruct.nodelist;

public class SingleList<E> {
	
	private class Node {
		private Node next;
		private E value;
	}
	
	private int length;
	private Node ref;
	
	public void addFirst(E value) {
		Node node = ref;
		ref = new Node();
		ref.next = node;
		ref.value = value;
		length++;
	}
	
	public E getFirst() {
		Node node = ref;
		ref = ref.next;
		length--;
		return node.value;
	}
	
	public void addLast(E value) {
		Node node = new Node();
		node.value = value;
		if (ref == null) {
			ref = node;
			return;
		} else {
			Node cur = ref;
			while (cur.next != null) {
				cur = cur.next;
			}
			cur.next = node;
		}
	}
	
	public E getLast() {
		if (ref == null) return null;
		Node cur = ref;
		while (cur.next != null) {
			cur = cur.next;
		}
		return cur.value;
	}
	
	public void reverse() {
		Node prev = null;
		Node cur = ref;
		Node next = null;
		while (cur != null) {
			next = cur.next;
			cur.next = prev;
			prev = cur;
			cur = next;
		}
		ref = prev;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("[ ");
		Node node = ref;
		while(node != null) {
			sb.append(node.value);
			if (node.next == null) {
				sb.append(" ]");
			} else {
				sb.append(", ");
			}
			node = node.next;
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		SingleList<String> slist = new SingleList<>();
		slist.addFirst("Hello ");
		System.out.println(slist);
		slist.addFirst("ugly ");
		slist.addFirst("world ");
		System.out.println(slist);
		slist.reverse();
		System.out.println(slist);
	}

}
