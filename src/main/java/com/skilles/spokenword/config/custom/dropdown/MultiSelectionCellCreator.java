package com.skilles.spokenword.config.custom.dropdown;

import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Function;

public class MultiSelectionCellCreator<T> extends DropdownBoxEntry.DefaultSelectionCellCreator<T>
{

    private final Collection<T> selectedList;

    private final Function<T, String> textFunction;

    public MultiSelectionCellCreator(Collection<T> initialValues, Function<T, String> textFunction)
    {
        super();
        this.selectedList = new HashSet<>(initialValues);
        this.textFunction = textFunction;
    }

    public Collection<T> getSelectedList()
    {
        return selectedList;
    }

    @Override
    public DropdownBoxEntry.SelectionCellElement<T> create(T selection)
    {
        return new MultiSelectionElement(selection, textFunction);
    }

    class MultiSelectionElement extends DropdownBoxEntry.DefaultSelectionCellElement<T>
    {
        private final Function<T, Component> toTextFunction;
        public MultiSelectionElement(T r, Function<T, String> textFunction)
        {
            super(r, entity ->
            {
                var text = Component.literal(textFunction.apply(entity));

                if (!MultiSelectionCellCreator.this.selectedList.contains(entity))
                {
                    return text;
                }

                return text.withStyle(ChatFormatting.STRIKETHROUGH);
            });

            this.toTextFunction = textFunction.andThen(Component::literal);
        }

        @Override
        public @Nullable Component getSearchKey()
        {
            if ((MultiSelectionCellCreator.this.selectedList.isEmpty() && isKeywordEmpty()) ||
                    isSelected())
            {
                return null;
            }

            return toTextFunction.apply(r);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
        {
            if (!this.rendering)
            {
                return false;
            }

            if (!isWithinBounds(mouseX, mouseY))
            {
                return false;
            }

            if (!MultiSelectionCellCreator.this.selectedList.remove(r))
            {
                MultiSelectionCellCreator.this.selectedList.add(r);
            }

            setValue(MultiSelectionCellCreator.this.selectedList);

            return true;
        }

        private boolean isWithinBounds(double mouseX, double mouseY)
        {
            return mouseX >= (double) this.x && mouseX <= (double) (this.x + this.width) && mouseY >= (double) this.y && mouseY <= (double) (this.y + this.height);
        }

        private void setValue(Collection<T> selections)
        {
            MultiSelectionTopCellElement<T> topCell = (MultiSelectionTopCellElement<T>) this.getEntry().getSelectionElement().getTopRenderer();
            topCell.setValue(selections);
            topCell.resetSelections();
        }

        private boolean isKeywordEmpty()
        {
            var selectionElement = this.getEntry().getSelectionElement();

            if (selectionElement == null)
            {
                return true;
            }

            return ((MultiSelectionTopCellElement<T>) selectionElement.getTopRenderer()).isKeywordEmpty();
        }

        public boolean isSelected()
        {
            return MultiSelectionCellCreator.this.selectedList.contains(r);
        }
    }
}
