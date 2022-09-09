package com.skilles.spokenword.behaviors.actions;

public class RespawnAction extends ActionBehavior
{

    public RespawnAction()
    {
        super(RespawnAction::respawn);
    }

    private static void respawn()
    {
        getPlayer().respawn();
    }

}
