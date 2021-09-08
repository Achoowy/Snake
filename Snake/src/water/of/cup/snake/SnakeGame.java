package water.of.cup.snake;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import water.of.cup.boardgames.BoardGames;
import water.of.cup.boardgames.game.BoardItem;
import water.of.cup.boardgames.game.Button;
import water.of.cup.boardgames.game.Clock;
import water.of.cup.boardgames.game.Game;
import water.of.cup.boardgames.game.GameConfig;
import water.of.cup.boardgames.game.GameImage;
import water.of.cup.boardgames.game.GamePlayer;
import water.of.cup.boardgames.game.inventories.GameInventory;
import water.of.cup.boardgames.game.maps.MapData;
import water.of.cup.boardgames.game.maps.Screen;
import water.of.cup.boardgames.game.storage.GameStorage;
import water.of.cup.boardgames.game.storage.StorageType;

public class SnakeGame extends Game {
	private int[][] grid;
	private Button[][] gridButtons;
	private Screen screen;
	private int apples;
	private int[] appleLoc;
	private int[] direction;
	private int[] headPosition;
	private int size = 3;
	private SnakeRunnable snakeRunnable;
	private int speed;
	
	public SnakeGame(int rotation) {
		super(rotation);
		grid = new int[9][12];
		createGridButtons();
	}

	@Override
	protected void startGame() {
		this.setInGame();
		screen.renderScreen();
		grid = new int[9][12];

		// set snake start values
		grid[0][6] = 1;
		grid[1][6] = 2;
		grid[2][6] = 3;
		headPosition = new int[] { 6, 2 };
		size = 3;
		apples = 0;
		direction = new int[] { 0, 1 };
		spawnApple();
		updateButtons();
		
		// get speed
		String speedType = (String) gameInventory.getGameData("speed");
		if (speedType.equals(ConfigUtil.GUI_SNAKE_SLOW.toString())) 
			speed = 4;
		else if (speedType.equals(ConfigUtil.GUI_SNAKE_NORMAL.toString()))
			speed = 3;
		else if (speedType.equals(ConfigUtil.GUI_SNAKE_FAST.toString()))
			speed = 2;

		// start snake runnable
		snakeRunnable = new SnakeRunnable(this);
		snakeRunnable.runTaskTimer(BoardGames.getInstance(), 40, speed);
	}

	private void updateButtons() {
		for (int y = 0; y < 9; y++)
			for (int x = 0; x < 12; x++) {
				if (grid[y][x] != 0)
					gridButtons[y][x].setImage("SNAKE_SNAKE");
				else
					gridButtons[y][x].setImage("SNAKE_EMPTY");
			}
		gridButtons[appleLoc[1]][appleLoc[0]].setImage("SNAKE_FOOD");
		mapManager.renderBoard();
	}

	protected void move() {
		int[] nextLoc = new int[] { headPosition[0] + direction[0], headPosition[1] + direction[1] };
		// check that nextLoc is on board
		if (nextLoc[0] < 0 || nextLoc[1] < 0 || nextLoc[0] >= 12 || nextLoc[1] >= 9) {
			this.endGame(null);
			return;
		}
		// check for snake collision
		if (grid[nextLoc[1]][nextLoc[0]] > 1) {
			this.endGame(null);
			return;
		}

		// move snake
		headPosition = nextLoc;
		grid[headPosition[1]][headPosition[0]] = size + 1;
		if (nextLoc[0] == appleLoc[0] && nextLoc[1] == appleLoc[1]) { // check if apple is eaten
			// apple eaten
			apples++;
			size++;
			spawnApple();
		} else {
			// apple not eaten
			for (int y = 0; y < 9; y++)
				for (int x = 0; x < 12; x++)
					if (grid[y][x] > 0)
						grid[y][x]--;
		}
		updateButtons();
	}

	private void spawnApple() {
		if (size == 9 * 12 - 1)
			endGame(null);
		int x = (int) (Math.random() * 12);
		int y = (int) (Math.random() * 9);
		while (grid[y][x] != 0) {
			x = (int) (Math.random() * 12);
			y = (int) (Math.random() * 12);
		}
		appleLoc = new int[] { x, y };
	}

	@Override
	public void endGame(GamePlayer winner) {
		if (snakeRunnable != null && !snakeRunnable.isCancelled()) {
			snakeRunnable.cancel();
		}
		snakeRunnable = null;
		this.setInGame(false);
		teamManager.getGamePlayers().get(0).getPlayer().sendMessage(ConfigUtil.CHAT_SNAKE_POINTS.buildString(apples * (5 - speed) + ""));
		updateStoragePoints();
		clearGamePlayers();
	}

	@Override
	protected void setMapInformation(int rotation) {
		this.mapStructure = new int[][] { { -1 } };
		this.placedMapVal = -1;

		screen = new Screen(this, "SNAKE_BOARD", 2, new int[] { 0, 0 }, new int[][] { { 1 } }, rotation);
		screens.add(screen);
	}

	private void createGridButtons() {
		gridButtons = new Button[9][12];
		for (int y = 0; y < 9; y++)
			for (int x = 0; x < 12; x++) {
				gridButtons[y][x] = new Button(this, "SNAKE_EMPTY", new int[] { 3 + x * 10, 19 + y * 10 }, 0, "pos");
				buttons.add(gridButtons[y][x]);
				gridButtons[y][x].setScreen(screen);
			}

	}

	@Override
	protected void setGameName() {
		this.gameName = "Snake";
	}

	@Override
	protected void setBoardImage() {
		this.gameImage = new GameImage("SNAKE_BOARD", 0);
	}

	@Override
	protected void clockOutOfTime() {
	}

	@Override
	protected Clock getClock() {
		return null;
	}

	@Override
	protected GameInventory getGameInventory() {
		return new SnakeInventory(this);
	}

	@Override
	protected GameStorage getGameStorage() {
		return new SnakeStorage(this);
	}

	@Override
	public ArrayList<String> getTeamNames() {
		return null;
	}

	@Override
	protected GameConfig getGameConfig() {
		return new SnakeConfig(this);
	}

	@Override
	public void click(Player player, double[] loc, ItemStack map) {
		GamePlayer gamePlayer = getGamePlayer(player);
		if (!teamManager.getGamePlayers().contains(gamePlayer))
			return;

		int[] pos = mapManager.getClickLocation(loc, map);
		
		int d = 0;
		if (direction[0] != 0)
			d = 1;
		direction = new int[] { 0, 0 };
		direction[d] = 1;
		if (pos[d] < gridButtons[headPosition[1]][headPosition[0]].getLocation()[d] + 6)
			direction[d] *= -1;

	}

	@Override
	protected void gamePlayerOutOfTime(GamePlayer turn) {
	}

	@Override
	public ItemStack getBoardItem() {
		return new BoardItem(gameName, new ItemStack(Material.GREEN_BANNER, 1));
	}

	@Override
	public boolean canPlaceBoard(Location loc, int rotation) {
		int[] centerLoc = mapManager.getMapValsLocationOnRotatedBoard(placedMapVal);
		int[] mapDimensions = mapManager.getRotatedDimensions();

		// calculate map bounds
		int t1X = -centerLoc[0];
		int t2X = mapDimensions[0] + t1X;

		int t1Y = 0;
		int t2Y = 0; // for future changes

		int t1Z = -centerLoc[1];
		int t2Z = mapDimensions[1] + t1Z;

		// calculate min and max bounds
		int maxX = Math.max(t1X, t2X);
		int minX = Math.min(t1X, t2X);

		int maxY = Math.max(t1Y, t2Y);
		int minY = Math.min(t1Y, t2Y);

		int maxZ = Math.max(t1Z, t2Z);
		int minZ = Math.min(t1Z, t2Z);

		// check if blocks are empty
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				for (int z = minZ; z <= maxZ; z++) {
					if (!loc.getWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z)
							.isEmpty())
						return false;

					// check that place on is not empty
					for (MapData mapData : mapManager.getMapDataAtLocationOnRotatedBoard(x - t1X, z - t1Z, y - t1Y)) {
						if (mapData.getMapVal() <= 0)
							continue;
						Location frameLoc = new Location(loc.getWorld(), loc.getBlockX() + x, loc.getBlockY() + y,
								loc.getBlockZ() + z);
						Block placedOn = frameLoc.getBlock().getRelative(mapData.getBlockFace().getOppositeFace());
						if (placedOn.getType() == Material.AIR)
							return false;
					}
				}
			}
		}
		return true;
	}

	protected void updateStoragePoints() {
		if (!hasGameStorage())
			return;

		if (gameStorage.canExecute(StorageType.POINTS)) {

			GamePlayer player = teamManager.getGamePlayers().get(0);

			LinkedHashMap<StorageType, Object> playerStats = BoardGames.getInstance().getStorageManager()
					.fetchPlayerStats(player.getPlayer(), getGameStore(), false);
			double mostPoints = 0;
			double points = apples * (5 - speed);

			if (playerStats != null && playerStats.containsKey(StorageType.POINTS))
				mostPoints = (Double) playerStats.get(StorageType.POINTS);
			if (points > mostPoints)
				gameStorage.setData(player.getPlayer(), StorageType.POINTS, points);
		}
	}
}
