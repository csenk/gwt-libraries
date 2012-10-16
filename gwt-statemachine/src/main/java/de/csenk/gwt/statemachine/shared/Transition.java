package de.csenk.gwt.statemachine.shared;

public interface Transition<S extends Enum<S>, E extends Enum<E>> {

	/**
	 * @param listener
	 */
	void addListener(TransitionListener<S, E> listener);
	
	/**
	 * @param fromState
	 * @param toState
	 * @param event
	 */
	void notifyListener(S fromState, E event);
	
	/**
	 * @return
	 */
	S nextState();
	
}
