package de.cuuky.varo.bot.discord.commands;

import java.awt.Color;

import de.cuuky.varo.bot.discord.DiscordBotCommand;
import de.cuuky.varo.configuration.configurations.config.ConfigSetting;
import de.cuuky.varo.player.VaroPlayer;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class OnlineCommand extends DiscordBotCommand {

	/*
	 * OLD CODE
	 */

	public OnlineCommand() {
		super("online", "Zeigt alle Spieler an, die online sind");
	}

	@Override
	public void onExecute(SlashCommandInteraction event) {
	    StringBuilder players = new StringBuilder();
		for (VaroPlayer vp : VaroPlayer.getOnlinePlayer()) {
		    if (players.length() >= (ConfigSetting.DISCORDBOT_USE_EMBEDS.getValueAsBoolean() ? MessageEmbed.DESCRIPTION_MAX_LENGTH : Message.MAX_CONTENT_LENGTH) - 23) {
		        players.append(", ...");
		        break;
		    } else if (players.length() == 0)
		        players.append(vp.getName());
		    else
		        players.append(", ").append(vp.getName());
		}
		getDiscordBot().reply(players.toString(), "Online (" + VaroPlayer.getOnlinePlayer().size() + ")", Color.BLUE, event);
	}
}