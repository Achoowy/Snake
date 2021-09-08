package water.of.cup.snake;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;

import water.of.cup.boardgames.config.GameRecipe;
import water.of.cup.boardgames.config.GameSound;
import water.of.cup.boardgames.game.Game;
import water.of.cup.boardgames.game.GameConfig;

public class SnakeConfig extends GameConfig {
    public SnakeConfig(Game game) {
        super(game);
    }

    @Override
    protected GameRecipe getGameRecipe() {
        HashMap<String, String> recipe = new HashMap<>();
        recipe.put("R", Material.REDSTONE.toString());
        recipe.put("G", Material.GREEN_WOOL.toString());
        recipe.put("L", Material.LEVER.toString());
        recipe.put("A", Material.GRAY_WOOL.toString());

        ArrayList<String> shape = new ArrayList<String>() {
            {
                add("AGA");
                add("AGA");
                add("RLR");
            }
        };

        return new GameRecipe(game.getName(), recipe, shape);
    }

    @Override
    protected ArrayList<GameSound> getGameSounds() {
        ArrayList<GameSound> gameSounds = new ArrayList<>();
        //gameSounds.add(new GameSound("click", Sound.BLOCK_WOOD_PLACE));
        return gameSounds;
    }

    @Override
    protected HashMap<String, Object> getCustomValues() {
        return null;
    }

    @Override
    protected int getWinAmount() {
        return 0;
    }
}