/*
 * Copyright 2012 Christian Senk
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.csenk.gwt.statemachine.shared.impl;

import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;

import de.csenk.gwt.statemachine.shared.ContinuativeStateMachineBuilder;
import de.csenk.gwt.statemachine.shared.StateMachine;
import de.csenk.gwt.statemachine.shared.StateMachineBuilder;
import de.csenk.gwt.statemachine.shared.Transition;
import de.csenk.gwt.statemachine.shared.TransitionBuilder;
import de.csenk.gwt.statemachine.shared.TransitionListener;
import de.csenk.gwt.statemachine.shared.TransitionToBuilder;

/**
 * @author senk.christian@gmail.com
 *
 * Default implementation of {@link StateMachineBuilder}.
 * Uses closures to ease the building of a new state machine.
 *
 * @param <S>
 * @param <E>
 */
public class StateMachineBuilderImpl<S extends Enum<S>, E extends Enum<E>> implements de.csenk.gwt.statemachine.shared.StateMachineBuilder<S, E> {

	private class TransitionToBuilderImpl implements TransitionToBuilder<S, E> {

		private final S state;

		/**
		 * @param state
		 */
		public TransitionToBuilderImpl(S state) {
			this.state = state;
		}

		/* (non-Javadoc)
		 * @see de.csenk.gwt.statemachine.shared.TransitionToBuilder#to(java.lang.Enum)
		 */
		@Override
		public TransitionBuilder<S, E> to(S state) {
			return new TransitionBuilderImpl(this.state, state);
		}
		
	}
	
	private class TransitionBuilderImpl implements TransitionBuilder<S, E> {

		private final S fromState;
		private final S toState;

		/**
		 * @param fromState
		 * @param toState
		 */
		public TransitionBuilderImpl(S fromState, S toState) {
			this.fromState = fromState;
			this.toState = toState;
		}

		/* (non-Javadoc)
		 * @see de.csenk.gwt.statemachine.shared.TransitionBuilder#on(java.lang.Enum, E[])
		 */
		@Override
		@SuppressWarnings("unchecked")
		public ContinuativeStateMachineBuilder<S, E> on(E event, E... events) {
			final Set<E> combinedEvents = Sets.newHashSet(event);
			if (events != null)
				combinedEvents.addAll(Sets.newHashSet(events));
			
			addTransition(fromState, toState, combinedEvents);
			return new ContinuativeStateMachineBuilderImpl(fromState, toState, combinedEvents);
		}

		/* (non-Javadoc)
		 * @see de.csenk.gwt.statemachine.shared.TransitionBuilder#onAny()
		 */
		@Override
		public ContinuativeStateMachineBuilder<S, E> onAny() {
			final Set<E> events = Sets.newHashSet(StateMachineBuilderImpl.this.events);
			
			addTransition(fromState, toState, events);
			return new ContinuativeStateMachineBuilderImpl(fromState, toState, events);
		}
		
	}

	private class ContinuativeStateMachineBuilderImpl implements ContinuativeStateMachineBuilder<S, E> {

		private final S fromState;
		private final S toState;
		private final Set<E> events;

		/**
		 * @param fromState
		 * @param toState
		 * @param events
		 */
		public ContinuativeStateMachineBuilderImpl(S fromState, S toState, Set<E> events) {
			this.fromState = fromState;
			this.toState = toState;
			this.events = events;
		}

		/* (non-Javadoc)
		 * @see de.csenk.gwt.statemachine.shared.StateMachineBuilder#from(java.lang.Enum)
		 */
		@Override
		public TransitionToBuilder<S, E> from(S state) {
			return StateMachineBuilderImpl.this.from(state);
		}

		/* (non-Javadoc)
		 * @see de.csenk.gwt.statemachine.shared.StateMachineBuilder#create(java.lang.Enum)
		 */
		@Override
		public StateMachine<S, E> create(S state) {
			return StateMachineBuilderImpl.this.create(state);
		}

		/* (non-Javadoc)
		 * @see de.csenk.gwt.statemachine.shared.ContinuativeStateMachineBuilder#listen(de.csenk.gwt.statemachine.shared.TransitionListener)
		 */
		@Override
		public ContinuativeStateMachineBuilder<S, E> listen(TransitionListener<S, E> listener) {
			addTransitionListener(fromState, toState, events, listener);
			return this;
		}
		
	}
	
	private final Table<S, E, Transition<S, E>> transitions;
	
	private final S[] states;
	private final E[] events;
	
	/**
	 * @param states
	 * @param events
	 */
	public StateMachineBuilderImpl(S[] states, E[] events) {
		this.states = states;
		this.events = events;
		
		this.transitions = HashBasedTable.create(this.states.length, this.events.length);
	}

	/* (non-Javadoc)
	 * @see de.csenk.gwt.statemachine.shared.StateMachineBuilder#from(java.lang.Enum)
	 */
	@Override
	public TransitionToBuilder<S, E> from(S state) {
		Preconditions.checkNotNull(state);
		return new TransitionToBuilderImpl(state);
	}

	/* (non-Javadoc)
	 * @see de.csenk.gwt.statemachine.shared.StateMachineBuilder#create(java.lang.Enum)
	 */
	@Override
	public StateMachine<S, E> create(S state) {
		return new StateMachineImpl<S, E>(state, HashBasedTable.create(transitions));
	}

	/**
	 * Adds one or more transitions (depends on the numbers of events specified) between two states.
	 * 
	 * @param fromState
	 * @param toState
	 * @param events
	 */
	private void addTransition(S fromState, S toState, Set<E> events) {
		for (E event : events)
			transitions.put(fromState, event, new TransitionImpl<S, E>(toState));
	}
	
	/**
	 * Adds a listener to one or more established transitions (depends on the numbers of events specified).
	 * 
	 * @param fromState
	 * @param toState
	 * @param events
	 * @param listener
	 */
	public void addTransitionListener(S fromState, S toState, Set<E> events, TransitionListener<S, E> listener) {
		for (E event : events)
			transitions.get(fromState, event).addListener(listener);
	}
	
}
