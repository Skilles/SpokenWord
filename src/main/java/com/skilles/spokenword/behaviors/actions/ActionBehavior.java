package com.skilles.spokenword.behaviors.actions;

import com.skilles.spokenword.behaviors.AbstractBehavior;
import com.skilles.spokenword.behaviors.BehaviorContext;

public abstract class ActionBehavior extends AbstractBehavior
{

    private final Runnable action;

    public ActionBehavior(Runnable action)
    {
        super();
        this.action = action;
    }

    @Override
    public void onActivate(BehaviorContext ctx)
    {
        action.run();
    }

}
