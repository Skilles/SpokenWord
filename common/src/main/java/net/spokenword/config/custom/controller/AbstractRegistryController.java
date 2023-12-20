package net.spokenword.config.custom.controller;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.dropdown.AbstractDropdownController;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.spokenword.config.custom.RegistryHelper;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class AbstractRegistryController<T> extends AbstractDropdownController<T>
{
    // TODO: Add selected item customization

    protected abstract Function<T, Component> displayFormatter();

    protected int getDecorationPadding() {
        return 4;
    }

    protected int getDropdownEntryPadding() {
        return 4;
    }

    protected @Nullable Predicate<T> optionFilter() {
        return null;
    }

    protected void renderDropdownEntry(GuiGraphics graphics, ResourceLocation identifier, int x, int y)
    {
    }

    protected void renderValueEntry(GuiGraphics graphics, T item, int x, int y, float delta)
    {
    }

    protected Function<T, String> alternativeKey() {
        return displayFormatter().andThen(Component::getString);
    }

    public final DefaultedRegistry<T> registry;

    public final RegistryHelper<T> registryHelper;


    /**
     * Constructs a dropdown controller with registry entries as the dropdown entries.
     *
     * @param option bound option
     */
    public AbstractRegistryController(Option<T> option, DefaultedRegistry<T> registry) {
        super(option, registry.entrySet().stream().map(entry -> entry.getKey().toString()).toList(), false, false);
        this.registry = registry;
        this.registryHelper = new RegistryHelper<>(registry);
    }

    @Override
    public String getString() {
        return registry.getKey(option.pendingValue()).toString();
    }

    @Override
    public void setFromString(String value) {
        option.requestSet(registryHelper.getFromName(value, option.pendingValue()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component formatValue() {
        return Component.literal(getString());
    }


    @Override
    public boolean isValueValid(String value) {
        return registryHelper.isRegistered(value, alternativeKey(), optionFilter());
    }

    public Stream<ResourceLocation> getMatchingIdentifiers(String value) {
        return registryHelper.getMatchingIdentifiers(value, alternativeKey(), optionFilter());
    }

    @Override
    protected String getValidValue(String value, int offset) {
        return getMatchingIdentifiers(value)
                .skip(offset)
                .findFirst()
                .map(ResourceLocation::toString)
                .orElseGet(this::getString);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
        return new AbstractRegistryControllerElement<>(this, screen, widgetDimension);
    }
}
