package com.ewoudje.waffleblocks.mixins.impl.base.features.clip;

import com.ewoudje.mixinflour.FlourClassTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class TransformerBlockHitResult implements FlourClassTransformer {

    @Override
    public void transform(ClassNode node) {
        MethodNode constructor = node.methods.stream()
                .filter(m -> m.name.equals("<init>") && m.desc.startsWith("(Z"))
                .findAny().orElseThrow();
        AbstractInsnNode blockPos = new VarInsnNode(Opcodes.ALOAD, 4);
        AbstractInsnNode center = new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/world/phys/Vec3", "atCenterOf", "(Lnet/minecraft/core/Vec3i;)Lnet/minecraft/world/phys/Vec3;");
        AbstractInsnNode subtract = new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/phys/Vec3", "subtract", "(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;");

        for (var instr : constructor.instructions) {

            if (instr.getOpcode() == Opcodes.INVOKESPECIAL) {
                constructor.instructions.insertBefore(instr, subtract);
                constructor.instructions.insertBefore(subtract, center);
                constructor.instructions.insertBefore(center, blockPos);

                break;
            }
        }

        MethodNode getLocation = new MethodNode();
        getLocation.name = "getLocation";
        getLocation.desc = "()Lnet/minecraft/world/phys/Vec3;";
        getLocation.access = Opcodes.ACC_PUBLIC;
        var instrList = getLocation.instructions;
        instrList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instrList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/phys/HitResult", "location", "Lnet/minecraft/world/phys/Vec3;"));
        instrList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        instrList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/phys/BlockHitResult", "blockPos", "Lnet/minecraft/core/BlockPos;"));
        instrList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/world/phys/Vec3", "atCenterOf", "(Lnet/minecraft/core/Vec3i;)Lnet/minecraft/world/phys/Vec3;"));
        instrList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/phys/Vec3", "add", "(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;"));
        instrList.add(new InsnNode(Opcodes.ARETURN));
        node.methods.add(getLocation);

        MethodNode withPosition = node.methods.stream()
                .filter(m -> m.name.equals("withPosition"))
                .findAny().orElseThrow();


        AbstractInsnNode posLoad = new VarInsnNode(Opcodes.ALOAD, 1);
        AbstractInsnNode center2 = new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/world/phys/Vec3", "atCenterOf", "(Lnet/minecraft/core/Vec3i;)Lnet/minecraft/world/phys/Vec3;");
        AbstractInsnNode add = new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/phys/Vec3", "add", "(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;");

        int hit = 0;
        for (var instr : withPosition.instructions) {
            if (instr.getOpcode() == Opcodes.GETFIELD) {
                if (++hit == 2) {
                    constructor.instructions.insertBefore(instr.getNext(), add);
                    constructor.instructions.insertBefore(add, center2);
                    constructor.instructions.insertBefore(center2, posLoad);
                    break;
                }
            }
        }
    }
}
