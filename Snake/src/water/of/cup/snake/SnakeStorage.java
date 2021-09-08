package water.of.cup.snake;

import water.of.cup.boardgames.game.Game;
import water.of.cup.boardgames.game.storage.GameStorage;
import water.of.cup.boardgames.game.storage.StorageType;

public class SnakeStorage extends GameStorage {

    public SnakeStorage(Game game) {
        super(game);
    }

    @Override
    protected String getTableName() {
        return "snake";
    }

    @Override
    protected StorageType[] getGameStores() {
        return new StorageType[] {
                StorageType.POINTS
        };
    }
}

