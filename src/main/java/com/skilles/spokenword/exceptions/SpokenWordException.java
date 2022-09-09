package com.skilles.spokenword.exceptions;

public class SpokenWordException extends RuntimeException
{

    public SpokenWordException(String message)
    {
        super("[Spokenword] " + message);
    }

}
