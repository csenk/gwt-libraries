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

import com.google.gwt.core.client.impl.WeakMapping;

import de.csenk.gwt.commons.bean.shared.observe.ObservableBean;
import de.csenk.gwt.commons.bean.shared.observe.ObservableProperty;

/**
 * @author senk.christian@googlemail.com
 *
 */
public abstract class AbstractObservableBean<T> implements ObservableBean<T> {

	protected final T observed;
	
	protected ObservableProperty<?> lastAccessedProperty;
	
	/**
	 * @param observed
	 */
	protected AbstractObservableBean(T observed) {
		this.observed = observed;
		WeakMapping.set(observed, ObservableBean.class.getName(), this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <V> ObservableProperty<V> getProperty(V propertyReference) {
		if (lastAccessedProperty == null)
			throw new IllegalStateException("You have to call getProperty(V) with a property reference.");
		
		return (ObservableProperty<V>) lastAccessedProperty;
	}

}
