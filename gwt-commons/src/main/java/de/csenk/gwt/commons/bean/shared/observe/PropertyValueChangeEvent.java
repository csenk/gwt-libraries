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

package de.csenk.gwt.commons.bean.shared.observe;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author senk.christian@googlemail.com
 *
 */
public class PropertyValueChangeEvent<T> extends GwtEvent<PropertyValueChangeHandler<T>> {

	/**
	 * Handler type.
	 */
	private static Type<PropertyValueChangeHandler<?>> TYPE = new Type<PropertyValueChangeHandler<?>>();

	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<PropertyValueChangeHandler<?>> getType() {
		return TYPE;
	}
	
	private final T oldValue;
	private final T newValue;
	
	/**
	 * Creates a value change event.
	 * 
	 * @param newValue the value
	 */
	public PropertyValueChangeEvent(T oldValue, T newValue) {
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Type<PropertyValueChangeHandler<T>> getAssociatedType() {
		return (Type) TYPE;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public T getNewValue() {
		return newValue;
	}

	/**
	 * @return the oldValue
	 */
	public T getOldValue() {
		return oldValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toDebugString() {
		return super.toDebugString() + getNewValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void dispatch(PropertyValueChangeHandler<T> handler) {
		handler.onPropertyValueChange(this);
	}
}
