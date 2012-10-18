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

package de.csenk.gwt.statemachine.shared;

/**
 * @author senk.christian@gmail.com
 *
 * Contract for a transition between two states.
 * Main purpose for internal uses.
 *
 * @param <S>
 * @param <E>
 */
public interface Transition<S extends Enum<S>, E extends Enum<E>> {

	/**
	 * Adds a {@link TransitionListener} to this transition.
	 * 
	 * @param listener
	 */
	void addListener(TransitionListener<S, E> listener);

	/**
	 * Executes this transition and notifies all registered listener.
	 * Returns the next state.
	 * 
	 * @param fromState
	 * @param event
	 * @return
	 */
	S transit(S fromState, E event);
	
}
