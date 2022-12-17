package com.skilles.spokenword.config.custom.dropdown.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EntityDropdown
{

    ListModes mode() default ListModes.ALL;

}
