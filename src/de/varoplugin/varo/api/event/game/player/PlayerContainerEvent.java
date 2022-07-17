package de.varoplugin.varo.api.event.game.player;

import de.varoplugin.varo.game.entity.player.VaroPlayer;
import de.varoplugin.varo.game.entity.player.PlayerContainer;

public class PlayerContainerEvent extends PlayerCancelableEvent {

    private final PlayerContainer container;

    public PlayerContainerEvent(PlayerContainer container, VaroPlayer player) {
        super(player);

        this.container = container;
    }

    public PlayerContainer getContainer() {
        return this.container;
    }
}