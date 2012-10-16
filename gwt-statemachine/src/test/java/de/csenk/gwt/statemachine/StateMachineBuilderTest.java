package de.csenk.gwt.statemachine;

import org.junit.Assert;
import org.junit.Test;

import de.csenk.gwt.statemachine.shared.StateMachine;
import de.csenk.gwt.statemachine.shared.StateMachines;

public class StateMachineBuilderTest {

	private enum TapeDeckStates {
		
		EMPTY,
		LOADED;
		
	}
	
	private enum TapeDeckEvents {
		
		LOAD,
		UNLOAD,
		
	}
	
	@Test
	public void testBuilderNotNull() {
		Assert.assertNotNull(StateMachines.buildStateMachine(TapeDeckStates.values(), TapeDeckEvents.values()));
	}
	
	@Test
	public void testDSL() {
		final StateMachine<TapeDeckStates, TapeDeckEvents> stateMachine = 
			StateMachines.buildStateMachine(TapeDeckStates.values(), TapeDeckEvents.values())
				.from(TapeDeckStates.EMPTY).to(TapeDeckStates.LOADED).on(TapeDeckEvents.LOAD)
				.from(TapeDeckStates.LOADED).to(TapeDeckStates.EMPTY).on(TapeDeckEvents.UNLOAD)
			.create(TapeDeckStates.EMPTY);
	}

}
