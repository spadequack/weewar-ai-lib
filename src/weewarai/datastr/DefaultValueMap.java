package weewarai.datastr;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * This class decorates a map so that it returns a default value if the key
 * given is not in the map
 * 
 * @param <K> the key class
 * @param <V> the value class
 */
public class DefaultValueMap<K, V> implements Map<K, V> {

	private Map<K, V> backingMap;
	private V defaultValue;

	public DefaultValueMap(Map<K, V> backingMap, V defaultValue, K uniqueDefaultKey) {
		this.backingMap = backingMap;
		this.defaultValue = defaultValue;
		// the uniqueDefaulyKey is necessary for the iterator to work on an
		// "empty" map
		backingMap.put(uniqueDefaultKey, defaultValue);
	}

	@Override
	public V get(Object o) {
		V value = backingMap.get(o);
		if (value == null)
			return defaultValue;
		return value;
	}

	@Override
	public V put(K key, V value) {
		return backingMap.put(key, value);
	}

	@Override
	public int size() {
		return backingMap.size();
	}

	@Override
	public void clear() {
		backingMap.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return backingMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return backingMap.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return backingMap.entrySet();
	}

	@Override
	public boolean isEmpty() {
		return backingMap.isEmpty();
	}

	@Override
	public Set<K> keySet() {
		return backingMap.keySet();
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		backingMap.putAll(m);
	}

	@Override
	public V remove(Object key) {
		return backingMap.remove(key);
	}

	@Override
	public Collection<V> values() {
		return backingMap.values();
	}
}
