package inf112.skeleton.app;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;

/**
 * RoboRally sprites by http://www.yeoldewebsiteknight.co.uk/roborally
 */
public class Tiled extends InputAdapter implements ApplicationListener {
    // player properties
    private Cell playerCell;
    private Cell playerDiedCell;
    private Cell playerWonCell;
    private Vector2 playerPosition = new Vector2();

    // map properties
    private TiledMap map;
    private final int MAP_SIZE_X = 5;
    private final int MAP_SIZE_Y = 5;

    // layer
    private TiledMapTileLayer boardLayer;
    private TiledMapTileLayer playerLayer;
    private TiledMapTileLayer collidableLayer;
    private TiledMapTileLayer holeLayer;
    private TiledMapTileLayer flagLayer;

    // camera properties
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;

    // win-lose messages
    private BitmapFont infoFont;

    @Override
    public void create() {
        // register input processor
        Gdx.input.setInputProcessor(this);

        // load the map including the layers
        map = new TmxMapLoader().load("assets/example.tmx");
        boardLayer = (TiledMapTileLayer) map.getLayers().get("Board");
        playerLayer = (TiledMapTileLayer) map.getLayers().get("Player");
        collidableLayer = (TiledMapTileLayer) map.getLayers().get("Collidable");
        holeLayer = (TiledMapTileLayer) map.getLayers().get("Hole");
        flagLayer = (TiledMapTileLayer) map.getLayers().get("Flag");

        // load Player on the tiled map
        Texture playerTexture = new Texture("player.png");
        TextureRegion[][] split = TextureRegion.split(playerTexture, 300, 300);
        playerCell = new Cell().setTile(new StaticTiledMapTile(split[0][0]));
        playerDiedCell = new Cell().setTile(new StaticTiledMapTile(split[0][1]));
        playerWonCell = new Cell().setTile(new StaticTiledMapTile(split[0][2]));
        playerPosition.set(0, 0);

        // create an orthographic camera, shows us 5x5 units of the world
        camera = new OrthographicCamera();
        camera.setToOrtho(false, MAP_SIZE_X, MAP_SIZE_Y);
        camera.position.x = 2.5f;
        camera.update();

        // set the unit scale to 1/300f (1 unit == 300 pixels)
        renderer = new OrthogonalTiledMapRenderer(map, 1 / 300f);
        renderer.setView(camera);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void render() {
        // clear the screen
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        // render map
        renderer.render();

        // update current player position
        playerLayer.setCell((int) playerPosition.x, (int) playerPosition.y, playerCell);

        // check what field the player is standing on
        if (holeLayer.getCell((int) playerPosition.x, (int) playerPosition.y) != null) {
            playerLayer.setCell((int) playerPosition.x, (int) playerPosition.y, playerDiedCell);
        }
        if (flagLayer.getCell((int) playerPosition.x, (int) playerPosition.y) != null) {
            playerLayer.setCell((int) playerPosition.x, (int) playerPosition.y, playerWonCell);
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public boolean keyUp(int keycode) {
        //first clear the player cell so in case of moving the player avoid to have the player still showing at the old position
        playerLayer.setCell((int) playerPosition.x, (int) playerPosition.y, null);

        if (keycode == Input.Keys.LEFT) {
            if (playerPosition.x > 0)
                playerPosition.x -= 1;
        }
        if (keycode == Input.Keys.RIGHT) {
            if (playerPosition.x < MAP_SIZE_X - 1)
                playerPosition.x += 1;
        }
        if (keycode == Input.Keys.UP) {
            if (playerPosition.y < MAP_SIZE_Y - 1)
                playerPosition.y += 1;
        }
        if (keycode == Input.Keys.DOWN) {
            if (playerPosition.y > 0)
                playerPosition.y -= 1;
        }
        return false;
    }
}
