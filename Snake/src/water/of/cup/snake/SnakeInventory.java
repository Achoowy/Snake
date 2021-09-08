package water.of.cup.snake;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;

import water.of.cup.boardgames.game.GamePlayer;
import water.of.cup.boardgames.game.inventories.GameInventory;
import water.of.cup.boardgames.game.inventories.GameOption;
import water.of.cup.boardgames.game.inventories.GameOptionType;

public class SnakeInventory extends GameInventory {

	private final SnakeGame game;

	public SnakeInventory(SnakeGame game) {
		super(game);
		this.game = game;
	}

	@Override
	protected ArrayList<GameOption> getOptions() {
		ArrayList<GameOption> options = new ArrayList<>();
		List<String> speeds = Arrays.asList(ConfigUtil.GUI_SNAKE_SLOW.toString(),
				ConfigUtil.GUI_SNAKE_NORMAL.toString(), ConfigUtil.GUI_SNAKE_FAST.toString());
		GameOption speed = new GameOption("speed", Material.EXPERIENCE_BOTTLE, GameOptionType.COUNT, null,
				speeds.get(1), speeds);
		options.add(speed);
		return options;
	}

	@Override
	protected int getMaxQueue() {
		return 3;
	}

	@Override
	protected int getMaxGame() {
		return 1;
	}

	@Override
	protected int getMinGame() {
		return 1;
	}

	@Override
	protected boolean hasTeamSelect() {
		return false;
	}

	@Override
	protected boolean hasGameWagers() {
		return false;
	}

	@Override
	protected boolean hasWagerScreen() {
		return false;
	}

	@Override
	protected boolean hasForfeitScreen() {
		return true;
	}

	@Override
	protected void onGameCreate(HashMap<String, Object> gameData, ArrayList<GamePlayer> players) {
		for (GamePlayer player : players) {
			player.getPlayer().sendMessage(
					water.of.cup.boardgames.config.ConfigUtil.CHAT_WELCOME_GAME.buildString(game.getAltName()));
		}
		game.startGame();
	}
}
