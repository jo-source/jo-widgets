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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jowidgets.util.Assert;
import org.jowidgets.util.Tuple;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class AnnotationCache {

	private static final Map<Tuple<Class, Class>, List<Annotation>> ANNOTATIONS_CACHE = new ConcurrentHashMap<Tuple<Class, Class>, List<Annotation>>();

	private AnnotationCache() {}

	public static <ANNOTATION_TYPE extends Annotation> ANNOTATION_TYPE getTypeAnnotationFromHierarchy(
		final Class<?> type,
		final Class<ANNOTATION_TYPE> annotationType) {
		final List<ANNOTATION_TYPE> resultList = getTypeAnnotationsFromHierarchy(type, annotationType);
		if (resultList.size() > 0) {
			return resultList.iterator().next();
		}
		else {
			return null;
		}
	}

	public static <ANNOTATION_TYPE extends Annotation> List<ANNOTATION_TYPE> getTypeAnnotationsFromHierarchy(
		final Class<?> type,
		final Class<ANNOTATION_TYPE> annotationType) {

		Assert.paramNotNull(type, "type");
		Assert.paramNotNull(annotationType, "annotationType");

		final Tuple<Class, Class> key = new Tuple<Class, Class>(type, annotationType);

		List<ANNOTATION_TYPE> result = (List<ANNOTATION_TYPE>) ANNOTATIONS_CACHE.get(key);
		if (result == null) {
			result = Collections.unmodifiableList(AnnotationUtils.getTypeAnnotationsFromHierarchy(type, annotationType));
			ANNOTATIONS_CACHE.put(key, (List<Annotation>) result);
		}

		return result;
	}
}
