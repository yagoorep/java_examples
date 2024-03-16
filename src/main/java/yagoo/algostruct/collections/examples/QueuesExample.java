package yagoo.algostruct.collections.examples;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

public class QueuesExample {

	public static void main(String[] args) {
		// Queue based on LinkedList
		Queue<Integer> q = new LinkedList<>();
		System.out.println("LinkedList's queue: ");
		for (int i = 5; i > 0; i--) {
		    q.add(i);
		    System.out.printf("in -> %d ", i);
		}
		System.out.printf("%nq = %s%n", q);
		while (!q.isEmpty()) System.out.printf("out <- %d ", q.poll());
		System.out.printf("%n - - -%n%n");
		
		//
		Queue<Integer> pq = new PriorityQueue<>();
		System.out.println("Priority queue: ");
		for (int i = 5; i > 0; i--) {
		    pq.add(i);
		    System.out.printf("in -> %d ", i);
		}
		System.out.printf("%npq = %s%n", pq);
		while (!pq.isEmpty()) System.out.printf("--> %d ", pq.poll());
		System.out.printf("%n - - -%n%n");
		
		// Comparator for priority queue
		Comparator<Integer> cmp = new Comparator<Integer>() {
		    // Even ascending then odd ascending
			public int compare(Integer o1, Integer o2) {
				if ((o1 % 2) != 0 && (o2 % 2) == 0) return 1;
				
				if ((o1 % 2) == 0 && (o2 % 2) != 0) return -1;
				
				if ((o1 % 2) == 0 && (o2 % 2) == 0) {
					return (o1 == o2) ? 0 : (o1 < o2) ? -1 : 1;
				} else {
					return (o1 == o2) ? 0 : (o1 < o2) ? -1 : 1;
				}
			}
		};
		
		Queue<Integer> pq2 = new PriorityQueue<>(cmp);
		System.out.println("Priority queue with comparator: ");
		//pq2.add(1); pq2.add(3); pq2.add(2); pq2.add(4);
		for (int i = 0; i < 4; i++) {
		    int number = ThreadLocalRandom.current().nextInt(1, 5);
		    pq2.add(number);
		    System.out.printf("in -> %d ", number);
		}
		System.out.printf("%npq2 = %s%n", pq2);
		while (!pq2.isEmpty()) System.out.printf("--> %d ", pq2.poll());
		System.out.printf("%n - - -%n%n");
	}

}
