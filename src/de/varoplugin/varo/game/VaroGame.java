package de.varoplugin.varo.game;

import de.varoplugin.varo.VaroPlugin;
import de.varoplugin.varo.api.event.game.VaroStateChangeEvent;
import de.varoplugin.varo.game.heartbeat.Heartbeat;
import de.varoplugin.varo.game.player.VaroPlayer;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author CuukyOfficial
 * @version v0.1
 */
public class VaroGame implements Varo {

    private VaroPlugin plugin;
    private VaroState state;

    private Heartbeat heartbeat;

    public VaroGame() {
        this.state = VaroGameState.LOBBY;
    }

    private void loadHeartbeat() {
        if (this.heartbeat != null) this.heartbeat.cancel();
        if ((this.heartbeat = this.state.createHeartbeat()) != null)
            this.heartbeat.initialize(this);
    }

    @Override
    public void initialize(VaroPlugin plugin) {
        this.plugin = plugin;
        this.loadHeartbeat();
    }

    @Override
    public boolean register(VaroPlayer player) {
        return false;
    }

    @Override
    public Collection<VaroPlayer> getPlayers() {
        // TODO: Players
        return new ArrayList<>();
    }

    @Override
    public VaroState getState() {
        return this.state;
    }

    @Override
    public boolean setState(VaroState state) {
        if (this.plugin.isCancelled(new VaroStateChangeEvent(this, state))) return false;
        this.state = state;
        this.loadHeartbeat();
        this.state.getListeners(this).forEach(l -> this.plugin.getServer().getPluginManager().registerEvents(l, this.plugin));
        return true;
    }

    @Override
    public VaroPlugin getPlugin() {
        return this.plugin;
    }
}