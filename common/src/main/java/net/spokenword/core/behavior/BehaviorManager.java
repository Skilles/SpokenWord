package net.spokenword.core.behavior;

import net.spokenword.SpokenWord;

import java.util.ArrayList;
import java.util.List;

public class BehaviorManager {

    private static BehaviorManager INSTANCE;

    private final List<Behavior<?>> behaviors;

    private final ConfigBehaviorFactory configBehaviorFactory;

    public BehaviorManager() {
        INSTANCE = this;
        this.behaviors = new ArrayList<>();
        this.configBehaviorFactory = new ConfigBehaviorFactory();
    }

    public void register(Behavior<?> behavior) {
        behaviors.add(behavior);
        behavior.subscribe();
    }

    public void register(List<Behavior<?>> behaviors) {
        this.behaviors.addAll(behaviors);
        behaviors.forEach(Behavior::subscribe);
    }

    public void reset() {
        behaviors.forEach(Behavior::unsubscribe);
        behaviors.clear();
        register(configBehaviorFactory.createBehaviors());
    }

    public void refreshBehaviors() {
        SpokenWord.getLogger().info("Refreshing behaviors...");
        INSTANCE.reset();
    }
}
