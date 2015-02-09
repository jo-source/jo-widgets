/*
 * Copyright (c) 2013, grossmann
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

package org.jowidgets.beanvalidation.bootstrap.api;

import java.util.Iterator;
import java.util.ServiceLoader;

import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.jowidgets.classloading.api.SharedClassLoader;
import org.jowidgets.util.Assert;

public final class BeanValidatorFactory {

	private static IBeanValidatorFactory instance = createDefaultFactory();

	private BeanValidatorFactory() {}

	private static IBeanValidatorFactory createDefaultFactory() {
		IBeanValidatorFactory result = null;
		final ServiceLoader<IBeanValidatorFactory> serviceLoader = ServiceLoader.load(
				IBeanValidatorFactory.class,
				SharedClassLoader.getCompositeClassLoader());

		final Iterator<IBeanValidatorFactory> services = serviceLoader.iterator();
		if (services.hasNext()) {
			result = services.next();
		}
		if (result == null) {
			result = new DefaultBeanValidatorFactory();
		}
		return result;
	}

	public static IBeanValidatorFactory getInstance() {
		return instance;
	}

	public static synchronized void setFactory(final IBeanValidatorFactory factory) {
		Assert.paramNotNull(factory, "factory");
		instance = factory;
	}

	public static Validator create() {
		return getInstance().create();
	}

	private static class DefaultBeanValidatorFactory implements IBeanValidatorFactory {

		@Override
		public Validator create() {
			try {
				return Validation.buildDefaultValidatorFactory().getValidator();
			}
			catch (final ValidationException e) {
				//TODO MG change error handling (maybe ignore)
				//CHECKSTYLE:OFF
				e.printStackTrace();
				//CHECKSTYLE:ON
				return null;
			}
		}

	}

}
