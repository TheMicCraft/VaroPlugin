package de.varoplugin.varo.task;

import de.varoplugin.varo.api.event.game.VaroPlayerLoginEvent;
import de.varoplugin.varo.game.VaroKickResult;
import de.varoplugin.varo.game.Varo;
import de.varoplugin.varo.game.KickResult;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;

public class KickNonRegisteredPlayerListener extends VaroListenerTask {

    public KickNonRegisteredPlayerListener(Varo varo) {
        super(varo);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) return;
        KickResult result = this.getVaro().getPlayer(event.getPlayer()) == null ? VaroKickResult.NOT_A_PARTICIPANT :
                VaroKickResult.ALLOWED;

        VaroPlayerLoginEvent loginKickEvent = new VaroPlayerLoginEvent(this.getVaro(), event, result);
        this.getVaro().getPlugin().callEvent(loginKickEvent);
        if (loginKickEvent.getResult() != VaroKickResult.ALLOWED) {
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        }
    }
}