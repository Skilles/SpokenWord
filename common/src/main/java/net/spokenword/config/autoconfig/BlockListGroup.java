package net.spokenword.config.autoconfig;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigField;
import dev.isxander.yacl3.config.v2.api.autogen.ListGroup;
import dev.isxander.yacl3.config.v2.api.autogen.OptionAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.spokenword.config.custom.controller.BlockControllerBuilder;

import java.util.List;

public class BlockListGroup implements ListGroup.ValueFactory<Block>, ListGroup.ControllerFactory<Block>
{

    @Override
    public ControllerBuilder<Block> createController(ListGroup annotation, ConfigField<List<Block>> field, OptionAccess storage, Option<Block> option)
    {
        return BlockControllerBuilder.create(option);
    }

    @Override
    public Block provideNewValue()
    {
        return Blocks.GRASS_BLOCK;
    }

}
