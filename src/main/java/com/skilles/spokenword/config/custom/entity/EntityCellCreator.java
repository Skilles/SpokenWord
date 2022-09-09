package com.skilles.spokenword.config.custom.entity;

import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;

public class EntityCellCreator extends DropdownBoxEntry.DefaultSelectionCellCreator<EntityType<?>>
{

    private final Collection<EntityType<?>> selectedEntityList;

    public EntityCellCreator(Collection<EntityType<?>> initialValues)
    {
        super();
        this.selectedEntityList = new HashSet<>(initialValues);
    }

    public Collection<EntityType<?>> getSelectedEntityList()
    {
        return selectedEntityList;
    }

    @Override
    public DropdownBoxEntry.SelectionCellElement<EntityType<?>> create(EntityType selection)
    {
        return new EntitySelectionElement(selection);
    }

    class EntitySelectionElement extends DropdownBoxEntry.DefaultSelectionCellElement<EntityType<?>>
    {

        public EntitySelectionElement(EntityType r)
        {
            super(r, entity ->
            {
                var text = Component.literal(entity.getDescription().getString());

                if (!EntityCellCreator.this.selectedEntityList.contains(entity))
                {
                    return text;
                }

                return text.withStyle(ChatFormatting.STRIKETHROUGH);
            });
        }

        @Override
        public @Nullable Component getSearchKey()
        {
            if ((EntityCellCreator.this.selectedEntityList.isEmpty() && isKeywordEmpty()) ||
                    EntityCellCreator.this.selectedEntityList.contains(r))
            {
                return null;
            }

            return Component.literal(this.r.getDescription().getString());
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

            if (!EntityCellCreator.this.selectedEntityList.remove(r))
            {
                EntityCellCreator.this.selectedEntityList.add(r);
            }

            setValue(EntityCellCreator.this.selectedEntityList);

            return true;
        }

        private boolean isWithinBounds(double mouseX, double mouseY)
        {
            return mouseX >= (double) this.x && mouseX <= (double) (this.x + this.width) && mouseY >= (double) this.y && mouseY <= (double) (this.y + this.height);
        }

        private void setValue(Collection<EntityType<?>> entities)
        {
            EntityTopCell topCell = (EntityTopCell) this.getEntry().getSelectionElement().getTopRenderer();
            topCell.setValue(entities);
            topCell.resetSelections();
        }

        private boolean isKeywordEmpty()
        {
            var selectionElement = this.getEntry().getSelectionElement();

            if (selectionElement == null)
            {
                return true;
            }

            return ((EntityTopCell) selectionElement.getTopRenderer()).isKeywordEmpty();
        }

    }

}
