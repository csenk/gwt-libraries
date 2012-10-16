package de.csenk.gwt.statemachine.shared;

public interface TransitionBuilder<S extends Enum<S>, E extends Enum<E>> {

	/**
	 * @param event
	 * @param events
	 * @return
	 */
	@SuppressWarnings("unchecked")
	ContinuativeStateMachineBuilder<S, E> on(E event, E... events);
	
	/**
	 * @return
	 */
	ContinuativeStateMachineBuilder<S, E> onAny();
	
}
