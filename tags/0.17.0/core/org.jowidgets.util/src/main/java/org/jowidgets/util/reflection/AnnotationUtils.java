/*
 * Copyright (c) 2012, grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 *   names of its contributors may be used to endorse or promote products
 *   derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

package org.jowidgets.util.reflection;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

import org.jowidgets.util.Assert;
import org.jowidgets.util.IIterationCallback;

public final class AnnotationUtils {

	private AnnotationUtils() {}

	public static <ANNOTATION_TYPE extends Annotation> List<ANNOTATION_TYPE> getTypeAnnotationsFromHierarchy(
		final Class<?> type,
		final Class<ANNOTATION_TYPE> annotationType) {

		Assert.paramNotNull(type, "type");
		Assert.paramNotNull(annotationType, "annotationType");

		final List<ANNOTATION_TYPE> result = new LinkedList<ANNOTATION_TYPE>();
		ReflectionUtils.iterateHierarchy(type, new IIterationCallback<Class<?>>() {
			@Override
			public void next(final Class<?> type) {
				final ANNOTATION_TYPE annotation = type.getAnnotation(annotationType);
				if (annotation != null) {
					result.add(annotation);
				}
			}
		});
		return result;
	}

}
