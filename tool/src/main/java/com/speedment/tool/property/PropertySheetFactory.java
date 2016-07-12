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
package com.speedment.tool.property;

import com.speedment.runtime.annotation.Api;
import com.speedment.tool.config.DocumentProperty;
import javafx.collections.ObservableList;
import org.controlsfx.control.PropertySheet.Item;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 *
 * @author  Emil Forslund
 * @since   2.2.0
 */
@Api(version="3.0")
public final class PropertySheetFactory {

    private final Map<Class<?>, Function<DocumentProperty, ObservableList<Item>>> constructors;

    public PropertySheetFactory() {
        this.constructors = new ConcurrentHashMap<>();
    }

    public PropertySheetFactory install(Class<?> type, Function<DocumentProperty, ObservableList<Item>> propertylister) {
        this.constructors.put(type, propertylister);
        return this;
    }

    public ObservableList<Item> build(DocumentProperty node) {
        requireNonNull(node);
        final Function<DocumentProperty, ObservableList<Item>> constructor = constructors.get(node.mainInterface());

        if (constructor == null) {
            throw new IllegalArgumentException(
                "The specified document '" + node
                + "' of main interface type '" + node.mainInterface().getSimpleName()
                + "' is not known."
            );
        }

        return constructor.apply(node);
    }
}