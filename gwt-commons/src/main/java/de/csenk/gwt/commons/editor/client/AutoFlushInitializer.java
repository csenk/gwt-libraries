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

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorContext;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.editor.client.impl.Initializer;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

import de.csenk.gwt.commons.editor.shared.AutoFlushBeanEditorDriver;
import de.csenk.gwt.commons.editor.shared.FlushEvent;


/**
 * @author senk.christian@googlemail.com
 */
public class AutoFlushInitializer extends Initializer {

	private final Collection<HandlerRegistration> handlerRegistrations;
	protected final AutoFlushBeanEditorDriver<?, ?> driver;

	/**
	 * @param handlerRegistrations
	 */
	public AutoFlushInitializer(Collection<HandlerRegistration> handlerRegistrations, AutoFlushBeanEditorDriver<?, ?> driver) {
		this.handlerRegistrations = handlerRegistrations;
		this.driver = driver;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <Q> boolean visit(EditorContext<Q> ctx) {
		final HasValueChangeHandlers<Q> editor = asHasValueChangeHandlers(ctx.getEditor());
		if (editor != null) {
			handlerRegistrations.add(editor.addValueChangeHandler(createValueChangeHandler(ctx)));
		}
		
		return super.visit(ctx);
	}

	/**
	 * @param ctx
	 * @return
	 */
	private <Q> ValueChangeHandler<Q> createValueChangeHandler(final EditorContext<Q> ctx) {
		return new ValueChangeHandler<Q>() {

			@Override
			public void onValueChange(ValueChangeEvent<Q> event) {
				flush(ctx);
				driver.fireEvent(new FlushEvent());
			}
			
		};
	}
	
	/**
	 * @see com.google.gwt.editor.client.impl.Flusher.endVisit(EditorContext<Q> ctx)
	 * @param ctx
	 */
	protected <Q> void flush(EditorContext<Q> ctx) {
		// Flush ValueAware editors
		final ValueAwareEditor<Q> asValue = ctx.asValueAwareEditor();
		if (asValue != null) {
			asValue.flush();
		}

		// Pull value from LeafValueEditors and update edited object
		final LeafValueEditor<Q> asLeaf = ctx.asLeafValueEditor();
		if (asLeaf != null && ctx.canSetInModel()) {
			ctx.setInModel(asLeaf.getValue());
		}
	}
	
	/**
	 * @param editor
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <Q> HasValueChangeHandlers<Q> asHasValueChangeHandlers(final Editor<Q> editor) {
		if (editor instanceof HasValueChangeHandlers)
			return (HasValueChangeHandlers<Q>)editor;
		
		return null;
	}
	
}
