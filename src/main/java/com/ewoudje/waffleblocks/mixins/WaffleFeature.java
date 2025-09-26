package com.ewoudje.waffleblocks.mixins;

import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WaffleFeature {
    private static final Logger LOGGER = LoggerFactory.getLogger(WaffleFeature.class);

    private final String name;
    private final Set<String> commonClasses;
    private final Set<String> clientClasses;
    private final boolean enabled;

    private WaffleFeature(String name, String packageName, Set<String> commonClasses, Set<String> clientClasses, boolean enabled) {
        this.name = name;
        this.commonClasses = commonClasses.stream().map(i -> packageName + "." + i).collect(Collectors.toUnmodifiableSet());
        this.clientClasses = clientClasses.stream().map(i -> packageName + "." + i).collect(Collectors.toUnmodifiableSet());
        this.enabled = enabled;
    }

    public static Stream<WaffleFeature> findFeatures(String root) {

        ClassPath path;
        try {
            path = ClassPath.from(ClassLoader.getSystemClassLoader());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return path.getTopLevelClassesRecursive(root).stream()
                .filter(c -> c.getSimpleName().equals("package-info"))
                .map(ClassPath.ClassInfo::getName)
                .map(WaffleFeature::loadClass)
                .filter(p -> p.isAnnotationPresent(WaffleFeatureSpec.class))
                .map(WaffleFeature::readFeature);
    }

    private static WaffleFeature readFeature(Class<?> feature)  {
        WaffleFeatureSpec spec = feature.getAnnotation(WaffleFeatureSpec.class);
        WaffleFeatureMixins defs = feature.getAnnotation(WaffleFeatureMixins.class);

        assert spec != null;

        Set<String> commonClasses = new HashSet<>();
        Set<String> clientClasses = new HashSet<>();
        Set<String> defined;

        if (defs != null) {
            for (WaffleFeatureMixin mixin : defs.value()) {
                if (mixin.client()) {
                    clientClasses.add(mixin.value().getSimpleName());
                } else {
                    commonClasses.add(mixin.value().getSimpleName());
                }
            }

            defined = Arrays.stream(defs.value())
                    .map(WaffleFeatureMixin::value)
                    .map(Class::getSimpleName)
                    .collect(Collectors.toSet());
        } else {
            defined = Set.of();
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
        return new WaffleFeature(spec.name(), feature.getPackageName(), commonClasses, clientClasses, enabled);
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
}
