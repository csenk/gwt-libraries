package de.csenk.gwt.statemachine.shared;

/**
 * @author senk.christian@gmail.com
 *
 */
public interface StateMachine<S extends Enum<S>, E extends Enum<E>> {

	/**
	 * Transits this state machine into the next state using the most appropriate transition.
	 * 
	 * @param event
	 * @return
	 */
	S transit(E event);
	
	/**
	 * @return
	 */
	S getState();
	
}
