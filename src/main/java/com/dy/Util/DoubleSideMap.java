package com.dy.Util;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DoubleSideMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -954522029667245634L;
	private HashMap<K, V> KVMap;
	private HashMap<V, K> VKMap;

	public DoubleSideMap() {
		super();
		this.KVMap = new HashMap<K, V>();
		this.VKMap = new HashMap<V, K>();
	}

	@Override
	public void clear() {
		this.KVMap.clear();
		this.VKMap.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return this.KVMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return this.VKMap.containsKey(value);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Set entrySet() {
		return this.VKMap.keySet();
	}

	@Override
	public V get(Object key) {
		return this.KVMap.get(key);
	}

	public K getByValue(Object value) {
		return this.VKMap.get(value);
	}

	@Override
	public boolean isEmpty() {
		return this.KVMap.isEmpty() && this.VKMap.isEmpty();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Set keySet() {
		return this.KVMap.keySet();
	}

	@Override
	public V put(K key, V value) {
		V ret = null;
		boolean hasKey = this.containsKey(key);
		boolean hasValue = this.containsValue(value);
		if (hasKey && hasValue) {

		} else if (hasKey || hasValue) {
			throw new IllegalArgumentException("This Map already contains this key or value");
		}
		ret = this.KVMap.put(key, value);
		this.VKMap.put(value, key);
		return ret;
	}

	@Override
	public V remove(Object key) {
		V value = this.KVMap.get(key);
		this.KVMap.remove(key);
		this.VKMap.remove(value);
		return value;
	}

	@Override
	public int size() {
		return this.KVMap.size();
	}

	@Override
	public Collection<V> values() {
		return this.KVMap.values();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"KVMap\":{");
		this.KVMap.forEach((K, V) -> {
			sb.append("\"" + K.toString() + "\":");
			sb.append("\"" + V.toString() + "\"");
			sb.append(",");
		});
		if(this.KVMap.size()>0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("},\"VKMap\":{");
		this.VKMap.forEach((K, V) -> {
			sb.append("\"" + K.toString() + "\":");
			sb.append("\"" + V.toString() + "\"");
			sb.append(",");
		});
		if(this.VKMap.size()>0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("}}");
		return sb.toString();
	}

}
