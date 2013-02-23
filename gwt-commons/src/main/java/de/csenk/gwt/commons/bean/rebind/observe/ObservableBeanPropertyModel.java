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

package de.csenk.gwt.commons.bean.rebind.observe;

import com.google.gwt.core.ext.typeinfo.JType;
import com.google.web.bindery.autobean.gwt.rebind.model.JBeanMethod;

/**
 * @author senk.christian@googlemail.com
 *
 */
public class ObservableBeanPropertyModel {

	/**
	 * @param name
	 * @param type 
	 * @param accessors 
	 * @return
	 */
	public static ObservableBeanPropertyModel create(String name, JType type, ObservableBeanMethodModel[] accessors) {
		final ObservableBeanMethodModel getter = determineAccessor(JBeanMethod.GET, accessors);
		final ObservableBeanMethodModel setter = determineAccessor(JBeanMethod.SET, accessors);
		
		return new ObservableBeanPropertyModel(name, type, getter, setter);
	}

	/**
	 * @param action
	 * @param accessors
	 * @return
	 */
	private static ObservableBeanMethodModel determineAccessor(JBeanMethod action, ObservableBeanMethodModel[] accessors) {
		for (int accessorIndex = 0; accessorIndex < accessors.length; accessorIndex++)
			if (accessors[accessorIndex].getAction() == action)
				return accessors[accessorIndex];

		return null;
	}

	private final String name;
	private final JType type;
	private final ObservableBeanMethodModel getter;
	private final ObservableBeanMethodModel setter;

	/**
	 * @param name
	 * @param type 
	 * @param setter 
	 * @param getter 
	 */
	public ObservableBeanPropertyModel(String name, JType type, ObservableBeanMethodModel getter, ObservableBeanMethodModel setter) {
		this.name = name;
		this.type = type;
		this.getter = getter;
		this.setter = setter;
	}

	/**
	 * @return the getter
	 */
	public ObservableBeanMethodModel getGetter() {
		return getter;
	}

	/**
	 * @return the setter
	 */
	public ObservableBeanMethodModel getSetter() {
		return setter;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the type
	 */
	public JType getType() {
		return type;
	}
	
}
