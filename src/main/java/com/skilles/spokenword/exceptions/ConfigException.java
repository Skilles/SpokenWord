package com.skilles.spokenword.exceptions;

public final class ConfigException extends SpokenWordException
{

    public ConfigException(String message, Throwable thrown)
    {
        this(message + " :: " + thrown.getMessage());
    }

    public ConfigException(String message)
    {
        super("[Config] : " + message);
    }

}
