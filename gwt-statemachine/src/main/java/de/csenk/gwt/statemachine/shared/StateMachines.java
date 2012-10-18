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

import de.csenk.gwt.statemachine.shared.impl.StateMachineBuilderImpl;

/**
 * @author senk.christian@gmail.com
 *
 * Class to start with building of new {@link StateMachine}s.
 * Named in the guava style like "Map(s)", "List(s)".
 */
public class StateMachines {

	/**
	 * Instances of this class are not required since all necessary methods a statically available.
	 */
	private StateMachines() {}
	
	/**
	 * Creates a builder object {@link StateMachineBuilder} to build up a new state machine.
	 * 
	 * @param states
	 * @param events
	 * @return
	 */
	public static <S extends Enum<S>, E extends Enum<E>> StateMachineBuilder<S, E> buildStateMachine(S[] states, E[] events) {
		return new StateMachineBuilderImpl<S, E>(states, events);
	}
	
}
