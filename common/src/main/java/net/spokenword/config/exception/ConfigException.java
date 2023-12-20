package net.spokenword.config.exception;

public class ConfigException extends RuntimeException {

    public ConfigException(Throwable cause) {
        super("SpokenWord config has failed", cause);
    }
}
