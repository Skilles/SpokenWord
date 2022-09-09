package com.skilles.spokenword.behaviors.actions;

import com.skilles.spokenword.behaviors.AbstractBehavior;
import com.skilles.spokenword.behaviors.RegexPair;

public abstract class ActionBehavior extends AbstractBehavior
{

    private final Runnable action;

    public ActionBehavior(Runnable action)
    {
        super();
        this.action = action;
    }

    @Override
    public void activate(RegexPair... args)
    {
        action.run();
    }

}
