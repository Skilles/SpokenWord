package net.spokenword.config.autoconfig.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoConfigOption {

    String name();

    String description() default "";

    boolean slider() default false;

    boolean tickbox() default false;
}
