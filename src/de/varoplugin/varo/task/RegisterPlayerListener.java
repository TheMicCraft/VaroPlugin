package de.varoplugin.varo.task;

import de.varoplugin.varo.game.Varo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;

public class RegisterPlayerListener extends VaroListenerTask {

    public RegisterPlayerListener(Varo varo) {
        super(varo);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLobbyLogin(PlayerLoginEvent event) {
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) return;

        // TODO: Add register before start config option and make default player state configurable
        this.getVaro().register(event.getPlayer());
    }
}