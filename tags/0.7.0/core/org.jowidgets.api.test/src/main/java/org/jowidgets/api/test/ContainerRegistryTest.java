/*
 * Copyright (c) 2011, Nikolaus Moll
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

package org.jowidgets.api.test;

import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;

import org.jowidgets.api.controller.IContainerRegistry;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.ILabel;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.junit.Test;

public class ContainerRegistryTest {

	@Test
	public void testSimpleRegistration() {
		Toolkit.getApplicationRunner().run(new IApplication() {

			@Override
			public void start(final IApplicationLifecycle lifecycle) {

				final IFrame frame = Toolkit.createRootFrame(BPF.frame(), lifecycle);

				final ContainerRegistry registry = new ContainerRegistry();

				frame.addContainerRegistry(registry);
				frame.setVisible(true);

				Assert.assertEquals(0, registry.getControls().size());

				final ILabel label = frame.add(BPF.label());

				Assert.assertTrue(registry.getControls().contains(label));

				final ContainerRegistry registry2 = new ContainerRegistry();
				frame.addContainerRegistry(registry2);

				Assert.assertTrue(registry.getControls().contains(label));

				frame.dispose();

				Assert.assertFalse(registry.getControls().contains(label));
				Assert.assertFalse(registry.getControls().contains(label));
			}
		});
	}

	@Test
	public void testSimpleRecursiveRegistration() {
		Toolkit.getApplicationRunner().run(new IApplication() {

			@Override
			public void start(final IApplicationLifecycle lifecycle) {

				final IFrame frame = Toolkit.createRootFrame(BPF.frame(), lifecycle);

				final ContainerRegistry registry = new ContainerRegistry();

				frame.addContainerRegistry(registry);
				frame.setVisible(true);

				Assert.assertEquals(0, registry.getControls().size());

				final IContainer subContainer = frame.add(BPF.composite());
				final ILabel label = subContainer.add(BPF.label());

				Assert.assertTrue(registry.getControls().contains(subContainer));
				Assert.assertTrue(registry.getControls().contains(label));

				final ContainerRegistry registry2 = new ContainerRegistry();
				frame.addContainerRegistry(registry2);

				Assert.assertTrue(registry2.getControls().contains(subContainer));
				Assert.assertTrue(registry2.getControls().contains(label));

				frame.dispose();

				Assert.assertFalse(registry.getControls().contains(subContainer));
				Assert.assertFalse(registry.getControls().contains(label));

				Assert.assertFalse(registry2.getControls().contains(subContainer));
				Assert.assertFalse(registry2.getControls().contains(label));
			}
		});
	}

	@Test
	public void testRecursiveRegistration() {
		Toolkit.getApplicationRunner().run(new IApplication() {

			@Override
			public void start(final IApplicationLifecycle lifecycle) {

				final IFrame frame = Toolkit.createRootFrame(BPF.frame(), lifecycle);

				final ContainerRegistry registry = new ContainerRegistry();

				frame.addContainerRegistry(registry);
				frame.setVisible(true);

				Assert.assertEquals(0, registry.getControls().size());

				final IContainer subContainer = frame.add(BPF.composite());
				final IContainer subSubContainer = subContainer.add(BPF.scrollComposite());
				final ILabel label1 = subSubContainer.add(BPF.label());
				final ILabel label2 = subContainer.add(BPF.label());
				final ILabel label3 = frame.add(BPF.label());

				Assert.assertTrue(registry.getControls().contains(subContainer));
				Assert.assertTrue(registry.getControls().contains(subSubContainer));
				Assert.assertTrue(registry.getControls().contains(label1));
				Assert.assertTrue(registry.getControls().contains(label2));
				Assert.assertTrue(registry.getControls().contains(label3));

				final ContainerRegistry registry2 = new ContainerRegistry();
				frame.addContainerRegistry(registry2);

				Assert.assertTrue(registry2.getControls().contains(subContainer));
				Assert.assertTrue(registry2.getControls().contains(subSubContainer));
				Assert.assertTrue(registry2.getControls().contains(label1));
				Assert.assertTrue(registry2.getControls().contains(label2));
				Assert.assertTrue(registry2.getControls().contains(label3));

				frame.dispose();

				Assert.assertFalse(registry.getControls().contains(subContainer));
				Assert.assertFalse(registry.getControls().contains(subSubContainer));
				Assert.assertFalse(registry.getControls().contains(label1));
				Assert.assertFalse(registry.getControls().contains(label2));
				Assert.assertFalse(registry.getControls().contains(label3));

				Assert.assertFalse(registry2.getControls().contains(subContainer));
				Assert.assertFalse(registry2.getControls().contains(subSubContainer));
				Assert.assertFalse(registry2.getControls().contains(label1));
				Assert.assertFalse(registry2.getControls().contains(label2));
				Assert.assertFalse(registry2.getControls().contains(label3));
			}
		});
	}

	final class ContainerRegistry implements IContainerRegistry {

		private final List<IControl> controls = new LinkedList<IControl>();

		@Override
		public void register(final IControl control) {
			controls.add(control);
		}

		@Override
		public void unregister(final IControl control) {
			controls.remove(control);
		}

		protected List<IControl> getControls() {
			return controls;
		}

	}

}
