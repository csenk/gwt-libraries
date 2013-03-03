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

package de.csenk.gwt.commons.editor.client;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.HasValue;

import de.csenk.gwt.commons.editor.shared.AutoFlushBeanEditorDriver;
import de.csenk.gwt.commons.editor.shared.FlushEvent;
import de.csenk.gwt.commons.editor.shared.FlushHandler;


/**
 * @author senk.christian@googlemail.com
 *
 */
public class AutoFlushBeanEditorDriverTest extends GWTTestCase {
	
	public class ActorBean {
		
		private String name;
		private int age;
		private List<String> roles = new ArrayList<String>();
		
		/**
		 * @param name
		 * @param age
		 */
		public ActorBean(String name, int age) {
			this.name = name;
			this.age = age;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
		
		/**
		 * @return the age
		 */
		public int getAge() {
			return age;
		}
		
		/**
		 * @param age the age to set
		 */
		public void setAge(int age) {
			this.age = age;
		}

		/**
		 * @return the roles
		 */
		public List<String> getRoles() {
			return roles;
		}

		/**
		 * @param roles the roles to set
		 */
		public void setRoles(List<String> roles) {
			this.roles = roles;
		}
		
	}

	public class ValueBean<V> implements HasValue<V>, LeafValueEditor<V> {

		private final HandlerManager handlerManager;
		
		private V value;
		
		/**
		 * 
		 */
		public ValueBean() {
			this.handlerManager = new HandlerManager(this);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setValue(V value) {
			setValue(value, false);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public V getValue() {
			return value;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public HandlerRegistration addValueChangeHandler(ValueChangeHandler<V> handler) {
			return handlerManager.addHandler(ValueChangeEvent.getType(), handler);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void fireEvent(GwtEvent<?> event) {
			handlerManager.fireEvent(event);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setValue(V value, boolean fireEvents) {
			final V oldValue = this.value;
			this.value = value;
			
			if (fireEvents)
				ValueChangeEvent.fireIfNotEqual(this, oldValue, this.value);
		}
		
	}
	
	public class ActorEditor implements Editor<ActorBean> {
		
		ValueBean<String> name = new ValueBean<String>();
		ValueBean<Integer> age = new ValueBean<Integer>();
		
		ListEditor<String, Editor<String>> roles = ListEditor.of(createEditorSource());

		/**
		 * @return
		 */
		private EditorSource<Editor<String>> createEditorSource() {
			return new EditorSource<Editor<String>>() {

				@Override
				public Editor<String> create(int index) {
					return new ValueBean<String>();
				}
				
			};
		}
		
	}
	
	public interface Driver extends AutoFlushBeanEditorDriver<ActorBean, ActorEditor> { }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getModuleName() {
		return "de.csenk.gwt.commons.Editor";
	}
	
	@Test
	public void testCreateNotNull() {
		assertNotNull(GWT.create(Driver.class));
	}

	@Test
	public void testSimpleAutoFlush() {
		final ActorBean model = new ActorBean("Sheldon", 28);
		
		final ActorEditor editor = new ActorEditor();
		final Driver driver = GWT.create(Driver.class);
		
		driver.initialize(editor);
		driver.edit(model);
		
		editor.name.setValue("Leonard", false);
		editor.age.setValue(30, true);
		
		Assert.assertEquals("Sheldon", model.getName());
		Assert.assertEquals(30, model.getAge());
	}
	
	@Test
	public void testComplexAutoFlush() {
		final ActorBean model = new ActorBean("Sheldon", 28);
		
		final ActorEditor editor = new ActorEditor();
		final Driver driver = GWT.create(Driver.class);
		
		final ValueBean<Boolean> flushHandlerWasCalled = new ValueBean<Boolean>();
		driver.addFlushHandler(new FlushHandler() {
			
			@Override
			public void onFlush(FlushEvent event) {
				flushHandlerWasCalled.setValue(true);
			}
			
		});
		
		driver.initialize(editor);
		driver.edit(model);
		
		editor.roles.getList().add("Flash");

		final ValueBean<String> flashEditor = (ValueBean<String>) editor.roles.getEditors().get(0);
		flashEditor.setValue("Doppler effect", true);
		
		assertTrue(flushHandlerWasCalled.getValue());
	}
	
	@Test
	public void testWithMultipleEdits() {
		final ActorBean model = new ActorBean("Sheldon", 28);
		
		final ActorEditor editor = new ActorEditor();
		final Driver driver = GWT.create(Driver.class);
		
		final ValueBean<Integer> flushHandlerCallTimes = new ValueBean<Integer>();
		flushHandlerCallTimes.setValue(0);
		
		driver.addFlushHandler(new FlushHandler() {
			
			@Override
			public void onFlush(FlushEvent event) {
				flushHandlerCallTimes.setValue(flushHandlerCallTimes.getValue() + 1);
			}
			
		});
		
		driver.initialize(editor);
		
		driver.edit(model);
		driver.edit(model);
		
		editor.name.setValue("Leonard", true);
		
		assertEquals(1, (int)flushHandlerCallTimes.getValue());
	}
	
	@Test
	public void testCompositeEditorFlush() {
		final ActorBean model = new ActorBean("Sheldon", 28);
		
		final ActorEditor editor = new ActorEditor();
		final Driver driver = GWT.create(Driver.class);
		
		driver.initialize(editor);
		driver.edit(model);
		
		editor.roles.getList().add("Flash"); //Should issue a flush?!?!?! Ask team xD
		assertEquals("Flash", model.getRoles().get(0));
		
		final ValueBean<String> roleEditor = (ValueBean<String>) editor.roles.getEditors().get(0);
		
		roleEditor.setValue("Doppler effect", true); //Should isse a flush
		assertEquals("Doppler effect", model.getRoles().get(0));
	}
	
	@Test
	public void testDetaching() {
		final ActorBean model = new ActorBean("Sheldon", 28);
		
		final ActorEditor editor = new ActorEditor();
		final Driver driver = GWT.create(Driver.class);
		
		final ValueBean<Boolean> flushHandlerWasCalled = new ValueBean<Boolean>();
		driver.addFlushHandler(new FlushHandler() {
			
			@Override
			public void onFlush(FlushEvent event) {
				flushHandlerWasCalled.setValue(true);
			}
			
		});
		
		driver.initialize(editor);
		driver.edit(model);
		
		editor.roles.getList().add("Flash");
		final ValueBean<String> flashEditor = (ValueBean<String>) editor.roles.getEditors().get(0);
		editor.roles.getList().remove("Flash");
		
		flashEditor.setValue("Doppler effect", true); //Should NOT issue a flush
		
		assertNull(flushHandlerWasCalled.getValue());
	}
}
