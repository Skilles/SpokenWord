package net.spokenword.core.event.context;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class AbstractEventContext<T> implements EventContext<T> {
    private Map<String, String> metadata;

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
        lazyInitializeMetadata();
        if (!metadata.containsKey(key)) {
            throw new IllegalArgumentException("Metadata key " + key + " does not exist");
        }
        return metadata.get(key);
    }

    protected Map<String, String> getMetadata() {
        return null;
    }

    private void lazyInitializeMetadata() {
        if (metadata == null) {
            var newData = getMetadata();
            if (newData != null) {
                metadata = newData;
            } else {
                throw new IllegalArgumentException("Metadata is not supported for this context");
            }
        }
    }
}
