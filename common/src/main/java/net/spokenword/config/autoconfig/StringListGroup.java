package net.spokenword.config.autoconfig;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import dev.isxander.yacl3.api.controller.DropdownStringControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigField;
import dev.isxander.yacl3.config.v2.api.autogen.ListGroup;
import dev.isxander.yacl3.config.v2.api.autogen.OptionAccess;

import java.util.List;

public class StringListGroup implements ListGroup.ValueFactory<String>, ListGroup.ControllerFactory<String>
{

    @Override
    public String provideNewValue()
    {
        return "Option 1";
    }

    @Override
    public ControllerBuilder<String> createController(ListGroup annotation, ConfigField<List<String>> field, OptionAccess storage, Option<String> option)
    {
        var builder = DropdownStringControllerBuilder.create(option);
        builder.values("Option 1", "Option 2", "Option 3");
        return builder;
    }
}
