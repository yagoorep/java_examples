package yagoo.algostruct.collections.examples;

import java.util.LinkedHashMap;
import java.util.Map;

// LRU Cache
public class SimpleLRUCache<K, V> extends LinkedHashMap<K, V> {
	//
	private static final long serialVersionUID = 4391021327398876593L;
	
	private final int capacity;
	
	public SimpleLRUCache(int capacity) {
		super(capacity + 1, 1.1f, true);
		this.capacity = capacity;
	}
	
	@Override
	protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
		return this.size() > capacity;
	}
	
	public static void main(String[] args) {
		//
		Map<Integer, String> cache = new SimpleLRUCache<>(2);
		cache.put(1, null);
		cache.put(2, null);
		cache.put(3, null);
		System.out.println(cache);
		cache.get(2);
		System.out.println(cache);
		cache.put(9, null);
		System.out.println(cache);
	}

}
