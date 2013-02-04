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

package de.csenk.gwt.commons.bean.rebind.observe;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JGenericType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.TypeOracle;

import de.csenk.gwt.commons.bean.shared.observe.ObservableBean;
import de.csenk.gwt.commons.bean.shared.observe.ObservableBeanFactory;

/**
 * @author senk.christian@googlemail.com
 *
 */
public class ObservableBeanFactoryModel {

	/**
	 * @param logger
	 * @param sourceType
	 * @return
	 * @throws UnableToCompleteException 
	 */
	public static ObservableBeanFactoryModel create(TreeLogger logger, JClassType sourceType) throws UnableToCompleteException {
		final TypeOracle oracle = sourceType.getOracle();
		
	    final JGenericType beanBaseInterface = oracle.findType(ObservableBean.class.getCanonicalName()).isGenericType();
	    final JClassType beanFactoryBaseInterface = oracle.findType(ObservableBeanFactory.class.getCanonicalName()).isInterface();
		
	    final Set<ObservableBeanFactoryMethodModel> beanFactoryMethods = modelBeanFactoryMethods(logger, beanBaseInterface, beanFactoryBaseInterface, sourceType);
	    final Map<JClassType, ObservableBeanModel> beans = modelBeans(beanFactoryMethods);
	    
		return new ObservableBeanFactoryModel(beanBaseInterface, beanFactoryBaseInterface, beanFactoryMethods, beans);
	}
	
	/**
	 * @param beanFactoryMethods
	 * @return
	 */
	private static Map<JClassType, ObservableBeanModel> modelBeans(Set<ObservableBeanFactoryMethodModel> beanFactoryMethods) {
		final Map<JClassType, ObservableBeanModel> beans = Maps.newHashMap();
		
		for (ObservableBeanFactoryMethodModel methodModel : beanFactoryMethods) {
			beans.put(methodModel.getBeanModel().getSourceType(), methodModel.getBeanModel());
		}
		
		return ImmutableMap.copyOf(beans);
	}

	/**
	 * @param logger 
	 * @param beanBaseInterface 
	 * @param beanFactoryBaseInterface
	 * @param sourceTyp
	 * @return
	 * @throws UnableToCompleteException 
	 */
	private static Set<ObservableBeanFactoryMethodModel> modelBeanFactoryMethods(TreeLogger logger, JClassType beanBaseInterface, JClassType beanFactoryBaseInterface, JClassType sourceType) throws UnableToCompleteException {
		final Set<ObservableBeanFactoryMethodModel> beanFactoryMethods = Sets.newHashSet();
		
		for (JMethod method : sourceType.getOverridableMethods()) {
			if (!method.getEnclosingType().equals(beanFactoryBaseInterface)) {
				beanFactoryMethods.add(ObservableBeanFactoryMethodModel.create(logger, beanBaseInterface, method)); // Ignore methods in ObservableBeanFactory
			}
		}
		
		return ImmutableSet.copyOf(beanFactoryMethods);
	}

	private final JGenericType beanBaseInterface;
	private final JClassType beanFactoryBaseInterface;
	private final Set<ObservableBeanFactoryMethodModel> beanFactoryMethods;
	private final Map<JClassType, ObservableBeanModel> beans;
	
	/**
	 * @param beanBaseInterface
	 * @param beanFactoryBaseInterface
	 * @param beanFactoryMethods
	 * @param beans 
	 */
	private ObservableBeanFactoryModel(
			JGenericType beanBaseInterface,
			JClassType beanFactoryBaseInterface,
			Set<ObservableBeanFactoryMethodModel> beanFactoryMethods, 
			Map<JClassType, ObservableBeanModel> beans) {
		this.beanBaseInterface = beanBaseInterface;
		this.beanFactoryBaseInterface = beanFactoryBaseInterface;
		this.beanFactoryMethods = beanFactoryMethods;
		this.beans = beans;
	}

	/**
	 * @return the observableBeanInterface
	 */
	public JGenericType getBeanBaseInterface() {
		return beanBaseInterface;
	}

	/**
	 * @return the observableBeanFactoryInterface
	 */
	public JClassType getBeanFactoryBaseInterface() {
		return beanFactoryBaseInterface;
	}

	/**
	 * @return the observableBeanFactoryMethods
	 */
	public Set<ObservableBeanFactoryMethodModel> getBeanFactoryMethods() {
		return beanFactoryMethods;
	}

	/**
	 * @return the beans
	 */
	public Map<JClassType, ObservableBeanModel> getBeans() {
		return beans;
	}

}
