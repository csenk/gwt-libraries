package de.csenk.gwt.statemachine.shared;

public interface TransitionListener<S extends Enum<S>, E extends Enum<E>> {

	/**
	 * @param fromState
	 * @param toState
	 * @param event
	 */
	void onTransit(S fromState, S toState, E event);
	
}
