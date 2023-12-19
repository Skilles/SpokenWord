package net.spokenword.config.autoconfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoConfigOption
{
    public String name();

    public String description() default "";

    public boolean slider() default false;

    public boolean tickbox() default false;
}
