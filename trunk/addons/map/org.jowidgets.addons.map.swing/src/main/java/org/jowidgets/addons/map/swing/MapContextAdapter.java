/*
 * Copyright (c) 2011, H.Westphal
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

package org.jowidgets.addons.map.swing;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.swt.widgets.Display;
import org.jowidgets.addons.map.common.IDesignationListener;
import org.jowidgets.addons.map.common.IMapContext;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.toolkit.Toolkit;

import de.micromata.opengis.kml.v_2_2_0.Feature;

final class MapContextAdapter implements IMapContext {

	private final Display display;
	private final IMapContext context;

	MapContextAdapter(final Display display, final IMapContext context) {
		this.display = display;
		this.context = context;
	}

	private <T> T call(final Callable<T> callable) {
		if (!Toolkit.getUiThreadAccess().isUiThread()) {
			throw new IllegalStateException("method must be called in UI thread");
		}
		final AtomicReference<T> result = new AtomicReference<T>();
		final AtomicReference<Throwable> exception = new AtomicReference<Throwable>();
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				try {
					result.set(callable.call());
				}
				catch (final Throwable t) {
					exception.set(t);
				}
			}
		});
		if (exception.get() == null) {
			return result.get();
		}
		final Throwable t = exception.get();
		if (t instanceof Error) {
			throw (Error) t;
		}
		if (t instanceof RuntimeException) {
			throw (RuntimeException) t;
		}
		throw new RuntimeException(t);
	}

	@Override
	public double[] getBoundingBox() {
		return call(new Callable<double[]>() {
			@Override
			public double[] call() throws Exception {
				return context.getBoundingBox();
			}
		});
	}

	@Override
	public boolean flyTo(final String placemarkId, final double range) {
		return call(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return context.flyTo(placemarkId, range);
			}
		});
	}

	@Override
	public boolean flyTo(final double latitude, final double longitude, final double range) {
		return call(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return context.flyTo(latitude, longitude, range);
			}
		});
	}

	@Override
	public boolean addFeature(final Feature feature) {
		return call(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return context.addFeature(feature);
			}
		});
	}

	@Override
	public boolean removeFeature(final String featureId) {
		return call(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return context.removeFeature(featureId);
			}
		});
	}

	@Override
	public boolean removeAllFeatures() {
		return call(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return context.removeAllFeatures();
			}
		});
	}

	@Override
	public <T> boolean startDesignation(final Class<T> type, final IDesignationListener<? super T> listener) {
		final IUiThreadAccess uiThreadAccess = Toolkit.getUiThreadAccess();
		return call(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return context.startDesignation(type, new IDesignationListener<T>() {
					@Override
					public void onDesignation(final T object) {
						uiThreadAccess.invokeLater(new Runnable() {
							@Override
							public void run() {
								listener.onDesignation(object);
							}
						});
					};
				});
			}
		});
	}

	@Override
	public boolean endDesignation() {
		return call(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return context.endDesignation();
			}
		});
	}

	@Override
	public boolean isDesignationRunning() {
		return call(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return context.isDesignationRunning();
			}
		});
	}

}
