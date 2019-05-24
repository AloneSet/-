package com.cy.collection;

/**
 * 自定义ArrayList集合
 * @author Alone
 * @param <E>
 */
public class MyArrayList<E>{

	private Object [] datas;
	private int size;
	private static final Object[] DEFAULT_ARRAY = {};
	private static final int DEFAULT_CAPACITY = 10;
	
	public MyArrayList(){
		this(DEFAULT_CAPACITY);
	}
	public MyArrayList(int initCapacity) {
		if(initCapacity < 0) 
			throw new IllegalArgumentException("Illegal Capacity: "+initCapacity);
		else if(initCapacity == 0) this.datas = DEFAULT_ARRAY;
		else this.datas = new Object[initCapacity];
	}
	// 增加元素
	public boolean add(E element) {
		checkCapacity(size+1);
		datas[size++] = element;
		return true;
	}
	// 插入元素
	public boolean add(int index, E element) {
		checkIndex(index);
		checkCapacity(size+1);
		int copyLength = size - index;
		System.arraycopy(datas, index, datas, index+1, copyLength);
		datas[index] = element;
		size++;
		return true;
	}
	public boolean remove(int index) {
		checkIndex(index);
		fastRemove(index);
		return true;
	}
	public boolean remove(E element) {
		if(element == null) {
			for (int i = 0; i < size ; i++) {
				if(null == datas[i]) {
					fastRemove(i);
					return true;
				}
			}
		}else {
			for (int i = 0; i < size ; i++) {
				if(datas[i].equals(element)) {
					fastRemove(i);
					return true;
				}
			}
		}
		return false;
	}
	public boolean removeAll(E element) {
		if(element == null) {
			for (int i = 0; i < size; i++) {
				if(null == datas[i]) {
					fastRemove(i);
				}
			}
		}else{
			for (int i = 0; i < size; i++) {
				if(element.equals(datas[i])) {
					fastRemove(i);
				}
			}
		}
		return true;
	}
	// 删除元素的方法
	private void fastRemove(int index) {
		int copyLength = size - index - 1;
		if(copyLength > 0)
			System.arraycopy(datas, index+1, datas, index, copyLength);
		datas[--size]=null;
	}
	// 修改指定位置的元素
	public boolean set(int index, E element) {
		checkIndex(index);
		datas[index] = element;
		return true;
	}
	// 查找元素第一次出现的索引位值
	public int indexOf(E element) {
		for(int i = 0; i < size; i++) {
			if(datas[i].equals(element)) return i;
		}
		return -1;
	}
	// 查找元素最后一次出现的索引位值
	public int lastIndexOf(E element) {
		for(int i = size-1; i >= 0; i--) {
			if(datas[i].equals(element)) return i;
		}
		return -1;
	}
	// 验证数组容量
	private void checkCapacity(int minCapacity) {
		if( datas == DEFAULT_ARRAY) 
			minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
		if(minCapacity > datas.length)
			arrGrow(minCapacity);
	}
	// 数组扩容
	private void arrGrow(int minCapacity) {
		int oldCapacity = datas.length;
		int newCapacity = oldCapacity + (oldCapacity>>1);
		if(newCapacity < minCapacity)
			newCapacity = minCapacity;
		Object [] newArr = new Object[newCapacity]; 
		System.arraycopy(datas, 0, newArr, 0, oldCapacity);
		this.datas = newArr;
	}
	// 获取元素
	@SuppressWarnings("unchecked")
	public E get(int index) {
		checkIndex(index);
		return (E) datas[index];
	}
	// 验证index是否合法
	private void checkIndex(int index) {
		if(index < 0 || index >= size) 
			throw new ArrayIndexOutOfBoundsException("index:"+index);
	}
	// 返回集合长度
	public int size() {
		return size;
	}
	//返回一个数组
	@SuppressWarnings("unchecked")
	public Object[] toArray() {
		Object [] newArr = new Object[size]; 
		System.arraycopy(datas, 0, newArr, 0, size);
		return newArr;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(int i = 0; i < size; i++)
			sb.append(get(i)+",");
		sb.replace(sb.length()-1, sb.length(), "]");
		return sb.toString();
	}
}
