package com.skilles.spokenword.config.custom.dropdown;

import com.google.common.collect.ImmutableList;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import me.shedaniel.math.Rectangle;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

// Allows for overriding the inner behavior of the dropdown menu such as selection sorting and rendering
public class MultiDropdownMenuBuilder<T> extends DropdownMenuBuilder<T>
{

    public MultiDropdownMenuBuilder(Component fieldNameKey, DropdownBoxEntry.SelectionTopCellElement<T> topCellElement, DropdownBoxEntry.SelectionCellCreator<T> cellCreator)
    {
        super(Component.translatable("text.cloth-config.reset_value"), fieldNameKey, topCellElement, cellCreator);
    }

    @Override
    public @NotNull DropdownBoxEntry<T> build()
    {
        DropdownBoxEntry<T> entry = new MultiDropdownBoxEntry<>(
                this.getFieldNameKey(),
                this.getResetButtonKey(),
                this.isRequireRestart(),
                this.defaultValue,
                this.saveConsumer,
                this.selections,
                this.topCellElement,
                this.cellCreator
        );
        entry.setTooltipSupplier(() -> this.tooltipSupplier.apply(entry.getValue()));
        if (this.errorSupplier != null) {
            entry.setErrorSupplier(() -> this.errorSupplier.apply(entry.getValue()));
        }

        entry.setSuggestionMode(this.suggestionMode);
        return entry;
    }

    static class MultiDropdownBoxEntry<T> extends DropdownBoxEntry<T>
    {
        public MultiDropdownBoxEntry(
                Component fieldName,
                @NotNull Component resetButtonKey,
                boolean requiresRestart,
                @Nullable Supplier<T> defaultValue,
                @Nullable Consumer<T> saveConsumer,
                @Nullable Iterable<T> selections,
                @NotNull DropdownBoxEntry.SelectionTopCellElement<T> topRenderer,
                @NotNull DropdownBoxEntry.SelectionCellCreator<T> cellCreator
        ) {
            super(fieldName, resetButtonKey, null, requiresRestart, defaultValue, saveConsumer, selections, topRenderer, cellCreator);
            this.selectionElement = new DropdownBoxEntry.SelectionElement<>(
                    this,
                    new Rectangle(0, 0, 150, 20),
                    new MultiDropdownMenuElement(selections == null ? ImmutableList.of() : ImmutableList.copyOf(selections)),
                    topRenderer,
                    cellCreator
            );
        }

        @Override
        public @NotNull ImmutableList<T> getSelections()
        {
            return ImmutableList.of();
        }

        class MultiDropdownMenuElement extends DefaultDropdownMenuElement<T>
        {
            public MultiDropdownMenuElement(@NotNull ImmutableList<T> selections)
            {
                super(selections);
            }

            @Override
            public void search()
            {
                if (this.isSuggestionMode()) {
                    this.currentElements.clear();
                    String keyword = this.lastSearchKeyword.getString().toLowerCase();

                    for(DropdownBoxEntry.SelectionCellElement<T> cell : this.cells) {
                        var multiCell = (MultiSelectionCellCreator<T>.MultiSelectionElement)cell;
                        Component key = cell.getSearchKey();
                        if (key == null || key.getString().toLowerCase().contains(keyword) || multiCell.isSelected()) {
                            this.currentElements.add(cell);
                        }
                    }

                    Comparator<SelectionCellElement<?>> c = Comparator.comparingDouble(
                            i ->
                            {
                                if (i.getSearchKey() == null || ((MultiSelectionCellCreator<T>.MultiSelectionElement)i).isSelected())
                                {
                                    return Double.MAX_VALUE;
                                }
                                return this.similarity(i.getSearchKey().getString(), keyword);
                            }
                    );
                    this.currentElements.sort(c.reversed());
                } else {
                    this.currentElements.clear();
                    this.currentElements.addAll(this.cells);
                }
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button)
            {
                if (!this.isExpanded())
                {
                    return false;
                }

                this.updateScrollingState(mouseX, mouseY, button);

                if (this.scrolling)
                {
                    return true;
                }

                GuiEventListener guiEventListener = null;

                for (GuiEventListener guiEventListener2 : List.copyOf(this.children()))
                {
                    if (guiEventListener2.mouseClicked(mouseX, mouseY, button))
                    {
                        guiEventListener = guiEventListener2;
                    }
                }

                if (guiEventListener != null)
                {
                    this.setFocused(guiEventListener);
                    if (button == 0)
                    {
                        this.setDragging(true);
                    }
                    return true;
                }

                return false;
            }
        }
    }
}
