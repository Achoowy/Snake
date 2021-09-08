package water.of.cup.snake;

import java.util.ArrayList;

import water.of.cup.boardgames.extension.BoardGamesConfigOption;
import water.of.cup.boardgames.extension.BoardGamesExtension;
import water.of.cup.boardgames.game.Game;

public class Snake extends BoardGamesExtension {

	@Override
	public ArrayList<Class<? extends Game>> getGames() {
		ArrayList<Class<? extends Game>> games = new ArrayList<Class<? extends Game>>();
		games.add(SnakeGame.class);
		return games;
	}

	@Override
	public String getExtensionName() {
		return "Snake";
	}

	@Override
	public ArrayList<BoardGamesConfigOption> getExtensionConfig() {
		ArrayList<BoardGamesConfigOption> configOptions = new ArrayList<>();
		for(ConfigUtil configUtil : ConfigUtil.values()) {
			configOptions.add(new BoardGamesConfigOption(configUtil.getPath(), configUtil.getDefaultValue()));
		}
		return configOptions;
	}
}