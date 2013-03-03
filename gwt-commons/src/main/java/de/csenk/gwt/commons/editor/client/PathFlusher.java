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

import com.google.gwt.editor.client.EditorContext;
import com.google.gwt.editor.client.EditorVisitor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.ValueAwareEditor;


/**
 * @author senk.christian@googlemail.com
 *
 */
public class PathFlusher extends EditorVisitor {

	private final String path;

	/**
	 * @param path
	 */
	public PathFlusher(String path) {
		this.path = path;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> boolean visit(EditorContext<T> ctx) {
		if (!path.equals(ctx.getAbsolutePath()))
			return true; //TODO Optimize here by checking if it is necessary to descent further ...
		
		flush(ctx);
		return true;
	}

	/**
	 * @see com.google.gwt.editor.client.impl.Flusher.endVisit(EditorContext<Q> ctx)
	 * @param ctx
	 */
	private <Q> void flush(EditorContext<Q> ctx) {
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
	
}
