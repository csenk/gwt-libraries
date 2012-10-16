package de.csenk.gwt.statemachine.shared;

public interface StateMachineBuilder<S extends Enum<S>, E extends Enum<E>> {
	
	/**
	 * @param event
	 * @param events
	 * @return
	 */
	TransitionToBuilder<S, E> from(S state);
	
	/**
	 * @return
	 */
	StateMachine<S, E> create(S state);
	
}
