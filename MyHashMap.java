package com.cy.collection;

/**
 * 	简陋的HashMap，省略了红黑树的实现，hash值的一些问题
 * @author Alone
 */
public class MyHashMap<K,V> {
	
	// 1.定义节点类
	private class Node<K,V>{ 
		final int hash;
		final K key;
		V value;
		Node<K,V> next;
		public Node(int hash, K key, V value, Node<K, V> next) {
			this.hash = hash;
			this.key = key;
			this.value = value;
			this.next = next;
		} 
		@Override
		public String toString() {
			return key+"="+value;
		}
	}
	private Node<K,V> [] dataTable; 
	private static final int DEFUALT_CAPACITY= 1 << 4; // 数组初始化长度为16
	private static final int MAXIMUM_CAPACITY= 1 << 30; // 最大容量
	private int size;
	public int arrL;
	public MyHashMap() { this(DEFUALT_CAPACITY); }
	public MyHashMap(int initCapacity) {
		if(initCapacity < 0) 
			throw new RuntimeException("IllegalCapacity:"+initCapacity);
		else if(initCapacity > MAXIMUM_CAPACITY)
			initCapacity = MAXIMUM_CAPACITY;
		else 
			initCapacity = tableSizeFor(initCapacity);
		this.dataTable = new Node[initCapacity];
	}
	// 为了保证数组的长度一定是2的n次方，所以直接采用了HashMap中的算法
	private int tableSizeFor(int initCapacity) {
		int n = initCapacity - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
	}
	// 数组扩容
	private void grow(int capacity) {
		int oldCapacity = dataTable.length;
		Node<K,V> [] newTable = null;
		if(capacity >= oldCapacity * 0.75) {
			newTable = new Node[capacity<<1]; // 右移一位*2，数组长度保证是2的n次幂
			System.arraycopy(dataTable, 0, newTable, 0, dataTable.length);
			dataTable = newTable;
		}
	}
	// 计算key的hash值 
	private static final int hash(Object key, int length) {
		return key == null ? 0 : key.hashCode()&(length-1);
	}
	// 存放元素
	public V put(K key, V value) {
		int h = hash(key, dataTable.length);
		Node<K,V> newNode = new Node<K, V>(h, key, value, null); // 构造出新的节点
		Node<K,V> temp = dataTable[h]; // 获取该位置上的元素
		if(temp == null) { // 没有元素时直接放到位置上就行
			dataTable[h] = newNode;
			arrL++;
		}else { // 有元素的情况下分两种情况考虑： key是否有重复，key是否为空值
			Node<K,V> tNode = null;
			if(key == null) { //1. key为空
				while(temp != null) { 
					tNode = temp;
					if(null == temp.key) { // key重复的情况则替换value
						V v = temp.value;
						temp.value = value;
						return v;
					}
					temp = temp.next;
				}
			}else { // 2. key不为空
				while(temp != null) {
					tNode = temp;
					if(key.equals(temp.key)) { // key重复的情况则替换value值
						V v = temp.value;
						temp.value = value;
						return v;
					}
					temp = temp.next;
				}
			}
			if(tNode != null)
				tNode.next = newNode; // 若没有key重复的情况，那么新节点就是当前链表的最后一个元素
		}
		if(++size >= dataTable.length*0.75) grow(dataTable.length);  //
		return null;
	}
	public V get(K key) {
		V value = null;
		for (int i = dataTable.length, prevHash = -1; i >= 16; i >>= 1) {
			int hash = hash(key, i);
			if(prevHash == hash) continue;
			prevHash = hash;
			value = getNode(key, hash);
			if(value != null) break;
		}
		return value;
	}
	// 通过key取元素
	private V getNode(K key, int hash) {
		Node<K,V> target = dataTable[hash];
		if(key == null) {
			for (Node<K,V> temp = target; temp != null; temp = temp.next) {
				if(null == temp.key) return temp.value;
			}
		}else {
			for (Node<K,V> temp = target; temp != null; temp = temp.next) {
				if(key.equals(temp.key)) return temp.value;
			}
		}
		return null;
	}
	public V remove(K key) {
		V value = null;
		for (int i = dataTable.length, prevHash = -1; i >= 16; i >>= 1) {
			int hash = hash(key, i);
			if(prevHash == hash) continue;
			prevHash = hash;
			value = removeNode(key, hash);
			if(value != null) break;
		}
		return value;
	}
	// 根据key删除元素
	public V removeNode(K key, int index) {
		Node<K,V> target = dataTable[index];
		if(null == target) return null;
		Node<K,V> node = null;
		if(key == null) {
			if(null == target.key) {
				dataTable[index] = target.next;
				size--;
				return target.value;
			}
			for (Node<K,V> temp = target; temp != null; temp = temp.next) {
				if(null == temp.key) {
					node.next = temp.next;
					size--;
					return temp.value;
				}
				node = temp;
			}
		}else {
			if(key.equals(target.key)) {
				dataTable[index] = target.next;
				size--;
				return target.value;
			}
			for (Node<K,V> temp = target; temp != null; temp = temp.next) {
				if(key.equals(temp.key)) {
					node.next = temp.next;
					size--;
					return temp.value;
				}
				node = temp;
			}
		}
		return null;
	}
	public int length() {
		return dataTable.length;
	}
	// 返回元素个数
	public int size() {
		return size;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{");
		int count = 0;
		outer:for (Node<K, V> node : dataTable) {
			if(node != null) {
				for (Node<K, V> t = node; t != null; t = t.next) {
					sb.append(t+", ");	
					if(++count >= size) {
						break outer;
					}
				}
			}
		}
		if(sb.length()>3) {
			sb.replace(sb.length()-2, sb.length(), "}");
		}else {
			sb.append("}");
		}
		return sb.toString();
	}
}
