package com.skilles.spokenword.exceptions;

public class BehaviorException extends SpokenWordException
{

    public BehaviorException(String message)
    {
        super("[Behavior] : " + message);
    }

}
