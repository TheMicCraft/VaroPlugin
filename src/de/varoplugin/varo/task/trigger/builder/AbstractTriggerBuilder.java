package de.varoplugin.varo.task.trigger.builder;

import de.varoplugin.varo.task.trigger.VaroTrigger;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractTriggerBuilder<B> implements TriggerBuilder<B> {

    private final List<Function<B, VaroTrigger>> layerTrigger;
    private final List<VaroTrigger> children;
    private final Plugin plugin;

    public AbstractTriggerBuilder(Plugin plugin) {
        this.plugin = plugin;
        this.layerTrigger = new LinkedList<>();
        this.children = new LinkedList<>();
    }

    protected void orTrigger(Function<B, VaroTrigger> triggerFunction) {
        this.layerTrigger.add(triggerFunction);
    }

    @Override
    public TriggerBuilder<B> or(VaroTrigger trigger) {
        this.layerTrigger.add(b -> trigger.clone());
        return this;
    }

    @Override
    public TriggerBuilder<B> and(VaroTrigger... trigger) {
        this.children.addAll(Arrays.asList(trigger));
        return this;
    }

    @Override
    public VaroTrigger build(B build) {
        List<VaroTrigger> layer = this.layerTrigger.stream().map(trigger -> trigger.apply(build)).collect(Collectors.toList());
        layer.forEach(t -> this.children.forEach(c -> t.addChildren(c.clone())));

        if (layer.size() == 0) return null;
        else if (layer.size() == 1) return layer.get(0);

        VaroTrigger bitch = new BitchTrigger(this.plugin);
        layer.forEach(bitch::addChildren);
        return bitch;
    }

    @Override
    public VaroTrigger complete(B buildInfo) {
        VaroTrigger built = this.build(buildInfo);
        built.activate();
        return built;
    }
}