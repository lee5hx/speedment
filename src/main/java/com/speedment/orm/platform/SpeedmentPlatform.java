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
package com.speedment.orm.platform;

import com.speedment.orm.config.model.ProjectManager;
import com.speedment.orm.config.model.impl.ProjectManagerImpl;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

/**
 *
 * @author pemi
 */
public class SpeedmentPlatform {

    private final Map<Class<?>, Component> plugins;
    private final AtomicBoolean running;

    private SpeedmentPlatform() {
        plugins = new ConcurrentHashMap<>();
        running = new AtomicBoolean();
        init();
    }

    public static SpeedmentPlatform getInstance() {
        return PlatformHolder.INSTANCE;
    }

    private static class PlatformHolder {

        private static final SpeedmentPlatform INSTANCE = new SpeedmentPlatform();
    }

    public <T extends Component> T put(Class<T> clazz, T component) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(component);
        final T oldPlugin = (T) plugins.get(clazz);
        component.added();
        plugins.put(clazz, component);
        if (oldPlugin != null) {
            oldPlugin.removed();
        }
        return oldPlugin;
    }

    public <T extends Component> T remove(Class<T> clazz) {
        final T result = (T) plugins.remove(clazz);
        if (result != null) {
            result.removed();
        }
        return result;
    }

    public <T extends Component> T get(Class<T> clazz) {
        return (T) plugins.get(clazz);
    }

    public Stream<Entry<Class<?>, Component>> componentStream() {
        return plugins.entrySet().stream();
    }

    private void init() {
        put(ProjectManager.class, new ProjectManagerImpl());
    }

    public SpeedmentPlatform start() {
        // Todo: apply standard component
        running.set(true);
        return this;
    }

    public SpeedmentPlatform stop() {
        running.set(false);
        componentStream().map(Entry::getValue).forEach(Component::removed);
        return this;
    }

}