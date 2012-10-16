package de.csenk.gwt.statemachine.shared;

import de.csenk.gwt.statemachine.shared.impl.StateMachineBuilderImpl;


public class StateMachines {

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
