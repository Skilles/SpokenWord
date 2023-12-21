package net.spokenword.core.event.context;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BlockEventContext extends AbstractEventContext<Block> {

    private final Block block;

    public BlockEventContext(Block block, BlockPos pos) {
        super(Map.of("pos", pos.toShortString()));
        this.block = block;
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
}
