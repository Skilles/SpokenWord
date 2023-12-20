package net.spokenword.config.custom.controller.impl;

import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.impl.controller.AbstractControllerBuilderImpl;
import net.minecraft.world.level.block.Block;
import net.spokenword.config.custom.controller.BlockController;
import net.spokenword.config.custom.controller.BlockControllerBuilder;

public class BlockControllerBuilderImpl extends AbstractControllerBuilderImpl<Block> implements BlockControllerBuilder {

    public BlockControllerBuilderImpl(Option<Block> option) {
        super(option);
    }

    @Override
    public Controller<Block> build() {
        return new BlockController(option);
    }
}
