package com.ewoudje.waffleblocks;

import com.ewoudje.waffleblocks.util.GridChunkHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class WaffleAttachments {
    private static final DeferredRegister<AttachmentType<?>> REGISTER = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES.key(), WaffleBlocks.MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<GridChunkHelper.GridChunkAttachment>> GRID_CHUNK = REGISTER.register("grid_chunk",
            () -> AttachmentType.builder(() -> new GridChunkHelper.GridChunkAttachment())
                    .serialize(GridChunkHelper.GridChunkAttachment.CODEC)
                    .build());


    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}
