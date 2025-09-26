package com.ewoudje.wafflecreate;

import com.ewoudje.waffleblocks.api.ClientGrid;
import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.ServerGrid;
import com.ewoudje.waffleblocks.api.components.GridComponentType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

public interface IGridContraption extends Grid, ClientGrid, ServerGrid {

    boolean waffle$isInitialized();

    Grid waffle$init(int id);

    StructureTemplate.StructureBlockInfo waffle$getBlockInfo(BlockPos pos);

    void waffle$addBlock(Level level, BlockPos pos, Pair<StructureTemplate.StructureBlockInfo, BlockEntity> pair);

    void waffle$removeBlock(Level level, BlockPos pos);
}
