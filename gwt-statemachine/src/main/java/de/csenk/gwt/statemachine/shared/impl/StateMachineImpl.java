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

import com.google.common.collect.Table;

import de.csenk.gwt.statemachine.shared.StateMachine;
import de.csenk.gwt.statemachine.shared.Transition;

/**
 * @author senk.christian@gmail.com
 *
 * Default implementation of {@link StateMachine}.
 * Created by {@link StateMachineBuilderImpl}.
 *
 * @param <S>
 * @param <E>
 */
public class StateMachineImpl<S extends Enum<S>, E extends Enum<E>> implements StateMachine<S, E> {

	private final Table<S, E, Transition<S, E>> transitions;

	private S state;
	
	/**
	 * @param state
	 * @param transitions
	 */
	public StateMachineImpl(S state, Table<S, E, Transition<S, E>> transitions) {
		this.state = state;
		this.transitions = transitions;
	}

	/* (non-Javadoc)
	 * @see de.csenk.gwt.statemachine.shared.StateMachine#transit(java.lang.Enum)
	 */
	@Override
	public S transit(E event) {
		final Transition<S, E> transition = transitions.get(state, event);
		if (transition == null)
			return state;
		
		state = transition.transit(state, event);
		return state;
	}

	/* (non-Javadoc)
	 * @see de.csenk.gwt.statemachine.shared.StateMachine#getState()
	 */
	@Override
	public S getState() {
		return state;
	}

}
