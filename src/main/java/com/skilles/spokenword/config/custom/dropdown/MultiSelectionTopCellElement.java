package com.skilles.spokenword.config.custom.dropdown;

import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

import java.util.Collection;

public class MultiSelectionTopCellElement<T> extends DropdownBoxEntry.DefaultSelectionTopCellElement<T>
{

    private boolean isEdited;

    private FormattedCharSequence cachedCount;

    @Override
    public boolean isEdited()
    {
        return isEdited;
    }

    private boolean resetSelections;

    public MultiSelectionTopCellElement(int initialCount, T defaultValue)
    {
        super(defaultValue, (string) -> defaultValue, (entity) -> Component.empty());
        cachedCount = FormattedCharSequence.forward(String.valueOf(initialCount), Style.EMPTY.withBold(true));
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, int x, int y, int width, int height, float delta)
    {
        // Display the selection count
        Minecraft.getInstance()
                .font
                .drawShadow(matrices, cachedCount, (float)(x - 20), (float)(y + 6), 16777215);

        super.render(matrices, mouseX, mouseY, x, y, width, height, delta);
    }

    @Override
    public Component getSearchTerm()
    {
        if (resetSelections)
        {
            resetSelections = false;
            return Component.empty();
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

    public void setValue(Collection<T> selections)
    {
        this.cachedCount = FormattedCharSequence.forward(String.valueOf(selections.size()),
                Style.EMPTY.withBold(true));
        this.isEdited = true;
    }

    public void resetSelections()
    {
        this.resetSelections = true;
    }

}
