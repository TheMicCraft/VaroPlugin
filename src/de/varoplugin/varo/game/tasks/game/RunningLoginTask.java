package de.varoplugin.varo.game.tasks.game;

import de.varoplugin.varo.game.entity.player.VaroPlayer;
import de.varoplugin.varo.game.tasks.VaroGameTask;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * @author CuukyOfficial
 * @version v0.1
 */
public class RunningLoginTask extends VaroGameTask {

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        VaroPlayer player = this.varo.getPlayer(event.getPlayer());
        if (player != null) return;
        // TODO: Let the UI set the kick message
        event.setKickMessage("Nah men not now");
        event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
    }
}