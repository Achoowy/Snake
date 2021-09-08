package water.of.cup.snake;

import org.bukkit.ChatColor;

import water.of.cup.boardgames.BoardGames;
import water.of.cup.boardgames.config.ConfigInterface;

public enum ConfigUtil implements ConfigInterface {

	CHAT_SNAKE_POINTS("settings.messages.chat.snake.points", "You got %num% points!");

	private final String path;
	private final String defaultValue;
	private static final BoardGames instance = BoardGames.getInstance();

	ConfigUtil(String path, String defaultValue) {
		this.path = path;
		this.defaultValue = defaultValue;
	}

	@Override
	public String toString() {
		String configString = instance.getConfig().getString(this.path);

		if (configString == null)
			return "";

		return ChatColor.translateAlternateColorCodes('&', configString);
	}

	public String toRawString() {
		return ChatColor.stripColor(this.toString());
	}

	@Override
	public String getPath() {
		return this.path;
	}

	@Override
	public String getDefaultValue() {
		return this.defaultValue;
	}

	public String buildString(String replaceWith) {
		String formatted = this.toString();

		formatted = formatted.replace("%player%", replaceWith).replace("%game%", replaceWith).replace("%num%",
				replaceWith);
		return formatted;
	}
}
