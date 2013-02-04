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

package de.csenk.gwt.commons.bean.rebind.observe.impl;

import java.io.PrintWriter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.editor.rebind.model.ModelUtils;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.web.bindery.autobean.gwt.rebind.model.JBeanMethod;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

import de.csenk.gwt.commons.bean.rebind.SourceGeneration;
import de.csenk.gwt.commons.bean.rebind.observe.ObservableBeanFactoryMethodModel;
import de.csenk.gwt.commons.bean.rebind.observe.ObservableBeanFactoryModel;
import de.csenk.gwt.commons.bean.rebind.observe.ObservableBeanMethodModel;
import de.csenk.gwt.commons.bean.rebind.observe.ObservableBeanModel;
import de.csenk.gwt.commons.bean.shared.observe.PropertyValueChangeEvent;
import de.csenk.gwt.commons.bean.shared.observe.impl.AbstractObservableBean;
import de.csenk.gwt.commons.bean.shared.observe.impl.AbstractObservableBeanFactory;
import de.csenk.gwt.commons.bean.shared.observe.impl.AbstractObservableProperty;

/**
 * @author senk.christian@googlemail.com
 *
 */
public class ObservableBeanFactoryCreator {

	private final TreeLogger logger;
	private final GeneratorContext context;
	
	private final JClassType sourceType;
	private final String simpleTargetTypeName;
	private final ObservableBeanFactoryModel model;
	
	/**
	 * @param logger
	 * @param context
	 * @param typeName
	 * @throws UnableToCompleteException 
	 */
	public ObservableBeanFactoryCreator(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
		this.logger = logger;
		this.context = context;

		final TypeOracle oracle = context.getTypeOracle();
		this.sourceType = oracle.findType(typeName).isInterface();
		if (sourceType == null) {
			die(logger, typeName + " is not an interface type");
		}
		
		this.simpleTargetTypeName = sourceType.getName().replace('.', '_') + "Impl";
		this.model = ObservableBeanFactoryModel.create(logger, sourceType);
	}

	/**
	 * @param logger
	 * @param message
	 * @throws UnableToCompleteException
	 */
	private void die(TreeLogger logger, String message) throws UnableToCompleteException {
		logger.log(TreeLogger.ERROR, message);
		throw new UnableToCompleteException();
	}

	/**
	 * @return
	 */
	public String create() {
		final String packageName = sourceType.getPackage().getName();
		
		final PrintWriter targetTypePrintWriter = context.tryCreate(logger, packageName, simpleTargetTypeName);
	    if (targetTypePrintWriter == null) {
	    	return packageName + "." + simpleTargetTypeName;
	    }
		
		final ClassSourceFileComposerFactory beanFactoryFactory = new ClassSourceFileComposerFactory(packageName, simpleTargetTypeName);
		beanFactoryFactory.setSuperclass(AbstractObservableBeanFactory.class.getCanonicalName());
		beanFactoryFactory.addImplementedInterface(sourceType.getQualifiedSourceName());
		
		beanFactoryFactory.addImport(AutoBean.class.getCanonicalName());
		beanFactoryFactory.addImport(GWT.class.getCanonicalName());
		
		final SourceWriter beanFactoryWriter = beanFactoryFactory.createSourceWriter(context, targetTypePrintWriter);
	    try {
	    	beanFactoryWriter.println();
	    	
	    	for (ObservableBeanModel beanModel : model.getBeans().values()) {
	    		writeObservableBean(beanModel);
	    	}
	    	
	    	writeAutoBeanFactory(sourceType.getOracle(), beanFactoryWriter);
	    	writeFactoryMethods(beanFactoryWriter);
	    } finally {
	    	beanFactoryWriter.commit(logger);
	    }
	    
		return beanFactoryFactory.getCreatedClassName();
	}

	/**
	 * @param typeOracle 
	 * @param beanFactoryWriter
	 */
	private void writeAutoBeanFactory(TypeOracle typeOracle, SourceWriter beanFactoryWriter) {
		final JClassType autoBeanFactoryInterface = context.getTypeOracle().findType(AutoBeanFactory.class.getCanonicalName());
		
		beanFactoryWriter.println("public interface AutoBeanFactory extends %s {", autoBeanFactoryInterface.getQualifiedSourceName());
		
		for (ObservableBeanModel beanModel : model.getBeans().values()) {
			beanFactoryWriter.indentln("%s<%s> create%s();", AutoBean.class.getSimpleName(), beanModel.getSourceType().getQualifiedSourceName(), beanModel.getTopLevelSourceTypeName());
		}
		beanFactoryWriter.println("}");
		beanFactoryWriter.println();
		
		beanFactoryWriter.println("private static final AutoBeanFactory autoBeanFactory = GWT.create(AutoBeanFactory.class);");
		beanFactoryWriter.println();
	}

	/**
	 * @param beanModel 
	 * 
	 */
	private void writeObservableBean(ObservableBeanModel beanModel) {
		final String packageName = beanModel.getSourceType().getPackage().getName();
		
		final PrintWriter targetTypePrintWriter = context.tryCreate(logger, packageName, beanModel.getSimpleTargetTypeName());
	    if (targetTypePrintWriter == null) {
	    	return;
	    }
	    
	    final ClassSourceFileComposerFactory beanFactory = new ClassSourceFileComposerFactory(packageName, beanModel.getSimpleTargetTypeName());
		beanFactory.setSuperclass(String.format("%s<%s>", AbstractObservableBean.class.getSimpleName(), beanModel.getSourceType().getQualifiedSourceName()));
		
		beanFactory.addImport(AbstractObservableBean.class.getCanonicalName());
		beanFactory.addImport(AbstractObservableProperty.class.getCanonicalName());
		beanFactory.addImport(PropertyValueChangeEvent.class.getCanonicalName());
		
		final SourceWriter beanWriter = beanFactory.createSourceWriter(context, targetTypePrintWriter);
		try {
			beanWriter.println();
			
			writeBeanShimField(beanWriter, beanModel);
			writeConstructor(beanWriter, beanModel);
			writeAsMethod(beanWriter, beanModel);
		} finally {
			beanWriter.commit(logger);
		}
	}

	/**
	 * @param beanWriter
	 * @param beanModel 
	 */
	private void writeAsMethod(SourceWriter beanWriter, ObservableBeanModel beanModel) {
		beanWriter.println("public %s as() {", beanModel.getSourceType().getQualifiedSourceName());
		beanWriter.indentln("return shim;");
		beanWriter.println("}");
		beanWriter.println();
	}

	/**
	 * @param beanWriter
	 * @param beanModel
	 */
	private void writeConstructor(SourceWriter beanWriter, ObservableBeanModel beanModel) {
		beanWriter.println("public %s(%s observed) {", beanModel.getSimpleTargetTypeName(), beanModel.getSourceType().getName());
		beanWriter.indentln("super(observed);");
		beanWriter.println("}");
		beanWriter.println();
	}

	/**
	 * @param beanWriter 
	 * @param beanModel 
	 * 
	 */
	private void writeBeanShimField(SourceWriter beanWriter, ObservableBeanModel beanModel) {
		beanWriter.println("private final %1$s shim = new %1$s() {", beanModel.getSourceType().getQualifiedSourceName());
		beanWriter.indent();
		beanWriter.println();

		writeShimFields(beanWriter, beanModel);
		writeShimMethods(beanWriter, beanModel);
		
		beanWriter.outdent();
		beanWriter.println("};");
		beanWriter.println();
	}

	/**
	 * @param beanWriter
	 * @param beanModel
	 */
	private void writeShimFields(SourceWriter beanWriter, ObservableBeanModel beanModel) {
		for (ObservableBeanMethodModel methodModel : beanModel.getMethods()) {
			if (methodModel.getAction() != JBeanMethod.SET)
				continue;
			
			beanWriter.println("private final %1$s<%2$s> %3$s = new %1$s<%2$s>() {", AbstractObservableProperty.class.getSimpleName(), ModelUtils.getQualifiedBaseSourceName(methodModel.getPropertyType()), methodModel.getPropertyName());
			beanWriter.indent();
			beanWriter.println();
			
			beanWriter.println("public void set(%s value) {", ModelUtils.getQualifiedBaseSourceName(methodModel.getPropertyType()));
			beanWriter.indent();
			
			//TODO Get old and new value
			beanWriter.println("fireEvent(new %s(null, null));", PropertyValueChangeEvent.class.getSimpleName());
			beanWriter.println("%s.this.observed.%s(value);", beanModel.getSimpleTargetTypeName(), methodModel.getMethod().getName());
			
			beanWriter.outdent();
			beanWriter.println("}");
			
			beanWriter.outdent();
			beanWriter.println("};");
			beanWriter.println();
		}
	}

	/**
	 * @param beanWriter
	 * @param beanModel
	 */
	private void writeShimMethods(SourceWriter beanWriter, ObservableBeanModel beanModel) {
		for (ObservableBeanMethodModel methodModel : beanModel.getMethods()) {
			beanWriter.println("public %s {", SourceGeneration.getBaseMethodDeclaration(methodModel.getMethod()));
			beanWriter.indent();
			
			switch (methodModel.getAction()) {
			case GET:
				beanWriter.println("%s.this.lastAccessedProperty = this.%s;", beanModel.getSimpleTargetTypeName(), methodModel.getPropertyName());
				beanWriter.println("return %s.this.observed.%s();", beanModel.getSimpleTargetTypeName(), methodModel.getMethod().getName());
				break;
			case SET:
				beanWriter.println("this.%s.set(%s);", methodModel.getPropertyName(), methodModel.getMethod().getParameters()[0].getName());
				break;
			}
			
			beanWriter.outdent();
			beanWriter.println("}");
			beanWriter.println();
		}
	}

	/**
	 * @param beanFactoryWriter
	 */
	private void writeFactoryMethods(SourceWriter beanFactoryWriter) {
		for (ObservableBeanFactoryMethodModel methodModel : model.getBeanFactoryMethods()) {
			final JMethod method = methodModel.getMethod();
			
			beanFactoryWriter.println("public %s %s() {", method.getReturnType().getParameterizedQualifiedSourceName(), method.getName());
			beanFactoryWriter.indent();
			
			beanFactoryWriter.println("final AutoBean<%s> observed = autoBeanFactory.create%s();", methodModel.getBeanModel().getSourceType().getQualifiedSourceName(), methodModel.getBeanModel().getTopLevelSourceTypeName());
			
			final ObservableBeanModel beanModel = methodModel.getBeanModel();
			beanFactoryWriter.println("return new %s(observed.as());", beanModel.getQualifiedTargetTypeName());
			
			beanFactoryWriter.outdent();
			beanFactoryWriter.println("}");
			
			beanFactoryWriter.println();
		}
	}

}
