package net.spokenword.core.event.context;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BlockEventContext extends AbstractEventContext<Block> {

    private final Block block;

    private final BlockPos pos;

    public BlockEventContext(Block block, BlockPos pos) {
        this.block = block;
        this.pos = pos;
    }

    @Override
    @NotNull
    public Block getFilterable() {
        return block;
    }

    @Override
    @NotNull
    public String getSourceName() {
        return block.getName().getString();
    }

    @Override
    protected Map<String, String> getMetadata() {
        return Map.of("pos", pos.toShortString());
    }
}
