package com.skilles.spokenword.exceptions;

public class UnknownBehaviorException extends ConfigException
{

    public UnknownBehaviorException(String id)
    {
        super("No message behavior with id '" + id + "'");
    }

}
