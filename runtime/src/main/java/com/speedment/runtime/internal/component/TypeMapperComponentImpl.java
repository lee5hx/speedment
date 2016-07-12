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
package com.speedment.runtime.internal.component;

import com.speedment.runtime.component.TypeMapperComponent;
import com.speedment.runtime.config.mapper.TypeMapper;
import com.speedment.runtime.config.mapper.bigdecimal.BigDecimalToDouble;
import com.speedment.runtime.config.mapper.identity.ArrayIdentityMapper;
import com.speedment.runtime.config.mapper.identity.BigDecimalIdentityMapper;
import com.speedment.runtime.config.mapper.identity.BlobIdentityMapper;
import com.speedment.runtime.config.mapper.identity.BooleanIdentityMapper;
import com.speedment.runtime.config.mapper.identity.ByteIdentityMapper;
import com.speedment.runtime.config.mapper.identity.ClobIdentityMapper;
import com.speedment.runtime.config.mapper.identity.DateIdentityMapper;
import com.speedment.runtime.config.mapper.identity.DoubleIdentityMapper;
import com.speedment.runtime.config.mapper.identity.FloatIdentityMapper;
import com.speedment.runtime.config.mapper.identity.IntegerIdentityMapper;
import com.speedment.runtime.config.mapper.identity.LongIdentityMapper;
import com.speedment.runtime.config.mapper.identity.NClobIdentityMapper;
import com.speedment.runtime.config.mapper.identity.ObjectIdentityMapper;
import com.speedment.runtime.config.mapper.identity.RefIdentityMapper;
import com.speedment.runtime.config.mapper.identity.RowIdIdentityMapper;
import com.speedment.runtime.config.mapper.identity.SQLXMLIdentityMapper;
import com.speedment.runtime.config.mapper.identity.ShortIdentityMapper;
import com.speedment.runtime.config.mapper.identity.StringIdentityMapper;
import com.speedment.runtime.config.mapper.identity.TimeIdentityMapper;
import com.speedment.runtime.config.mapper.identity.TimestampIdentityMapper;
import com.speedment.runtime.config.mapper.identity.URLIdentityMapper;
import com.speedment.runtime.config.mapper.identity.UUIDIdentityMapper;
import com.speedment.runtime.config.mapper.integer.IntegerZeroOneToBooleanMapper;
import com.speedment.runtime.config.mapper.largeobject.ClobToStringMapper;
import com.speedment.runtime.config.mapper.string.StringToLocaleMapper;
import com.speedment.runtime.config.mapper.string.TrueFalseStringToBooleanMapper;
import com.speedment.runtime.config.mapper.string.YesNoStringToBooleanMapper;
import com.speedment.runtime.config.mapper.time.DateToIntMapper;
import com.speedment.runtime.config.mapper.time.DateToLongMapper;
import com.speedment.runtime.config.mapper.time.TimeToIntMapper;
import com.speedment.runtime.config.mapper.time.TimeToLongMapper;
import com.speedment.runtime.config.mapper.time.TimestampToIntMapper;
import com.speedment.runtime.config.mapper.time.TimestampToLongMapper;
import com.speedment.runtime.license.Software;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 *
 * @author  Emil Forslund
 * @since   2.2.0
 */
public final class TypeMapperComponentImpl extends InternalOpenSourceComponent implements TypeMapperComponent {

    private final Map<String, TypeMapper<?, ?>> mappers;

    /**
     * Constructs the component.
     */
    public TypeMapperComponentImpl() {
        this.mappers = new ConcurrentHashMap<>();

        // Identity mappers
        install(ArrayIdentityMapper::new);
        install(BigDecimalIdentityMapper::new);
        install(BlobIdentityMapper::new);
        install(BooleanIdentityMapper::new);
        install(ByteIdentityMapper::new);
        install(ClobIdentityMapper::new);
        install(DateIdentityMapper::new);
        install(DoubleIdentityMapper::new);
        install(FloatIdentityMapper::new);
        install(IntegerIdentityMapper::new);
        install(LongIdentityMapper::new);
        install(NClobIdentityMapper::new);
        install(ObjectIdentityMapper::new);
        install(RefIdentityMapper::new);
        install(RowIdIdentityMapper::new);
        install(ShortIdentityMapper::new);
        install(SQLXMLIdentityMapper::new);
        install(StringIdentityMapper::new);
        install(TimeIdentityMapper::new);
        install(TimestampIdentityMapper::new);
        install(URLIdentityMapper::new);

        // Special time mappers
        install(DateToLongMapper::new);
        install(TimestampToLongMapper::new);
        install(TimeToLongMapper::new);
        install(DateToIntMapper::new);
        install(TimestampToIntMapper::new);
        install(TimeToIntMapper::new);

        // Special string mappers
        install(StringToLocaleMapper::new);
        install(TrueFalseStringToBooleanMapper::new);
        install(YesNoStringToBooleanMapper::new);

        // Special BigDecimal object mappers
        install(BigDecimalToDouble::new);

        // Special Large object mappers
        install(ClobToStringMapper::new);
        
        // Special integer mappers
        install(IntegerZeroOneToBooleanMapper::new);

        // Other mappers
        install(UUIDIdentityMapper::new);

    }
    
    @Override
    protected String getDescription() {
        return "Holds all the type mappers that have been installed into the Speedment Platform. " + 
            "A Type Mapper is used to convert between database types and java types.";
    }

    @Override
    public void install(Supplier<TypeMapper<?, ?>> typeMapperConstructor) {
        final TypeMapper<?, ?> mapper = typeMapperConstructor.get();
        mappers.put(mapper.getClass().getName(), mapper);
    }

    @Override
    public final Stream<TypeMapper<?, ?>> stream() {
        return mappers.values().stream();
    }

    @Override
    public Optional<TypeMapper<?, ?>> get(String absoluteClassName) {
        return Optional.ofNullable(mappers.get(absoluteClassName));
    }

    @Override
    public Stream<Software> getDependencies() {
        return Stream.empty();
    }
}