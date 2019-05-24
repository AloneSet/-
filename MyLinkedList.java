package com.cy.collection;

/**
 * 自定义LinkedList集合，只实现了增删改查等基础功能
 * @author Alone
 * @param <E>
 */
public class MyLinkedList<E> {
	private Node<E> first; // 第一个节点
	private Node<E> last; // 最后一个节点
	private int size; // 元素个数
	
	public int size() {
		return size;
	}
	// 增加节点
	public boolean add(E element) {
		linkLast(element);
		return true;
	}
	// 插入节点
	public boolean add(int index, E element) {
		checkIndex(index);
		if(index == 0) {
			linkFirst(element);
		}else if(index == size-1) {
			linkLast(element);
		}else {
			Node<E> temp = node(index);
			linkBefore(element, temp);
		}
		return true;
	}
	// 在链表首段添加一个节点
	private void linkFirst(E element) {
		final Node<E> f = first;
		final Node<E> newNode = new Node<E>(null, element, f);
		first = newNode;
		if(f==null) {
			last = newNode;
		}else {
			f.prev = newNode;
		}
		size++;
	}
	// 把节点添加在链表末端
	private void linkLast(E element) {
		final Node<E> l = last;
		final Node<E> newNode = new Node<E>(l, element, null);
		last = newNode;
		if(l == null) {
			first = newNode;
		}else {
			l.next = newNode;
		}
		size++;
	}
	// 插入一个节点e，在节点suc之前，suc不为空
	private void linkBefore(E element, Node<E> suc) {
		final Node<E> pred = suc.prev;
		final Node<E> newNode = new Node<>(pred, element, suc);
		suc.prev = newNode;
		if(pred == null)
			first = newNode;
		else 
			pred.next = newNode;
		size++;
	}
	// 按照指定元素删除节点 
	public boolean remove(E element) {
		if(element == null) {
			for(Node<E> temp = first; temp != null; temp = temp.next) {
				if(null == temp.data) {
					removeNode(temp);
					return true;
				}
			}
		}else {
			for(Node<E> temp = first; temp != null; temp = temp.next) {
				if(element.equals(temp.data)) {
					removeNode(temp);
					return true;
				}
			}
		}
		return false;
	}
	// 按照指定索引删除节点
	public E remove(int index) {
		checkIndex(index);
		if(index == 0) return removeFirst(first);
		if(index == size - 1) return removeLast(last);
		return removeNode(node(index));
	}
	// 删除第一个节点
	private E removeFirst(Node<E> f) {
		Node<E> nextd = f.next;
		if(nextd == null) {
			last = null;
		}else {
			nextd.prev = null;
		}
		first = nextd;
		size--;
		return f.data;
	}
	// 删除最后一个节点
	private E removeLast(Node<E> l) {
		Node<E> pred = l.prev;
		if(pred == null) {
			first = null;
		}else {
			pred.next = null;
		}
		last = pred;
		size--;
		return l.data;
	}
	// 删除节点
	private E removeNode(Node<E> n) {
		Node<E> pred = n.prev;
		Node<E> nextd = n.next;
		if(pred == null) {
			first = nextd;
			if(nextd == null) 
				last = null;
			else
				nextd.prev = null;
		}else {
			pred.next = nextd;
			if(nextd == null) 
				last = pred;
			else
				nextd.prev = pred;
		}
		size--;
		return n.data;
	}
	// 修改指定索引上的元素
	public boolean set(int index, E element) {
		node(index).data = element;
		return true;
	}
	// 查找元素
	public int indexOf(E element) {
		Node<E> temp = first;
		if(element == null) {
			for (int i = 0; i < size; i++) {
				if(null == temp.data) return i;
				temp = temp.next;
			}
		}else {
			for (int i = 0; i < size; i++) {
				if(element.equals(temp.data)) return i;
				temp = temp.next;
			}
		}
		return -1;
	}
	// 节点类
	@SuppressWarnings("hiding")
	private class Node<E>{
		E data;
		Node<E> prev;
		Node<E> next;
//		public Node(){}
		public Node(Node<E> prev, E data, Node<E> next) {
			this.data = data;
			this.prev = prev;
			this.next = next;
		}
	}
	// 按照指定索引获取元素
	public E get(int index) {
		checkIndex(index);
		Node<E> temp = node(index);
		if(null != temp) return temp.data;
		return null;
	}
	// 按照指定索引查找节点
	private Node<E> node(int index){
		Node<E> temp = null;
		if(index < (size>>1)) {
			temp = first;
			for(int i = 0; i < index; i++) 
				temp = temp.next;
			return temp;
		}else {
			temp = last;
			for(int i = size - 1; i > index; i--) 
				temp = temp.prev;		
			return temp;
		}
	}
	// 验证index是否合法
	private void checkIndex(int index) {
		if(index < 0 || index >= size) 
			throw new ArrayIndexOutOfBoundsException("index:"+index);
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Node<E> temp = first;
		sb.append("[");
		for(int i = 0; i < size; i++) {
			sb.append(temp.data+",");
			temp = temp.next;
		}
			
		sb.replace(sb.length()-1, sb.length(), "]");
		return sb.toString();
	}
}
