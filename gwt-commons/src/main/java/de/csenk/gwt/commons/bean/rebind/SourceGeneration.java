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

package de.csenk.gwt.commons.bean.rebind;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.editor.rebind.model.ModelUtils;

/**
 * @author senk.christian@googlemail.com
 *
 */
public final class SourceGeneration {

	/**
	 * 
	 */
	private SourceGeneration() { }
	
	/**
	 * @param method
	 * @return
	 */
	public static String getBaseMethodDeclaration(JMethod method) {
		final List<String> paramDeclarations = Lists.transform(Lists.newArrayList(method.getParameters()), new Function<JParameter, String>() {

			@Override
			@Nullable
			public String apply(@Nullable JParameter input) {
				return String.format("%s %s", ModelUtils.getQualifiedBaseSourceName(input.getType()), input.getName());
			}
			
		});
		
		final List<String> throwDeclarations = Lists.transform(Lists.newArrayList(method.getThrows()), new Function<JType, String>() {

			@Override
			@Nullable
			public String apply(@Nullable JType input) {
				return ModelUtils.getQualifiedBaseSourceName(input);
			}
			
		});
		
		final String paramListDeclaration = Joiner.on(", ").join(paramDeclarations);
		final String throwsDeclaration = throwDeclarations.size() > 0 ? String.format("throws %s", Joiner.on(", ").join(throwDeclarations)) : "";
		final String returnName = ModelUtils.getQualifiedBaseSourceName(method.getReturnType());
		
		return String.format("%s %s(%s) %s", returnName, method.getName(), paramListDeclaration, throwsDeclaration);
	}
	
}
