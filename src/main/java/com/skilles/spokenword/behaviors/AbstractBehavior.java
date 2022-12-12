package com.skilles.spokenword.behaviors;

import com.skilles.spokenword.exceptions.ConfigException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public abstract class AbstractBehavior
{

    private boolean enabled;

    public AbstractBehavior()
    {
        this.enabled = false;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    protected abstract void onActivate(BehaviorContext ctx);

    public boolean activate(BehaviorContext ctx)
    {
        if (!enabled)
        {
            return false;
        }

        if (!(ctx.player() instanceof LocalPlayer))
        {
            throw new ConfigException("Behavior can only be activated by a local player");
        }

        onActivate(ctx);
        return true;
    }

    protected static LocalPlayer getPlayer()
    {
        return Minecraft.getInstance().player;
    }

    @Override
    public String toString()
    {
        var typeName = getClass().getSimpleName();
        return typeName + "{" +
                "enabled=" + enabled +
                '}';
    }

}
