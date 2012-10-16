package de.csenk.gwt.statemachine.shared;

public interface ContinuativeStateMachineBuilder<S extends Enum<S>, E extends Enum<E>> extends StateMachineBuilder<S, E> {

	/**
	 * @param listener
	 * @return
	 */
	ContinuativeStateMachineBuilder<S, E> listen(TransitionListener<S, E> listener);
	
}
