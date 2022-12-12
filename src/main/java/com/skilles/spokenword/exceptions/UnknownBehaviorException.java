package com.skilles.spokenword.exceptions;

public final class UnknownBehaviorException extends BehaviorException
{

    public UnknownBehaviorException(String id)
    {
        super("No message behavior with id '" + id + "'");
    }

}
