package com.ewoudje.waffleblocks.mixins.impl.base.features.clip;

import com.ewoudje.waffleblocks.mixins.impl.base.accessors.HitResultAccessor;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FriendlyByteBuf.class)
public abstract class MixinFriendlyByteBuf {

    @Shadow public abstract BlockPos readBlockPos();
    @Shadow public abstract <T extends Enum<T>> T readEnum(Class<T> enumClass);
    @Shadow public abstract FriendlyByteBuf writeBlockPos(BlockPos pos);
    @Shadow public abstract FriendlyByteBuf writeEnum(Enum<?> value);
    @Shadow public abstract ByteBuf writeFloat(float par1);
    @Shadow public abstract FriendlyByteBuf writeBoolean(boolean value);
    @Shadow public abstract float readFloat();
    @Shadow public abstract boolean readBoolean();

    /**
     * @author ewoudje
     * @reason BlockHitResult now stores relative coords in themselves
     */
    @Overwrite
    public BlockHitResult readBlockHitResult() {
        BlockPos blockpos = this.readBlockPos();
        Direction direction = this.readEnum(Direction.class);
        float f = this.readFloat();
        float f1 = this.readFloat();
        float f2 = this.readFloat();
        boolean flag = this.readBoolean();
        return new BlockHitResult(Vec3.atCenterOf(blockpos).add(f, f1, f2), direction, blockpos, flag);
    }

    /**
     * @author ewoudje
     * @reason BlockHitResult now stores relative coords in themselves
     */
    @Overwrite
    public void writeBlockHitResult(BlockHitResult result) {
        BlockPos blockpos = result.getBlockPos();
        this.writeBlockPos(blockpos);
        this.writeEnum(result.getDirection());
        Vec3 vec3 = ((HitResultAccessor)result).getInternalPosition();
        this.writeFloat((float)vec3.x);
        this.writeFloat((float)vec3.y);
        this.writeFloat((float)vec3.z);
        this.writeBoolean(result.isInside());
    }
}
