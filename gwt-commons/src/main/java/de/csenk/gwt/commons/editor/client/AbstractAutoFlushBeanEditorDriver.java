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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorVisitor;
import com.google.gwt.editor.client.impl.AbstractAutoFlushEditorDelegate;
import com.google.gwt.editor.client.impl.AbstractEditorDelegate;
import com.google.gwt.editor.client.impl.AbstractSimpleBeanEditorDriver;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

import de.csenk.gwt.commons.editor.shared.AutoFlushBeanEditorDriver;
import de.csenk.gwt.commons.editor.shared.FlushEvent;
import de.csenk.gwt.commons.editor.shared.FlushHandler;

/**
 * @author senk.christian@googlemail.com
 *
 */
public abstract class AbstractAutoFlushBeanEditorDriver<T, E extends Editor<T>> 
		extends AbstractSimpleBeanEditorDriver<T, E>
		implements AutoFlushBeanEditorDriver<T, E> {

	private final HandlerManager handlerManager;
	private final List<HandlerRegistration> autoFlushHandlerRegistrations = new ArrayList<HandlerRegistration>();
	
	/**
	 * 
	 */
	public AbstractAutoFlushBeanEditorDriver() {
		this.handlerManager = new HandlerManager(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HandlerRegistration addFlushHandler(FlushHandler handler) {
		return handlerManager.addHandler(FlushEvent.getType(), handler);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected EditorVisitor createInitializerVisitor() {
		for (HandlerRegistration handlerRegistration : autoFlushHandlerRegistrations)
			handlerRegistration.removeHandler();
		
		autoFlushHandlerRegistrations.clear();		
		return new AutoFlushInitializer(autoFlushHandlerRegistrations, this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configureDelegate(AbstractEditorDelegate<T, E> rootDelegate) {
		final AbstractAutoFlushEditorDelegate<T,E> delegate = (AbstractAutoFlushEditorDelegate<T, E>) rootDelegate;
		delegate.setDriver(this);
		
		super.configureDelegate(rootDelegate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void flushPath(String path) {
//		checkObject();
//	    errors = new ArrayList<EditorError>();
	    accept(new PathFlusher(path));
	    //TODO Collect the error?!
	}
	
}
