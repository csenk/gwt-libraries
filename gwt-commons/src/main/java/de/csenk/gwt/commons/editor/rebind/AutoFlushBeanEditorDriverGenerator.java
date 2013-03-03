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

package de.csenk.gwt.commons.editor.rebind;

import com.google.gwt.editor.client.impl.AbstractAutoFlushEditorDelegate;
import com.google.gwt.editor.rebind.SimpleBeanEditorDriverGenerator;

import de.csenk.gwt.commons.editor.client.AbstractAutoFlushBeanEditorDriver;
import de.csenk.gwt.commons.editor.shared.AutoFlushBeanEditorDriver;

/**
 * @author senk.christian@googlemail.com
 */
public class AutoFlushBeanEditorDriverGenerator extends SimpleBeanEditorDriverGenerator {
	
	@Override
	protected Class<?> getDriverInterfaceType() {
		return AutoFlushBeanEditorDriver.class;
	}

	@Override
	protected Class<?> getDriverSuperclassType() {
		return AbstractAutoFlushBeanEditorDriver.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Class<?> getEditorDelegateType() {
		return AbstractAutoFlushEditorDelegate.class;
	}
	
}
