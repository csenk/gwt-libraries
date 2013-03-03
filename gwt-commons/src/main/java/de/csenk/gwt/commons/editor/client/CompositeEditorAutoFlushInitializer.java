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

package de.csenk.gwt.commons.editor.client;

import java.util.Collection;

import com.google.gwt.editor.client.EditorContext;
import com.google.gwt.event.shared.HandlerRegistration;

import de.csenk.gwt.commons.editor.shared.AutoFlushBeanEditorDriver;

/**
 * @author senk.christian@googlemail.com
 *
 */
public class CompositeEditorAutoFlushInitializer extends AutoFlushInitializer {

	private final String path;

	/**
	 * @param handlerRegistrations
	 * @param driver
	 * @param path 
	 */
	public CompositeEditorAutoFlushInitializer(
			Collection<HandlerRegistration> handlerRegistrations,
			AutoFlushBeanEditorDriver<?, ?> driver, String path) {
		super(handlerRegistrations, driver);
		
		this.path = path;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected <Q> void flush(EditorContext<Q> ctx) {
		super.flush(ctx);
		driver.flushPath(path);
	}

}
