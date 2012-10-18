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
 * Almost final builder to stage to "commit" a transition between two states.
 *
 * @param <S>
 * @param <E>
 */
public interface TransitionBuilder<S extends Enum<S>, E extends Enum<E>> {

	/**
	 * Establishes one or more transitions (depends on the numbers of events specified) between two states.
	 * 
	 * @param event
	 * @param events
	 * @return
	 */
	@SuppressWarnings("unchecked")
	ContinuativeStateMachineBuilder<S, E> on(E event, E... events);
	
	/**
	 * Establishes as much transitions as events are available between two states.
	 * 
	 * @return
	 */
	ContinuativeStateMachineBuilder<S, E> onAny();
	
}
