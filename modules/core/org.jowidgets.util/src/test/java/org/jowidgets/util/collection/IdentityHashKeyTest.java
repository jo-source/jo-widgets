/*
 * Copyright (c) 2016, grossmann
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

package org.jowidgets.util.collection;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import junit.framework.Assert;

public class IdentityHashKeyTest {

    @Test
    public void testPersistenceObject() {

        final PersistenceObject persistenceObject = new PersistenceObject();
        final PersistenceObject persistenceObject2 = new PersistenceObject();

        Assert.assertTrue(persistenceObject.equals(persistenceObject2));
        Assert.assertTrue(persistenceObject2.equals(persistenceObject));

        persistenceObject.setId(Long.valueOf(1));
        Assert.assertFalse(persistenceObject.equals(persistenceObject2));
        Assert.assertFalse(persistenceObject2.equals(persistenceObject));

        persistenceObject2.setId(Long.valueOf(1));
        Assert.assertTrue(persistenceObject.equals(persistenceObject2));
        Assert.assertTrue(persistenceObject2.equals(persistenceObject));
    }

    @Test
    public void testPersistenceObjectFailInHashSet() {

        final Set<PersistenceObject> hashSet = new HashSet<PersistenceObject>();

        final PersistenceObject persistenceObject = new PersistenceObject();
        final PersistenceObject persistenceObject2 = new PersistenceObject();

        hashSet.add(persistenceObject);
        Assert.assertTrue(hashSet.contains(persistenceObject));

        persistenceObject.setId(Long.valueOf(1));
        Assert.assertFalse(hashSet.contains(persistenceObject));
        Assert.assertFalse(hashSet.contains(persistenceObject2));

        persistenceObject2.setId(Long.valueOf(1));
        Assert.assertFalse(hashSet.contains(persistenceObject2));

        hashSet.add(persistenceObject);
        Assert.assertTrue(hashSet.contains(persistenceObject));
        Assert.assertTrue(hashSet.contains(persistenceObject2));

        Assert.assertEquals(2, hashSet.size());
    }

    @Test
    public void testIdentityHashKey() {

        final Set<IdentityHashKey> hashSet = new HashSet<IdentityHashKey>();

        final PersistenceObject persistenceObject = new PersistenceObject();
        final IdentityHashKey hashKey = new IdentityHashKey(persistenceObject);

        final PersistenceObject persistenceObject2 = new PersistenceObject();
        final IdentityHashKey hashKey2 = new IdentityHashKey(persistenceObject2);

        hashSet.add(hashKey);
        Assert.assertTrue(hashSet.contains(hashKey));

        persistenceObject.setId(Long.valueOf(1));
        Assert.assertTrue(hashSet.contains(hashKey));
        Assert.assertFalse(hashSet.contains(hashKey2));

        persistenceObject2.setId(Long.valueOf(1));
        Assert.assertFalse(hashSet.contains(hashKey2));

        hashSet.add(hashKey2);
        Assert.assertTrue(hashSet.contains(hashKey));
        Assert.assertTrue(hashSet.contains(hashKey2));

        Assert.assertEquals(2, hashSet.size());
    }

    private static final class PersistenceObject {

        private Object id;

        public void setId(final Object id) {
            this.id = id;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((id == null) ? 0 : id.hashCode());
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof PersistenceObject)) {
                return false;
            }
            final PersistenceObject other = (PersistenceObject) obj;
            if (id == null) {
                if (other.id != null) {
                    return false;
                }
            }
            else if (!id.equals(other.id)) {
                return false;
            }
            return true;
        }

    }

}
