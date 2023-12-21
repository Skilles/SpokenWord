package net.spokenword.core.format;

public enum MessageVariable {
    SELF("self"),
    PLAYER("player"),
    ENTITY("entity"),
    BLOCK("block"),
    POSITION("pos"),
    ITEM("item"),
    KILLER("killer"),
    REASON("reason"),
    MESSAGE("message");

    private static final char PREFIX = '%';

    private final String name;

    MessageVariable(String name) {
        this.name = name;
    }

    public String replace(String message, String value) {
        return message.replace(PREFIX + name + PREFIX, value);
    }

    public static Builder builder(String message) {
        return new Builder(message);
    }

    public static class Builder {
        private String message;

        private Builder(String message) {
            this.message = message;
        }

        public Builder replace(MessageVariable variable, String value) {
            this.message = variable.replace(message, value);
            return this;
        }

        public String build() {
            return message;
        }
    }
}
