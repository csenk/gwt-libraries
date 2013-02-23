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
import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.web.bindery.autobean.gwt.rebind.model.JBeanMethod;

/**
 * @author senk.christian@googlemail.com
 *
 */
public class ObservableBeanModel {

	/**
	 * @param logger 
	 * @param type
	 * @return
	 * @throws UnableToCompleteException 
	 */
	public static ObservableBeanModel create(TreeLogger logger, JClassType type) throws UnableToCompleteException {
		final String topLevelTypeName = type.getName().replace('.', '_');
		final String simpleImplementationTypeName = topLevelTypeName + "ObservableBean";
		
		final String packageName = type.getPackage().getName();
		final String qualifiedImplementationTypeName = packageName + "." + simpleImplementationTypeName;
		
		final Set<ObservableBeanMethodModel> methods = modelMethods(type);
		final Map<String, ObservableBeanPropertyModel> properties = modelProperties(logger, methods, type.getOracle());
		
		return new ObservableBeanModel(
				type, 
				topLevelTypeName, 
				simpleImplementationTypeName, 
				qualifiedImplementationTypeName, 
				methods,
				properties);
	}

	/**
	 * @param logger 
	 * @param methods
	 * @param typeOracle 
	 * @return
	 * @throws UnableToCompleteException 
	 */
	private static Map<String, ObservableBeanPropertyModel> modelProperties(TreeLogger logger, Set<ObservableBeanMethodModel> methods, TypeOracle typeOracle) throws UnableToCompleteException {
		final Multimap<String, ObservableBeanMethodModel> associatedMethods = LinkedListMultimap.create();
		for (ObservableBeanMethodModel methodModel : methods) {
			final JBeanMethod action = methodModel.getAction();
			if (action != JBeanMethod.SET && action != JBeanMethod.GET)
				continue;
			
			final String propertyName = action.inferName(methodModel.getMethod());
			associatedMethods.put(propertyName, methodModel);
		}
		
		final Map<String, ObservableBeanPropertyModel> properties = Maps.newHashMap();
		for (String propertyName : associatedMethods.keySet()) {
			if (properties.containsKey(propertyName))
				die(logger, "Multiple getters/setters for property %s. Check spelling and for correct camel case.", propertyName);
			
			final ObservableBeanMethodModel[] propertyAccessors = Iterables.toArray(associatedMethods.get(propertyName), ObservableBeanMethodModel.class);
			final JType propertyType = determinePropertyType(propertyAccessors[0], typeOracle);
			
			properties.put(propertyName, ObservableBeanPropertyModel.create(propertyName, propertyType, propertyAccessors));
		}
		
		return ImmutableMap.copyOf(properties);
	}

	/**
	 * @param logger
	 * @param msg
	 * @throws UnableToCompleteException 
	 */
	private static void die(TreeLogger logger, String msg, Object... args) throws UnableToCompleteException {
		logger.log(Type.ERROR, String.format(msg, args));
		throw new UnableToCompleteException();
	}

	/**
	 * @param typeOracle 
	 * @param propertyAccessors
	 * @return
	 */
	private static JType determinePropertyType(ObservableBeanMethodModel methodModel, TypeOracle typeOracle) {
		final JBeanMethod action = methodModel.getAction();
		final JMethod method = methodModel.getMethod();
		
		final JType type = action == JBeanMethod.SET ? method.getParameters()[0].getType() : methodModel.getMethod().getReturnType();
		if (type.isPrimitive() != null)
			return typeOracle.findType(type.isPrimitive().getQualifiedBoxedSourceName());
		
		return type;
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

	private final JClassType type;
	private final String topLevelSourceTypeName;
	private final String simpleTargetTypeName;
	private final String qualifiedTargetTypeName;
	private final Set<ObservableBeanMethodModel> methods;
	private final Map<String, ObservableBeanPropertyModel> properties;

	/**
	 * @param type
	 * @param simpleTargetTypeName
	 * @param qualifiedTargetTypeName 
	 * @param methods 
	 * @param properties 
	 */
	public ObservableBeanModel(JClassType type, String topLevelSourceTypeName, String simpleTargetTypeName, String qualifiedTargetTypeName, Set<ObservableBeanMethodModel> methods, Map<String, ObservableBeanPropertyModel> properties) {
		this.type = type;
		this.topLevelSourceTypeName = topLevelSourceTypeName;
		this.simpleTargetTypeName = simpleTargetTypeName;
		this.qualifiedTargetTypeName = qualifiedTargetTypeName;
		this.methods = methods;
		this.properties = properties;
	}
	
	/**
	 * @return the beanInterface
	 */
	public JClassType getType() {
		return type;
	}

	/**
	 * @return the sourceTypeName
	 */
	public String getTopLevelTypeName() {
		return topLevelSourceTypeName;
	}

	/**
	 * @return the simpleTargetTypeName
	 */
	public String getSimpleImplementationTypeName() {
		return simpleTargetTypeName;
	}

	/**
	 * @return the qualifiedTargetTypeName
	 */
	public String getQualifiedImplementationTypeName() {
		return qualifiedTargetTypeName;
	}

	/**
	 * @return the methods
	 */
	public Set<ObservableBeanMethodModel> getMethods() {
		return methods;
	}

	/**
	 * @return the properties
	 */
	public Map<String, ObservableBeanPropertyModel> getProperties() {
		return properties;
	}

}
