package com.skilles.spokenword.behaviors;

import java.util.Collection;

class AdvancedMessageBehavior extends MessageBehavior
{
    private int threshold;

    public AdvancedMessageBehavior(Collection<String> messages, Collection<String> filters)
    {
        super(messages, filters);
    }

    public void setThreshold(int threshold)
    {
        this.threshold = threshold;
    }

    @Override
    public void onActivate(BehaviorContext ctx)
    {
        if (ctx.value().orElseThrow() == threshold || threshold == 0)
        {
            super.onActivate(ctx);
        }
    }

}
