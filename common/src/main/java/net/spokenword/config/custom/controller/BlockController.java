package net.spokenword.config.custom.controller;

import dev.isxander.yacl3.api.Option;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public class BlockController extends AbstractRegistryController<Block> {

    public BlockController(Option<Block> option) {
        super(option, BuiltInRegistries.BLOCK);
    }

    @Override
    protected Function<Block, Component> displayFormatter() {
        return Block::getName;
    }

    @Override
    protected int getDecorationPadding() {
        return 16;
    }

    @Override
    protected void renderDropdownEntry(GuiGraphics graphics, ResourceLocation identifier, int x, int y) {
        var block = registry.get(identifier);
        var item = block.asItem();
        var itemStack = item.getDefaultInstance();
        graphics.renderFakeItem(itemStack, x, y);
    }

    @Override
    protected void renderValueEntry(GuiGraphics graphics, Block item, int x, int y, float delta) {
        var itemStack = item.asItem().getDefaultInstance();
        graphics.renderFakeItem(itemStack, x, y);
    }
}
