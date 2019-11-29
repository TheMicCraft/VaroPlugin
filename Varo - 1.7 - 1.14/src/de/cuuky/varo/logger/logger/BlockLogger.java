package de.cuuky.varo.logger.logger;

import de.cuuky.varo.data.DataManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import de.cuuky.varo.logger.Logger;

public class BlockLogger extends Logger {

	public BlockLogger(String name) {
		super(name, true);
	}

	public void println(Block block, Player player) {
		if(!DataManager.getInstance().getItemHandler().getDestroyedBlocks().shallLog(block))
			return;

		String log = "[" + getCurrentDate() + "] " + player.getName() + " mined " + block.getType().toString() + " at x:" + block.getLocation().getBlockX() + " y:" + block.getLocation().getBlockY() + " z:" + block.getLocation().getBlockZ() + " in the world '" + block.getWorld().getName() + "'!";

		pw.println(log);
		logs.add(log);

		pw.flush();
	}
}
