package water.of.cup.snake;

import org.bukkit.scheduler.BukkitRunnable;

public class SnakeRunnable extends BukkitRunnable {
	private SnakeGame game;

	public SnakeRunnable(SnakeGame snakeGame) {
		this.game = snakeGame;
	}

	@Override
	public void run() {
		game.move();
	}
}
