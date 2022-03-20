package de.cuuky.varo.bot.discord;

import java.awt.Color;
import java.io.File;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.logging.Level;

import de.cuuky.varo.Varo;
import de.cuuky.varo.app.Main;
import de.cuuky.varo.bot.Bot;
import de.cuuky.varo.bot.BotChannel;
import de.cuuky.varo.bot.discord.listener.DiscordBotEventListener;
import de.cuuky.varo.configuration.configurations.config.ConfigSetting;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class DiscordBot extends ListenerAdapter implements Bot {

	private static final String CONFIG_PATH_PREFIX = "eventChannel.";

	private final HashMap<String, Long> channelIds = new HashMap<>();
	private Varo varo;
	private JDA jda;
	private long guildId;

	@Override
	public void init(Varo varo) {
		if (!ConfigSetting.DISCORDBOT_ENABLED.getValueAsBoolean())
			return;

		if (ConfigSetting.DISCORDBOT_TOKEN.getValue().equals(ConfigSetting.DISCORDBOT_TOKEN.getDefaultValue()) || ConfigSetting.DISCORDBOT_GUILD_ID.getValueAsLong() == -1L) {
			varo.getLogger().severe(Main.getConsolePrefix() + "DiscordBot deactivated because of missing information in the config (DiscordToken or ServerID missing)");
			return;
		}

		varo.getLogger().info(Main.getConsolePrefix() + "Activating discord bot... (Errors might appear - don't mind them)");

		this.varo = varo;
		this.guildId = ConfigSetting.DISCORDBOT_GUILD_ID.getValueAsLong();

		JDABuilder builder = JDABuilder.createDefault(ConfigSetting.DISCORDBOT_TOKEN.getValueAsString());
		builder.setActivity(Activity.playing(ConfigSetting.DISCORDBOT_STATUS.getValueAsString()));
		builder.setStatus(OnlineStatus.ONLINE);
		if (ConfigSetting.DISCORDBOT_PRIVILIGES.getValueAsBoolean()) {
			builder.setMemberCachePolicy(MemberCachePolicy.ALL);
			builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
		}

		try {
			this.jda = builder.build();
			varo.getLogger().info(Main.getConsolePrefix() + "Waiting for the bot to be ready...");
			this.jda.awaitReady();
			this.jda.addEventListener(new DiscordBotEventListener());
		} catch (Throwable t) {
			varo.getLogger().log(Level.SEVERE, Main.getConsolePrefix() + "Couldn't connect to Discord", t);
			this.jda = null;
			return;
		}

		if (this.jda.getGuildById(this.guildId) == null) {
			varo.getLogger().severe(Main.getConsolePrefix() + "Cannot get server from ID " + this.guildId);
			this.shutdown();
		}

		varo.getLogger().info(Main.getConsolePrefix() + "DiscordBot enabled successfully!");
	}

	@Override
	public void shutdown() {
		this.jda.shutdownNow();
		this.jda = null;
	}

	@Override
	public boolean isEnabled() {
		return this.jda != null;
	}

	@Override
	public void sendFile(String message, File file, BotChannel botChannel) {
		if (!isEnabled())
			return;

		this.getChannel(botChannel, channel -> channel.sendFile(file, message.replace("_", "\\_")).queue());
	}

	@Override
	public void sendMessage(String message, String title, Color color, BotChannel botChannel) {
		if (!isEnabled())
			return;

		this.getChannel(botChannel, channel -> {
			if (ConfigSetting.DISCORDBOT_EMBEDS_ENABLED.getValueAsBoolean()) {
				channel.sendMessageEmbeds(this.buildEmbed(message, title, color)).queue();
			} else
				channel.sendMessage(message).queue();
		});
	}

	@Override
	public void sendRawMessage(String message, BotChannel botChannel) {
		if (!isEnabled())
			return;

		this.getChannel(botChannel, channel -> {
			channel.sendMessage(message).queue();
		});
	}

	public void reply(String message, String title, Color color, IReplyCallback replyCallback) {
		if (ConfigSetting.DISCORDBOT_EMBEDS_ENABLED.getValueAsBoolean())
			replyCallback.replyEmbeds(this.buildEmbed(message, title, color)).queue();
		else
			replyCallback.reply(message).queue();
	}

	private MessageEmbed buildEmbed(String message, String title, Color color) {
		EmbedBuilder embed = new EmbedBuilder().setDescription(message).setTitle(title);
		if (ConfigSetting.DISCORDBOT_EMBEDS_RANDOM_COLOR.getValueAsBoolean())
			embed.setColor(this.getRandomColor());
		else
			embed.setColor(color);
		return embed.build();
	}

	private long getChannelId(BotChannel botChannel) {
		return this.channelIds.computeIfAbsent(botChannel.getPath(), path -> {
			long id = ConfigSetting.getEntryByPath(CONFIG_PATH_PREFIX + path).getValueAsLong();
			return id != -1L ? id : ConfigSetting.DISCORDBOT_EVENT_DEFAULT.getValueAsLong();
		});
	}

	private void getChannel(BotChannel botChannel, Consumer<TextChannel> callback) {
		long id = this.getChannelId(botChannel);
		TextChannel channel = this.jda.getTextChannelById(id);
		if (channel != null) {
			if (channel.getGuild().getIdLong() == this.guildId)
				try {
					callback.accept(channel);
				} catch (InsufficientPermissionException e) {
					this.varo.getLogger().warning("The Discord bot is unable to interact with a channel because of a missing permission: " + e.getPermission());
				}
			else
				this.varo.getLogger().warning("Discord channel belongs to a different guild (id: " + id + ", name: " + botChannel.getPath() + ")");
		} else
			this.varo.getLogger().warning("Missing Discord channel (id: " + id + ", name: " + botChannel.getPath() + ")");
	}

	Varo getVaro() {
		return varo;
	}
}
