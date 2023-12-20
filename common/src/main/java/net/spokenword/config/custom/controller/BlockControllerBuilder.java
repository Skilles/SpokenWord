package net.spokenword.config.custom.controller;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import net.minecraft.world.level.block.Block;
import net.spokenword.config.custom.controller.impl.BlockControllerBuilderImpl;

public interface BlockControllerBuilder extends ControllerBuilder<Block> {

    static BlockControllerBuilder create(Option<Block> option) {
        return new BlockControllerBuilderImpl(option);
    }
}
