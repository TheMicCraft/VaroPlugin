package de.varoplugin.varo.game.task;

import de.varoplugin.varo.api.event.game.VaroGameLoginEvent;
import de.varoplugin.varo.game.DefaultKickResult;
import de.varoplugin.varo.game.Varo;
import de.varoplugin.varo.game.VaroKickResult;
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
        VaroKickResult result = this.getVaro().getPlayer(event.getPlayer()) == null ? DefaultKickResult.NOT_A_PARTICIPANT :
                DefaultKickResult.ALLOWED;

        VaroGameLoginEvent loginKickEvent = new VaroGameLoginEvent(this.getVaro(), event, result);
        this.getVaro().getPlugin().callEvent(loginKickEvent);
        if (loginKickEvent.getResult() != DefaultKickResult.ALLOWED) {
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        }
    }
}
