package de.varoplugin.varo.game.entity.player.tasks;

import de.varoplugin.varo.api.event.game.VaroStateChangeEvent;
import de.varoplugin.varo.api.event.game.player.VaroPlayerStateChangeEvent;
import de.varoplugin.varo.game.entity.player.VaroPlayer;
import de.varoplugin.varo.game.entity.player.VaroPlayerState;
import org.bukkit.event.EventHandler;

/**
 * Represents a player task depending on the current player state.
 * Unregisters if the player state has changed.
 *
 * @author CuukyOfficial
 * @version v0.1
 */
public abstract class VaroPlayerStateTask extends VaroPlayerTask {

    private final VaroPlayerState state;

    public VaroPlayerStateTask(VaroPlayer player) {
        super(player);

        this.state = player.getState();
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        this.player.registerTasks(this.player.getState());
        super.cancel();
    }

    /**
     * Returns if the task has been cancelled.
     *
     * @param event The event
     * @return If the task has been cancelled.
     */
    @EventHandler
    public boolean onPlayerStateChange(VaroPlayerStateChangeEvent event) {
        if (this.player.equals(event.getPlayer()) && !this.state.equals(event.getPlayer().getState())) {
            this.unregister();
            this.player.registerTasks(event.getState());
            return true;
        }
        return false;
    }

    @Override
    public boolean onGameStateChange(VaroStateChangeEvent event) {
        if (super.onGameStateChange(event)) {
            this.player.registerTasks(this.player.getState());
            return true;
        }
        return false;
    }
}