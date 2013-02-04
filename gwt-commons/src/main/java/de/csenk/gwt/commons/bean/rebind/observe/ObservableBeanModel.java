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
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.web.bindery.autobean.gwt.rebind.model.JBeanMethod;

/**
 * @author senk.christian@googlemail.com
 *
 */
public class ObservableBeanModel {

	/**
	 * @param sourceType
	 * @return
	 */
	public static ObservableBeanModel create(JClassType sourceType) {
		final String topLevelSourceTypeName = sourceType.getName().replace('.', '_');
		final String simpleTargetTypeName = topLevelSourceTypeName + "ObservableBean";
		
		final String packageName = sourceType.getPackage().getName();
		final String qualifiedTargetTypeName = packageName + "." + simpleTargetTypeName;
		
		final Set<ObservableBeanMethodModel> methods = modelMethods(sourceType);
		final Map<String, ObservableBeanPropertyModel> properties = modelProperties(methods);
		
		return new ObservableBeanModel(sourceType, topLevelSourceTypeName, simpleTargetTypeName, qualifiedTargetTypeName, methods);
	}

	/**
	 * @param methods
	 * @return
	 */
	private static Map<String, ObservableBeanPropertyModel> modelProperties(Set<ObservableBeanMethodModel> methods) {
		final Map<String, ObservableBeanPropertyModel> properties = Maps.newHashMap();
		
		
		
		return ImmutableMap.copyOf(properties);
	}

	/**
	 * @param sourceType
	 * @return
	 */
	private static Set<ObservableBeanMethodModel> modelMethods(JClassType sourceType) {
		final Set<ObservableBeanMethodModel> methods = Sets.newHashSet();
		
		for (JMethod method : sourceType.getMethods()) {
			if (method.isPrivate()) {
				continue; // Ignore private methods
			}
			
			final ObservableBeanMethodModel methodModel = ObservableBeanMethodModel.create(method);
			if (methodModel.getAction() == JBeanMethod.GET || methodModel.getAction() == JBeanMethod.SET) {
				methods.add(methodModel);
			}
		}
		
		return ImmutableSet.copyOf(methods);
	}

	private final JClassType sourceType;
	private final String topLevelSourceTypeName;
	private final String simpleTargetTypeName;
	private final String qualifiedTargetTypeName;
	private final Set<ObservableBeanMethodModel> methods;

	/**
	 * @param sourceType
	 * @param simpleTargetTypeName
	 * @param qualifiedTargetTypeName 
	 * @param methods 
	 */
	public ObservableBeanModel(JClassType sourceType, String topLevelSourceTypeName, String simpleTargetTypeName, String qualifiedTargetTypeName, Set<ObservableBeanMethodModel> methods) {
		this.sourceType = sourceType;
		this.topLevelSourceTypeName = topLevelSourceTypeName;
		this.simpleTargetTypeName = simpleTargetTypeName;
		this.qualifiedTargetTypeName = qualifiedTargetTypeName;
		this.methods = methods;
	}
	
	/**
	 * @return the beanInterface
	 */
	public JClassType getSourceType() {
		return sourceType;
	}

	/**
	 * @return the sourceTypeName
	 */
	public String getTopLevelSourceTypeName() {
		return topLevelSourceTypeName;
	}

	/**
	 * @return the simpleTargetTypeName
	 */
	public String getSimpleTargetTypeName() {
		return simpleTargetTypeName;
	}

	/**
	 * @return the qualifiedTargetTypeName
	 */
	public String getQualifiedTargetTypeName() {
		return qualifiedTargetTypeName;
	}

	/**
	 * @return the methods
	 */
	public Set<ObservableBeanMethodModel> getMethods() {
		return methods;
	}

}
