package de.varoplugin.varo.game.task.trigger.builder;

import de.varoplugin.varo.game.Varo;
import de.varoplugin.varo.game.VaroState;
import de.varoplugin.varo.task.trigger.VaroTrigger;
import de.varoplugin.varo.game.task.trigger.GameStateTrigger;
import de.varoplugin.varo.task.trigger.builder.AbstractTriggerBuilder;
import de.varoplugin.varo.task.trigger.builder.TriggerBuilder;
import org.bukkit.plugin.Plugin;

public class VaroTriggerBuilder extends AbstractTriggerBuilder<Varo> implements TriggerBuilder<Varo> {

    public VaroTriggerBuilder(Plugin plugin) {
        super(plugin);
    }

    public VaroTriggerBuilder or(VaroState state) {
        this.orTrigger(varo -> new GameStateTrigger(varo, state));
        return this;
    }

    public VaroTriggerBuilder orNot(VaroState state) {
        this.orTrigger(varo -> new GameStateTrigger(varo, state, false));
        return this;
    }

    @Override
    public VaroTriggerBuilder or(VaroTrigger trigger) {
        super.or(trigger);
        return this;
    }

    @Override
    public VaroTriggerBuilder and(VaroTrigger... triggers) {
        super.and(triggers);
        return this;
    }
}