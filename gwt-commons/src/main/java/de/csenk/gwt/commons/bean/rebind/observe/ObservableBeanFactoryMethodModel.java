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

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.editor.rebind.model.ModelUtils;

/**
 * @author senk.christian@googlemail.com
 *
 */
public class ObservableBeanFactoryMethodModel {

	/**
	 * @param logger 
	 * @param beanBaseInterface 
	 * @param method
	 * @return
	 * @throws UnableToCompleteException 
	 */
	public static ObservableBeanFactoryMethodModel create(TreeLogger logger, JClassType beanBaseInterface, JMethod method) throws UnableToCompleteException {
		final JClassType returnType = method.getReturnType().isInterface();
		if (returnType == null || returnType.equals(beanBaseInterface)) {
			die(logger, "The return type of method %s must be ObservableBean.", method.getName());
		}
		
		final JClassType beanInterface = ModelUtils.findParameterizationOf(beanBaseInterface, returnType)[0];
		if (beanInterface.isInterface() == null) {
			die(logger, "The %s parameterization is not an interface", beanInterface.getQualifiedSourceName());
		}
		
		final ObservableBeanModel beanModel = ObservableBeanModel.create(beanInterface);

		return new ObservableBeanFactoryMethodModel(method, beanModel);
	}
	
	/**
	 * @param logger
	 * @param message
	 * @param args
	 * @throws UnableToCompleteException
	 */
	private static void die(TreeLogger logger, String message, Object... args) throws UnableToCompleteException {
		logger.log(TreeLogger.ERROR, String.format(message, args));
		throw new UnableToCompleteException();
	}
	
	private final JMethod method;
	private final ObservableBeanModel beanModel;
	
	/**
	 * @param method
	 * @param beanModel
	 */
	private ObservableBeanFactoryMethodModel(JMethod method, ObservableBeanModel beanModel) {
		this.method = method;
		this.beanModel = beanModel;
	}

	/**
	 * @return the method
	 */
	public JMethod getMethod() {
		return method;
	}

	/**
	 * @return the beanInterface
	 */
	public ObservableBeanModel getBeanModel() {
		return beanModel;
	}
	
}
