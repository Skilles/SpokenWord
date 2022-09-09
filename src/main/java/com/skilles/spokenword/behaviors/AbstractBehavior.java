package com.skilles.spokenword.behaviors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public abstract class AbstractBehavior
{

    public AbstractBehavior()
    {

    }

    abstract public void activate(RegexPair... regex);

    protected static LocalPlayer getPlayer()
    {
        return Minecraft.getInstance().player;
    }

}
