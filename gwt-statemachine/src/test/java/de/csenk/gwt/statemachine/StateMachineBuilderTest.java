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

package de.csenk.gwt.statemachine;

import org.junit.Assert;
import org.junit.Test;

import de.csenk.gwt.statemachine.shared.StateMachine;
import de.csenk.gwt.statemachine.shared.StateMachines;
import de.csenk.gwt.statemachine.shared.TransitionListener;

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
	public void testOneTransition() {
		final StateMachine<TapeDeckStates, TapeDeckEvents> stateMachine = 
			StateMachines.buildStateMachine(TapeDeckStates.values(), TapeDeckEvents.values())
				.from(TapeDeckStates.EMPTY).to(TapeDeckStates.LOADED).on(TapeDeckEvents.LOAD)
			.create(TapeDeckStates.EMPTY);
		
		Assert.assertEquals(TapeDeckStates.EMPTY, stateMachine.getState());
		
		stateMachine.transit(TapeDeckEvents.UNLOAD);
		Assert.assertEquals(TapeDeckStates.EMPTY, stateMachine.getState());
		
		stateMachine.transit(TapeDeckEvents.LOAD);
		Assert.assertEquals(TapeDeckStates.LOADED, stateMachine.getState());
		
		stateMachine.transit(TapeDeckEvents.LOAD);
		Assert.assertEquals(TapeDeckStates.LOADED, stateMachine.getState());
		
		stateMachine.transit(TapeDeckEvents.UNLOAD);
		Assert.assertEquals(TapeDeckStates.LOADED, stateMachine.getState());
	}
	
	@Test
	public void testTwoTransition() {
		final StateMachine<TapeDeckStates, TapeDeckEvents> stateMachine = 
			StateMachines.buildStateMachine(TapeDeckStates.values(), TapeDeckEvents.values())
				.from(TapeDeckStates.EMPTY).to(TapeDeckStates.LOADED).on(TapeDeckEvents.LOAD)
				.from(TapeDeckStates.LOADED).to(TapeDeckStates.EMPTY).on(TapeDeckEvents.UNLOAD)
			.create(TapeDeckStates.EMPTY);
		
		Assert.assertEquals(TapeDeckStates.EMPTY, stateMachine.getState());
		
		stateMachine.transit(TapeDeckEvents.UNLOAD);
		Assert.assertEquals(TapeDeckStates.EMPTY, stateMachine.getState());
		
		stateMachine.transit(TapeDeckEvents.LOAD);
		Assert.assertEquals(TapeDeckStates.LOADED, stateMachine.getState());
		
		stateMachine.transit(TapeDeckEvents.LOAD);
		Assert.assertEquals(TapeDeckStates.LOADED, stateMachine.getState());
		
		stateMachine.transit(TapeDeckEvents.UNLOAD);
		Assert.assertEquals(TapeDeckStates.EMPTY, stateMachine.getState());
	}
	
	@Test
	public void testTransitioningOnAny() {
		final StateMachine<TapeDeckStates, TapeDeckEvents> stateMachine = 
				StateMachines.buildStateMachine(TapeDeckStates.values(), TapeDeckEvents.values())
					.from(TapeDeckStates.EMPTY).to(TapeDeckStates.LOADED).onAny()
				.create(TapeDeckStates.EMPTY);
			
			Assert.assertEquals(TapeDeckStates.EMPTY, stateMachine.getState());
			
			stateMachine.transit(TapeDeckEvents.UNLOAD);
			Assert.assertEquals(TapeDeckStates.LOADED, stateMachine.getState());
			
			stateMachine.transit(TapeDeckEvents.LOAD);
			Assert.assertEquals(TapeDeckStates.LOADED, stateMachine.getState());
			
			stateMachine.transit(TapeDeckEvents.LOAD);
			Assert.assertEquals(TapeDeckStates.LOADED, stateMachine.getState());
			
			stateMachine.transit(TapeDeckEvents.UNLOAD);
			Assert.assertEquals(TapeDeckStates.LOADED, stateMachine.getState());
	}

	@Test(expected = RuntimeException.class)
	public void testTransitionListener() {
		final StateMachine<TapeDeckStates, TapeDeckEvents> stateMachine = 
			StateMachines.buildStateMachine(TapeDeckStates.values(), TapeDeckEvents.values())
				.from(TapeDeckStates.EMPTY).to(TapeDeckStates.LOADED).on(TapeDeckEvents.LOAD).listen(new TransitionListener<TapeDeckStates, TapeDeckEvents>() {
					
					@Override
					public void onTransit(TapeDeckStates fromState, TapeDeckStates toState, TapeDeckEvents event) {
						throw new RuntimeException("Expected exception");
					}
					
				})
				.from(TapeDeckStates.LOADED).to(TapeDeckStates.EMPTY).on(TapeDeckEvents.UNLOAD)
			.create(TapeDeckStates.EMPTY);
		
		stateMachine.transit(TapeDeckEvents.LOAD);
	}

}
