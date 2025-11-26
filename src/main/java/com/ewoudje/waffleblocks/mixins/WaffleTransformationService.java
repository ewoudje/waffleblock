package com.ewoudje.waffleblocks.mixins;

import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.IncompatibleEnvironmentException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class WaffleTransformationService implements ITransformationService {
    private WaffleClassTransformerImpl transformer;

    @Override
    public @NotNull String name() {
        return "WaffleTransformationService";
    }

    @Override
    public void initialize(IEnvironment environment) {
        transformer = new WaffleClassTransformerImpl();
    }

    @Override
    public void onLoad(IEnvironment env, Set<String> otherServices) throws IncompatibleEnvironmentException {

    }

    @Override
    public @NotNull List<? extends ITransformer<?>> transformers() {
        return List.of(transformer);
    }
}
