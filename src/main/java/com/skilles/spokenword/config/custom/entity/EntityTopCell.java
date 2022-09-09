package com.skilles.spokenword.config.custom.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.skilles.spokenword.SpokenWord;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;

import java.util.Collection;

public class EntityTopCell extends DropdownBoxEntry.DefaultSelectionTopCellElement<EntityType<?>>
{

    private String cachedValue;

    private boolean isEdited;

    private boolean resetSelections;

    // The top cell will display the selection count
    public EntityTopCell(int initialCount)
    {
        super(SpokenWord.DEFAULT_ENTITY, (string) -> SpokenWord.DEFAULT_ENTITY, (entity) -> Component.empty());
        cachedValue = String.valueOf(initialCount);
    }

    @Override
    public boolean isEdited()
    {
        return isEdited;
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, int x, int y, int width, int height, float delta)
    {
        // this.textFieldWidget.setValue(cachedValue);

        super.render(matrices, mouseX, mouseY, x, y, width, height, delta);
    }

    /**
     * Cursed way to have sorted selections
     */
    @Override
    public Component getSearchTerm()
    {
        if (resetSelections)
        {
            resetSelections = false;
            return Component.empty();
        }

        if (isKeywordEmpty())
        {
            return Component.literal("\0");
        }

        return super.getSearchTerm();
    }

    public boolean isKeywordEmpty()
    {
        return this.textFieldWidget.getValue().isEmpty();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        // Hide dropdown on right click
        if (button == 1)
        {
            this.getParent().setFocused(null);
            return false;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void setValue(Collection<EntityType<?>> entities)
    {
        this.cachedValue = String.valueOf(entities.size());
        this.isEdited = true;
    }

    public void resetSelections()
    {
        this.resetSelections = true;
    }

}
