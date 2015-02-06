/**
 *
 * Copyright (c) 2006-2015, Speedment, Inc. All Rights Reserved.
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
package com.speedment.codegen.model;

import java.util.stream.Stream;

/**
 *
 * @author Duncan
 */
public interface CodeModel {

    /**
     * The number of model types there are. If new types are added to this enum,
     * new models and new views will have be be created. All implementations of
     * <code>CodeViewBuilder</code> will have to be updated to support the new
     * enum case.
     */
    public static enum Type {

        ANNOTATION, BLOCK, CLASS, CONSTRUCTOR, DEPENDENCY, EXPRESSION, FIELD,
        INTERFACE, METHOD, OPERATOR, PACKAGE, STATEMENT, TYPE, PARAMETER, GENERIC_PARAMETER, MODIFIER, JAVADOC;
    }

    /**
     * Returns the type of this model.
     *
     * @return the type.
     */
    Type getModelType();

    default Stream<? extends CodeModel> stream() {
        return Stream.empty();
    }

}