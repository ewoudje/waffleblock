package com.ewoudje.waffleblocks.mixins;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class WafflePlugin implements IMixinConfigPlugin {
    private final String implPkg = getClass().getPackageName() + ".impl";

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        try {
            return  WaffleFeature.findFeatures(implPkg)
                    .flatMap(WaffleFeature::getClasses)
                    .map(s -> s.replace(implPkg + ".", "").replace(".class", ""))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load waffle mixins", e);
        }
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
