package de.cuuky.varo.listener;

import de.cuuky.varo.Main;
import de.cuuky.varo.entity.player.VaroPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerRespawnListener implements Listener {

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		new BukkitRunnable() {
			@Override
			public void run() {
				VaroPlayer.getPlayer(event.getPlayer()).setNormalAttackSpeed();
			}
		}.runTaskLater(Main.getInstance(), 20L);
	}
}
