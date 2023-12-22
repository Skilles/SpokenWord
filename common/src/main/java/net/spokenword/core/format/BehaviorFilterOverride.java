package net.spokenword.core.format;

import java.util.List;

@FunctionalInterface
public interface BehaviorFilterOverride<T> {
    boolean contains(List<T> filter, T value);
}
