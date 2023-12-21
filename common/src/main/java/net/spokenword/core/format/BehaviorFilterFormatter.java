package net.spokenword.core.format;

@FunctionalInterface
public interface BehaviorFilterFormatter<TValue, TFilter> {
    TValue format(TFilter value);
}
