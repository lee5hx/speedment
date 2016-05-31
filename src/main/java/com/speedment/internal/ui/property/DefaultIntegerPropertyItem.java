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
package com.speedment.internal.ui.property;

import static com.speedment.internal.ui.property.AbstractPropertyItem.defaultDecorator;
import com.speedment.internal.ui.property.DefaultIntegerPropertyItem.DefaultIntegerPropertyEditor;
import com.speedment.internal.ui.util.ObservableUtil;
import java.util.Objects;
import java.util.function.Consumer;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.StringConverter;
import org.controlsfx.property.editor.AbstractPropertyEditor;

/**
 *
 * @author Emil Forslund
 */
public final class DefaultIntegerPropertyItem extends AbstractPropertyItem<Number, IntegerProperty, DefaultIntegerPropertyEditor> {

    private final IntegerProperty valueProperty;
    private final IntegerBinding defaultValue;

    public DefaultIntegerPropertyItem(IntegerProperty value, IntegerBinding defaultValue, String name, String description) {
        super(value, name, description, defaultDecorator());
        this.valueProperty = value;
        this.defaultValue = defaultValue;
    }

    public DefaultIntegerPropertyItem(IntegerProperty value, IntegerBinding defaultValue, String name, String description, Consumer<DefaultIntegerPropertyEditor> decorator) {
        super(value, name, description, decorator);
        this.valueProperty = value;
        this.defaultValue = defaultValue;
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }

    @Override
    protected DefaultIntegerPropertyEditor createUndecoratedEditor() {
        return new DefaultIntegerPropertyEditor(this);
    }

    public final static class DefaultIntegerPropertyEditor extends AbstractPropertyEditor<Integer, DefaultIntegerNode> {

        private DefaultIntegerPropertyEditor(DefaultIntegerPropertyItem item) {
            super(item, new DefaultIntegerNode(item.valueProperty, item.defaultValue));
        }

        @Override
        protected ObservableValue<Integer> getObservableValue() {
            return super.getEditor().spinner.getValueFactory().valueProperty();
        }

        @Override
        public void setValue(Integer value) {
            super.getEditor().setValue(value);
        }

        public void setMin(int min) {
            ((IntegerSpinnerValueFactory) super.getEditor().spinner.getValueFactory()).setMin(min);
        }

        public void setMax(int max) {
            ((IntegerSpinnerValueFactory) super.getEditor().spinner.getValueFactory()).setMax(max);
        }

    }

    private final static class DefaultIntegerNode extends HBox {

        private final IntegerProperty enteredValue;
        private final CheckBox auto;
        private final Spinner<Integer> spinner;

        private final static double SPACING = 8.0d;

        private DefaultIntegerNode(IntegerProperty valueProperty, IntegerBinding defaultValue) {
            this.auto = new CheckBox("Auto");
            this.spinner = new Spinner<>(Integer.MIN_VALUE, Integer.MAX_VALUE, defaultValue.get(), 1);

            final boolean isAutoByDefault = valueProperty.get() == defaultValue.get();

            this.enteredValue = new SimpleIntegerProperty(valueProperty.get());
            this.auto.selectedProperty().setValue(isAutoByDefault);

            if (isAutoByDefault) {
                ObservableUtil.bind(spinner.getValueFactory().valueProperty(), defaultValue);
            }

            this.spinner.disableProperty().bind(auto.selectedProperty());
            this.spinner.setEditable(true);

            final StringConverter<Integer> sci = spinner.getValueFactory().getConverter();
            final StringConverter<Integer> sci2 = new StringConverter<Integer>() {
                @Override
                public Integer fromString(String value) {
                    try {
                        return sci.fromString(value);
                    } catch (final NumberFormatException nfe) {
                        return 0;
                    }
                }

                @Override
                public String toString(Integer value) {
                    return sci.toString(value);
                }
            };
            this.spinner.getValueFactory().setConverter(sci2);

            this.auto.selectedProperty().addListener((ob, o, isAuto) -> {
                spinner.getValueFactory().valueProperty().unbind();

                if (isAuto) {
                    enteredValue.setValue(spinner.getValue());
                    ObservableUtil.bind(spinner.getValueFactory().valueProperty(), defaultValue);
                } else if (Objects.equals(
                    defaultValue.getValue(),
                    spinner.getValue()
                )) {
                    spinner.getValueFactory().valueProperty().setValue(enteredValue.get());
                }
            });

            super.getChildren().addAll(auto, spinner);

            setAlignment(Pos.CENTER_LEFT);
            auto.setAlignment(Pos.CENTER_LEFT);

            setHgrow(spinner, Priority.ALWAYS);
            setHgrow(auto, Priority.SOMETIMES);
            setSpacing(SPACING);
        }

        private void setValue(Integer value) {
            spinner.getValueFactory().valueProperty().unbind();
            spinner.getValueFactory().valueProperty().setValue(value);
        }
    }
}
