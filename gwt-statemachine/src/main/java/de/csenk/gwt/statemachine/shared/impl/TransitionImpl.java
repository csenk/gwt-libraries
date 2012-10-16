package de.csenk.gwt.statemachine.shared.impl;

import de.csenk.gwt.statemachine.shared.Transition;
import de.csenk.gwt.statemachine.shared.TransitionListener;

public class TransitionImpl<S extends Enum<S>, E extends Enum<E>> implements Transition<S, E> {

	private final S nextState;

	/**
	 * @param nextState
	 */
	public TransitionImpl(S nextState) {
		this.nextState = nextState;
	}

	/* (non-Javadoc)
	 * @see de.csenk.gwt.statemachine.shared.Transition#addListener(de.csenk.gwt.statemachine.shared.TransitionListener)
	 */
	@Override
	public void addListener(TransitionListener<S, E> listener) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see de.csenk.gwt.statemachine.shared.Transition#notifyListener(java.lang.Enum, java.lang.Enum)
	 */
	@Override
	public void notifyListener(S fromState, E event) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see de.csenk.gwt.statemachine.shared.Transition#nextState()
	 */
	@Override
	public S nextState() {
		return nextState;
	}

}
