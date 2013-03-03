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

package de.csenk.gwt.commons.editor.shared;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author senk.christian@googlemail.com
 */
public class FlushEvent extends GwtEvent<FlushHandler> {

	/**
	 * Handler type.
	 */
	private static Type<FlushHandler> TYPE = new Type<FlushHandler>();

	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<FlushHandler> getType() {
		return TYPE;
	}

	/**
	 * Creates a value change event.
	 * 
	 * @param value
	 *            the value
	 */
	public FlushEvent() { }

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public final Type<FlushHandler> getAssociatedType() {
		return (Type) TYPE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toDebugString() {
		return super.toDebugString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void dispatch(FlushHandler handler) {
		handler.onFlush(this);
	}

}
