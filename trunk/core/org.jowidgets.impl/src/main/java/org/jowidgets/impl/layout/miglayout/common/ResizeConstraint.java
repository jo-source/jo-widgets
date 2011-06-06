//CHECKSTYLE:OFF

package org.jowidgets.impl.layout.miglayout.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;

import org.jowidgets.impl.layout.miglayout.MigLayoutToolkit;

/**
 * A parsed constraint that specifies how an entity (normally column/row or component) can shrink or
 * grow compared to other entities.
 */
final class ResizeConstraint implements Externalizable {
	static final Float WEIGHT_100 = new Float(100);

	/**
	 * How flexilble the entity should be, relative to other entities, when it comes to growing. <code>null</code> or
	 * zero mean it will never grow. An entity that has twise the growWeight compared to another entity will get twice
	 * as much of available space.
	 * <p>
	 * "grow" are only compared within the same "growPrio".
	 */
	Float grow = null;

	/**
	 * The relative priority used for determining which entities gets the extra space first.
	 */
	int growPrio = 100;

	Float shrink = WEIGHT_100;

	int shrinkPrio = 100;

	public ResizeConstraint() // For Externalizable
	{}

	ResizeConstraint(final int shrinkPrio, final Float shrinkWeight, final int growPrio, final Float growWeight) {
		this.shrinkPrio = shrinkPrio;
		this.shrink = shrinkWeight;
		this.growPrio = growPrio;
		this.grow = growWeight;
	}

	// ************************************************
	// Persistence Delegate and Serializable combined.
	// ************************************************

	private Object readResolve() throws ObjectStreamException {
		return MigLayoutToolkit.getLayoutUtil().getSerializedObject(this);
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		final LayoutUtil layoutUtil = MigLayoutToolkit.getLayoutUtil();
		layoutUtil.setSerializedObject(this, layoutUtil.readAsXML(in));
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		if (getClass() == ResizeConstraint.class) {
			MigLayoutToolkit.getLayoutUtil().writeAsXML(out, this);
		}
	}
}
