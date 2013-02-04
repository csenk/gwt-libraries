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

import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.web.bindery.autobean.gwt.rebind.model.JBeanMethod;

/**
 * @author senk.christian@googlemail.com
 *
 */
public class ObservableBeanMethodModel {

	/**
	 * @param method
	 * @return
	 */
	public static ObservableBeanMethodModel create(JMethod method) {
		final JBeanMethod action = JBeanMethod.which(method);		
		return new ObservableBeanMethodModel(action, method);
	}
	
	private final JBeanMethod action;
	private final JMethod method;
	
	/**
	 * @param action
	 * @param method
	 */
	public ObservableBeanMethodModel(JBeanMethod action, JMethod method) {
		this.action = action;
		this.method = method;
	}

	/**
	 * @return the action
	 */
	public JBeanMethod getAction() {
		return action;
	}

	/**
	 * @return the method
	 */
	public JMethod getMethod() {
		return method;
	}
	
}
