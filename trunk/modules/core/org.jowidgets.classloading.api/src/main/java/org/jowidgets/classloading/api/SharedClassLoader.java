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

package org.jowidgets.classloading.api;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jowidgets.util.Assert;
import org.jowidgets.util.CollectionUtils;

public final class SharedClassLoader {

	private static final ISharedClassLoader INSTANCE = new SharedClassLoaderImpl();

	private SharedClassLoader() {}

	public static ISharedClassLoader getInstance() {
		return INSTANCE;
	}

	/**
	 * Adds a class loader to the shared class loader
	 * 
	 * @param classLoaderReference The class loader reference to add
	 */
	public static void addClassLoader(final IClassLoaderReference classLoaderReference) {
		getInstance().addClassLoader(classLoaderReference);
	}

	/**
	 * Adds a class loader to the shared class loader
	 * 
	 * @param classLoader The class loader to add
	 */
	public static void addClassLoader(final ClassLoader classLoader) {
		getInstance().addClassLoader(classLoader);
	}

	/**
	 * Gets the composite class loader that uses all registered class loaders to resolve
	 * the class to load.
	 * The shared class loader always uses the SystemClassLoader and the ThreadContextLocalClassLoader
	 * as default (e.g. if no classLoader was added)
	 * 
	 * @return The composite class loader
	 */
	public static ClassLoader getCompositeClassLoader() {
		return getInstance().getCompositeClassLoader();
	}

	private static final class SharedClassLoaderImpl implements ISharedClassLoader {

		private final Set<IClassLoaderReference> classLoaders;
		private final ClassLoader compositeClassLoader;

		private SharedClassLoaderImpl() {
			this.classLoaders = new LinkedHashSet<IClassLoaderReference>();
			this.compositeClassLoader = new CompositeClassLoaderImpl();
			addClassLoader(ClassLoader.getSystemClassLoader());
		}

		@Override
		public void addClassLoader(final IClassLoaderReference classLoaderReference) {
			Assert.paramNotNull(classLoaderReference, "classLoaderReference");
			addClassLoaderImpl(classLoaderReference);
		}

		@Override
		public void addClassLoader(final ClassLoader classLoader) {
			addClassLoaderImpl(ClassLoaderReference.create(classLoader));
		}

		private synchronized void addClassLoaderImpl(final IClassLoaderReference classLoader) {
			Assert.paramNotNull(classLoader, "classLoader");
			classLoaders.add(classLoader);
		}

		@Override
		public List<IClassLoaderReference> getClassLoaders(final String className) {
			final List<IClassLoaderReference> result = new LinkedList<IClassLoaderReference>();
			for (final IClassLoaderReference classLoaderRef : classLoaders) {
				try {
					classLoaderRef.getClassLoader().loadClass(className);
					result.add(classLoaderRef);
				}
				catch (final Exception e) {
					//Nothing to do, this loader may not know the class
				}
			}
			return Collections.unmodifiableList(result);
		}

		@Override
		public ClassLoader getCompositeClassLoader() {
			return compositeClassLoader;
		}

		private final class CompositeClassLoaderImpl extends ClassLoader {

			@Override
			protected Class<?> findClass(final String name) throws ClassNotFoundException {
				for (final IClassLoaderReference classLoaderRef : classLoaders) {
					try {
						return classLoaderRef.getClassLoader().loadClass(name);
					}
					catch (final Exception e) {
						//Nothing to do, this loader may not know the class
					}
				}
				throw new ClassNotFoundException("Class with the name '" + name + "' not found");
			}

			@Override
			protected URL findResource(final String name) {
				for (final IClassLoaderReference classLoaderRef : classLoaders) {
					try {
						return classLoaderRef.getClassLoader().getResource(name);
					}
					catch (final Exception e) {
						//Nothing to do, this loader may not know the class
					}
				}
				return null;
			}

			@Override
			protected Enumeration<URL> findResources(final String name) throws IOException {
				final List<URL> result = new LinkedList<URL>();
				for (final IClassLoaderReference classLoaderRef : classLoaders) {
					try {
						final Enumeration<URL> resources = classLoaderRef.getClassLoader().getResources(name);
						CollectionUtils.addFromEnumerationToCollection(result, resources);
					}
					catch (final Exception e) {
						//Nothing to do, this loader may not know the class
					}
				}
				return CollectionUtils.enumerationFromCollection(result);
			}

		}

	}

}
