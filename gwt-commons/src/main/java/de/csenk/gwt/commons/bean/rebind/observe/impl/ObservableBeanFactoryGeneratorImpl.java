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

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;

/**
 * @author senk.christian@googlemail.com
 *
 */
public class ObservableBeanFactoryGeneratorImpl extends Generator {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
		final ObservableBeanFactoryCreator creator = new ObservableBeanFactoryCreator(logger, context, typeName);
		return creator.create();
	}

//	private final Generator beanGenerator = new ObservableBeanGeneratorImpl();
//	
//	/* (non-Javadoc)
//	 * @see de.csenk.gwt.commons.base.rebind.impl.ClassGenerator#createClassName(com.google.gwt.core.ext.TreeLogger, com.google.gwt.core.ext.GeneratorContext, java.lang.String)
//	 */
//	@Override
//	protected ClassName createClassName(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
//		final TypeOracle typeOracle = context.getTypeOracle();
//		final JClassType factoryIntfType = typeOracle.findType(typeName);
//		if (factoryIntfType == null)
//			failGenerating(logger, String.format("Couldn't find type '%s'", typeName));
//		
//		final String factoryPckgName = factoryIntfType.getPackage() != null ? factoryIntfType.getPackage().getName() : "";
//		final String factoryImplName = factoryIntfType.getName().replace('.', '_') + "Impl";
//		
//		return new ClassName(factoryPckgName, factoryImplName);
//	}
//
//	/* (non-Javadoc)
//	 * @see de.csenk.gwt.commons.base.rebind.impl.ClassGenerator#createSourceWriter(com.google.gwt.core.ext.TreeLogger, com.google.gwt.core.ext.GeneratorContext, java.lang.String, de.csenk.gwt.commons.base.rebind.impl.ClassName)
//	 */
//	@Override
//	protected SourceWriter createSourceWriter(TreeLogger logger, GeneratorContext context, String typeName, ClassName className) throws UnableToCompleteException {
//		final PrintWriter printWriter = context.tryCreate(logger, className.getPackageName(), className.getClassName());
//	    if (printWriter == null)
//	      return null;
//
//	    final JClassType classType = context.getTypeOracle().findType(typeName);
//	    
//	    final ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(className.getPackageName(), className.getClassName());
//	    composerFactory.addImplementedInterface(classType.getErasedType().getQualifiedSourceName());
//
//	    return composerFactory.createSourceWriter(context, printWriter);
//	}
//
//	/* (non-Javadoc)
//	 * @see de.csenk.gwt.commons.base.rebind.impl.ClassGenerator#generateClass(com.google.gwt.core.ext.TreeLogger, com.google.gwt.core.ext.GeneratorContext, java.lang.String, de.csenk.gwt.commons.base.rebind.impl.ClassName, com.google.gwt.user.rebind.SourceWriter)
//	 */
//	@Override
//	protected void generateClass(TreeLogger logger, GeneratorContext context, String typeName, ClassName className, SourceWriter sourceWriter) throws UnableToCompleteException {
//		final JClassType classType = context.getTypeOracle().findType(typeName);
//		
//		generateMethods(logger, context, classType, sourceWriter);
//	}
//
//	/**
//	 * @param logger 
//	 * @param context 
//	 * @param classType
//	 * @param sourceWriter
//	 * @throws UnableToCompleteException 
//	 */
//	private void generateMethods(TreeLogger logger, GeneratorContext context, JClassType classType, SourceWriter sourceWriter) throws UnableToCompleteException {
//		for (JMethod method : classType.getMethods()) {
//			final JParameterizedType returnType = method.getReturnType().isParameterized();
//			if (returnType == null) //TODO Check against ObservableBean.class
//				failGenerating(logger, String.format("Method '%s' does not return an ObservableBean", method.getName()));
//			
//			if (returnType.getTypeArgs().length != 1)
//				failGenerating(logger, String.format("Return type of method '%s' must be parameterized", method.getName()));
//				
//			final JClassType beanClass = returnType.getTypeArgs()[0];
//			final String generatedClass = beanGenerator.generate(logger, context, beanClass.getQualifiedSourceName());
//			
//			sourceWriter.println("public %s %s() {", method.getReturnType().getParameterizedQualifiedSourceName(), method.getName());
//			sourceWriter.indentln("return null;");
//			sourceWriter.println("}");
//		}
//	}

}
