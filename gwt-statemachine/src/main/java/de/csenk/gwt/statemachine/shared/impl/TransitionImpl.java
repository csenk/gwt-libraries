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

package de.csenk.gwt.statemachine.shared.impl;

import java.util.Set;

import com.google.common.collect.Sets;

import de.csenk.gwt.statemachine.shared.Transition;
import de.csenk.gwt.statemachine.shared.TransitionListener;

/**
 * @author senk.christian@gmail.com
 *
 * Default implementation of {@link Transition}.
 * Maintains a listener collection of {@link TransitionListener}.
 *
 * @param <S>
 * @param <E>
 */
public class TransitionImpl<S extends Enum<S>, E extends Enum<E>> implements Transition<S, E> {

	private final Set<TransitionListener<S, E>> listener = Sets.newHashSet();
	
	private final S nextState;

	/**
	 * @param nextState
	 */
	public TransitionImpl(S nextState) {
		this.nextState = nextState;
	}

	/* (non-Javadoc)
	 * @see de.csenk.gwt.statemachine.shared.Transition#addListener(de.csenk.gwt.statemachine.shared.TransitionListener)
	 */
	@Override
	public void addListener(TransitionListener<S, E> listener) {
		this.listener.add(listener);
	}

	/* (non-Javadoc)
	 * @see de.csenk.gwt.statemachine.shared.Transition#transit(java.lang.Enum, java.lang.Enum)
	 */
	@Override
	public S transit(S fromState, E event) {
		for (TransitionListener<S, E> listener : this.listener)
			listener.onTransit(fromState, nextState, event);
		
		return nextState;
	}



}
