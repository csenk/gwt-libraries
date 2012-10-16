package de.csenk.gwt.statemachine.shared;

public interface TransitionToBuilder<S extends Enum<S>, E extends Enum<E>> {

	TransitionBuilder<S, E> to(S state);
	
}
