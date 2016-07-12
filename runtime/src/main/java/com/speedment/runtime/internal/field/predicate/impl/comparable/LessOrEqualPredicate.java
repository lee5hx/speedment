/**
 *
 * Copyright (c) 2006-2016, Speedment, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.speedment.runtime.internal.field.predicate.impl.comparable;

import com.speedment.runtime.field.predicate.SpeedmentPredicate;
import com.speedment.runtime.field.trait.FieldTrait;
import com.speedment.runtime.field.trait.ReferenceFieldTrait;
import com.speedment.runtime.internal.field.predicate.iface.type.BinaryOperation;

import static com.speedment.runtime.field.predicate.PredicateType.LESS_OR_EQUAL;

/**
 *
 * @author pemi
 * @param <ENTITY> the entity type
 * @param <V> value type
 */
public class LessOrEqualPredicate<ENTITY, D, V extends Comparable<? super V>>
        extends BaseComparablePredicate<ENTITY, D, V>
        implements SpeedmentPredicate<ENTITY, D, V>, BinaryOperation<V> {

    public LessOrEqualPredicate(FieldTrait field, ReferenceFieldTrait<ENTITY, D, V> referenceField, V operand0) {
        super(LESS_OR_EQUAL, field, referenceField, operand0, LessOrEqualPredicate::test);
    }

    private static <V extends Comparable<? super V>> boolean test(V a, V b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.compareTo(b) >= 0;
    }

}