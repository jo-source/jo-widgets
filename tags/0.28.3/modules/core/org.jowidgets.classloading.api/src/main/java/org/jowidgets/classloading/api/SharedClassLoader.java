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
	 * @param classLoader The class loader to add
	 */
	public static void addClassLoader(final IClassLoader classLoader) {
		getInstance().addClassLoader(classLoader);
	}

	/**
	 * Removes a class loader from the shared class loader
	 * 
	 * @param classLoader The class loader to remove
	 */
	public static void removeClassLoader(final IClassLoader classLoader) {
		getInstance().removeClassLoader(classLoader);
	}

	/**
	 * Gets the composite class loader that uses all registered class loaders to resolve
	 * the class to load.
	 * The shared class loader always uses the SystemClassLoader and the ThreadContextLocalClassLoader
	 * as default (e.g. if no classloader was added)
	 * 
	 * @return The composite class loader
	 */
	public static ClassLoader getCompositeClassLoader() {
		return getInstance().getCompositeClassLoader();
	}

	private static final class SharedClassLoaderImpl implements ISharedClassLoader {

		private final Set<IClassLoader> classLoaders;
		private final ClassLoader compositeClassLoader;

		private SharedClassLoaderImpl() {
			this.classLoaders = new LinkedHashSet<IClassLoader>();
			this.compositeClassLoader = new CompositeClassLoaderImpl();
			addClassLoader(ClassLoaderAdapter.create(ClassLoader.getSystemClassLoader()));
			addClassLoader(new CurrentThreadClassLoader());
		}

		@Override
		public void addClassLoader(final IClassLoader classLoader) {
			Assert.paramNotNull(classLoader, "classLoader");
			classLoaders.add(classLoader);
		}

		@Override
		public void removeClassLoader(final IClassLoader classLoader) {
			Assert.paramNotNull(classLoader, "classLoader");
			classLoaders.remove(classLoader);
		}

		@Override
		public ClassLoader getCompositeClassLoader() {
			return compositeClassLoader;
		}

		private final class CompositeClassLoaderImpl extends ClassLoader {

			@Override
			protected Class<?> findClass(final String name) throws ClassNotFoundException {
				for (final IClassLoader classLoader : new LinkedList<IClassLoader>(classLoaders)) {
					try {
						return classLoader.findClass(name);
					}
					catch (final Exception e) {
						//Nothing to do, this loader may not know the class
					}
				}
				throw new ClassNotFoundException("Class with the name '" + name + "' not found");
			}

			@Override
			protected URL findResource(final String name) {
				for (final IClassLoader classLoader : new LinkedList<IClassLoader>(classLoaders)) {
					try {
						final URL result = classLoader.findResource(name);
						if (result != null) {
							return result;
						}
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
				for (final IClassLoader classLoader : new LinkedList<IClassLoader>(classLoaders)) {
					try {
						final Enumeration<URL> resources = classLoader.findResources(name);
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

	private static final class CurrentThreadClassLoader implements IClassLoader {

		@Override
		public Class<?> findClass(final String name) throws ClassNotFoundException {
			final ClassLoader tccl = Thread.currentThread().getContextClassLoader();
			if (tccl != null) {
				return tccl.loadClass(name);
			}
			else {
				throw new ClassNotFoundException("No Thread context classloader set");
			}
		}

		@Override
		public URL findResource(final String name) {
			final ClassLoader tccl = Thread.currentThread().getContextClassLoader();
			if (tccl != null) {
				return tccl.getResource(name);
			}
			else {
				return null;
			}
		}

		@Override
		public Enumeration<URL> findResources(final String name) throws IOException {
			final ClassLoader tccl = Thread.currentThread().getContextClassLoader();
			if (tccl != null) {
				return tccl.getResources(name);
			}
			else {
				return null;
			}
		}
	}

}
