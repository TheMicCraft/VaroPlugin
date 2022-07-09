package de.varoplugin.varo.ui.listener;

import de.cuuky.cfw.version.VersionUtils;
import de.varoplugin.varo.game.entity.player.VaroPlayer;
import de.varoplugin.varo.game.task.player.AbstractHeartbeatTask;

public class PlayerShowCountdownTask extends AbstractHeartbeatTask {

    public PlayerShowCountdownTask(VaroPlayer player) {
        super(player, false);
    }

    @Override
    public void run() {
        VersionUtils.getVersionAdapter().sendActionbar(this.getPlayer().getPlayer(), String.valueOf(this.getPlayer().getCountdown()));
    }
}
