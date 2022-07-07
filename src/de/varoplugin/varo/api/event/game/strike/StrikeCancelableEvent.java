package de.varoplugin.varo.api.event.game.strike;

import de.varoplugin.varo.game.strike.VaroStrike;
import org.bukkit.event.Cancellable;

public abstract class StrikeCancelableEvent extends StrikeEvent implements Cancellable {

    private boolean cancelled;

    public StrikeCancelableEvent(VaroStrike strike) {
        super(strike);
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

}