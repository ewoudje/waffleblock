package com.ewoudje.waffleblocks.mixins;

import org.objectweb.asm.tree.ClassNode;

public interface WaffleClassTransformer {

    ClassNode transform(ClassNode node);

}
