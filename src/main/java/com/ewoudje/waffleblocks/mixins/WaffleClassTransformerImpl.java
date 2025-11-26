package com.ewoudje.waffleblocks.mixins;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TargetType;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WaffleClassTransformerImpl implements ITransformer<ClassNode> {
    private final Map<Target<ClassNode>, String> overrideClasses = new HashMap<>();

    public WaffleClassTransformerImpl() {
        WaffleFeature.findFeatures(getClass().getPackageName() + ".impl")
                .flatMap(WaffleFeature::getOverrideClasses)
                .forEach(s -> {
                    String[] split = s.split(";", 2);
                    if (split.length != 2) throw new IllegalArgumentException("Invalid override class: " + s);
                    overrideClasses.put(Target.targetClass(split[1]), split[0]);
                });
    }

    @Override
    public @NotNull ClassNode transform(ClassNode input, ITransformerVotingContext context) {
        String transformerName = overrideClasses.get(Target.targetClass(context.getClassName()));
        WaffleClassTransformer transformer;
        try {
            Class<? extends WaffleClassTransformer> clazz = (Class<? extends WaffleClassTransformer>) Class.forName(transformerName);
            transformer = clazz.getConstructor().newInstance();
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return transformer.transform(input);
    }

    @Override
    public @NotNull TransformerVoteResult castVote(ITransformerVotingContext context) {
        return TransformerVoteResult.YES;
    }

    @Override
    public @NotNull Set<Target<ClassNode>> targets() {
        return overrideClasses.keySet();
    }

    @Override
    public @NotNull TargetType<ClassNode> getTargetType() {
        return TargetType.CLASS;
    }
}
