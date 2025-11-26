package com.ewoudje.waffleblocks.mixins;

import com.google.common.reflect.ClassPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WaffleFeature {
    private static final Logger LOGGER = LoggerFactory.getLogger(WaffleFeature.class);
    private static HashMap<String, Set<WaffleFeature>> features = new HashMap<>();

    private final String name;
    private final Set<String> commonClasses;
    private final Set<String> clientClasses;
    private final Set<String> overrideClasses;
    private final boolean enabled;

    private WaffleFeature(String name, String packageName, Set<String> commonClasses, Set<String> clientClasses, Set<String> overrideClasses, boolean enabled) {
        this.name = name;
        this.commonClasses = commonClasses.stream().map(i -> packageName + "." + i).collect(Collectors.toUnmodifiableSet());
        this.clientClasses = clientClasses.stream().map(i -> packageName + "." + i).collect(Collectors.toUnmodifiableSet());
        this.overrideClasses = overrideClasses.stream().map(i -> packageName + "." + i).collect(Collectors.toUnmodifiableSet());
        this.enabled = enabled;
    }

    public static Stream<WaffleFeature> findFeatures(String root) {
        if (!features.containsKey(root)) {
            ClassPath path;
            try {
                path = ClassPath.from(ClassLoader.getSystemClassLoader());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            features.put(root, path.getTopLevelClassesRecursive(root).stream()
                    .filter(c -> c.getSimpleName().equals("package-info"))
                    .map(ClassPath.ClassInfo::getName)
                    .map(WaffleFeature::loadClass)
                    .filter(p -> p.isAnnotationPresent(WaffleFeatureSpec.class))
                    .map(WaffleFeature::readFeature)
                    .collect(Collectors.toSet()));
        }

        return features.get(root).stream();
    }

    private static WaffleFeature readFeature(Class<?> feature)  {
        WaffleFeatureSpec spec = feature.getAnnotation(WaffleFeatureSpec.class);
        WaffleFeatureMixins def = feature.getAnnotation(WaffleFeatureMixins.class);
        WaffleFeatureClassTransformers transform = feature.getAnnotation(WaffleFeatureClassTransformers.class);

        assert spec != null;

        WaffleFeatureMixin[] defs;
        WaffleFeatureClassTransformer[] transformers;

        Set<String> commonClasses = new HashSet<>();
        Set<String> clientClasses = new HashSet<>();
        Set<String> transformerClasses = new HashSet<>();
        Set<String> defined = new HashSet<>();

        if (def == null) {
            WaffleFeatureMixin f = feature.getAnnotation(WaffleFeatureMixin.class);
            defs = f == null ? new WaffleFeatureMixin[] { } : new WaffleFeatureMixin[] { f };
        } else defs = def.value();

        if (transform == null) {
            WaffleFeatureClassTransformer f = feature.getAnnotation(WaffleFeatureClassTransformer.class);
            transformers = f == null ? new WaffleFeatureClassTransformer[] { } : new WaffleFeatureClassTransformer[] { f };
        } else transformers = transform.value();

        for (WaffleFeatureMixin mixin : defs) {
            if (mixin.client()) {
                clientClasses.add(mixin.value().getSimpleName());
            } else {
                commonClasses.add(mixin.value().getSimpleName());
            }

            defined.add(mixin.value().getSimpleName());
        }


        for (WaffleFeatureClassTransformer o : transformers) {
            transformerClasses.add(o.transformer().getSimpleName() + ";" + o.target());
            defined.add(o.transformer().getSimpleName());
        }

        var reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream(feature.getPackageName().replace(".", "/")))));
        var lines = reader.lines()
                .filter(Objects::nonNull)
                .filter(s -> !s.equals("package-info.class"))
                .filter(s -> !defined.contains(s))
                .collect(Collectors.toSet());

        if (spec.isClientOnly()) {
            clientClasses.addAll(lines);
        } else {
            commonClasses.addAll(lines);
        }

        boolean enabled = !feature.isAnnotationPresent(WaffleFeatureDisable.class);
        return new WaffleFeature(spec.name(), feature.getPackageName(), commonClasses, clientClasses, transformerClasses, enabled);
    }

    public Stream<String> getClasses() {
        if (!enabled) {
            return Stream.empty();
        } else if (MixinEnvironment.getCurrentEnvironment().getSide() == MixinEnvironment.Side.CLIENT) {
            return Stream.concat(commonClasses.stream(), clientClasses.stream());
        } else {
            return commonClasses.stream();
        }
    }

    private static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Stream<String> getOverrideClasses() {
        return overrideClasses.stream();
    }
}
