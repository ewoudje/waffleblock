package com.ewoudje.waffleblocks.impl.simple;

import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.components.GridComponentType;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

public class GridComponentTracker<G extends Grid, C> {
    private static final Map<Class<?>, GridComponentTracker<?, ?>> class2tracker = new HashMap<>();
    private final List<GridComponentTracker<?, ?>> inheritedComponents = new ArrayList<>();
    private final List<GridComponentTracker<?, ?>> superComponents = new ArrayList<>();
    private final Map<G, C> components = new WeakHashMap<>();
    private final GridComponentType<G, C> type;

    public GridComponentTracker(@NotNull GridComponentType<G, C> type) {
        this.type = type;

        class2tracker.put(type.getComponentClass(), this);

        streamInheritors(type.getComponentClass())
                .map(GridComponentTracker::loadClass)
                .map(class2tracker::get)
                .filter(Objects::nonNull)
                .forEach(c -> {
                    c.inheritedComponents.add(this);
                    superComponents.add(c);
                    c.orderInheritors();
                });
    }


    public C getComponent(G grid) {
        C result = components.get(grid);
        if (result != null) return result;

        for (GridComponentTracker<?, ?> inheritor : inheritedComponents) {
            result = ((GridComponentTracker<G, ? extends C>) inheritor).getComponent(grid);

            if (result != null) {
                return result;
            }
        }

        return null;
    }

    public void setComponent(G grid, C component) {
        if (component == null) {
            var removed = components.remove(grid);
            type.onRemove(grid, removed);
            superComponents.forEach(c -> ((GridComponentTracker<G, C>) c).type.onRemove(grid, removed));
            return;
        }

        if (components.put(grid, component) == null) {
            type.onAdd(grid, component);
            superComponents.forEach(c -> ((GridComponentTracker<G, C>) c).type.onAdd(grid, component));
        }
    }

    private void orderInheritors() {
        inheritedComponents.sort(Comparator.comparingInt(c -> c.type.getComponentClass().isAssignableFrom(type.getComponentClass()) ? 1 : 0));
    }

    private static Stream<Class<?>> streamInheritors(Class<?> clazz) {
        if (clazz == null) return Stream.empty();

        return Stream.concat(
                Stream.concat(Stream.of(clazz.getSuperclass()), streamInheritors(clazz.getSuperclass())),
                Arrays.stream(clazz.getInterfaces())
                        .flatMap(i -> Stream.concat(Stream.of(i), streamInheritors(i)))).filter(Objects::nonNull);

    }

    private static Class<?> loadClass(Class<?> clazz) {
        try {
            Class<?> r = Class.forName(clazz.getName(), true, clazz.getClassLoader());

            if (!class2tracker.containsKey(r)) {
                GridComponentType<?, ?> type = GridComponentType.get(r);
                if (type != null)
                    class2tracker.put(r, new GridComponentTracker<>(type));
            }

            return r;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static <G extends Grid> Map<GridComponentType<?, ?>, GridComponentTracker<G, ?>> makeTrackers() {
        return new HashMap<>() {
            @Override
            public GridComponentTracker<G, ?> get(Object key) {
                var r = super.get(key);
                if (r != null) return r;
                GridComponentType<G, ?> type = (GridComponentType<G, ?>) key;

                GridComponentTracker<G, ?> tracker = (GridComponentTracker<G, ?>) class2tracker.get(type.getComponentClass());

                if (tracker == null)
                    tracker = new GridComponentTracker<>(type);

                put((GridComponentType<?, ?>) key, tracker);
                return tracker;
            }
        };
    }
}
