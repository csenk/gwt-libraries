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

package de.csenk.gwt.commons.bean.client.observe.impl;

import org.junit.Test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

import de.csenk.gwt.commons.bean.shared.observe.ObservableBean;
import de.csenk.gwt.commons.bean.shared.observe.PropertyValueChangeEvent;
import de.csenk.gwt.commons.bean.shared.observe.PropertyValueChangeHandler;


/**
 * @author senk.christian@googlemail.com
 *
 */
public class ObservableBeanFactoryImplTest extends GWTTestCase {

	public interface Actor {
		
		void setName(String name);
		
		String getName();
		
	}
	
	public interface ObservableBeanFactory extends de.csenk.gwt.commons.bean.shared.observe.ObservableBeanFactory {
		
		ObservableBean<Actor> create();
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getModuleName() {
		return "de.csenk.gwt.commons.Bean";
	}
	
	@Test
	public void testCreateNotNull() {
		final ObservableBeanFactory actorObservableBeanFactory = GWT.create(ObservableBeanFactory.class);
		
		final ObservableBean<Actor> actorObservableBean = actorObservableBeanFactory.create();
		assertNotNull(actorObservableBean);
	}
	
	@Test
	public void testAsNotNull() {
		final ObservableBeanFactory actorObservableBeanFactory = GWT.create(ObservableBeanFactory.class);
		
		final ObservableBean<Actor> actorObservableBean = actorObservableBeanFactory.create();
		assertNotNull(actorObservableBean.as());
	}
	
	@Test
	public void testGetPropertyNotNull() {
		final ObservableBeanFactory actorObservableBeanFactory = GWT.create(ObservableBeanFactory.class);
		
		final ObservableBean<Actor> actorObservableBean = actorObservableBeanFactory.create();
		final Actor actor = actorObservableBean.as();
		
		assertNotNull(actorObservableBean.getProperty(actor.getName()));
	}
	
	@Test
	public void testPropertyValueChangeEvent() {
		final ObservableBeanFactory actorObservableBeanFactory = GWT.create(ObservableBeanFactory.class);
		
		final ObservableBean<Actor> actorObservableBean = actorObservableBeanFactory.create();
		final Actor actor = actorObservableBean.as();
		
		actorObservableBean.getProperty(actor.getName()).addPropertyValueChangeHandler(new PropertyValueChangeHandler<String>() {
			
			@Override
			public void onPropertyValueChange(PropertyValueChangeEvent<String> event) {
				assertEquals(null, event.getOldValue());
				assertEquals("Sheldon", event.getNewValue());
			}
			
		});
		
		actor.setName("Sheldon");
	}
	
}
