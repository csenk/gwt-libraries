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

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/**
 * @author senk.christian@googlemail.com
 *
 */
public interface HasPropertyValueChangeHandlers<T> extends HasHandlers {
	
	/**
	 * Adds a {@link PropertyValueChangeEvent} handler.
	 * 
	 * @param handler the handler
	 * @return the registration for the event
	 */
	HandlerRegistration addPropertyValueChangeHandler(PropertyValueChangeHandler<T> handler);
	
}
