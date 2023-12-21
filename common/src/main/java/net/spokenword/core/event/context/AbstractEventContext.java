package net.spokenword.core.event.context;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class AbstractEventContext<T> implements EventContext<T> {
    private final Map<String, String> metadata;

    AbstractEventContext() {
        this.metadata = null;
    }

    AbstractEventContext(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    @Override
    @Nullable
    public T getFilterable() {
        return null;
    }

    @Override
    @Nullable
    public String getSourceName() {
        return null;
    }

    @Override
    @Nullable
    public String getTargetName() {
        return null;
    }

    @Override
    public final String getMeta(String key) {
        if (metadata == null) {
            throw new IllegalArgumentException("Metadata is not supported for this context");
        }
        return metadata.getOrDefault(key, "");
    }
}
