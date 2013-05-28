package com.github.vortexellauncher.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class ArrayListModel<E> extends ArrayList<E> implements ListModel<E> {
	private static final long serialVersionUID = 1L;
	
	private HashSet<ListDataListener> listeners = new HashSet<ListDataListener>();
	
	public ArrayListModel() {
		super();	
	}
	
	/**
	 * Tells the associated ListDataListeners that the object has been updated
	 * @param index The index of the object
	 */
	public void refresh(int index) {
		sendEventChanged(index, index);
	}
	
	@Override
	public boolean add(E e) {
		super.add(e);
		sendEventAdded(size()-1, size()-1);
		return true;
	}
	
	@Override
	public void add(int index, E element) {
		super.add(index, element);
		sendEventAdded(index, index);
	}
	
	@Override
	public E remove(int index) {
		E val = super.remove(index);
		sendEventRemoved(index, index);
		return val;
	}
	
	@Override
	public boolean remove(Object o) {
		int index = indexOf(o);
		if (index != -1) {
			remove(index);
			return true;
		}
		return false;
	}
	
	@Override
	public E set(int index, E element) {
		E val = super.set(index, element);
		sendEventChanged(index, index);
		return val;
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		int oldsize = size();
		boolean val = super.removeAll(c);
		sendEventRefreshFull(oldsize);
		return val;
	}
	
	@Override
	public int getSize() {
		return super.size();
	}

	@Override
	public E getElementAt(int index) {
		return super.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}
	
	private void sendEventRemoved(int indexFirst, int indexLast) {
		ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, indexFirst, indexLast);
		for(ListDataListener l : listeners) {
			l.intervalRemoved(e);
		}
	}
	private void sendEventAdded(int indexFirst, int indexLast) {
		ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, indexFirst, indexLast);
		for(ListDataListener l : listeners) {
			l.intervalAdded(e);
		}
	}
	private void sendEventChanged(int indexFirst, int indexLast) {
		ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, indexFirst, indexLast);
		for(ListDataListener l : listeners) {
			l.contentsChanged(e);
		}
	}
	private void sendEventRefreshFull(int oldsize) {
		ListDataEvent e1 = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, size(), oldsize);
		ListDataEvent e2 = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, size() - 1);
		for(ListDataListener l : listeners) {
			l.intervalRemoved(e1);
			l.contentsChanged(e2);
		}
	}

}
