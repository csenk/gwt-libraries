package de.csenk.gwt.statemachine.shared.impl;

import com.google.common.collect.HashBasedTable;

import de.csenk.gwt.statemachine.shared.StateMachine;
import de.csenk.gwt.statemachine.shared.Transition;

public class StateMachineImpl<S extends Enum<S>, E extends Enum<E>> implements StateMachine<S, E> {

	/**
	 * @param create
	 */
	public StateMachineImpl(HashBasedTable<S, E, Transition<S, E>> transitions) {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see de.csenk.gwt.statemachine.shared.StateMachine#transit(java.lang.Enum)
	 */
	@Override
	public S transit(E event) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.csenk.gwt.statemachine.shared.StateMachine#getState()
	 */
	@Override
	public S getState() {
		// TODO Auto-generated method stub
		return null;
	}

}
