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

package com.google.gwt.editor.client.impl;

import java.util.Collection;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.editor.client.CompositeEditor;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorVisitor;
import com.google.gwt.event.shared.HandlerRegistration;

import de.csenk.gwt.commons.editor.client.CompositeEditorAutoFlushInitializer;
import de.csenk.gwt.commons.editor.shared.AutoFlushBeanEditorDriver;

/**
 * @author senk.christian@googlemail.com
 *
 */
public abstract class AbstractAutoFlushEditorDelegate<T, E extends Editor<T>> extends SimpleBeanEditorDelegate<T, E> {
	
	public class AutoFlushEditorDelegateChain<C, S extends Editor<C>> extends Chain<C, S> {

		private final Multimap<S, HandlerRegistration> handlerRegistrations = LinkedListMultimap.create();
		
		private Collection<HandlerRegistration> currentRegistrations;
		
		/**
		 * @param composedEditor
		 * @param composedElementType
		 */
		AutoFlushEditorDelegateChain(CompositeEditor<T, C, S> composedEditor, Class<C> composedElementType) {
			super(composedEditor, composedElementType);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void attach(C object, S subEditor) {
			currentRegistrations = handlerRegistrations.get(subEditor);
			super.attach(object, subEditor);
			currentRegistrations = null;
			
			driver.flushPath(getPath());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void detach(S subEditor) {
			final Collection<HandlerRegistration> registrations = handlerRegistrations.removeAll(subEditor);
			for (HandlerRegistration handlerRegistration : registrations)
				handlerRegistration.removeHandler();
			
			super.detach(subEditor);
		}

		/**
		 * @return
		 */
		private EditorVisitor createInitializerVisitor() {
			if (currentRegistrations == null)
				throw new IllegalStateException("createInitializerVisitor() must be called inbetween attach()/detach()");
			
			return new CompositeEditorAutoFlushInitializer(currentRegistrations, driver, getPath());
		}
		
	}
	
	private AutoFlushBeanEditorDriver<?, ?> driver;
	private AutoFlushEditorDelegateChain<?, ?> editorChain;
	
	/**
	 * Delegates back into {@link AutoFlushEditorDelegateChain} because the created visitor needs chain context.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected EditorVisitor createInitializerVisitor() {
		return editorChain.createInitializerVisitor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected <R, S extends Editor<R>> void addSubDelegate(AbstractEditorDelegate<R, S> subDelegate, String path, S subEditor) {
		final AbstractAutoFlushEditorDelegate<R, S> delegate = (AbstractAutoFlushEditorDelegate<R, S>) subDelegate;
		delegate.setDriver(driver);
		
		super.addSubDelegate(subDelegate, path, subEditor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected <R, S extends Editor<R>> void createChain(Class<R> composedElementType) {
		CompositeEditor<T, R, S> editor = (CompositeEditor<T, R, S>) getEditor();
	    editorChain = new AutoFlushEditorDelegateChain<R, S>(editor, composedElementType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Chain<?, ?> getEditorChain() {
		return editorChain;
	}

	/**
	 * @param driver
	 */
	public void setDriver(AutoFlushBeanEditorDriver<?, ?> driver) {
		this.driver = driver;
	}
	
}
