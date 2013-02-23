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

/**
 * A controller for an implementation of a bean interface. Instances of
 * ObservableBeans are obtained from an {@link ObservableBeanFactory}.
 * 
 * @param <T> the type of interface that will be wrapped.
 * 
 * @author senk.christian@googlemail.com
 */
public interface ObservableBean<T> {

	/**
	 * Returns a proxy implementation of the <code>T</code> interface which will
	 * delegate to the underlying wrapped object, if any.
	 * 
	 * @return a proxy that delegates to the wrapped object
	 */
	T as();
	
	/**
	 * Returns a controller for a particular bean property.
	 * As {@code propertyReference} you simply use the property getter.
	 * 
	 * <pre>
	 * final ObservableBean<Actor> actorObservableBean = actorObservableBeanFactory.create();
	 * final Actor actor = actorObservableBean.as();
	 * 
	 * actorObservableBean.getProperty(actor.getName());
	 * </pre>
	 * 
	 * To be truth, this is a bit magic and can be a little confusing because the actual property value
	 * isn't used to return the {@link ObservableProperty}. Instead the {@link ObservableBean} remembers
	 * the last property accessed and returns it here.
	 * 
	 * @param propertyReference actual discarded reference to the property value.
	 * @return the {@link ObservableProperty} associated with the last accessed bean property.
	 */
	<V> ObservableProperty<V> getProperty(V propertyReference);
	
}
