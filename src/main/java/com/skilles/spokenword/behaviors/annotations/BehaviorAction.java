package com.skilles.spokenword.behaviors.annotations;

import com.skilles.spokenword.behaviors.actions.ActionBehavior;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BehaviorAction
{

    String id();

    Class<? extends ActionBehavior> behavior();

}
