package yagoo.algostruct.collections.examples;

import java.util.Collections;
import java.util.Comparator;
//import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class SetExample {

	public static void main(String[] args) {
		// Set size if compare equals
		Set<Integer> set = new TreeSet<>(new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				return 0;
			}
		});
		set.add(1);
		set.add(2);
		set.add(1);
		System.out.println(set);
		
		//
		SortedSet<Integer> s1 = new TreeSet<>();
		for (int i = 1; i <= 10; i++) {
			s1.add(i);
		}
		System.out.println("-> " + s1);
		System.out.println(getNextElement((NavigableSet<Integer>) s1, 4));
		System.out.println(getPrevElems(s1, 4));
	}
	
	static Integer getNextElement(NavigableSet<Integer> s, Integer elem) {
		/*Iterator<Integer> itr = s.iterator();
		while (itr.hasNext()) {
			if (itr.next() == elem) {
				return itr.hasNext() ? itr.next() : null;
			}
		}
		return null;*/
		return s.higher(elem);
	}
	
	static Set<Integer> getPrevElems(SortedSet<Integer> s, Integer elem) {
		return (s != null && s.contains(elem)) ? s.headSet(elem) : Collections.emptySet();
	}
	
}
