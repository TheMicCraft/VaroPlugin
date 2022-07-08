package de.varoplugin.varo.game.task.player;

import de.varoplugin.varo.api.event.game.player.VaroPlayerCountdownChangeEvent;
import de.varoplugin.varo.game.entity.player.VaroPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;

public class PlayerNoKickRadiusListener extends AbstractPlayerListener {

    private final int checkCountdown;
    private final int checkRadius;

    public PlayerNoKickRadiusListener(VaroPlayer player) {
        super(player);

        this.checkCountdown = 5;
        this.checkRadius = 30;
        // TODO: Configurable
    }

    @EventHandler
    public void onPlayerKick(VaroPlayerCountdownChangeEvent event) {
        if (!event.getPlayer().equals(this.getPlayer()) || event.getCountdown() >= this.checkCountdown) return;

        for (Entity entity : event.getPlayer().getPlayer().getNearbyEntities(this.checkRadius, this.checkRadius, this.checkRadius)) {
            if (entity.getType() != EntityType.PLAYER) return;
            VaroPlayer other = this.getVaro().getPlayer(entity.getUniqueId());
            if (other.getTeam() != this.getPlayer().getTeam()) {
                // TODO: Added event
                event.setCountdown(this.checkCountdown);
            }
        }
    }
}
