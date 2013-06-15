package com.github.vortexellauncher.gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JList;

public class ItemClickListener implements ItemListener {

	private Object lastSelected = null;
	private Set<Object> watchedItems = new HashSet<Object>();
	private Set<ItemListener> listeners = new HashSet<ItemListener>();
	
	public ItemClickListener() {
		
	}
	
	public ItemClickListener addWatchedItem(Object item) {
		watchedItems.add(item);
		return this;
	}
	
	public void removeWatchedItem(Object item) {
		watchedItems.remove(item);
	}
	
	public ItemClickListener addClickListener(ItemListener l) {
		listeners.add(l);
		return this;
	}
	
	public void removeClickListeners(ItemListener l) {
		listeners.remove(l);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.DESELECTED) {
			lastSelected = e.getItem();
		} else if (watchedItems.contains(e.getItem())) {
			implSetSelected(e, lastSelected);
			for(ItemListener l : listeners) {
				l.itemStateChanged(e);
			}
		}
	}
	
	private void implSetSelected(ItemEvent e, Object item) {
		Object src = e.getSource();
		if (src instanceof JComboBox) {
			((JComboBox)src).setSelectedItem(item);
		} else if (src instanceof JList) {
			((JList)src).setSelectedValue(item, true);
		} else {
			setSelected(e, item);
		}
	}
	
	/**
	 * Override this if the source of the item is not a JList or a JComboBox.
	 * @param e The current event (Note: This is the item selected event, not the deselected event)
	 * @param item The item that previously was selected
	 */
	public void setSelected(ItemEvent e, Object item) {};

}
