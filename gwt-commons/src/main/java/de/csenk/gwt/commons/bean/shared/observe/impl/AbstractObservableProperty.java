/*
 * Copyright 2012 Christian Senk
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.csenk.gwt.commons.bean.shared.observe.impl;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

import de.csenk.gwt.commons.bean.shared.observe.ObservableProperty;
import de.csenk.gwt.commons.bean.shared.observe.PropertyValueChangeEvent;
import de.csenk.gwt.commons.bean.shared.observe.PropertyValueChangeHandler;

/**
 * @author senk.christian@googlemail.com
 *
 */
public abstract class AbstractObservableProperty<V> implements ObservableProperty<V> {

	private final HandlerManager handlerManager;
	
	/**
	 * 
	 */
	public AbstractObservableProperty() {
		handlerManager = new HandlerManager(this);
	}
	
	/**
	 * @param value
	 */
	public abstract void set(V value);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HandlerRegistration addPropertyValueChangeHandler(PropertyValueChangeHandler<V> handler) {
		return handlerManager.addHandler(PropertyValueChangeEvent.getType(), handler);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}
	
}
