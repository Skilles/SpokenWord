package net.spokenword.config.custom.controller;

import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.dropdown.AbstractDropdownControllerElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbstractRegistryControllerElement<T> extends AbstractDropdownControllerElement<T, ResourceLocation>
{
    private final AbstractRegistryController<T> controller;
    protected T currentItem = null;
    protected Map<ResourceLocation, T> matchingItems = new HashMap<>();


    public AbstractRegistryControllerElement(AbstractRegistryController<T> control, YACLScreen screen, Dimension<Integer> dim) {
        super(control, screen, dim);
        this.controller = control;
    }

    @Override
    protected void drawValueText(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        var oldDimension = getDimension();
        setDimension(getDimension().withWidth(getDimension().width() - getDecorationPadding()));
        super.drawValueText(graphics, mouseX, mouseY, delta);
        setDimension(oldDimension);
        if (currentItem != null) {
            var x = getDimension().xLimit() - getDecorationPadding() - 2;
            var y = getDimension().y() + 2;
            controller.renderValueEntry(graphics, currentItem, x, y, delta);
        }
    }

    @Override
    public List<ResourceLocation> computeMatchingValues() {
        List<ResourceLocation> identifiers = controller.getMatchingIdentifiers(inputField).toList();
        currentItem = controller.registryHelper.getFromName(inputField, null);
        for (ResourceLocation identifier : identifiers) {
            matchingItems.put(identifier, controller.getRegistry().get(identifier));
        }
        return identifiers;
    }

    @Override
    protected void renderDropdownEntry(GuiGraphics graphics, ResourceLocation identifier, int n) {
        super.renderDropdownEntry(graphics, identifier, n);

        var x = getDimension().xLimit() - getDecorationPadding() - 2;
        var y = getDimension().y() + n * getDimension().height() + 4;

        controller.renderDropdownEntry(graphics, identifier, x, y);
    }

    @Override
    public String getString(ResourceLocation identifier) {
        return identifier.toString();
    }

    @Override
    protected int getDecorationPadding() {
        return controller.getDecorationPadding();
    }

    @Override
    protected int getDropdownEntryPadding() {
        return controller.getDropdownEntryPadding();
    }

    @Override
    protected int getControlWidth() {
        return super.getControlWidth() + getDecorationPadding();
    }

    @Override
    protected Component getValueText() {
        if (inputField.isEmpty() || controller == null)
            return super.getValueText();

        if (inputFieldFocused)
            return Component.literal(inputField);

        return controller.displayFormatter().apply(controller.option().pendingValue());
    }
}